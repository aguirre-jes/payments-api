package jeaguirre.me.applications.usescases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jeaguirre.me.adapters.outputs.persistence.entities.Payment;
import jeaguirre.me.applications.PaymentService;

@ApplicationScoped
public class GetPaymentByIdUseCase {
    private final PaymentService paymentService;

    @Inject
    public GetPaymentByIdUseCase(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public Payment execute(int id) {
        return paymentService.getPaymentById(id);
    }
}
