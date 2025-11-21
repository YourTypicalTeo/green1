// In: com.greenride.repository.BookingRepository.java
package com.greenride.repository;

import com.greenride.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // ...
}