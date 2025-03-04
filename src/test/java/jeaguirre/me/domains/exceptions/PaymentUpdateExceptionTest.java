package jeaguirre.me.domains.exceptions;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PaymentUpdateExceptionTest {

    @Test
    void constructor_shouldCreateExceptionWithMessageAndCause() {
        String errorMessage = "Error updating payment";
        Throwable cause = new RuntimeException("Original error");

        PaymentUpdateException exception = new PaymentUpdateException(errorMessage, cause);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @Test
    void constructor_shouldCreateExceptionWithMessageAndNullCause() {
        String errorMessage = "Payment update failed";

        PaymentUpdateException exception = new PaymentUpdateException(errorMessage, null);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void exception_shouldBeRuntimeException() {
        PaymentUpdateException exception = new PaymentUpdateException("Test exception", null);

        assertNotNull(exception);
        assertEquals(RuntimeException.class, exception.getClass().getSuperclass());
    }

    @Test
    void exceptionThrown_shouldBeCaughtAsRuntimeException() {
        String errorMessage = "Payment update error";

        RuntimeException caughtException = assertThrows(RuntimeException.class, () -> {
            throw new PaymentUpdateException(errorMessage, null);
        });

        assertEquals(PaymentUpdateException.class, caughtException.getClass());
        assertEquals(errorMessage, caughtException.getMessage());
    }

}
