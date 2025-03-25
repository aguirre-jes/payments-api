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
class FindPaymentsByPayerIdUseCaseIntegrationTest {

    @Inject
    private FindPaymentsByPayerIdUseCase findPaymentsByPayerIdUseCase;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    @Test
    @Transactional
    void executeShouldReturnPaymentsForSpecificPayer() {
        List<Payment> paymentsFromPayer1 = findPaymentsByPayerIdUseCase.execute(101);

        assertNotNull(paymentsFromPayer1);
    }

    @Test
    @Transactional
    void executeShouldReturnEmptyListForNonExistentPayer() {
        PaymentSearchException exception = assertThrows(
                PaymentSearchException.class,
                () -> findPaymentsByPayerIdUseCase.execute(999));

        assertNotNull(exception.getMessage(), "Exception message should not be null");
    }
}
