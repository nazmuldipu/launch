package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.commons.Validator;
import com.ship.nazmul.ship.commons.utils.DateUtil;
import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.*;
import com.ship.nazmul.ship.entities.accountings.AdminAgentLedger;
import com.ship.nazmul.ship.entities.accountings.AdminCashbook;
import com.ship.nazmul.ship.entities.accountings.AdminShipLedger;
import com.ship.nazmul.ship.entities.accountings.ShipCashBook;
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
import com.ship.nazmul.ship.services.accountings.AdminAgentLedgerService;
import com.ship.nazmul.ship.services.accountings.AdminCashbookService;
import com.ship.nazmul.ship.services.accountings.AdminShipLedgerService;
import com.ship.nazmul.ship.services.accountings.ShipCashBookService;
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
    private final ShipCashBookService shipCashBookService;
    private final AdminAgentLedgerService adminAgentLedgerService;


    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, CategoryService categoryService, SeatService seatService, AdminCashbookService adminCashbookService, AdminShipLedgerService adminShipLedgerService, ShipCashBookService shipCashBookService, AdminAgentLedgerService adminAgentLedgerService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.seatService = seatService;
        this.adminCashbookService = adminCashbookService;
        this.adminShipLedgerService = adminShipLedgerService;
        this.shipCashBookService = shipCashBookService;
        this.adminAgentLedgerService = adminAgentLedgerService;
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
    public Booking getServiceAdminBooking(Long id) {
        Booking booking = this.getOne(id);
        User currentUser = SecurityConfig.getCurrentUser();
        if (currentUser.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()) && Validator.containsShip(currentUser.getShips(), booking.getShip()) && !booking.isCancelled())
            return booking;

        return null;
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
    public List<Booking> getByCreateDateWithoutCanceled(Date date) {
        Date begin = DateUtil.getDayStart(date);
        Date end = DateUtil.getDayEnd(date);
        return this.bookingRepository.findByCreatedBetweenAndCancelledFalse(begin, end);
    }

    @Override
    public Page<Booking> getAllBookingsWithoutCanceled(int page) {
        return this.bookingRepository.findByCancelledFalse(PageAttr.getPageRequest(page));
    }

    @Override
    public List<Booking> getBookingsByShipIdAndCreateDate(Long shipId, Date date) {
        Date begin = DateUtil.getDayStart(date);
        Date end = DateUtil.getDayEnd(date);
        return this.bookingRepository.findByShipIdAndCreatedBetweenAndCancelledFalse(shipId, begin, end);
    }

    @Override
    public Page<Booking> getBookingsByShipId(Long shipId, int page) {
        return this.bookingRepository.findByShipIdAndCancelledFalse(shipId, PageAttr.getPageRequest(page));
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
        if (user.hasRole(Role.ERole.ROLE_ADMIN.toString())) {
            booking = this.save(booking);
            if (this.confirmBooking(booking)) {
                if (booking.geteStatus() == Seat.EStatus.SEAT_SOLD) {
                    booking = this.approveBooking(booking);
                } else if(booking.geteStatus() == Seat.EStatus.SEAT_RESERVED){
                    booking = this.reserveBooking(booking);
                }
                return booking;
            }
        }
        return null;
    }

    @Override
    public Booking confirmReservation(Long bookingId) throws NotFoundException, UserAlreadyExistsException, NullPasswordException, UserInvalidException, ParseException {
        Booking booking = this.getOne(bookingId);
        System.out.println(booking);
        return this.approveBooking(booking);
    }

    private Booking approveBooking(Booking booking) throws NotFoundException, UserAlreadyExistsException, NullPasswordException, UserInvalidException, ParseException {
        booking.seteStatus(Seat.EStatus.SEAT_SOLD);
        for (SubBooking subBooking : booking.getSubBookingList()) {
            this.seatService.updateStatusMap(subBooking.getSeat().getId(), subBooking.getDate(), booking.geteStatus());
        }

        booking.setConfirmed(true);
        booking.setApproved(true);
        booking.setPaid(true);
        booking = this.save(booking);
        if (SecurityConfig.getCurrentUser().hasRole(Role.ERole.ROLE_ADMIN.toString())) {
            booking.setHotelswaveAgentDiscount(booking.getShip().getHotelswavePercentage() * booking.getTotalPayablePrice() / 100);
            this.adminSellSeatAccounting(booking, false);
        } else if (SecurityConfig.getCurrentUser().hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString())) {
            this.serviceAdminSellSeatAccounting(booking, false);
        } else if(SecurityConfig.getCurrentUser().hasRole(Role.ERole.ROLE_AGENT.toString())){
            this.adminAgentSellsSeatAccount(booking, false);
        }
        return booking;
    }

    private Booking reserveBooking(Booking booking) throws NotFoundException, ParseException {
        booking.seteStatus(Seat.EStatus.SEAT_RESERVED);
        for (SubBooking subBooking : booking.getSubBookingList()) {
            this.seatService.updateStatusMap(subBooking.getSeat().getId(), subBooking.getDate(), booking.geteStatus());
        }
        return booking;
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
    private void adminSellSeatAccounting(Booking booking, boolean cancel) throws NotFoundException {
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

    private void adminAgentSellsSeatAccount(Booking booking, boolean cancel){
        int commission = booking.getShip().getHotelswavePercentage() * booking.getTotalPayablePrice() / (100 * 2);
        String explanation = (cancel ? "Cancel " : "") + "Booking for booking id " + booking.getId();

        //1) Debit AdminAgentLedger = total_advance - hotelswave_agent_discount
        AdminAgentLedger adminAgentLedger;
        if (cancel) {
            adminAgentLedger = new AdminAgentLedger(booking.getCreatedBy(), new Date(), explanation, 0, booking.getTotalPayablePrice() - commission);
        } else {
            adminAgentLedger = new AdminAgentLedger(SecurityConfig.getCurrentUser(), new Date(), explanation, booking.getTotalPayablePrice() -commission, 0);
        }
        adminAgentLedger.setRef(booking.getId().toString());
        adminAgentLedger.setApproved(true);
        adminAgentLedger = this.adminAgentLedgerService.updateBalanceAndSave(booking.getCreatedBy().getId(), adminAgentLedger);

        //2) Admin_Hotel_Ledger credit ( total amount - hotelswave discount)
        int hotelswavePercentages = (int) (booking.getTotalPayablePrice() * booking.getShip().getHotelswavePercentage() / 100.00);
        int amount = booking.getTotalPayablePrice() - hotelswavePercentages; //deduct hotels wave percentages;
        AdminShipLedger adminShipLedger;
        if (!cancel) {
            adminShipLedger = new AdminShipLedger( booking.getShip(), new Date(), explanation, 0, amount);
        } else {
            adminShipLedger = new AdminShipLedger( booking.getShip(), new Date(), explanation, amount, 0);
        }
        adminShipLedger.setApproved(true);
        adminShipLedger.setRef(booking.getId().toString());
        this.adminShipLedgerService.updateAdminShipLedger( booking.getShip().getId(), adminShipLedger);
    }

    private void serviceAdminSellSeatAccounting(Booking booking, boolean cancel) {
        Ship ship = booking.getShip();
        String explanation = (cancel ? "Cancel" : "") + "Booking for booking id " + booking.getId();
        //1)Add amount to shipCashbook
        ShipCashBook shipCashBook;
        if (!cancel) {
            shipCashBook = new ShipCashBook(ship, new Date(), explanation, booking.getTotalPayablePrice(), 0);
        } else {
            shipCashBook = new ShipCashBook(ship, new Date(), explanation, 0, booking.getTotalPayablePrice());
        }
        shipCashBook.setApproved(true);
        shipCashBook = this.shipCashBookService.debitAmount(shipCashBook);
        return;
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
    public Booking createAdminAgentBooking(Booking booking) throws ForbiddenException, NotFoundException, ParseException, UserAlreadyExistsException, NullPasswordException, UserInvalidException {
        System.out.println(booking);
        //1) Security check if user has sufficient permission for this action
        User user = SecurityConfig.getCurrentUser();
        if (!user.hasRole(Role.ERole.ROLE_AGENT.toString())) throw new ForbiddenException("Access denied");

        int agentBalance = this.adminAgentLedgerService.getAdminAgentBalance(user.getId());

        // 2) Add subBookingList to booking and Calculate Booking
        booking.setSubBookingList(this.calculateSubBookingList(booking.getSubBookingList()));
        booking = this.calculateBooking(booking);
        booking.setShip(booking.getSubBookingList().get(0).getSeat().getCategory().getShip());
        booking.setShipName(booking.getShip().getName());
        booking.setCategoryName(booking.getSubBookingList().get(0).getSeat().getCategory().getName());

        int agentCommission = (int) (booking.getShip().getHotelswavePercentage() * booking.getTotalPayablePrice() / (200.0 ));
        System.out.println("HOTELS WAVE PERCENTAGE :" + booking.getShip().getHotelswavePercentage());
        System.out.println("Commission: " + agentCommission + ", Balance : " + agentBalance + ", Payable : " + (booking.getTotalPayablePrice() - agentCommission));

        if(agentBalance > (booking.getTotalPayablePrice() - agentCommission)){
            if (user.hasRole(Role.ERole.ROLE_AGENT.toString())) {
                booking = this.save(booking);
                if (this.confirmBooking(booking)) {
                    if (booking.geteStatus() == Seat.EStatus.SEAT_SOLD) {
                        booking = this.approveBooking(booking);
                    } else if(booking.geteStatus() == Seat.EStatus.SEAT_RESERVED){
                        booking = this.reserveBooking(booking);
                    }
                    return booking;
                }
            }
        } else {
            throw new ForbiddenException("In sufficient balace");
        }
        return null;
    }

    @Override
    public Booking createServiceAdminBooking(Booking booking) throws ForbiddenException, NotFoundException, ParseException, UserAlreadyExistsException, NullPasswordException, UserInvalidException {
        //1) Security check if user has sufficient permission for this action
        User user = SecurityConfig.getCurrentUser();
        if (!user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString())) throw new ForbiddenException("Access denied");

        // 2) Add subBookingList to booking and Calculate Booking
        booking.setSubBookingList(this.calculateSubBookingList(booking.getSubBookingList()));
        booking = this.calculateBooking(booking);
        booking.setShip(booking.getSubBookingList().get(0).getSeat().getCategory().getShip());
        booking.setShipName(booking.getShip().getName());
        booking.setCategoryName(booking.getSubBookingList().get(0).getSeat().getCategory().getName());

        if (user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString())) {
            booking = this.save(booking);
            if (this.confirmBooking(booking)) {
                if (booking.geteStatus() == Seat.EStatus.SEAT_SOLD) {
                    booking = this.approveBooking(booking);
                }else if(booking.geteStatus() == Seat.EStatus.SEAT_RESERVED){
                    booking = this.reserveBooking(booking);
                }
                return booking;
            }
        }
        return null;
    }

    @Override
    public Booking createServiceAgentBooking(Booking booking) {
        return null;
    }

    @Override
    public Page<Booking> getMySells(int page) {
        return this.bookingRepository.findByCreatedByIdAndCancelledFalse(SecurityConfig.getCurrentUser().getId(), PageAttr.getPageRequest(page));
    }

    @Override
    public void cancelReservation(Long bookingId) throws ParseException, NotFoundException, ForbiddenException, UserAlreadyExistsException, NullPasswordException, UserInvalidException {
        Booking booking = this.getOne(bookingId);
        System.out.println(booking);
        booking.setCancelled(true);
        this.clearBooking(booking);
        this.save(booking);
    }

    @Override
    public void cancelBooking(Long bookingId) throws ForbiddenException, NotFoundException, ParseException {
        User currentUser = SecurityConfig.getCurrentUser();
        Booking booking = this.getOne(bookingId);
        System.out.println(booking.getCreatedBy().getName() + ":" + booking.getCreatedBy().getRoles());
        System.out.println(currentUser.getRoles());
        if (!booking.isCancelled()) {
            if (booking.getCreatedBy().hasRole(Role.ERole.ROLE_ADMIN.toString())) {
                if (!currentUser.isAdmin()) throw new ForbiddenException("Access denied");
                this.adminSellSeatAccounting(booking, true);
            } else if (booking.getCreatedBy().hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString())) {
                if (!currentUser.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
                    throw new ForbiddenException("Access denied");
                this.serviceAdminSellSeatAccounting(booking, true);
            } else if (booking.getCreatedBy().hasRole(Role.ERole.ROLE_AGENT.toString())) {
                if (!currentUser.isAdmin()) throw new ForbiddenException("Access denied");
                this.adminAgentSellsSeatAccount(booking, true);
//            } else if (booking.getCreatedBy().hasRole(Role.ERole.ROLE_SERVICE_AGENT.toString())) {
//                if (!currentUser.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
//                    throw new ForbiddenException("Access denied");
//                this.subtractFromServiceAgentLedger(booking, true);
//            } else if (booking.getCreatedBy().hasRole(Role.ERole.ROLE_USER.toString())) {
//                if (!currentUser.isAdmin()) throw new ForbiddenException("Access denied");
//                this.userPaymentAccounting(booking, true);
//            }
            }
                this.clearBooking(booking);
                booking.setCancelled(true);
                this.bookingRepository.save(booking);

        }
    }

    private void clearBooking(Booking booking) throws ParseException, NotFoundException, ForbiddenException {
        for (SubBooking subBooking : booking.getSubBookingList()) {
            //Clear Booking and Status map
            this.seatService.clearSeatStatusAndBookingIdMap(subBooking.getSeat().getId(), subBooking.getDate(), booking);
        }
    }
}
