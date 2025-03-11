package jeaguirre.me.adapters.inputs.rest.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class PaymentResponseTest {

    @Test
    void shouldSetAndGetId() {
        PaymentResponse response = new PaymentResponse();
        int id = 1001;

        response.setId(id);

        assertEquals(id, response.getId());
    }

    @Test
    void shouldSetAndGetAmount() {
        PaymentResponse response = new PaymentResponse();
        BigDecimal amount = new BigDecimal("100.50");

        response.setAmount(amount);

        assertEquals(amount, response.getAmount());
    }

    @Test
    void shouldSetAndGetCurrency() {
        PaymentResponse response = new PaymentResponse();
        String currency = "USD";

        response.setCurrency(currency);

        assertEquals(currency, response.getCurrency());
    }

    @Test
    void shouldSetAndGetPaymentDate() {
        PaymentResponse response = new PaymentResponse();
        LocalDateTime paymentDate = LocalDateTime.of(2023, 10, 15, 14, 30);

        response.setPaymentDate(paymentDate);

        assertEquals(paymentDate, response.getPaymentDate());
    }

    @Test
    void shouldSetAndGetStatus() {
        PaymentResponse response = new PaymentResponse();
        String status = "Completed";

        response.setStatus(status);

        assertEquals(status, response.getStatus());
    }

    @Test
    void shouldSetAndGetPayerId() {
        PaymentResponse response = new PaymentResponse();
        int payerId = 101;

        response.setPayerId(payerId);

        assertEquals(payerId, response.getPayerId());
    }

    @Test
    void shouldSetAndGetPayeeId() {
        PaymentResponse response = new PaymentResponse();
        int payeeId = 201;

        response.setPayeeId(payeeId);

        assertEquals(payeeId, response.getPayeeId());
    }

    @Test
    void shouldSetAndGetPaymentMethod() {
        PaymentResponse response = new PaymentResponse();
        String paymentMethod = "Credit Card";

        response.setPaymentMethod(paymentMethod);

        assertEquals(paymentMethod, response.getPaymentMethod());
    }

    @Test
    void shouldSetAndGetTransactionId() {
        PaymentResponse response = new PaymentResponse();
        String transactionId = "TXN12345";

        response.setTransactionId(transactionId);

        assertEquals(transactionId, response.getTransactionId());
    }

    @Test
    void shouldSetAndGetDescription() {
        PaymentResponse response = new PaymentResponse();
        String description = "Payment for invoice #123";

        response.setDescription(description);

        assertEquals(description, response.getDescription());
    }

    @Test
    void shouldHaveDefaultValues() {
        PaymentResponse response = new PaymentResponse();

        assertEquals(0, response.getId());
        assertNull(response.getAmount());
        assertNull(response.getCurrency());
        assertNull(response.getPaymentDate());
        assertNull(response.getStatus());
        assertEquals(0, response.getPayerId());
        assertEquals(0, response.getPayeeId());
        assertNull(response.getPaymentMethod());
        assertNull(response.getTransactionId());
        assertNull(response.getDescription());
    }

    @Test
    void shouldSetAndGetAllProperties() {
        PaymentResponse response = new PaymentResponse();
        int id = 1234;
        BigDecimal amount = new BigDecimal("150.75");
        String currency = "EUR";
        LocalDateTime paymentDate = LocalDateTime.of(2023, 9, 20, 10, 15);
        String status = "Pending";
        int payerId = 105;
        int payeeId = 205;
        String paymentMethod = "Bank Transfer";
        String transactionId = "TXN67890";
        String description = "Payment for services";

        response.setId(id);
        response.setAmount(amount);
        response.setCurrency(currency);
        response.setPaymentDate(paymentDate);
        response.setStatus(status);
        response.setPayerId(payerId);
        response.setPayeeId(payeeId);
        response.setPaymentMethod(paymentMethod);
        response.setTransactionId(transactionId);
        response.setDescription(description);

        assertEquals(id, response.getId());
        assertEquals(amount, response.getAmount());
        assertEquals(currency, response.getCurrency());
        assertEquals(paymentDate, response.getPaymentDate());
        assertEquals(status, response.getStatus());
        assertEquals(payerId, response.getPayerId());
        assertEquals(payeeId, response.getPayeeId());
        assertEquals(paymentMethod, response.getPaymentMethod());
        assertEquals(transactionId, response.getTransactionId());
        assertEquals(description, response.getDescription());
    }
}
