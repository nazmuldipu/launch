package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.commons.Validator;
import com.ship.nazmul.ship.commons.utils.DateUtil;
import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.*;
import com.ship.nazmul.ship.entities.accountings.*;
import com.ship.nazmul.ship.exceptions.exists.UserAlreadyExistsException;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.UserInvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.exceptions.nullpointer.NullPasswordException;
import com.ship.nazmul.ship.repositories.BookingRepository;
import com.ship.nazmul.ship.services.*;
import com.ship.nazmul.ship.services.accountings.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ShipService shipService;
    private final CategoryService categoryService;
    private final SeatService seatService;
    private final AdminCashbookService adminCashbookService;
    private final ShipAdminCashbookService shipAdminCashbookService;
    private final ShipAdminLedgerService shipAdminLedgerService;
    private final ShipAgentLedgerService shipAgentLedgerService;
    //    private final AdminShipLedgerService adminShipLedgerService;
//    private final ShipCashBookService shipCashBookService;
    private final AdminAgentLedgerService adminAgentLedgerService;


    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, ShipService shipService, CategoryService categoryService, SeatService seatService, AdminCashbookService adminCashbookService, ShipAdminCashbookService shipAdminCashbookService, ShipAdminLedgerService shipAdminLedgerService, ShipAgentLedgerService shipAgentLedgerService, AdminAgentLedgerService adminAgentLedgerService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.shipService = shipService;
        this.categoryService = categoryService;
        this.seatService = seatService;
        this.adminCashbookService = adminCashbookService;
        this.shipAdminCashbookService = shipAdminCashbookService;
        this.shipAdminLedgerService = shipAdminLedgerService;
        this.shipAgentLedgerService = shipAgentLedgerService;
//        this.adminShipLedgerService = adminShipLedgerService;
//        this.shipCashBookService = shipCashBookService;
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
        booking.setShip(this.shipService.getOne(booking.getShip().getId()));
        booking.setShipName(booking.getShip().getName());
        if (user.hasRole(Role.ERole.ROLE_ADMIN.toString())) {
            booking = this.save(booking);
            if (this.confirmBooking(booking)) {
                if (booking.geteStatus() == Seat.EStatus.SEAT_SOLD) {
                    booking = this.approveBooking(booking);
                } else if (booking.geteStatus() == Seat.EStatus.SEAT_RESERVED) {
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
            int hotelswaveCommission = 0;
            for (int i = 0; i < booking.getSubBookingList().size(); i++) {
                hotelswaveCommission += booking.getShip().getHotelswaveCommission();
            }
            booking.setHotelswaveDiscount(hotelswaveCommission);
            this.adminSellSeatAccounting(booking, false);
        } else if (SecurityConfig.getCurrentUser().hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString())) {
            this.serviceAdminSellSeatAccounting(booking, false);
        } else if (SecurityConfig.getCurrentUser().hasRole(Role.ERole.ROLE_AGENT.toString())) {
            int hotelswaveCommission = 0;
            for (int i = 0; i < booking.getSubBookingList().size(); i++) {
                hotelswaveCommission += booking.getShip().getHotelswaveCommission();
            }
            booking.setHotelswaveDiscount(hotelswaveCommission / 2);
            booking.setHotelswaveAgentDiscount(hotelswaveCommission / 2);
            this.adminAgentSellsSeatAccount(booking, false);
        } else if (SecurityConfig.getCurrentUser().hasRole(Role.ERole.ROLE_SERVICE_AGENT.toString())) {
            int agentDiscount = 0;
            for (int i = 0; i < booking.getSubBookingList().size(); i++) {
                agentDiscount += SecurityConfig.getCurrentUser().getCommission();
            }
            booking.setAgentDiscount(agentDiscount);
            this.shipAgentSellsSeatAccount(booking, false);
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
        List<SubBooking> subBookingList = booking.getSubBookingList();
        for (SubBooking subBooking : subBookingList) {
            LocalDate date = subBooking.getDate();
            boolean av = this.seatService.checkSeatAvailability(subBooking.getLocalSeatId(), date);
            if (av) {
                this.seatService.updateSeatStatusAndBookingMap(subBooking.getLocalSeatId(), date, booking.geteStatus(), booking);
            } else {
                return false;
            }
        }
        return true;
    }

    /* 1) Admin_Cashbook debit total booking amount
     * 2) Admin_Ship_Ledger credit ( total amount - hotelswave discount)
     * */
    private void adminSellSeatAccounting(Booking booking, boolean cancel) throws NotFoundException {
        //1) Admin_Cashbook debit total booking amount
        Ship ship = booking.getShip();
        String explanation = (cancel ? "Cancel " : "") + ship.getShipNumber() + ":" + ship.getName() + " - Booking for booking id " + booking.getId();
        AdminCashbook adminCashbook;
        if (!cancel) {
            adminCashbook = new AdminCashbook(LocalDateTime.now(), explanation, booking.getTotalPayablePrice(), 0);
        } else {
            adminCashbook = new AdminCashbook(LocalDateTime.now(), explanation, 0, booking.getTotalPayablePrice());
        }
        adminCashbook.setApproved(true);
        adminCashbook.setRef(booking.getId().toString());
        this.adminCashbookService.updateBalanceAndSave(adminCashbook);

        //2) Admin_Hotel_Ledger credit ( total amount - hotelswave discount)
        int amount = booking.getTotalPayablePrice() - booking.getHotelswaveDiscount(); //deduct hotels wave percentages;
        ShipAdminLedger shipAdminLedger;
//        AdminShipLedger adminShipLedger;
        if (!cancel) {
            shipAdminLedger = new ShipAdminLedger(ship.getAdmin(), LocalDateTime.now(), explanation, 0, amount);
//            adminShipLedger = new AdminShipLedger(ship, new Date(), explanation, 0, amount);
        } else {
            shipAdminLedger = new ShipAdminLedger(ship.getAdmin(), LocalDateTime.now(), explanation, amount, 0);
//            adminShipLedger = new AdminShipLedger(ship, new Date(), explanation, amount, 0);
        }

        shipAdminLedger.setApproved(true);
        shipAdminLedger.setRef(booking.getId().toString());
        this.shipAdminLedgerService.addShipAdminLedger(ship.getAdmin().getId(), shipAdminLedger);
    }

    private void shipAgentSellsSeatAccount(Booking booking, boolean cancel) {
        int shipAgentCommission = booking.getAgentDiscount();
//        for (int i = 0; i < booking.getSubBookingList().size(); i++) {
//            shipAgentCommission += booking.getSubBookingList().get(i).getSeat().getCategory().getAgentDiscount();
//        }
        String explanation = (cancel ? "Cancel " : "") + "Booking for booking id " + booking.getId();
        //1) Debit ShipAgentLedger = total_advance - shipAgentCommission
        ShipAgentLedger shipAgentLedger;
        if (cancel) {
            shipAgentLedger = new ShipAgentLedger(booking.getCreatedBy(), LocalDateTime.now(), explanation, 0, booking.getTotalPayablePrice() - shipAgentCommission);
        } else {
            shipAgentLedger = new ShipAgentLedger(SecurityConfig.getCurrentUser(), LocalDateTime.now(), explanation, booking.getTotalPayablePrice() - shipAgentCommission, 0);
        }
        shipAgentLedger.setRef(booking.getId().toString());
        shipAgentLedger.setApproved(true);

        shipAgentLedger = this.shipAgentLedgerService.updateBalanceAndSave(shipAgentLedger.getAgent().getId(), shipAgentLedger);
    }

    private void adminAgentSellsSeatAccount(Booking booking, boolean cancel) throws NotFoundException {
        int commission = booking.getHotelswaveAgentDiscount();
        String explanation = (cancel ? "Cancel " : "") + "Booking for booking id " + booking.getId();

        //1) Debit AdminAgentLedger = total_advance - hotelswave_agent_discount
        AdminAgentLedger adminAgentLedger;
        if (cancel) {
            adminAgentLedger = new AdminAgentLedger(booking.getCreatedBy(), LocalDateTime.now(), explanation, 0, booking.getTotalPayablePrice() - commission);
        } else {
            adminAgentLedger = new AdminAgentLedger(SecurityConfig.getCurrentUser(), LocalDateTime.now(), explanation, booking.getTotalPayablePrice() - commission, 0);
        }
        adminAgentLedger.setRef(booking.getId().toString());
        adminAgentLedger.setApproved(true);
        adminAgentLedger = this.adminAgentLedgerService.updateBalanceAndSave(booking.getCreatedBy().getId(), adminAgentLedger);

        //2) Admin_Hotel_Ledger credit ( total amount - hotelswave discount - agent amount)
        int amount = booking.getTotalPayablePrice() - booking.getHotelswaveDiscount() - booking.getHotelswaveAgentDiscount();

        ShipAdminLedger shipAdminLedger;
        if (!cancel) {
            shipAdminLedger = new ShipAdminLedger(booking.getShip().getAdmin(), LocalDateTime.now(), explanation, 0, amount);
        } else {
            shipAdminLedger = new ShipAdminLedger(booking.getShip().getAdmin(), LocalDateTime.now(), explanation, amount, 0);
        }
        shipAdminLedger.setApproved(true);
        shipAdminLedger.setRef(booking.getId().toString());
        this.shipAdminLedgerService.addShipAdminLedger(booking.getShip().getAdmin().getId(), shipAdminLedger);
    }

    private void serviceAdminSellSeatAccounting(Booking booking, boolean cancel) {
        Ship ship = booking.getShip();
        String explanation = (cancel ? "Cancel" : "") + "Booking for booking id " + booking.getId();
        //1)Add amount to shipCashbook
//        ShipCashBook shipCashBook;
        ShipAdminCashbook shipAdminCashbook;
        if (!cancel) {
            shipAdminCashbook = new ShipAdminCashbook(LocalDateTime.now(), ship.getAdmin(), explanation, booking.getTotalPayablePrice(), 0);
//            shipCashBook = new ShipCashBook(ship, new Date(), explanation, booking.getTotalPayablePrice(), 0);
        } else {
            shipAdminCashbook = new ShipAdminCashbook(LocalDateTime.now(), ship.getAdmin(), explanation, 0, booking.getTotalPayablePrice());
//            shipCashBook = new ShipCashBook(ship, new Date(), explanation, 0, booking.getTotalPayablePrice());
        }
        shipAdminCashbook.setApproved(true);
        shipAdminCashbook = this.shipAdminCashbookService.debitAmount(shipAdminCashbook);
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
        totalDiscount += booking.getBookingDiscount();
        booking.setTotalFare(totalFare);
        booking.setTotalDiscount(totalDiscount);
        booking.setTotalCommission(totalCommission);
        booking.setTotalPayablePrice(totalFare - totalDiscount - booking.getPromotionDiscount());
        return booking;
    }

    List<SubBooking> calculateSubBookingList(List<SubBooking> subBookingList) throws NotFoundException, ParseException {
        List<SubBooking> newSubBookingList = new ArrayList<>();
        for (SubBooking subBooking : subBookingList) {
            subBooking.setLocalSeatId(subBooking.getSeat().getId());
            Seat seat = this.seatService.getOne(subBooking.getLocalSeatId());
            long cid = subBooking.getCategoryId();
            subBooking.setSeat(seat);
            subBooking = this.calculateSubBooking(subBooking, cid);
            newSubBookingList.add(subBooking);
        }
        return newSubBookingList;
    }

    SubBooking calculateSubBooking(SubBooking subBooking, Long categoryId) {
        LocalDate ld = subBooking.getDate();
        Integer discount = this.categoryService.getDiscount(categoryId, ld);//subBooking.getDiscount();
        if (discount == null) {
            discount = subBooking.getDiscount();
        }
        subBooking.setDiscount(discount);
        subBooking.setPayablePrice(subBooking.getFare() - subBooking.getDiscount());
        return subBooking;
    }

    @Override
    public Booking createAdminAgentBooking(Booking booking) throws ForbiddenException, NotFoundException, ParseException, UserAlreadyExistsException, NullPasswordException, UserInvalidException {
        //1) Security check if user has sufficient permission for this action
        User user = SecurityConfig.getCurrentUser();
        if (!user.hasRole(Role.ERole.ROLE_AGENT.toString())) throw new ForbiddenException("Access denied");

        int agentBalance = this.adminAgentLedgerService.getAdminAgentBalance(user.getId());

        // 2) Add subBookingList to booking and Calculate Booking
        booking.setSubBookingList(this.calculateSubBookingList(booking.getSubBookingList()));
        booking = this.calculateBooking(booking);
        booking.setShip(this.shipService.getOne(booking.getShip().getId()));
        booking.setShipName(booking.getShip().getName());
//        booking.setCategoryName(booking.getSubBookingList().get(0).getSeat().getCategory().getName());

        int agentCommission = booking.getShip().getHotelswaveCommission() / 2;

        if (agentBalance > (booking.getTotalPayablePrice() - agentCommission)) {
            if (user.hasRole(Role.ERole.ROLE_AGENT.toString())) {
                booking = this.save(booking);
                if (this.confirmBooking(booking)) {
                    if (booking.geteStatus() == Seat.EStatus.SEAT_SOLD) {
                        booking = this.approveBooking(booking);
                    } else if (booking.geteStatus() == Seat.EStatus.SEAT_RESERVED) {
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
        Set<Ship> userShips = user.getShips();
        Ship ship = this.shipService.getOne(booking.getShip().getId());

        boolean valid = false;
        for(Ship sh : userShips){
            if(sh.getId().equals(ship.getId())){
                valid = true;
            }
        }
        if (!user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()) || !valid) throw new ForbiddenException("Access denied");

        // 2) Add subBookingList to booking and Calculate Booking
        booking.setSubBookingList(this.calculateSubBookingList(booking.getSubBookingList()));
        booking = this.calculateBooking(booking);
        booking.setShip(this.shipService.getOne(booking.getShip().getId()));
        booking.setShipName(booking.getShip().getName());
//        booking.setCategoryName(booking.getSubBookingList().get(0).getSeat().getCategory().getName());

        if (user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString())) {
            booking = this.save(booking);
            if (this.confirmBooking(booking)) {
                if (booking.geteStatus() == Seat.EStatus.SEAT_SOLD) {
                    booking = this.approveBooking(booking);
                } else if (booking.geteStatus() == Seat.EStatus.SEAT_RESERVED) {
                    booking = this.reserveBooking(booking);
                }
                return booking;
            }
        }
        return null;
    }

    @Override
    public Booking createServiceAgentBooking(Booking booking) throws ForbiddenException, NotFoundException, ParseException, UserAlreadyExistsException, NullPasswordException, UserInvalidException {
        //1) Security check if user has sufficient permission for this action
        User user = SecurityConfig.getCurrentUser();
        Set<Ship> userShips = user.getShips();
        Ship ship = this.shipService.getOne(booking.getShip().getId());

        boolean valid = false;
        for(Ship sh : userShips){
            if(sh.getId().equals(ship.getId())){
                valid = true;
            }
        }
        if (!user.hasRole(Role.ERole.ROLE_SERVICE_AGENT.toString()) || !valid) throw new ForbiddenException("Access denied");

        int agentBalance = this.shipAgentLedgerService.getServiceAgentBalance(user.getId());
        // 2) Add subBookingList to booking and Calculate Booking
        booking.setSubBookingList(this.calculateSubBookingList(booking.getSubBookingList()));
        booking = this.calculateBooking(booking);
        booking.setShip(ship);
        booking.setShipName(booking.getShip().getName());
//        booking.setCategoryName(booking.getSubBookingList().get(0).getSeat().getCategory().getName());

        int shipAgentCommission = 0;
        for (int i = 0; i < booking.getSubBookingList().size(); i++) {
            shipAgentCommission += user.getCommission();
        }

        if (agentBalance > (booking.getTotalPayablePrice() - shipAgentCommission)) {
            booking = this.save(booking);
            if (this.confirmBooking(booking)) {
                booking.seteStatus(Seat.EStatus.SEAT_SOLD);
                booking = this.approveBooking(booking);
                return booking;
            }
        } else {
            throw new ForbiddenException("In sufficient balace");
        }
        return null;
    }

    @Override
    public Page<Booking> getMySells(int page) {
        return this.bookingRepository.findByCreatedByIdAndCancelledFalse(SecurityConfig.getCurrentUser().getId(), PageAttr.getPageRequest(page));
    }

    @Override
    public void cancelReservation(Long bookingId) throws ParseException, NotFoundException, ForbiddenException, UserAlreadyExistsException, NullPasswordException, UserInvalidException {
        Booking booking = this.getOne(bookingId);
        booking.setCancelled(true);
        this.clearBooking(booking);
        this.save(booking);
    }

    /*Remove a seat from a booking without calculation part
     * @param seatId     id of seat that to be remove
     * @param bookingId  id of booking that to be remove from
     *
     * Steps : 1) Clear seat status map and booking id map
     *         2) Update discounts
     *         3) Recalculate all subBooking and Booking object
     *         */
    @Override
    public void cancelReservationSeat(Long seatId, Long bookingId) throws NotFoundException, ForbiddenException, ParseException, UserAlreadyExistsException, NullPasswordException, UserInvalidException {
        Booking booking = this.getOne(bookingId);

        //Get SubBooking to be remove
        SubBooking subBooking = booking.getSubBookingList().stream()
                .filter(sb -> Objects.equals(sb.getSeat().getId(), seatId))
                .findAny()
                .get();

        //Get new subBooking list after remove SubBooking with provide seat Id
        List<SubBooking> subBookingList = booking.getSubBookingList().stream()
                .filter(sb -> !sb.getSeat().getId().equals(seatId))
                .collect(Collectors.toList());

        this.seatService.clearSeatStatusAndBookingIdMap(seatId, subBooking.getDate(), booking);

        //Update discounts and commissions
        booking.setBookingDiscount(booking.getBookingDiscount() - (booking.getBookingDiscount() / booking.getSubBookingList().size()));
        booking.setHotelswaveDiscount(booking.getHotelswaveDiscount() - (booking.getHotelswaveDiscount() / booking.getSubBookingList().size()));
        booking.setHotelswaveAgentDiscount(booking.getHotelswaveAgentDiscount() - (booking.getHotelswaveAgentDiscount() / booking.getSubBookingList().size()));
        booking.setAgentDiscount(booking.getAgentDiscount() - (booking.getAgentDiscount() / booking.getSubBookingList().size()));

        //Recalculate and update booking
        booking.setSubBookingList(this.calculateSubBookingList(subBookingList));
        booking = this.calculateBooking(booking);
        this.save(booking);
    }

    /*Remove a list of seat from booking without accounting
     * @param booingId   id of booking that to be remove from
     * @param seatIds    id's of seat that to be remove
     * */
    @Override
    public void cancelReservationSeats(Long bookingId, List<Long> seatIds) throws UserInvalidException, ForbiddenException, ParseException, NullPasswordException, NotFoundException, UserAlreadyExistsException {
        for (Long seatId : seatIds) {
            this.cancelReservationSeat(seatId, bookingId);
        }
    }

    @Override
    public void cancelBooking(Long bookingId) throws ForbiddenException, NotFoundException, ParseException {
        User currentUser = SecurityConfig.getCurrentUser();
        Booking booking = this.getOne(bookingId);
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
            } else if (booking.getCreatedBy().hasRole(Role.ERole.ROLE_SERVICE_AGENT.toString())) {
                if (!currentUser.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
                    throw new ForbiddenException("Access denied");
                this.shipAgentSellsSeatAccount(booking, true);
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

    /*Remove a list of seat from booking with accounting
     * @param booingId   id of booking that to be remove from
     * @param seatIds    id's of seat that to be remove
     * */
    @Override
    public void cancelBookingSeats(Long bookingId, List<Long> seatIds) throws ForbiddenException, UserInvalidException, NullPasswordException, ParseException, UserAlreadyExistsException, NotFoundException {
        User currentUser = SecurityConfig.getCurrentUser();
        if (!currentUser.isAdmin() && !currentUser.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
            throw new ForbiddenException("Access denied");

        Booking booking = this.getOne(bookingId);
        if (!booking.isCancelled()) {
            if (booking.getCreatedBy().hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString())) {
                this.removeServiceAdminBookingSeatsAccounting(booking, seatIds);
            } else if (booking.getCreatedBy().isAdmin()) {
                this.removeAdminBookingSeatsAccounting(booking, seatIds);
            } else if (booking.getCreatedBy().hasRole(Role.ERole.ROLE_AGENT.toString())) {
                this.removeAdminAgentBookingSeatsAccounting(booking, seatIds);
            } else if (booking.getCreatedBy().hasRole(Role.ERole.ROLE_SERVICE_AGENT.toString())) {
                this.removeServiceAgentBookingSeatsAccounting(booking, seatIds);
            }

            this.cancelReservationSeats(booking.getId(), seatIds);
        }
    }

    @Override
    public List<Booking> getBookingListByShipIdAndDate(Long shipId, LocalDate date) {
        return this.bookingRepository.findDistinctByShipIdAndSubBookingListDateAndCancelledFalse(shipId, date);
    }

    @Override
    public List<Booking> getBookingListByCreatedIdAndShipIdAndDate(Long userId, Long shipId, Date date) {
        Date begin = DateUtil.getDayStart(date);
        Date end = DateUtil.getDayEnd(date);
        List<Booking> bookingList =this.bookingRepository.findDistinctByCreatedBetweenAndCreatedByIdAndShipIdAndCancelledFalse(begin, end, userId, shipId);
        return bookingList;
    }

    @Override
    public List<Booking> getReservationListByUserIdShipIdAndDate(Long userId, Long shipId, LocalDate date) {
        return this.bookingRepository.findDistinctByCreatedByIdAndShipIdAndSubBookingListDateAndCancelledFalse(userId, shipId, date);
    }

    /*Remove sold booking seats from booking for SERVICE_ADMIN
     * @param booking    booking object that from remove
     * @param setIds     list of seat id's that to be remove
     *
     * step: 1) Credit amount from ShipAdminCashbook*/
    private void removeServiceAdminBookingSeatsAccounting(Booking booking, List<Long> seatIds) throws NotFoundException, ForbiddenException, ParseException, NullPasswordException, UserInvalidException, UserAlreadyExistsException {
        //Accounting parts : remove seats price from Service admin cash book
        String seatList = "";
        int amount = 0;
        for (SubBooking sb : booking.getSubBookingList()) {
            if (seatIds.contains(sb.getSeat().getId())) {
                seatList += sb.getSeat().getSeatNumber() + ", ";
                amount += sb.getPayablePrice();
            }
        }
        amount -= (seatIds.size() * (booking.getBookingDiscount() / booking.getSubBookingList().size()));
        String explanation = "Cancel Partial booking seats " + seatList + " for booking id " + booking.getId();
        ShipAdminCashbook shipAdminCashbook;
        shipAdminCashbook = new ShipAdminCashbook(LocalDateTime.now(), booking.getShip().getAdmin(), explanation, 0, amount);
        shipAdminCashbook.setApproved(true);
        shipAdminCashbook = this.shipAdminCashbookService.debitAmount(shipAdminCashbook);

        return;
    }

    /*Remove sold booking seats from booking for SERVICE_AGENT
     * @param booking    booking object that from remove
     * @param setIds     list of seat id's that to be remove
     *
     * step: 1) Credit amount from ShipAdminCashbook*/
    private void removeServiceAgentBookingSeatsAccounting(Booking booking, List<Long> seatIds) {
        //Accounting parts : remove seats price from Service admin cash book
        String seatList = "";
        int amount = 0;
        for (SubBooking sb : booking.getSubBookingList()) {
            if (seatIds.contains(sb.getSeat().getId())) {
                seatList += sb.getSeat().getSeatNumber() + ", ";
                amount += sb.getPayablePrice();
            }
        }
        amount -= (seatIds.size() * booking.getCreatedBy().getCommission());

        //1) Credit amount from ShipAdminCashbook
        String explanation = "Cancel Partial booking seats " + seatList + " for booking id " + booking.getId();
        ShipAgentLedger shipAgentLedger = new ShipAgentLedger(booking.getCreatedBy(), LocalDateTime.now(), explanation, 0, amount);
        shipAgentLedger.setRef(booking.getId().toString());
        shipAgentLedger.setApproved(true);
        shipAgentLedger = this.shipAgentLedgerService.updateBalanceAndSave(booking.getCreatedBy().getId(), shipAgentLedger);
        return;
    }


    /*Remove sold booking seats from booking for ADMIN
     * @param booking    booking object that from remove
     * @param setIds     list of seat id's that to be remove
     *
     * step: 1) Credit amount from AdminCashbook
     *       2) Debit amount from ShipAdminLedger after deducting hotelswave ledger*/
    private void removeAdminBookingSeatsAccounting(Booking booking, List<Long> seatIds) throws NotFoundException {
        String seatList = "";
        int amount = 0;
        for (SubBooking sb : booking.getSubBookingList()) {
            if (seatIds.contains(sb.getSeat().getId())) {
                seatList += sb.getSeat().getSeatNumber() + ", ";
                amount += sb.getPayablePrice();
            }
        }
        amount -= (seatIds.size() * (booking.getBookingDiscount() / booking.getSubBookingList().size()));

        //1) Credit amount from AdminCashbook
        String explanation = "Cancel Partial booking seats " + seatList + " for booking id " + booking.getId();
        AdminCashbook adminCashbook = new AdminCashbook(LocalDateTime.now(), explanation, 0, amount);
        adminCashbook.setApproved(true);
        adminCashbook.setRef(booking.getId().toString());
        this.adminCashbookService.updateBalanceAndSave(adminCashbook);

        //2) Debit amount from ShipAdminLedger after deducting hotelswave ledger
        amount -= (seatIds.size() * (booking.getHotelswaveDiscount() / booking.getSubBookingList().size()));
        ShipAdminLedger shipAdminLedger = new ShipAdminLedger(booking.getShip().getAdmin(), LocalDateTime.now(), explanation, amount, 0);
        shipAdminLedger.setApproved(true);
        shipAdminLedger.setRef(booking.getId().toString());
        this.shipAdminLedgerService.addShipAdminLedger(booking.getShip().getAdmin().getId(), shipAdminLedger);
    }

    /*Remove sold booking seats from booking for ADMIN_AGENT
     * @param booking    booking object that from remove
     * @param setIds     list of seat id's that to be remove
     *
     * step: 1) Credit amount into AdminAgentLedger
     *       2) Debit amount from ShipAdminLedger
     *       */
    private void removeAdminAgentBookingSeatsAccounting(Booking booking, List<Long> seatIds) throws NotFoundException {
        int size = booking.getSubBookingList().size();
        String seatList = "";
        int amount = 0;
        for (SubBooking sb : booking.getSubBookingList()) {
            if (seatIds.contains(sb.getSeat().getId())) {
                seatList += sb.getSeat().getSeatNumber() + ", ";
                amount += sb.getPayablePrice();
            }
        }
        amount -= (seatIds.size() * (booking.getBookingDiscount() / size));

        //1) Credit amount into AdminAgentLedger
        int commission = seatIds.size() * (booking.getHotelswaveAgentDiscount() / size);
        String explanation = "Cancel Partial booking seats " + seatList + " for booking id " + booking.getId();
        AdminAgentLedger adminAgentLedger = new AdminAgentLedger(booking.getCreatedBy(), LocalDateTime.now(), explanation, 0, amount - commission);
        adminAgentLedger.setRef(booking.getId().toString());
        adminAgentLedger.setApproved(true);
        adminAgentLedger = this.adminAgentLedgerService.updateBalanceAndSave(booking.getCreatedBy().getId(), adminAgentLedger);


        //2) Debit amount from ShipAdminLedger
        ShipAdminLedger shipAdminLedger = new ShipAdminLedger(booking.getShip().getAdmin(), LocalDateTime.now(), explanation, amount - (2 * commission), 0);
        shipAdminLedger.setApproved(true);
        shipAdminLedger.setRef(booking.getId().toString());
        this.shipAdminLedgerService.addShipAdminLedger(booking.getShip().getAdmin().getId(), shipAdminLedger);

    }

    private void clearBooking(Booking booking) throws ParseException, NotFoundException, ForbiddenException {
        for (SubBooking subBooking : booking.getSubBookingList()) {
            //Clear Booking and Status map
            this.seatService.clearSeatStatusAndBookingIdMap(subBooking.getSeat().getId(), subBooking.getDate(), booking);
        }
    }
}
