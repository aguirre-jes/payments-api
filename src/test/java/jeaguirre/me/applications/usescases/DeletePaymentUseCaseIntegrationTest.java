package jeaguirre.me.applications.usescases;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import jeaguirre.me.utils.OracleDbContainerExtension;

@ExtendWith(OracleDbContainerExtension.class)
@HelidonTest
@Testcontainers
class DeletePaymentUseCaseIntegrationTest {

    @Inject
    private CreatePaymentUseCase createPaymentUseCase;
    
    @Inject
    private DeletePaymentUseCase deletePaymentUseCase;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    private Payment testPayment;

    @BeforeEach
    void setUp() {
        testPayment = new Payment();
        testPayment.setId(60);
        testPayment.setAmount(new BigDecimal("250.00"));
        testPayment.setCurrency("USD");
        testPayment.setPaymentDate(LocalDateTime.now());
        testPayment.setStatus("Pending");
        testPayment.setPayerId(150);
        testPayment.setPayeeId(250);
        testPayment.setPaymentMethod("PayPal");
        testPayment.setTransactionId("TXN-DELETE-TEST");
        testPayment.setDescription("Delete Payment Use Case Integration Test");
    }

    @Test
    @Transactional
    void executeShouldDeleteExistingPayment() {
        // First create a payment
        createPaymentUseCase.execute(testPayment);
        
        // Verify it exists
        Payment existingPayment = entityManager.find(Payment.class, testPayment.getId());
        assertNotNull(existingPayment);
        
        // Delete the payment
        deletePaymentUseCase.execute(testPayment.getId());
        
        // Verify it was deleted
        Payment deletedPayment = entityManager.find(Payment.class, testPayment.getId());
        assertNull(deletedPayment);
    }
    
    @Test
    @Transactional
    void executeShouldDeleteMultiplePayments() {
        // Create multiple payments
        Payment payment1 = new Payment();
        payment1.setId(61);
        payment1.setAmount(new BigDecimal("100.00"));
        payment1.setCurrency("USD");
        payment1.setPaymentDate(LocalDateTime.now());
        payment1.setStatus("Completed");
        payment1.setPayerId(151);
        payment1.setPayeeId(251);
        
        Payment payment2 = new Payment();
        payment2.setId(62);
        payment2.setAmount(new BigDecimal("200.00"));
        payment2.setCurrency("EUR");
        payment2.setPaymentDate(LocalDateTime.now());
        payment2.setStatus("Pending");
        payment2.setPayerId(152);
        payment2.setPayeeId(252);
        
        createPaymentUseCase.execute(payment1);
        createPaymentUseCase.execute(payment2);
        
        // Verify both exist
        assertNotNull(entityManager.find(Payment.class, payment1.getId()));
        assertNotNull(entityManager.find(Payment.class, payment2.getId()));
        
        // Delete the first payment
        deletePaymentUseCase.execute(payment1.getId());
        
        // Verify first is deleted but second still exists
        assertNull(entityManager.find(Payment.class, payment1.getId()));
        assertNotNull(entityManager.find(Payment.class, payment2.getId()));
        
        // Delete the second payment
        deletePaymentUseCase.execute(payment2.getId());
        
        // Verify second is also deleted
        assertNull(entityManager.find(Payment.class, payment2.getId()));
    }
}
