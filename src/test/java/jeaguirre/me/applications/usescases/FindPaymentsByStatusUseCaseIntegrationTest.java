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
class FindPaymentsByStatusUseCaseIntegrationTest {

    @Inject
    private CreatePaymentUseCase createPaymentUseCase;
    
    @Inject
    private FindPaymentsByStatusUseCase findPaymentsByStatusUseCase;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    private Payment pendingPayment1;
    private Payment pendingPayment2;
    private Payment completedPayment;

    @BeforeEach
    void setUp() {
        pendingPayment1 = new Payment();
        pendingPayment1.setId(70);
        pendingPayment1.setAmount(new BigDecimal("150.00"));
        pendingPayment1.setCurrency("USD");
        pendingPayment1.setPaymentDate(LocalDateTime.now());
        pendingPayment1.setStatus("PENDING");
        pendingPayment1.setPayerId(160);
        pendingPayment1.setPayeeId(260);
        
        pendingPayment2 = new Payment();
        pendingPayment2.setId(71);
        pendingPayment2.setAmount(new BigDecimal("200.00"));
        pendingPayment2.setCurrency("EUR");
        pendingPayment2.setPaymentDate(LocalDateTime.now());
        pendingPayment2.setStatus("PENDING");
        pendingPayment2.setPayerId(161);
        pendingPayment2.setPayeeId(261);
        
        completedPayment = new Payment();
        completedPayment.setId(72);
        completedPayment.setAmount(new BigDecimal("250.00"));
        completedPayment.setCurrency("GBP");
        completedPayment.setPaymentDate(LocalDateTime.now());
        completedPayment.setStatus("COMPLETED");
        completedPayment.setPayerId(162);
        completedPayment.setPayeeId(262);
    }

    @Test
    @Transactional
    void executeShouldReturnPaymentsWithMatchingStatus() {
        // Create test payments
        createPaymentUseCase.execute(pendingPayment1);
        createPaymentUseCase.execute(pendingPayment2);
        createPaymentUseCase.execute(completedPayment);
        
        // Find payments with PENDING status
        List<Payment> pendingPayments = findPaymentsByStatusUseCase.execute("PENDING");
        
        // Verify results
        assertNotNull(pendingPayments);
        assertEquals(2, pendingPayments.size());
        
        boolean containsPending1 = false;
        boolean containsPending2 = false;
        
        for (Payment payment : pendingPayments) {
            if (payment.getId() == pendingPayment1.getId()) {
                containsPending1 = true;
            } else if (payment.getId() == pendingPayment2.getId()) {
                containsPending2 = true;
            }
        }
        
        assertTrue(containsPending1, "Results should contain pendingPayment1");
        assertTrue(containsPending2, "Results should contain pendingPayment2");
        
        // Find payments with COMPLETED status
        List<Payment> completedPayments = findPaymentsByStatusUseCase.execute("COMPLETED");
        
        // Verify results
        assertNotNull(completedPayments);
        assertEquals(1, completedPayments.size());
        assertEquals(completedPayment.getId(), completedPayments.get(0).getId());
    }
    
    @Test
    @Transactional
    void executeShouldReturnEmptyListForNonExistentStatus() {
        // Create test payments
        createPaymentUseCase.execute(pendingPayment1);
        createPaymentUseCase.execute(completedPayment);
        
        // Find payments with non-existent status
        List<Payment> cancelledPayments = findPaymentsByStatusUseCase.execute("CANCELLED");
        
        // Verify results
        assertNotNull(cancelledPayments);
        assertTrue(cancelledPayments.isEmpty());
    }
    
    @Test
    @Transactional
    void executeShouldBeCaseSensitive() {
        // Create test payments
        createPaymentUseCase.execute(pendingPayment1);
        
        // Find payments with same status but different case
        List<Payment> pendingPayments = findPaymentsByStatusUseCase.execute("pending");
        
        // Verify results (should be empty because of case-sensitivity)
        assertNotNull(pendingPayments);
        assertTrue(pendingPayments.isEmpty());
    }
}
