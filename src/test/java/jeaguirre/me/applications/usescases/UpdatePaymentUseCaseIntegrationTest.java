package jeaguirre.me.applications.usescases;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private UpdatePaymentUseCase updatePaymentUseCase;

    @Inject
    private GetPaymentByIdUseCase getPaymentByIdUseCase;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    @Test
    @Transactional
    void executeShouldUpdateExistingPayment() {

        // Create an updated version
        Payment updatedPayment = new Payment();
        updatedPayment.setId(7);
        updatedPayment.setAmount(new BigDecimal("350.00"));
        updatedPayment.setCurrency("EUR");
        updatedPayment.setPaymentDate(LocalDateTime.now());
        updatedPayment.setStatus("Completed");
        updatedPayment.setPayerId(103);
        updatedPayment.setPayeeId(203);
        updatedPayment.setPaymentMethod("Credit Card");
        updatedPayment.setTransactionId("TXN-UPDATE-NEW");
        updatedPayment.setDescription("Update Payment Use Case - Updated");

        // Update the payment
        updatePaymentUseCase.execute(updatedPayment);

        // Verify the payment was updated
        Payment retrievedPayment = getPaymentByIdUseCase.execute(7);

        assertNotNull(retrievedPayment);
        assertEquals(updatedPayment.getId(), retrievedPayment.getId());
        assertEquals(updatedPayment.getAmount(), retrievedPayment.getAmount());
        assertEquals(updatedPayment.getCurrency(), retrievedPayment.getCurrency());
        assertEquals(updatedPayment.getStatus(), retrievedPayment.getStatus());
        assertEquals(updatedPayment.getPaymentMethod(), retrievedPayment.getPaymentMethod());
        assertEquals(updatedPayment.getTransactionId(), retrievedPayment.getTransactionId());
        assertEquals(updatedPayment.getDescription(), retrievedPayment.getDescription());
    }
}
