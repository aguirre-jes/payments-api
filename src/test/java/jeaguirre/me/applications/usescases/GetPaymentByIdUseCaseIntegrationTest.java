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
class GetPaymentByIdUseCaseIntegrationTest {

    @Inject
    private CreatePaymentUseCase createPaymentUseCase;
    
    @Inject
    private GetPaymentByIdUseCase getPaymentByIdUseCase;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    private Payment testPayment;

    @BeforeEach
    void setUp() {
        testPayment = new Payment();
        testPayment.setId(40);
        testPayment.setAmount(new BigDecimal("200.00"));
        testPayment.setCurrency("USD");
        testPayment.setPaymentDate(LocalDateTime.now());
        testPayment.setStatus("Completed");
        testPayment.setPayerId(130);
        testPayment.setPayeeId(230);
        testPayment.setPaymentMethod("Credit Card");
        testPayment.setTransactionId("TXN-GET-TEST");
        testPayment.setDescription("Get Payment Use Case Integration Test");
    }

    @Test
    @Transactional
    void executeShouldRetrievePaymentById() {
        // First create a payment
        createPaymentUseCase.execute(testPayment);
        
        // Then retrieve it by ID
        Payment retrievedPayment = getPaymentByIdUseCase.execute(testPayment.getId());
        
        // Verify all fields match
        assertNotNull(retrievedPayment);
        assertEquals(testPayment.getId(), retrievedPayment.getId());
        assertEquals(testPayment.getAmount(), retrievedPayment.getAmount());
        assertEquals(testPayment.getCurrency(), retrievedPayment.getCurrency());
        assertEquals(testPayment.getStatus(), retrievedPayment.getStatus());
        assertEquals(testPayment.getPayerId(), retrievedPayment.getPayerId());
        assertEquals(testPayment.getPayeeId(), retrievedPayment.getPayeeId());
        assertEquals(testPayment.getPaymentMethod(), retrievedPayment.getPaymentMethod());
        assertEquals(testPayment.getTransactionId(), retrievedPayment.getTransactionId());
        assertEquals(testPayment.getDescription(), retrievedPayment.getDescription());
    }
}
