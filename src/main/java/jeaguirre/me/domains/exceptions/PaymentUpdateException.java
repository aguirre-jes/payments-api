package jeaguirre.me.domains.exceptions;

public class PaymentUpdateException extends RuntimeException {
    public PaymentUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
