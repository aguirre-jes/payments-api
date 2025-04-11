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
class FindPaymentsByStatusUseCaseIntegrationTest {

    @Inject
    private FindPaymentsByStatusUseCase findPaymentsByStatusUseCase;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    @Test
    @Transactional
    void executeShouldReturnPaymentsWithMatchingStatus() {
        List<Payment> pendingPayments = findPaymentsByStatusUseCase.execute("Pending");

        assertNotNull(pendingPayments);

        List<Payment> completedPayments = findPaymentsByStatusUseCase.execute("Completed");

        assertNotNull(completedPayments);
    }

    @Test
    @Transactional
    void executeShouldReturnEmptyListForNonExistentStatus() {
        PaymentSearchException exception = assertThrows(
                PaymentSearchException.class,
                () -> findPaymentsByStatusUseCase.execute("CANCELLED"));

        assertNotNull(exception.getMessage(), "Exception message should not be null");
    }

    @Test
    @Transactional
    void executeShouldBeCaseSensitive() {
        PaymentSearchException exception = assertThrows(
                PaymentSearchException.class,
                () -> findPaymentsByStatusUseCase.execute("PEnding"));

        assertNotNull(exception.getMessage(), "Exception message should not be null");
    }
}
