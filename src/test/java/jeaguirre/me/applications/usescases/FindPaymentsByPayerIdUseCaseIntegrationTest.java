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
class FindPaymentsByPayerIdUseCaseIntegrationTest {

    @Inject
    private CreatePaymentUseCase createPaymentUseCase;
    
    @Inject
    private FindPaymentsByPayerIdUseCase findPaymentsByPayerIdUseCase;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    private Payment paymentFromPayer1;
    private Payment anotherPaymentFromPayer1;
    private Payment paymentFromPayer2;

    @BeforeEach
    void setUp() {
        // Setup payments for payer 1
        paymentFromPayer1 = new Payment();
        paymentFromPayer1.setId(80);
        paymentFromPayer1.setAmount(new BigDecimal("125.00"));
        paymentFromPayer1.setCurrency("USD");
        paymentFromPayer1.setPaymentDate(LocalDateTime.now());
        paymentFromPayer1.setStatus("COMPLETED");
        paymentFromPayer1.setPayerId(170);  // Common payer ID
        paymentFromPayer1.setPayeeId(270);
        paymentFromPayer1.setPaymentMethod("Credit Card");
        paymentFromPayer1.setTransactionId("TXN-PAYER1-1");
        paymentFromPayer1.setDescription("First payment from payer 1");
        
        anotherPaymentFromPayer1 = new Payment();
        anotherPaymentFromPayer1.setId(81);
        anotherPaymentFromPayer1.setAmount(new BigDecimal("75.50"));
        anotherPaymentFromPayer1.setCurrency("EUR");
        anotherPaymentFromPayer1.setPaymentDate(LocalDateTime.now().minusDays(1));
        anotherPaymentFromPayer1.setStatus("PENDING");
        anotherPaymentFromPayer1.setPayerId(170);  // Common payer ID
        anotherPaymentFromPayer1.setPayeeId(271);
        anotherPaymentFromPayer1.setPaymentMethod("Bank Transfer");
        anotherPaymentFromPayer1.setTransactionId("TXN-PAYER1-2");
        anotherPaymentFromPayer1.setDescription("Second payment from payer 1");
        
        // Setup payment for different payer
        paymentFromPayer2 = new Payment();
        paymentFromPayer2.setId(82);
        paymentFromPayer2.setAmount(new BigDecimal("200.00"));
        paymentFromPayer2.setCurrency("GBP");
        paymentFromPayer2.setPaymentDate(LocalDateTime.now());
        paymentFromPayer2.setStatus("COMPLETED");
        paymentFromPayer2.setPayerId(171);  // Different payer ID
        paymentFromPayer2.setPayeeId(272);
        paymentFromPayer2.setPaymentMethod("PayPal");
        paymentFromPayer2.setTransactionId("TXN-PAYER2-1");
        paymentFromPayer2.setDescription("Payment from payer 2");
    }

    @Test
    @Transactional
    void executeShouldReturnPaymentsForSpecificPayer() {
        // Create test payments
        createPaymentUseCase.execute(paymentFromPayer1);
        createPaymentUseCase.execute(anotherPaymentFromPayer1);
        createPaymentUseCase.execute(paymentFromPayer2);
        
        // Find payments for payer 1
        List<Payment> paymentsFromPayer1 = findPaymentsByPayerIdUseCase.execute(170);
        
        // Verify results
        assertNotNull(paymentsFromPayer1);
        assertEquals(2, paymentsFromPayer1.size());
        
        // Verify correct payments are returned
        boolean containsFirstPayment = false;
        boolean containsSecondPayment = false;
        
        for (Payment payment : paymentsFromPayer1) {
            if (payment.getId() == paymentFromPayer1.getId()) {
                containsFirstPayment = true;
                assertEquals("TXN-PAYER1-1", payment.getTransactionId());
            } else if (payment.getId() == anotherPaymentFromPayer1.getId()) {
                containsSecondPayment = true;
                assertEquals("TXN-PAYER1-2", payment.getTransactionId());
            }
        }
        
        assertTrue(containsFirstPayment, "Results should contain first payment from payer 1");
        assertTrue(containsSecondPayment, "Results should contain second payment from payer 1");
        
        // Find payments for payer 2
        List<Payment> paymentsFromPayer2 = findPaymentsByPayerIdUseCase.execute(171);
        
        // Verify results
        assertNotNull(paymentsFromPayer2);
        assertEquals(1, paymentsFromPayer2.size());
        assertEquals(paymentFromPayer2.getId(), paymentsFromPayer2.get(0).getId());
        assertEquals("TXN-PAYER2-1", paymentsFromPayer2.get(0).getTransactionId());
    }
    
    @Test
    @Transactional
    void executeShouldReturnEmptyListForNonExistentPayer() {
        // Create test payments
        createPaymentUseCase.execute(paymentFromPayer1);
        createPaymentUseCase.execute(paymentFromPayer2);
        
        // Find payments for non-existent payer
        List<Payment> paymentsFromNonExistentPayer = findPaymentsByPayerIdUseCase.execute(999);
        
        // Verify results
        assertNotNull(paymentsFromNonExistentPayer);
        assertTrue(paymentsFromNonExistentPayer.isEmpty());
    }
    
    @Test
    @Transactional
    void executeShouldReturnEmptyListWhenNoPaymentsExist() {
        // Don't create any payments
        
        // Find payments for a payer
        List<Payment> paymentsFromPayer = findPaymentsByPayerIdUseCase.execute(170);
        
        // Verify results
        assertNotNull(paymentsFromPayer);
        assertTrue(paymentsFromPayer.isEmpty());
    }
    
    @Test
    @Transactional
    void executeShouldReturnPaymentsWithAllDetails() {
        // Create test payment
        createPaymentUseCase.execute(paymentFromPayer1);
        
        // Find payments for payer
        List<Payment> paymentsFromPayer = findPaymentsByPayerIdUseCase.execute(170);
        
        // Verify results
        assertNotNull(paymentsFromPayer);
        assertEquals(1, paymentsFromPayer.size());
        
        Payment retrievedPayment = paymentsFromPayer.get(0);
        assertEquals(paymentFromPayer1.getId(), retrievedPayment.getId());
        assertEquals(paymentFromPayer1.getAmount(), retrievedPayment.getAmount());
        assertEquals(paymentFromPayer1.getCurrency(), retrievedPayment.getCurrency());
        assertEquals(paymentFromPayer1.getStatus(), retrievedPayment.getStatus());
        assertEquals(paymentFromPayer1.getPayerId(), retrievedPayment.getPayerId());
        assertEquals(paymentFromPayer1.getPayeeId(), retrievedPayment.getPayeeId());
        assertEquals(paymentFromPayer1.getPaymentMethod(), retrievedPayment.getPaymentMethod());
        assertEquals(paymentFromPayer1.getTransactionId(), retrievedPayment.getTransactionId());
        assertEquals(paymentFromPayer1.getDescription(), retrievedPayment.getDescription());
    }
}
