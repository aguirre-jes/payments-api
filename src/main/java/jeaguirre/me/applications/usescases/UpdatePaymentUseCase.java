package jeaguirre.me.applications.usescases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jeaguirre.me.adapters.outputs.persistence.entities.Payment;
import jeaguirre.me.applications.PaymentService;

@ApplicationScoped
public class UpdatePaymentUseCase {
    private final PaymentService paymentService;

    @Inject
    public UpdatePaymentUseCase(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void execute(Payment payment) {
        paymentService.update(payment);
    }
}
