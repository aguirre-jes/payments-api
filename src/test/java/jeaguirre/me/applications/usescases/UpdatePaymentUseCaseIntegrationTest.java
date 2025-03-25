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
class UpdatePaymentUseCaseIntegrationTest {

    @Inject
    private CreatePaymentUseCase createPaymentUseCase;
    
    @Inject
    private UpdatePaymentUseCase updatePaymentUseCase;
    
    @Inject
    private GetPaymentByIdUseCase getPaymentByIdUseCase;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    private Payment originalPayment;

    @BeforeEach
    void setUp() {
        originalPayment = new Payment();
        originalPayment.setId(50);
        originalPayment.setAmount(new BigDecimal("300.00"));
        originalPayment.setCurrency("USD");
        originalPayment.setPaymentDate(LocalDateTime.now());
        originalPayment.setStatus("Pending");
        originalPayment.setPayerId(140);
        originalPayment.setPayeeId(240);
        originalPayment.setPaymentMethod("Bank Transfer");
        originalPayment.setTransactionId("TXN-UPDATE-ORIG");
        originalPayment.setDescription("Update Payment Use Case - Original");
    }

    @Test
    @Transactional
    void executeShouldUpdateExistingPayment() {
        // First create a payment
        createPaymentUseCase.execute(originalPayment);
        
        // Create an updated version
        Payment updatedPayment = new Payment();
        updatedPayment.setId(originalPayment.getId());
        updatedPayment.setAmount(new BigDecimal("350.00"));
        updatedPayment.setCurrency("EUR");
        updatedPayment.setPaymentDate(LocalDateTime.now());
        updatedPayment.setStatus("Completed");
        updatedPayment.setPayerId(originalPayment.getPayerId());
        updatedPayment.setPayeeId(originalPayment.getPayeeId());
        updatedPayment.setPaymentMethod("Credit Card");
        updatedPayment.setTransactionId("TXN-UPDATE-NEW");
        updatedPayment.setDescription("Update Payment Use Case - Updated");
        
        // Update the payment
        updatePaymentUseCase.execute(updatedPayment);
        
        // Verify the payment was updated
        Payment retrievedPayment = getPaymentByIdUseCase.execute(originalPayment.getId());
        
        assertNotNull(retrievedPayment);
        assertEquals(updatedPayment.getId(), retrievedPayment.getId());
        assertEquals(updatedPayment.getAmount(), retrievedPayment.getAmount());
        assertEquals(updatedPayment.getCurrency(), retrievedPayment.getCurrency());
        assertEquals(updatedPayment.getStatus(), retrievedPayment.getStatus());
        assertEquals(updatedPayment.getPaymentMethod(), retrievedPayment.getPaymentMethod());
        assertEquals(updatedPayment.getTransactionId(), retrievedPayment.getTransactionId());
        assertEquals(updatedPayment.getDescription(), retrievedPayment.getDescription());
    }
        
    @Test
    @Transactional
    void executeShouldUpdateOnlyChangedFields() {
        // First create a payment
        createPaymentUseCase.execute(originalPayment);
        
        // Create a payment with only some fields updated
        Payment partialUpdate = new Payment();
        partialUpdate.setId(originalPayment.getId());
        partialUpdate.setAmount(new BigDecimal("325.00"));
        partialUpdate.setCurrency(originalPayment.getCurrency());
        partialUpdate.setPaymentDate(originalPayment.getPaymentDate());
        partialUpdate.setStatus("Completed");
        partialUpdate.setPayerId(originalPayment.getPayerId());
        partialUpdate.setPayeeId(originalPayment.getPayeeId());
        partialUpdate.setPaymentMethod(originalPayment.getPaymentMethod());
        partialUpdate.setTransactionId(originalPayment.getTransactionId());
        partialUpdate.setDescription(null); // Remove description
        
        // Update the payment
        updatePaymentUseCase.execute(partialUpdate);
        
        // Verify only the specified fields were updated
        Payment retrievedPayment = getPaymentByIdUseCase.execute(originalPayment.getId());
        
        assertNotNull(retrievedPayment);
        assertEquals(new BigDecimal("325.00"), retrievedPayment.getAmount());
        assertEquals("Completed", retrievedPayment.getStatus());
        assertEquals(originalPayment.getCurrency(), retrievedPayment.getCurrency());
        assertEquals(originalPayment.getPayerId(), retrievedPayment.getPayerId());
        assertEquals(originalPayment.getPayeeId(), retrievedPayment.getPayeeId());
        assertEquals(originalPayment.getPaymentMethod(), retrievedPayment.getPaymentMethod());
        assertEquals(originalPayment.getTransactionId(), retrievedPayment.getTransactionId());
        assertNull(retrievedPayment.getDescription());
    }
}
