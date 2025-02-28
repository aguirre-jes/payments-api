package jeaguirre.me.domains.exceptions;

public class PaymentSaveException extends RuntimeException {
    public PaymentSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
