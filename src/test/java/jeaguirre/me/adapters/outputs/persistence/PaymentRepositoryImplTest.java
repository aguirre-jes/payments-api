package jeaguirre.me.adapters.outputs.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

}
