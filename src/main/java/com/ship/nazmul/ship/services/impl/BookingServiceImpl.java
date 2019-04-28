package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.commons.utils.DateUtil;
import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.*;
import com.ship.nazmul.ship.entities.accountings.AdminCashbook;
import com.ship.nazmul.ship.entities.accountings.AdminShipLedger;
import com.ship.nazmul.ship.exceptions.exists.UserAlreadyExistsException;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.UserInvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.exceptions.nullpointer.NullPasswordException;
import com.ship.nazmul.ship.repositories.BookingRepository;
import com.ship.nazmul.ship.services.BookingService;
import com.ship.nazmul.ship.services.CategoryService;
import com.ship.nazmul.ship.services.SeatService;
import com.ship.nazmul.ship.services.UserService;
import com.ship.nazmul.ship.services.accountings.AdminCashbookService;
import com.ship.nazmul.ship.services.accountings.AdminShipLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final SeatService seatService;
    private final AdminCashbookService adminCashbookService;
    private final AdminShipLedgerService adminShipLedgerService;


    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, CategoryService categoryService, SeatService seatService, AdminCashbookService adminCashbookService, AdminShipLedgerService adminShipLedgerService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.seatService = seatService;
        this.adminCashbookService = adminCashbookService;
        this.adminShipLedgerService = adminShipLedgerService;
    }

    @Override
    public Booking save(Booking booking) throws UserNotFoundException, UserInvalidException, UserAlreadyExistsException, NullPasswordException {
        User user = this.getUser(booking.getUser());
        booking.setUser(user);
        return this.bookingRepository.save(booking);
    }

    User getUser(User user) throws UserNotFoundException, UserAlreadyExistsException, NullPasswordException, UserInvalidException {
        User u = this.userService.findByUsernameOrPhone(user.getPhoneNumber());
        if (u == null) {
            u = new User(user.getName(), user.getPhoneNumber(), user.getPhoneNumber(), user.getPhoneNumber().substring(user.getPhoneNumber().length() - 6));
            if (user.getEmail() != null && user.getEmail().length() < 3) {
                u.setEmail(user.getEmail());
            }
            u = this.userService.save(u);
        }
        return u;
    }

    @Override
    public Booking getOne(Long id) {
        return this.bookingRepository.findOne(id);
    }

    @Override
    public Page<Booking> getAllBookings(int page) {
        return this.bookingRepository.findAll(PageAttr.getPageRequest(page));
    }

    @Override
    public List<Booking> getAllBookings() {
        return this.bookingRepository.findAll();
    }

    @Override
    public Page<Booking> getBookingsByShipId(Long shipId, int page) {
        return this.bookingRepository.findByShipId(shipId, PageAttr.getPageRequest(page));
    }

    @Override
    public Booking createBooking(Booking booking) {
        return null;
    }

    @Override
    @Transactional
    public Booking createAdminBooking(Booking booking) throws ForbiddenException, NotFoundException, ParseException, UserAlreadyExistsException, NullPasswordException, UserInvalidException {
        //1) Security check if user has sufficient permission for this action
        User user = SecurityConfig.getCurrentUser();
        if (!user.hasRole(Role.ERole.ROLE_ADMIN.toString())) throw new ForbiddenException("Access denied");

        // 2) Add subBookingList to booking and Calculate Booking
        booking.setSubBookingList(this.calculateSubBookingList(booking.getSubBookingList()));
        booking = this.calculateBooking(booking);
        booking.setShip(booking.getSubBookingList().get(0).getSeat().getCategory().getShip());
        booking.setShipName(booking.getShip().getName());
        booking.setCategoryName(booking.getSubBookingList().get(0).getSeat().getCategory().getName());
        //7) To confirm a booking user need to go following steps
        // 1 ) pay first
        // 2 ) update room booking map
        // 3 ) update ledger for hotel
        if (user.hasRole(Role.ERole.ROLE_ADMIN.toString())) {
            booking = this.save(booking);
            if (this.confirmBooking(booking)) {
                if(booking.geteStatus() == Seat.EStatus.SEAT_SOLD) {
                    booking.setConfirmed(true);
                    booking.setApproved(true);
                    booking.setPaid(true);
                    booking = this.save(booking);
                    this.adminSellRoomsAccounting(booking, false);
                }
                return booking;
            }
        }
        return null;
    }

    private boolean confirmBooking(Booking booking) throws NotFoundException, ForbiddenException, ParseException {
        //Update room status map and booking map
        for (SubBooking subBooking : booking.getSubBookingList()) {
            boolean av = this.seatService.checkSeatAvailability(subBooking.getSeat().getId(), subBooking.getDate());
            if (av) {
                this.seatService.updateSeatStatusAndBookingMap(subBooking.getSeat().getId(), subBooking.getDate(), booking.geteStatus(), booking);
            } else {
                return false;
            }
        }
        return true;
    }

    /*1) Admin_Cashbook debit total booking amount
     * 2) Admin_Ship_Ledger credit ( total amount - hotelswave discount)
     * */
    private void adminSellRoomsAccounting(Booking booking, boolean cancel) throws NotFoundException {
        //1) Admin_Cashbook debit total booking amount
        Ship ship = booking.getShip();
        String explanation = (cancel ? "Cancel " : "") + "Booking for booking id " + booking.getId();
        AdminCashbook adminCashbook;
        if (!cancel) {
            adminCashbook = new AdminCashbook(new Date(), explanation, booking.getTotalPayablePrice(), 0);
        } else {
            adminCashbook = new AdminCashbook(new Date(), explanation, 0, booking.getTotalPayablePrice());
        }
        adminCashbook.setApproved(true);
        adminCashbook.setRef(booking.getId().toString());
        this.adminCashbookService.updateBalanceAndSave(adminCashbook);

        //2) Admin_Hotel_Ledger credit ( total amount - hotelswave discount)
        int hotelswavePercentages = (int) (booking.getTotalPayablePrice() * ship.getHotelswavePercentage() / 100.00);
        int amount = booking.getTotalPayablePrice() - hotelswavePercentages; //deduct hotels wave percentages;
        AdminShipLedger adminShipLedger;
        if (!cancel) {
            adminShipLedger = new AdminShipLedger(ship, new Date(), explanation, 0, amount);
        } else {
            adminShipLedger = new AdminShipLedger(ship, new Date(), explanation, amount, 0);
        }
        adminShipLedger.setApproved(true);
        adminShipLedger.setRef(booking.getId().toString());
        this.adminShipLedgerService.updateAdminShipLedger(ship.getId(), adminShipLedger);

    }

    private Booking calculateBooking(Booking booking) {
        int totalFare = 0;
        int totalDiscount = 0;
        int totalCommission = 0;
        for (SubBooking subBooking : booking.getSubBookingList()) {
            totalFare += subBooking.getFare();
            totalDiscount += subBooking.getDiscount();
            totalCommission += subBooking.getCommission();
        }
        booking.setTotalFare(totalFare);
        booking.setTotalDiscount(totalDiscount);
        booking.setTotalCommission(totalCommission);
        booking.setTotalPayablePrice(totalFare - totalDiscount - booking.getPromotionDiscount());
        return booking;
    }

    List<SubBooking> calculateSubBookingList(List<SubBooking> subBookingList) throws NotFoundException, ParseException {
        List<SubBooking> newSubBookingList = new ArrayList<>();

        for (SubBooking subBooking : subBookingList) {
            Seat seat = this.seatService.getOne(subBooking.getSeat().getId());
            // 3) Create SubBooking for each room and each date
            SubBooking newSubBooking = new SubBooking(DateUtil.truncateTimeFromDate(subBooking.getDate()), subBooking.getDiscount(), subBooking.getCommission(), seat);
            // 4) Calculate each subBooking and add to subBookingList
            newSubBooking = this.calculateSubBooking(newSubBooking);
            newSubBookingList.add(newSubBooking);
        }
        return newSubBookingList;
    }

    SubBooking calculateSubBooking(SubBooking subBooking) {
        subBooking.setFare(subBooking.getSeat().getCategory().getFare());
        Integer discount = this.categoryService.getDiscount(subBooking.getSeat().getCategory().getId(), subBooking.getDate());//subBooking.getDiscount();
        if (discount == null) {
            discount = subBooking.getDiscount();
        }
        subBooking.setDiscount(discount);
        subBooking.setPayablePrice(subBooking.getFare() - subBooking.getDiscount());
        return subBooking;
    }

    @Override
    public Booking createAdminAgentBooking(Booking booking) {
        return null;
    }

    @Override
    public Booking createServiceAdminBooking(Booking booking) {
        return null;
    }

    @Override
    public Booking createServiceAgentBooking(Booking booking) {
        return null;
    }

    @Override
    public Page<Booking> getMySells(int page) {
        return this.bookingRepository.findByCreatedById(SecurityConfig.getCurrentUser().getId(), PageAttr.getPageRequest(page));
    }
}
