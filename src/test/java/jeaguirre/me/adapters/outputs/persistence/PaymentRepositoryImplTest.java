package jeaguirre.me.adapters.outputs.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
import jeaguirre.me.domains.ports.PaymentRepository;
import jeaguirre.me.utils.OracleDbContainerExtension;

@ExtendWith(OracleDbContainerExtension.class)
@HelidonTest
@Testcontainers
class PaymentRepositoryImplTest {

    @Inject
    PaymentRepository paymentRepository;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setId(4);
        payment.setAmount(new BigDecimal("100.00"));
        payment.setCurrency("USD");
        payment.setPaymentDate(LocalDateTime.now().minusHours(1));
        payment.setStatus("Completed");
        payment.setPayerId(101);
        payment.setPayeeId(201);
        payment.setPaymentMethod("Credit Card");
        payment.setTransactionId("TXN123456");
        payment.setDescription("Payment for invoice #123");
    }

    @Test
    @Transactional
    void savePayment() {
        paymentRepository.save(payment);
        Optional<Payment> retrievedPayment = paymentRepository.findById(payment.getId());
        assertTrue(retrievedPayment.isPresent());
        assertEquals(payment.getId(), retrievedPayment.get().getId());
    }

    @Test
    @Transactional
    void savePaymentWithNullValues() {
        Payment paymentWithNulls = new Payment();
        paymentWithNulls.setId(5);
        paymentWithNulls.setAmount(new BigDecimal("50.00"));
        paymentWithNulls.setCurrency("USD");
        paymentWithNulls.setPaymentDate(LocalDateTime.now());
        paymentWithNulls.setStatus("Pending");
        paymentWithNulls.setPayerId(104);
        paymentWithNulls.setPayeeId(204);
        paymentWithNulls.setPaymentMethod(null);
        paymentWithNulls.setTransactionId(null);
        paymentWithNulls.setDescription(null);

        assertDoesNotThrow(() -> {
            paymentRepository.save(paymentWithNulls);
        });

        Optional<Payment> retrievedPayment = paymentRepository.findById(paymentWithNulls.getId());
        assertTrue(retrievedPayment.isPresent());
        assertEquals(paymentWithNulls.getId(), retrievedPayment.get().getId());
    }

    @Test
    @Transactional
    void savePaymentThrowsPaymentSaveException() {
        Optional<Payment> existingPayment = paymentRepository.findById(1);
        assertTrue(existingPayment.isPresent(), "Payment with ID 1 should already exist");

        Payment invalidPayment = new Payment();
        invalidPayment.setId(1);
        invalidPayment.setAmount(new BigDecimal("200.00"));
        invalidPayment.setCurrency("USD");
        invalidPayment.setPaymentDate(LocalDateTime.now());
        invalidPayment.setStatus("Completed");
        invalidPayment.setPayerId(105);
        invalidPayment.setPayeeId(205);
        invalidPayment.setPaymentMethod("Credit Card");
        invalidPayment.setTransactionId("TXN123459");
        invalidPayment.setDescription("Duplicate payment");

        assertThrows(PaymentSaveException.class, () -> {
            paymentRepository.save(invalidPayment);
        });
    }

    @Test
    @Transactional
    void findById() {
        Optional<Payment> retrievedPayment = paymentRepository.findById(1);
        assertTrue(retrievedPayment.isPresent());
        assertEquals(1, retrievedPayment.get().getId());
    }

    @Test
    @Transactional
    void findByIdThrowsPaymentSearchException() {
        int nonExistentId = 999;
        assertThrows(PaymentSearchException.class, () -> {
            paymentRepository.findById(nonExistentId);
        });
    }

    @Test
    @Transactional
    void updatePayment() {
        Payment paymentUpdate;
        paymentUpdate = new Payment();
        paymentUpdate.setId(99);
        paymentUpdate.setAmount(new BigDecimal("900.00"));
        paymentUpdate.setCurrency("USD");
        paymentUpdate.setPaymentDate(LocalDateTime.now().minusHours(2));
        paymentUpdate.setStatus("Completed");
        paymentUpdate.setPayerId(101);
        paymentUpdate.setPayeeId(201);
        paymentUpdate.setPaymentMethod("Credit Card");
        paymentUpdate.setTransactionId("TXN1234589");
        paymentUpdate.setDescription("Payment for invoice #123");
        paymentRepository.save(paymentUpdate);
        paymentUpdate.setAmount(new BigDecimal("750.00"));
        paymentRepository.update(paymentUpdate);
        Optional<Payment> updatedPayment = paymentRepository.findById(99);
        assertTrue(updatedPayment.isPresent());
        assertEquals(new BigDecimal("750.00"), updatedPayment.get().getAmount());
    }

    @Test
    @Transactional
    void deleteById() {
        Optional<Payment> existingPayment = paymentRepository.findById(3);
        assertTrue(existingPayment.isPresent(), "Payment with ID 3 should already exist");
        paymentRepository.deleteById(3);
        assertThrows(PaymentSearchException.class, () -> {
            paymentRepository.findById(3);
        });
    }

    @Test
    @Transactional
    void deleteByIdWhenPaymentDoesNotExist() {
        int nonExistentId = 999;
        assertThrows(PaymentSearchException.class, () -> {
            paymentRepository.deleteById(nonExistentId);
        });
    }

    @Test
    @Transactional
    void findByStatus() {
        payment.setId(6);
        paymentRepository.save(payment);
        List<Payment> payments = paymentRepository.findByStatus("Completed");
        assertTrue(payments.size() > 0);
        assertEquals("Completed", payments.get(0).getStatus());
    }

    @Test
    @Transactional
    void findByStatusThrowsPaymentSearchException() {
        assertThrows(PaymentSearchException.class, () -> {
            paymentRepository.findByStatus("NonExistentStatus");
        });
    }

    @Test
    @Transactional
    void findByPayerId() {
        payment.setId(10);
        paymentRepository.save(payment);
        List<Payment> payments = paymentRepository.findByPayerId(101);
        assertTrue(payments.size() > 0);
        assertEquals(101, payments.get(0).getPayerId());
    }

    @Test
    @Transactional
    void findByPayerIdThrowsPaymentSearchException() {
        assertThrows(PaymentSearchException.class, () -> {
            paymentRepository.findByPayerId(999);
        });
    }

    @Test
    @Transactional
    void findByPayeeId() {
        payment.setId(11);
        paymentRepository.save(payment);
        List<Payment> payments = paymentRepository.findByPayeeId(201);
        assertTrue(payments.size() > 0);
        assertEquals(201, payments.get(0).getPayeeId());
    }

    @Test
    @Transactional
    void findByPayeeIdThrowsPaymentSearchException() {
        assertThrows(PaymentSearchException.class, () -> {
            paymentRepository.findByPayeeId(999);
        });
    }

    @Test
    @Transactional
    void findByPaymentDateBetween() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        List<Payment> payments = paymentRepository.findByPaymentDateBetween(startDate, endDate);
        assertTrue(payments.size() > 0);
    }

    @Test
    @Transactional
    void findByPaymentDateBetweenThrowsPaymentSearchException() {
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 31, 23, 59);
        assertThrows(PaymentSearchException.class, () -> {
            paymentRepository.findByPaymentDateBetween(startDate, endDate);
        });
    }
}