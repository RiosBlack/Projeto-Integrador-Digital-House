package com.grupo7.renthotels.service;

import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.model.Booking;
import com.grupo7.renthotels.model.Product;
import com.grupo7.renthotels.model.User;
import com.grupo7.renthotels.model.dto.BookingDTO;
import com.grupo7.renthotels.model.dto.ProductDTO;
import com.grupo7.renthotels.repository.BookingRepository;
import com.grupo7.renthotels.repository.ProductRepository;
import com.grupo7.renthotels.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    public ResponseEntity<BookingDTO> saveBooking(BookingDTO bookingDTO, String token) throws NotFoundException {
        if (!tokenService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Product product = productRepository.findByProductSku(bookingDTO.getProductDTO().getProductSku())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        List<ProductDTO> productsAvailableAt = productService.findByAvailability(bookingDTO.getBookingStartDate(), bookingDTO.getBookingEndDate());
        Optional<ProductDTO> availableSelectedProduct = productsAvailableAt
                .stream()
                .filter(p -> p.getProductSku().equals(bookingDTO.getProductDTO().getProductSku()))
                .findFirst();

        if (availableSelectedProduct.isEmpty())
            throw new NotFoundException("Produto não disponível");

        Booking booking = bookingDTO.toEntity();
        booking.setProduct(product);

        Optional<User> user = userRepository.findByUserSku(bookingDTO.getUserDTO().getUserSku());
        booking.setUser(user.get());

        DateTime initialDate = DateTime.parse(booking.getBookingStartDate().toString());
        DateTime finalDate = DateTime.parse(booking.getBookingEndDate().toString());

        Integer countDays = Days.daysBetween(initialDate,finalDate).getDays();
        Float bookingPrice = countDays * booking.getProduct().getProductPrice();
        booking.setBookingPrice(bookingPrice);

        booking = bookingRepository.save(booking);
        return ResponseEntity.ok(BookingDTO.from(booking));
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingDTO::from)
                .collect(Collectors.toList());
    }

    public ResponseEntity<BookingDTO> getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(BookingDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Void> deleteBooking(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            bookingRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<BookingDTO> getBookingBySku(Long sku) {
        return bookingRepository.findByBookingSku(sku)
                .map(BookingDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Void> deleteBookingBySku(Long sku) {
        Optional<Booking> booking = bookingRepository.findByBookingSku(sku);
        if (booking.isPresent()) {
            bookingRepository.deleteById(booking.get().getIdBooking());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<BookingDTO>> getByUserSku(Long userSku) {
        List<Booking> bookings = bookingRepository.findByUserUserSku(userSku);
        List<BookingDTO> bookingsDTO = bookings.stream().map(BookingDTO::from).collect(Collectors.toList());
        return ResponseEntity.ok(bookingsDTO);
    }

    public ResponseEntity<List<BookingDTO>> getByProductSku(Long productSku) {
        List<Booking> bookingsBySkuProduct = bookingRepository.findByProductProductSku(productSku);
        List<BookingDTO> bookingsDTO = bookingsBySkuProduct.stream().map(BookingDTO::from).collect(Collectors.toList());
        return ResponseEntity.ok(bookingsDTO);
    }
}
