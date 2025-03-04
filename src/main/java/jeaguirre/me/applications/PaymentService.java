package jeaguirre.me.applications;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jeaguirre.me.adapters.outputs.persistence.entities.Payment;
import jeaguirre.me.domains.ports.PaymentRepository;

@ApplicationScoped
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Inject
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public Payment getPaymentById(int id) {
        return paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public void update(Payment payment) {
        paymentRepository.update(payment);
    }

    public void deleteById(int id) {
        paymentRepository.deleteById(id);
    }

    public List<Payment> findByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }

    public List<Payment> findByPayerId(int payerId) {
        return paymentRepository.findByPayerId(payerId);
    }

    public List<Payment> findByPayeeId(int payeeId) {
        return paymentRepository.findByPayeeId(payeeId);
    }

    public List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }
}
