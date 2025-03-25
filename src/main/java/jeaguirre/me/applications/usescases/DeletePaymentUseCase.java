package jeaguirre.me.applications.usescases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jeaguirre.me.applications.PaymentService;

@ApplicationScoped
public class DeletePaymentUseCase {
    private final PaymentService paymentService;

    @Inject
    public DeletePaymentUseCase(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void execute(int id) {
        paymentService.deleteById(id);
    }
}
