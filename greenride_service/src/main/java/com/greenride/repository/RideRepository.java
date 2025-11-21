// In: com.greenride.repository.RideRepository.java
package com.greenride.repository;

import com.greenride.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Long> {
    // Spring Data JPA will let you create custom queries here later,
    // e.g., List<Ride> findByStartLocationAndDestination(...)
}