package jeaguirre.me.applications.usescases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jeaguirre.me.adapters.outputs.persistence.entities.Payment;
import jeaguirre.me.applications.PaymentService;

import java.util.List;

@ApplicationScoped
public class FindPaymentsByPayeeIdUseCase {
    private final PaymentService paymentService;

    @Inject
    public FindPaymentsByPayeeIdUseCase(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public List<Payment> execute(int payeeId) {
        return paymentService.findByPayeeId(payeeId);
    }
}
