package jeaguirre.me.adapters.inputs.rest.dto;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class CreatePaymentRequestTest {

    @Test
    void shouldSetAndGetAmount() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        BigDecimal amount = new BigDecimal("100.50");

        request.setAmount(amount);

        assertEquals(amount, request.getAmount());
    }

    @Test
    void shouldSetAndGetCurrency() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        String currency = "USD";

        request.setCurrency(currency);

        assertEquals(currency, request.getCurrency());
    }

    @Test
    void shouldSetAndGetPaymentDate() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        LocalDateTime paymentDate = LocalDateTime.of(2023, 10, 15, 14, 30);

        request.setPaymentDate(paymentDate);

        assertEquals(paymentDate, request.getPaymentDate());
    }

    @Test
    void shouldSetAndGetStatus() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        String status = "Completed";

        request.setStatus(status);

        assertEquals(status, request.getStatus());
    }

    @Test
    void shouldSetAndGetPayerId() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        int payerId = 101;

        request.setPayerId(payerId);

        assertEquals(payerId, request.getPayerId());
    }

    @Test
    void shouldSetAndGetPayeeId() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        int payeeId = 201;

        request.setPayeeId(payeeId);

        assertEquals(payeeId, request.getPayeeId());
    }

    @Test
    void shouldSetAndGetPaymentMethod() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        String paymentMethod = "Credit Card";

        request.setPaymentMethod(paymentMethod);

        assertEquals(paymentMethod, request.getPaymentMethod());
    }

    @Test
    void shouldSetAndGetTransactionId() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        String transactionId = "TXN12345";

        request.setTransactionId(transactionId);

        assertEquals(transactionId, request.getTransactionId());
    }

    @Test
    void shouldSetAndGetDescription() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        String description = "Payment for invoice #123";

        request.setDescription(description);

        assertEquals(description, request.getDescription());
    }

    @Test
    void shouldHaveDefaultValues() {
        CreatePaymentRequest request = new CreatePaymentRequest();

        assertNull(request.getAmount());
        assertNull(request.getCurrency());
        assertNull(request.getPaymentDate());
        assertNull(request.getStatus());
        assertEquals(0, request.getPayerId());
        assertEquals(0, request.getPayeeId());
        assertNull(request.getPaymentMethod());
        assertNull(request.getTransactionId());
        assertNull(request.getDescription());
    }

    @Test
    void shouldSetAndGetAllProperties() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        BigDecimal amount = new BigDecimal("150.75");
        String currency = "EUR";
        LocalDateTime paymentDate = LocalDateTime.of(2023, 9, 20, 10, 15);
        String status = "Pending";
        int payerId = 105;
        int payeeId = 205;
        String paymentMethod = "Bank Transfer";
        String transactionId = "TXN67890";
        String description = "Payment for services";

        request.setAmount(amount);
        request.setCurrency(currency);
        request.setPaymentDate(paymentDate);
        request.setStatus(status);
        request.setPayerId(payerId);
        request.setPayeeId(payeeId);
        request.setPaymentMethod(paymentMethod);
        request.setTransactionId(transactionId);
        request.setDescription(description);

        assertEquals(amount, request.getAmount());
        assertEquals(currency, request.getCurrency());
        assertEquals(paymentDate, request.getPaymentDate());
        assertEquals(status, request.getStatus());
        assertEquals(payerId, request.getPayerId());
        assertEquals(payeeId, request.getPayeeId());
        assertEquals(paymentMethod, request.getPaymentMethod());
        assertEquals(transactionId, request.getTransactionId());
        assertEquals(description, request.getDescription());
    }
}
