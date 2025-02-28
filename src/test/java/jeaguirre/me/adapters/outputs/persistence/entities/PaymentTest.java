package jeaguirre.me.adapters.outputs.persistence.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentTest {

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setId(1);
        payment.setAmount(new BigDecimal("100.00"));
        payment.setCurrency("USD");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("Completed");
        payment.setPayerId(101);
        payment.setPayeeId(201);
        payment.setPaymentMethod("Credit Card");
        payment.setTransactionId("TXN123456");
        payment.setDescription("Payment for invoice #123");
    }

    @Test
    void testGetId() {
        assertEquals(1, payment.getId());
    }

    @Test
    void testGetAmount() {
        assertEquals(new BigDecimal("100.00"), payment.getAmount());
    }

    @Test
    void testGetCurrency() {
        assertEquals("USD", payment.getCurrency());
    }

    @Test
    void testGetPaymentDate() {
        assertEquals(LocalDateTime.now().getYear(), payment.getPaymentDate().getYear());
    }

    @Test
    void testGetStatus() {
        assertEquals("Completed", payment.getStatus());
    }

    @Test
    void testGetPayerId() {
        assertEquals(101, payment.getPayerId());
    }

    @Test
    void testGetPayeeId() {
        assertEquals(201, payment.getPayeeId());
    }

    @Test
    void testGetPaymentMethod() {
        assertEquals("Credit Card", payment.getPaymentMethod());
    }

    @Test
    void testGetTransactionId() {
        assertEquals("TXN123456", payment.getTransactionId());
    }

    @Test
    void testGetDescription() {
        assertEquals("Payment for invoice #123", payment.getDescription());
    }
}
