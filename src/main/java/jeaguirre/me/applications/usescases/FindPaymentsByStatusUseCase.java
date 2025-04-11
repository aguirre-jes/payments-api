package jeaguirre.me.applications.usescases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jeaguirre.me.adapters.outputs.persistence.entities.Payment;
import jeaguirre.me.applications.PaymentService;

import java.util.List;

@ApplicationScoped
public class FindPaymentsByStatusUseCase {
    private final PaymentService paymentService;

    @Inject
    public FindPaymentsByStatusUseCase(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public List<Payment> execute(String status) {
        return paymentService.findByStatus(status);
    }
}
