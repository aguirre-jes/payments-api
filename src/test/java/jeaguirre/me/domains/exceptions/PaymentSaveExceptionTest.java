package jeaguirre.me.domains.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PaymentSaveExceptionTest {

    @Test
    void paymentSaveExceptionMessage() {
        String errorMessage = "Error saving payment";
        PaymentSaveException exception = assertThrows(PaymentSaveException.class, () -> {
            throw new PaymentSaveException(errorMessage, null);
        });
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void paymentSaveExceptionCause() {
        String errorMessage = "Error saving payment";
        Throwable cause = new RuntimeException("Underlying cause");
        PaymentSaveException exception = assertThrows(PaymentSaveException.class, () -> {
            throw new PaymentSaveException(errorMessage, cause);
        });
        assertEquals(errorMessage, exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void paymentSaveExceptionMessageAndCause() {
        String errorMessage = "Error saving payment";
        Throwable cause = new RuntimeException("Underlying cause");
        PaymentSaveException exception = new PaymentSaveException(errorMessage, cause);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}
