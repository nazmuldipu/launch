package com.ship.nazmul.ship.mapper;

import com.ship.nazmul.ship.dto.ShipDto;
import com.ship.nazmul.ship.dto.TicketDto;
import com.ship.nazmul.ship.dto.UserDto;
import com.ship.nazmul.ship.entities.Booking;
import com.ship.nazmul.ship.entities.Seat;
import com.ship.nazmul.ship.entities.SubBooking;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TicketMapperImpl implements TicketMapper {
    @Override
    public TicketDto toTicket(Booking booking) {
        if (booking == null) {
            return null;
        }

        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(booking.getId());
        if (booking.isCancelled() || (!booking.isConfirmed() && !booking.geteStatus().equals(Seat.EStatus.SEAT_RESERVED))) {
            ticketDto.setCancelled(booking.isCancelled());
            ticketDto.setConfirmed(booking.isConfirmed());
        } else {
            ticketDto.setCancelled(booking.isCancelled());
            ticketDto.setConfirmed(booking.isConfirmed());
            ticketDto.setApproved(booking.isApproved());
            ticketDto.setPaid(booking.isPaid());
            ticketDto.setCreated(booking.getCreated());
            ticketDto.setCreatedBy(new UserDto(booking.getCreatedBy().getName(), booking.getCreatedBy().getUsername(), booking.getCreatedBy().getPhoneNumber()));
            ticketDto.setUser(new UserDto(booking.getUser().getName(), booking.getUser().getUsername(), booking.getUser().getPhoneNumber()));
            ticketDto.setShip(new ShipDto(booking.getShip().getShipNumber(), booking.getShip().getShipName(), booking.getShip().getName(),
                    booking.getShip().getStartingPoint(), booking.getShip().getDroppingPoint(), booking.getShip().getStartTime()));
            ticketDto.setTotalFare(booking.getTotalFare());
            ticketDto.setTotalDiscount(booking.getTotalDiscount());
            ticketDto.setTotalCommission(booking.getTotalCommission());
            ticketDto.setTotalPayablePrice(booking.getTotalPayablePrice());
            ticketDto.seteStatus(booking.geteStatus());
            //Populate subBookingList
            List<SubBooking> subBookingList = new ArrayList<>();
            final int[] categoryPrice = {0};
            final int[] categoryPriority = {0};
            booking.getSubBookingList().forEach(sb -> {
                SubBooking subBooking = new SubBooking(sb.getDate(), sb.getSeatNumber(), sb.getFare(), sb.getDiscount(), sb.getPayablePrice());
                subBookingList.add(subBooking);
                if (categoryPrice[0] == 0) {
                    ticketDto.setCategory(sb.getSeat().getCategory().getName());
                    categoryPrice[0] = sb.getSeat().getCategory().getFare();
                    categoryPriority[0] = sb.getSeat().getCategory().getPriority();
                } else if (categoryPrice[0] > sb.getSeat().getCategory().getFare()) {
                    ticketDto.setCategory(sb.getSeat().getCategory().getName());
                    categoryPrice[0] = sb.getSeat().getCategory().getFare();
                    categoryPriority[0] = sb.getSeat().getCategory().getPriority();
                } else if (categoryPrice[0] == sb.getSeat().getCategory().getFare()) {
                    if (categoryPriority[0] > sb.getSeat().getCategory().getPriority()) {
                        ticketDto.setCategory(sb.getSeat().getCategory().getName());
                        categoryPrice[0] = sb.getSeat().getCategory().getFare();
                    }
                }
            });
            ticketDto.setSubBookingList(subBookingList);
        }
        return ticketDto;
    }
}
