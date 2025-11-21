package com.greenride.service;

import com.greenride.dto.CreateRideDTO;
import com.greenride.model.Ride;
import com.greenride.model.User;
import com.greenride.repository.RideRepository;
import com.greenride.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RideServiceImpl implements RideService {

    @Autowired private RideRepository rideRepository;
    @Autowired private UserRepository userRepository;

    @Override
    @Transactional
    public Ride createRide(CreateRideDTO dto, String driverUsername) {
        User driver = userRepository.findByUsername(driverUsername)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Ride ride = new Ride();
        ride.setStartLocation(dto.startLocation());
        ride.setDestination(dto.destination());
        ride.setDepartureTime(dto.departureTime());
        ride.setAvailableSeats(dto.availableSeats());
        ride.setDriver(driver);

        return rideRepository.save(ride);
    }

    @Override
    public List<Ride> searchRides(String start, String destination) {
        return rideRepository.findByStartLocationContainingIgnoreCaseAndDestinationContainingIgnoreCase(start, destination);
    }

    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found with ID: " + rideId));
    }

    @Override
    public List<Ride> getRidesByDriver(String driverUsername) {
        return rideRepository.findByDriver_Username(driverUsername);
    }
}