package com.greenride.controller.api;

import com.greenride.dto.CreateRideDTO;
import com.greenride.model.Ride;
import com.greenride.service.BookingService;
import com.greenride.service.RideService;
import com.greenride.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rides")
@Tag(name = "Rides & Bookings", description = "Endpoints for managing rides and bookings")
@SecurityRequirement(name = "Bearer Authentication")
public class ApiRideController {

    @Autowired private RideService rideService;
    @Autowired private BookingService bookingService;
    @Autowired private WeatherService weatherService;

    @Operation(summary = "Create a new Ride", description = "Drivers can post a new ride route.")
    @PostMapping
    public ResponseEntity<?> createRide(@Valid @RequestBody CreateRideDTO createRideDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Ride ride = rideService.createRide(createRideDto, username);
        return ResponseEntity.status(201).body("Ride created successfully with ID: " + ride.getId());
    }

    @Operation(summary = "Search for Rides", description = "Search by start location and destination.")
    @GetMapping("/search")
    public ResponseEntity<?> searchRides(
            @Parameter(description = "Starting city/location") @RequestParam String start,
            @Parameter(description = "Destination city/location") @RequestParam String dest) {
        List<Ride> rides = rideService.searchRides(start, dest);
        return ResponseEntity.ok(rides);
    }

    @Operation(summary = "Get Ride Details", description = "Fetches ride details along with Weather forecast (External Service).")
    @GetMapping("/{rideId}")
    public ResponseEntity<?> getRideDetails(@PathVariable Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        String weatherInfo = weatherService.getWeatherForecast(52.52, 13.41);
        return ResponseEntity.ok(Map.of("ride", ride, "weatherForecast", weatherInfo));
    }

    @Operation(summary = "Book a Seat", description = "Passengers can book a seat on a specific ride.")
    @PostMapping("/{rideId}/bookings")
    public ResponseEntity<?> bookSeat(@PathVariable Long rideId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            bookingService.bookRide(rideId, username);
            return ResponseEntity.ok("Booking confirmed!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "View Offered Rides", description = "History of rides created by the current driver.")
    @GetMapping("/my-offered-rides")
    public ResponseEntity<?> getMyOfferedRides() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(rideService.getRidesByDriver(username));
    }

    @Operation(summary = "View My Bookings", description = "History of bookings made by the current passenger.")
    @GetMapping("/my-bookings")
    public ResponseEntity<?> getMyBookings() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(bookingService.getMyBookings(username));
    }

    @Operation(summary = "Cancel Booking", description = "Cancel a booking. Must be done at least 10 mins before departure.")
    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            bookingService.cancelBooking(bookingId, username);
            return ResponseEntity.ok("Booking cancelled successfully. Seat restored.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}