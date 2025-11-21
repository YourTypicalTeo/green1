// In: com.greenride.controller.api.ApiRideController.java
package com.greenride.controller.api;

import com.greenride.dto.CreateRideDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rides")
public class ApiRideController {

    @PostMapping
    public ResponseEntity<?> createRide(@Valid @RequestBody CreateRideDTO createRideDto) {
        // TODO: Call RideService to create a ride
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchRides(@RequestParam String start, @RequestParam String dest) {
        // TODO: Call RideService to find rides
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<?> getRideDetails(@PathVariable Long rideId) {
        // TODO: Call RideService to get ride by ID
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{rideId}/bookings")
    public ResponseEntity<?> bookSeat(@PathVariable Long rideId) {
        // TODO: Call BookingService to create a booking
        return ResponseEntity.ok().build();
    }
}