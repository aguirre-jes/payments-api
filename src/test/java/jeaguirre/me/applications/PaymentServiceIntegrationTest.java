package jeaguirre.me.applications;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jeaguirre.me.adapters.outputs.persistence.entities.Payment;
import jeaguirre.me.domains.exceptions.PaymentSaveException;
import jeaguirre.me.domains.exceptions.PaymentSearchException;
import jeaguirre.me.utils.OracleDbContainerExtension;

@ExtendWith(OracleDbContainerExtension.class)
@HelidonTest
@Testcontainers
class PaymentServiceIntegrationTest {

    @Inject
    private PaymentService paymentService;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setId(20);
        payment.setAmount(new BigDecimal("150.00"));
        payment.setCurrency("USD");
        payment.setPaymentDate(LocalDateTime.now().minusHours(1));
        payment.setStatus("Completed");
        payment.setPayerId(110);
        payment.setPayeeId(210);
        payment.setPaymentMethod("Credit Card");
        payment.setTransactionId("TXN-SERVICE-TEST");
        payment.setDescription("Payment Service Integration Test");
    }

    @Test
    @Transactional
    void savePayment() {
        paymentService.savePayment(payment);

        Payment retrievedPayment = paymentService.getPaymentById(payment.getId());
        assertNotNull(retrievedPayment);
        assertEquals(payment.getId(), retrievedPayment.getId());
        assertEquals(payment.getAmount(), retrievedPayment.getAmount());
        assertEquals(payment.getStatus(), retrievedPayment.getStatus());
    }

    @Test
    @Transactional
    void savePaymentWithNullValues() {
        Payment paymentWithNulls = new Payment();
        paymentWithNulls.setId(21);
        paymentWithNulls.setAmount(new BigDecimal("75.00"));
        paymentWithNulls.setCurrency("EUR");
        paymentWithNulls.setPaymentDate(LocalDateTime.now());
        paymentWithNulls.setStatus("Pending");
        paymentWithNulls.setPayerId(111);
        paymentWithNulls.setPayeeId(211);
        paymentWithNulls.setPaymentMethod(null);
        paymentWithNulls.setTransactionId(null);
        paymentWithNulls.setDescription(null);

        assertDoesNotThrow(() -> {
            paymentService.savePayment(paymentWithNulls);
        });

        Payment retrievedPayment = paymentService.getPaymentById(paymentWithNulls.getId());
        assertNotNull(retrievedPayment);
        assertEquals(paymentWithNulls.getId(), retrievedPayment.getId());
        assertNull(retrievedPayment.getPaymentMethod());
        assertNull(retrievedPayment.getTransactionId());
        assertNull(retrievedPayment.getDescription());
    }

    @Test
    @Transactional
    void savePaymentThrowsPaymentSaveException() {
        // First, get an existing payment
        Payment existingPayment = paymentService.getPaymentById(1);
        assertNotNull(existingPayment);

        // Then try to save a payment with the same ID
        Payment duplicatePayment = new Payment();
        duplicatePayment.setId(1);
        duplicatePayment.setAmount(new BigDecimal("200.00"));
        duplicatePayment.setCurrency("USD");
        duplicatePayment.setPaymentDate(LocalDateTime.now());
        duplicatePayment.setStatus("Completed");
        duplicatePayment.setPayerId(105);
        duplicatePayment.setPayeeId(205);
        duplicatePayment.setPaymentMethod("Credit Card");
        duplicatePayment.setTransactionId("TXN123459");
        duplicatePayment.setDescription("Duplicate payment");

        assertThrows(PaymentSaveException.class, () -> {
            paymentService.savePayment(duplicatePayment);
        });
    }

    @Test
    @Transactional
    void getPaymentById() {
        Payment retrievedPayment = paymentService.getPaymentById(1);
        assertNotNull(retrievedPayment);
        assertEquals(1, retrievedPayment.getId());
    }

    @Test
    @Transactional
    void getPaymentByIdThrowsException() {
        int nonExistentId = 999;
        assertThrows(RuntimeException.class, () -> {
            paymentService.getPaymentById(nonExistentId);
        });
    }

    @Test
    @Transactional
    void updatePayment() {
        payment = new Payment();
        payment.setId(51);
        payment.setAmount(new BigDecimal("150.00"));
        payment.setCurrency("USD");
        payment.setPaymentDate(LocalDateTime.now().minusHours(1));
        payment.setStatus("Completed");
        payment.setPayerId(110);
        payment.setPayeeId(210);
        payment.setPaymentMethod("Credit Card");
        payment.setTransactionId("TXN-SERVICE-TEST01");
        payment.setDescription("Payment Service Integration Test");

        paymentService.savePayment(payment);
        payment.setStatus("Processing");
        payment.setAmount(new BigDecimal("850.00"));
        payment.setTransactionId("TXN-SERVICE-TEST08");
        payment.setDescription("Updated description");

        paymentService.update(payment);

        Payment updatedPayment = paymentService.getPaymentById(51);
        assertEquals(new BigDecimal("850.00"), updatedPayment.getAmount());
        assertEquals("Processing", updatedPayment.getStatus());
        assertEquals("Updated description", updatedPayment.getDescription());
    }

    @Test
    @Transactional
    void deleteById() {
        Payment retrievedPayment = paymentService.getPaymentById(20);
        assertNotNull(retrievedPayment);
        int paymentId = payment.getId();
        paymentService.deleteById(paymentId);
        assertThrows(RuntimeException.class, () -> {
            paymentService.getPaymentById(paymentId);
        });
    }

    @Test
    @Transactional
    void deleteByIdWhenPaymentDoesNotExist() {
        int nonExistentId = 999;
        assertThrows(PaymentSearchException.class, () -> {
            paymentService.deleteById(nonExistentId);
        });
    }

    @Test
    @Transactional
    void findByStatus() {
        List<Payment> payments = paymentService.findByStatus("Completed");
        assertFalse(payments.isEmpty());
        boolean found = payments.stream()
                .anyMatch(p -> p.getStatus().equals("Completed") && p.getId() == 1);
        assertTrue(found, "Should find the payment we just created with status 'Completed'");
    }

    @Test
    @Transactional
    void findByStatusThrowsException() {
        assertThrows(PaymentSearchException.class, () -> {
            paymentService.findByStatus("NonExistentStatus");
        });
    }

    @Test
    @Transactional
    void findByPayerId() {
        List<Payment> payments = paymentService.findByPayerId(102);
        assertFalse(payments.isEmpty());
        boolean found = payments.stream()
                .anyMatch(p -> p.getPayerId() == 102 && p.getId() == 2);
        assertTrue(found, "Should find the payment we just created with the expected payer ID");
    }

    @Test
    @Transactional
    void findByPayerIdThrowsException() {
        int nonExistentPayerId = 99999;
        assertThrows(PaymentSearchException.class, () -> {
            paymentService.findByPayerId(nonExistentPayerId);
        });
    }

    @Test
    @Transactional
    void findByPayeeId() {
        List<Payment> payments = paymentService.findByPayeeId(202);
        assertFalse(payments.isEmpty());

        // Verify at least one payment has the expected payee ID
        boolean found = payments.stream()
                .anyMatch(p -> p.getPayeeId() == 202 && p.getId() == 2);
        assertTrue(found, "Should find the payment we just created with the expected payee ID");
    }

    @Test
    @Transactional
    void findByPayeeIdThrowsException() {
        int nonExistentPayeeId = 99999;
        assertThrows(PaymentSearchException.class, () -> {
            paymentService.findByPayeeId(nonExistentPayeeId);
        });
    }

    @Test
    @Transactional
    void findByPaymentDateBetween() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        List<Payment> payments = paymentService.findByPaymentDateBetween(startDate, endDate);
        assertFalse(payments.isEmpty());
    }

    @Test
    @Transactional
    void findByPaymentDateBetweenThrowsException() {
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 31, 23, 59);

        assertThrows(PaymentSearchException.class, () -> {
            paymentService.findByPaymentDateBetween(startDate, endDate);
        });
    }
}
