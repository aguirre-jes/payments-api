package jeaguirre.me.applications.usescases;

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
import jeaguirre.me.utils.OracleDbContainerExtension;

@ExtendWith(OracleDbContainerExtension.class)
@HelidonTest
@Testcontainers
class FindPaymentsByPayeeIdUseCaseIntegrationTest {

    @Inject
    private CreatePaymentUseCase createPaymentUseCase;
    
    @Inject
    private FindPaymentsByPayeeIdUseCase findPaymentsByPayeeIdUseCase;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    private Payment paymentToPayee1;
    private Payment anotherPaymentToPayee1;
    private Payment paymentToPayee2;

    @BeforeEach
    void setUp() {
        // Setup payments for payee 1
        paymentToPayee1 = new Payment();
        paymentToPayee1.setId(83);
        paymentToPayee1.setAmount(new BigDecimal("150.00"));
        paymentToPayee1.setCurrency("USD");
        paymentToPayee1.setPaymentDate(LocalDateTime.now());
        paymentToPayee1.setStatus("COMPLETED");
        paymentToPayee1.setPayerId(172);
        paymentToPayee1.setPayeeId(280);  // Common payee ID
        paymentToPayee1.setPaymentMethod("Credit Card");
        paymentToPayee1.setTransactionId("TXN-PAYEE1-1");
        paymentToPayee1.setDescription("First payment to payee 1");
        
        anotherPaymentToPayee1 = new Payment();
        anotherPaymentToPayee1.setId(84);
        anotherPaymentToPayee1.setAmount(new BigDecimal("85.25"));
        anotherPaymentToPayee1.setCurrency("EUR");
        anotherPaymentToPayee1.setPaymentDate(LocalDateTime.now().minusDays(1));
        anotherPaymentToPayee1.setStatus("PENDING");
        anotherPaymentToPayee1.setPayerId(173);
        anotherPaymentToPayee1.setPayeeId(280);  // Common payee ID
        anotherPaymentToPayee1.setPaymentMethod("Bank Transfer");
        anotherPaymentToPayee1.setTransactionId("TXN-PAYEE1-2");
        anotherPaymentToPayee1.setDescription("Second payment to payee 1");
        
        // Setup payment for different payee
        paymentToPayee2 = new Payment();
        paymentToPayee2.setId(85);
        paymentToPayee2.setAmount(new BigDecimal("250.00"));
        paymentToPayee2.setCurrency("GBP");
        paymentToPayee2.setPaymentDate(LocalDateTime.now());
        paymentToPayee2.setStatus("COMPLETED");
        paymentToPayee2.setPayerId(174);
        paymentToPayee2.setPayeeId(281);  // Different payee ID
        paymentToPayee2.setPaymentMethod("PayPal");
        paymentToPayee2.setTransactionId("TXN-PAYEE2-1");
        paymentToPayee2.setDescription("Payment to payee 2");
    }

    @Test
    @Transactional
    void executeShouldReturnPaymentsForSpecificPayee() {
        // Create test payments
        createPaymentUseCase.execute(paymentToPayee1);
        createPaymentUseCase.execute(anotherPaymentToPayee1);
        createPaymentUseCase.execute(paymentToPayee2);
        
        // Find payments for payee 1
        List<Payment> paymentsToPayee1 = findPaymentsByPayeeIdUseCase.execute(280);
        
        // Verify results
        assertNotNull(paymentsToPayee1);
        assertEquals(2, paymentsToPayee1.size());
        
        // Verify correct payments are returned
        boolean containsFirstPayment = false;
        boolean containsSecondPayment = false;
        
        for (Payment payment : paymentsToPayee1) {
            if (payment.getId() == paymentToPayee1.getId()) {
                containsFirstPayment = true;
                assertEquals("TXN-PAYEE1-1", payment.getTransactionId());
            } else if (payment.getId() == anotherPaymentToPayee1.getId()) {
                containsSecondPayment = true;
                assertEquals("TXN-PAYEE1-2", payment.getTransactionId());
            }
        }
        
        assertTrue(containsFirstPayment, "Results should contain first payment to payee 1");
        assertTrue(containsSecondPayment, "Results should contain second payment to payee 1");
        
        // Find payments for payee 2
        List<Payment> paymentsToPayee2 = findPaymentsByPayeeIdUseCase.execute(281);
        
        // Verify results
        assertNotNull(paymentsToPayee2);
        assertEquals(1, paymentsToPayee2.size());
        assertEquals(paymentToPayee2.getId(), paymentsToPayee2.get(0).getId());
        assertEquals("TXN-PAYEE2-1", paymentsToPayee2.get(0).getTransactionId());
    }
    
    @Test
    @Transactional
    void executeShouldReturnEmptyListForNonExistentPayee() {
        // Create test payments
        createPaymentUseCase.execute(paymentToPayee1);
        createPaymentUseCase.execute(paymentToPayee2);
        
        // Find payments for non-existent payee
        List<Payment> paymentsToNonExistentPayee = findPaymentsByPayeeIdUseCase.execute(999);
        
        // Verify results
        assertNotNull(paymentsToNonExistentPayee);
        assertTrue(paymentsToNonExistentPayee.isEmpty());
    }
    
    @Test
    @Transactional
    void executeShouldReturnEmptyListWhenNoPaymentsExist() {
        // Don't create any payments
        
        // Find payments for a payee
        List<Payment> paymentsToPayee = findPaymentsByPayeeIdUseCase.execute(280);
        
        // Verify results
        assertNotNull(paymentsToPayee);
        assertTrue(paymentsToPayee.isEmpty());
    }
    
    @Test
    @Transactional
    void executeShouldReturnPaymentsWithDifferentStatuses() {
        // Create test payments with different statuses
        createPaymentUseCase.execute(paymentToPayee1);     // "COMPLETED"
        createPaymentUseCase.execute(anotherPaymentToPayee1); // "PENDING"
        
        // Find payments for payee
        List<Payment> paymentsToPayee = findPaymentsByPayeeIdUseCase.execute(280);
        
        // Verify results contain payments with different statuses
        assertNotNull(paymentsToPayee);
        assertEquals(2, paymentsToPayee.size());
        
        boolean hasCompletedPayment = false;
        boolean hasPendingPayment = false;
        
        for (Payment payment : paymentsToPayee) {
            if ("COMPLETED".equals(payment.getStatus())) {
                hasCompletedPayment = true;
            } else if ("PENDING".equals(payment.getStatus())) {
                hasPendingPayment = true;
            }
        }
        
        assertTrue(hasCompletedPayment, "Results should include a COMPLETED payment");
        assertTrue(hasPendingPayment, "Results should include a PENDING payment");
    }
}
