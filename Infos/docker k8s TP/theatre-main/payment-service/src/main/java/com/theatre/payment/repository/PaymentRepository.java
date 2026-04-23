package com.theatre.payment.repository;

import com.theatre.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaypalOrderId(String paypalOrderId);
    Optional<Payment> findByReservationId(Long reservationId);
}
