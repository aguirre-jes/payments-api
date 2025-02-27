package jeaguirre.me.adapters.outputs.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("Completed");
        payment.setPayerId(101);
        payment.setPayeeId(201);
        payment.setPaymentMethod("Credit Card");
        payment.setTransactionId("TXN123456");
        payment.setDescription("Payment for invoice #123");
    }

    @Test
    @Transactional
    void testSavePayment() {
        paymentRepository.save(payment);
        Optional<Payment> retrievedPayment = paymentRepository.findById(payment.getId());
        assertTrue(retrievedPayment.isPresent());
        assertEquals(payment.getId(), retrievedPayment.get().getId());
    }

    @Test
    @Transactional
    void testSavePaymentWithNullValues() {
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
    void testSavePaymentThrowsPaymentSaveException() {
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

}
