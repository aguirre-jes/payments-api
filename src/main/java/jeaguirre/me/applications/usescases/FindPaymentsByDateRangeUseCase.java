package jeaguirre.me.applications.usescases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jeaguirre.me.adapters.outputs.persistence.entities.Payment;
import jeaguirre.me.applications.PaymentService;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class FindPaymentsByDateRangeUseCase {
    private final PaymentService paymentService;

    @Inject
    public FindPaymentsByDateRangeUseCase(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public List<Payment> execute(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentService.findByPaymentDateBetween(startDate, endDate);
    }
}
