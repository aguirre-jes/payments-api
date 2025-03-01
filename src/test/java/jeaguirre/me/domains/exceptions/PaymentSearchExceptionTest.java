package jeaguirre.me.domains.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PaymentSearchExceptionTest {

    @Test
    void paymentSearchExceptionMessage() {
        String errorMessage = "Error finding payment";
        PaymentSearchException exception = assertThrows(PaymentSearchException.class, () -> {
            throw new PaymentSearchException(errorMessage, null);
        });
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void paymentSearchExceptionCause() {
        String errorMessage = "Error finding payment";
        Throwable cause = new RuntimeException("Underlying cause");
        PaymentSearchException exception = assertThrows(PaymentSearchException.class, () -> {
            throw new PaymentSearchException(errorMessage, cause);
        });
        assertEquals(errorMessage, exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void paymentSearchExceptionMessageAndCause() {
        String errorMessage = "Error finding payment";
        Throwable cause = new RuntimeException("Underlying cause");
        PaymentSearchException exception = new PaymentSearchException(errorMessage, cause);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}
