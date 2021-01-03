package com.ship.nazmul.ship.mapper;

import com.ship.nazmul.ship.dto.TicketDto;
import com.ship.nazmul.ship.entities.Booking;

public interface TicketMapper {
    TicketDto toTicket(Booking booking);
}
