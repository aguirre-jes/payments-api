package jeaguirre.me.domains.exceptions;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PaymentUpdateExceptionTest {

    @Test
    void constructorShouldCreateExceptionWithMessageAndCause() {
        String errorMessage = "Error updating payment";
        Throwable cause = new RuntimeException("Original error");

        PaymentUpdateException exception = new PaymentUpdateException(errorMessage, cause);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @Test
    void constructorShouldCreateExceptionWithMessageAndNullCause() {
        String errorMessage = "Payment update failed";

        PaymentUpdateException exception = new PaymentUpdateException(errorMessage, null);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void exceptionShouldBeRuntimeException() {
        PaymentUpdateException exception = new PaymentUpdateException("Test exception", null);

        assertNotNull(exception);
        assertEquals(RuntimeException.class, exception.getClass().getSuperclass());
    }

    @Test
    void exceptionThrownShouldBeCaughtAsRuntimeException() {
        String errorMessage = "Payment update error";

        RuntimeException caughtException = assertThrows(RuntimeException.class, () -> {
            throw new PaymentUpdateException(errorMessage, null);
        });

        assertEquals(PaymentUpdateException.class, caughtException.getClass());
        assertEquals(errorMessage, caughtException.getMessage());
    }

}
