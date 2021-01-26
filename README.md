## User booking process

### Ship admin : createServiceAdminBooking(Booking)

    1) Security check if the user has the proper Role, 
        User ship list contain provided ship
    2) Calculate all sub-bookings (calculateSubBookingList())
    3) Calculate Booking (calculateBooking())
    4) Update booking ship and ship_name
    5) if confirmBooking,
        Loop for each SubBookings
            a)  check if the seat for SubBooking is available
                if, then Update SeatStatus And BookingMap return true
                else return confirm false
        else booking Cancelled = true, return "Booking cannot confirmed"
    6) if booking Status == SEAT_RESERVED, 
        then Approve Booking
        a)approveBooking()
            i) Update Booking status
            ii) for each subbooking Update StatusMap
            iii) Set booking confirm, paid, approved to true
            iv) then do serviceAdminSellSeatAccounting()
            
    7) if booking Status == SEAT_RESERVED, 
        then Reserve Booking
        a)reserveBooking()
            i) Update Booking status
            ii) for each subbooking Update StatusMap

### Ship Agent : createServiceAgentBooking(Booking)
    1) Security check if the user has the proper Role, 
            User ship list contain provided ship
    2) check service agent balance getServiceAgentBalance
    3) Calculate all sub-bookings (calculateSubBookingList())
    4) Calculate Booking (calculateBooking())
    5) Update booking ship and ship_name
    6) Caclulate shipAgentCommission
    7) if agentBalance < (booking.getTotalPayablePrice() - shipAgentCommission) 
        then return "In sufficient balace"
    8)  if confirmBooking,
           Loop for each SubBookings
               a)  check if the seat for SubBooking is available
                   if, then Update SeatStatus And BookingMap return true
                   else return confirm false
           else booking Cancelled = true, return "Booking cannot confirmed"
    9) if booking Status == SEAT_RESERVED, 
           then Approve Booking
           a)approveBooking()
               i) Update Booking status
               ii) for each subbooking Update StatusMap
               iii) Set booking confirm, paid, approved to true
               iv) then calculate agentDiscount and set booking AgentDiscount value
               v) then do shipAgentSellsSeatAccount()
    10) if booking Status == SEAT_RESERVED,
            then check if ship-agent has permission has to reserve, 
                else return "access denied" 
            then Reserve Booking
            a)reserveBooking()
                i) Update Booking status
                ii) for each subbooking Update StatusMap
            
// Disable all user login
UPDATE h_users SET account_non_expired=false WHERE account_non_expired=true; 

//Enable all user login
UPDATE h_users SET account_non_expired=true WHERE account_non_expired=false;

//show all activated user
SELECT * from h_users WHERE account_non_expired=true; 
