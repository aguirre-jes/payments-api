package jeaguirre.me.applications.usescases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jeaguirre.me.adapters.outputs.persistence.entities.Payment;
import jeaguirre.me.applications.PaymentService;

@ApplicationScoped
public class CreatePaymentUseCase {

    private final PaymentService paymentService;

    @Inject
    public CreatePaymentUseCase(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public Payment execute(Payment payment) {
        paymentService.savePayment(payment);
        return payment;
    }

}
