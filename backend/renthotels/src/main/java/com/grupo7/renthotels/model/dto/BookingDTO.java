package com.grupo7.renthotels.model.dto;

import com.grupo7.renthotels.model.Booking;
import com.grupo7.renthotels.model.Product;
import com.grupo7.renthotels.model.User;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookingDTO {

    private Long bookingSku;

    private LocalDate bookingStartDate;

    private LocalDate bookingEndDate;

    private String bookingCheckInTime;

    private Float bookingPrice;

    private String originCity;

    private ProductDTO productDTO;

    private UserDTO userDTO;

    public static BookingDTO from(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setBookingSku(booking.getBookingSku());
        bookingDTO.setBookingStartDate(booking.getBookingStartDate());
        bookingDTO.setBookingEndDate(booking.getBookingEndDate());
        bookingDTO.setBookingCheckInTime(booking.getBookingCheckInTime());
        bookingDTO.setBookingPrice(booking.getBookingPrice());
        bookingDTO.setOriginCity(booking.getOriginCity());
        bookingDTO.setProductDTO(ProductDTO.from(booking.getProduct()));
        bookingDTO.setUserDTO(UserDTO.from(booking.getUser()));
        return bookingDTO;
    }

    public Booking toEntity(){
        Booking booking = new Booking();
        booking.setBookingSku(this.bookingSku);
        booking.setBookingStartDate(this.bookingStartDate);
        booking.setBookingEndDate(this.bookingEndDate);
        booking.setBookingCheckInTime(this.bookingCheckInTime);
        booking.setBookingPrice(this.bookingPrice);
        booking.setOriginCity(this.originCity);
        booking.setProduct(this.productDTO.toEntity());
        booking.setUser(this.userDTO.toEntity());
        return booking;
    }
}
