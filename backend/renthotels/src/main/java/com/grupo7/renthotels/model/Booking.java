package com.grupo7.renthotels.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_booking", nullable = false, unique = true)
    private Long idBooking;

    @Column(name = "booking_sku", nullable = false, unique = true)
    private Long bookingSku;

    @Column(name = "booking_start_date", nullable = false)
    private LocalDate bookingStartDate;

    @Column(name = "booking_end_date", nullable = false)
    private LocalDate bookingEndDate;

    @Column(name = "booking_checkin_time", length = 5)
    private String bookingCheckInTime;

    @Column(name = "booking_price")
    private Float bookingPrice;

    @Column(name = "origin_city")
    private String originCity;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    public void setBookingSku(Long bookingSku) { this.bookingSku = createSku(); }

    public void setExistingBookingSku(Long bookingSku) { this.bookingSku = bookingSku; }

    public Long createSku() {
        long generated = (long) (Math.random() * 9999999L);
        return bookingSku = (generated < 0 ? generated * -1 : generated);
    }

    @Builder
    public Booking(LocalDate bookingStartDate, LocalDate bookingEndDate, String bookingCheckInTime, Float bookingPrice) {
        this.bookingStartDate = bookingStartDate;
        this.bookingEndDate = bookingEndDate;
        this.bookingCheckInTime = bookingCheckInTime;
        this.bookingPrice = bookingPrice;
    }
}
