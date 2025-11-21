package com.greenride.service;

import com.greenride.model.Booking;
import com.greenride.model.Ride;
import com.greenride.model.User;
import com.greenride.repository.BookingRepository;
import com.greenride.repository.RideRepository;
import com.greenride.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired private RideRepository rideRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private UserRepository userRepository;

    @Transactional
    public void bookRide(Long rideId, String passengerUsername) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        User passenger = userRepository.findByUsername(passengerUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (ride.getDriver().getId().equals(passenger.getId())) {
            throw new RuntimeException("Driver cannot book their own ride.");
        }

        if (ride.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available for this ride!");
        }

        Booking booking = new Booking();
        booking.setRide(ride);
        booking.setPassenger(passenger);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("CONFIRMED");

        bookingRepository.save(booking);

        ride.setAvailableSeats(ride.getAvailableSeats() - 1);
        rideRepository.save(ride);
    }

    public List<Booking> getMyBookings(String username) {
        return bookingRepository.findByPassenger_Username(username);
    }

    @Transactional
    public void cancelBooking(Long bookingId, String username) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getPassenger().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to cancel this booking.");
        }

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Booking is already cancelled.");
        }

        LocalDateTime departureTime = booking.getRide().getDepartureTime();
        LocalDateTime now = LocalDateTime.now();
        long minutesUntilDeparture = Duration.between(now, departureTime).toMinutes();

        if (minutesUntilDeparture < 10) {
            throw new RuntimeException("Cannot cancel within 10 minutes of departure.");
        }

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        Ride ride = booking.getRide();
        ride.setAvailableSeats(ride.getAvailableSeats() + 1);
        rideRepository.save(ride);
    }
}