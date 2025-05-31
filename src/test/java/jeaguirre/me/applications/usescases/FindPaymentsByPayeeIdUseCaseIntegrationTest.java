package jeaguirre.me.applications.usescases;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jeaguirre.me.adapters.outputs.persistence.entities.Payment;
import jeaguirre.me.domains.exceptions.PaymentSearchException;
import jeaguirre.me.utils.OracleDbContainerExtension;

@ExtendWith(OracleDbContainerExtension.class)
@HelidonTest
@Testcontainers
class FindPaymentsByPayeeIdUseCaseIntegrationTest {

    @Inject
    private FindPaymentsByPayeeIdUseCase findPaymentsByPayeeIdUseCase;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    @Test
    @Transactional
    void executeShouldReturnPaymentsForSpecificPayee() {

        // Find payments for payee 1
        List<Payment> paymentsToPayee1 = findPaymentsByPayeeIdUseCase.execute(201);

        // Verify results
        assertNotNull(paymentsToPayee1);
        assertEquals(7, paymentsToPayee1.size());
    }

    @Test
    @Transactional
    void executeShouldReturnEmptyListForNonExistentPayee() {
        PaymentSearchException exception = assertThrows(
                PaymentSearchException.class,
                () -> findPaymentsByPayeeIdUseCase.execute(999));

        assertNotNull(exception.getMessage(), "Exception message should not be null");
    }
}
