package jeaguirre.me.domains.ports;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jeaguirre.me.adapters.outputs.persistence.entities.Payment;

public interface PaymentRepository {

    void save(Payment payment);

    Optional<Payment> findById(int id);

    void update(Payment payment);

    void deleteById(int id);

    List<Payment> findByStatus(String status);

    List<Payment> findByPayerId(int payerId);

    List<Payment> findByPayeeId(int payeeId);

    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}
