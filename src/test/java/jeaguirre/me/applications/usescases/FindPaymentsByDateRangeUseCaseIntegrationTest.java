package jeaguirre.me.applications.usescases;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
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
import jeaguirre.me.utils.OracleDbContainerExtension;

@ExtendWith(OracleDbContainerExtension.class)
@HelidonTest
@Testcontainers
class FindPaymentsByDateRangeUseCaseIntegrationTest {

    @Inject
    private FindPaymentsByDateRangeUseCase findPaymentsByDateRangeUseCase;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    @Test
    @Transactional
    void executeShouldReturnPaymentsWithinDateRange() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(1);
        LocalDateTime endDate = now.plusDays(1);

        List<Payment> recentPayments = findPaymentsByDateRangeUseCase.execute(startDate, endDate);

        assertNotNull(recentPayments);
        assertFalse(recentPayments.isEmpty(), "Should find payments within date range");
    }
}
