package com.greenride.repository;

import com.greenride.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByStartLocationContainingIgnoreCaseAndDestinationContainingIgnoreCase(String startLocation, String destination);
    List<Ride> findByDriver_Username(String username);
}