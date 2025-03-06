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
import jeaguirre.me.domains.exceptions.PaymentSaveException;
import jeaguirre.me.utils.OracleDbContainerExtension;

@ExtendWith(OracleDbContainerExtension.class)
@HelidonTest
@Testcontainers
class CreatePaymentUseCaseIntegrationTest {

    @Inject
    private CreatePaymentUseCase createPaymentUseCase;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setId(30);
        payment.setAmount(new BigDecimal("175.50"));
        payment.setCurrency("USD");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("Pending");
        payment.setPayerId(120);
        payment.setPayeeId(220);
        payment.setPaymentMethod("Bank Transfer");
        payment.setTransactionId("TXN-CREATE-TEST");
        payment.setDescription("Create Payment Use Case Integration Test");
    }

    @Test
    @Transactional
    void executeShouldSavePaymentWithNullOptionalFields() {
        Payment paymentWithNulls = new Payment();
        paymentWithNulls.setId(31);
        paymentWithNulls.setAmount(new BigDecimal("200.25"));
        paymentWithNulls.setCurrency("EUR");
        paymentWithNulls.setPaymentDate(LocalDateTime.now());
        paymentWithNulls.setStatus("Processing");
        paymentWithNulls.setPayerId(121);
        paymentWithNulls.setPayeeId(221);
        paymentWithNulls.setPaymentMethod(null);
        paymentWithNulls.setTransactionId(null);
        paymentWithNulls.setDescription(null);

        Payment result = createPaymentUseCase.execute(paymentWithNulls);

        assertNotNull(result, "Result should not be null");
        assertEquals(paymentWithNulls.getId(), result.getId());
        assertNull(result.getPaymentMethod());
        assertNull(result.getTransactionId());
        assertNull(result.getDescription());

        Payment savedPayment = entityManager.find(Payment.class, paymentWithNulls.getId());
        assertNotNull(savedPayment, "Payment should be saved in the database");
        assertEquals(new BigDecimal("200.25"), savedPayment.getAmount());
        assertEquals("EUR", savedPayment.getCurrency());
        assertEquals("Processing", savedPayment.getStatus());
        assertNull(savedPayment.getPaymentMethod());
        assertNull(savedPayment.getTransactionId());
        assertNull(savedPayment.getDescription());
    }

    @Test
    @Transactional
    void executeShouldThrowExceptionWhenSavingDuplicateId() {
        createPaymentUseCase.execute(payment);

        Payment duplicatePayment = new Payment();
        duplicatePayment.setId(payment.getId());
        duplicatePayment.setAmount(new BigDecimal("300.00"));
        duplicatePayment.setCurrency("GBP");
        duplicatePayment.setPaymentDate(LocalDateTime.now());
        duplicatePayment.setStatus("Completed");
        duplicatePayment.setPayerId(122);
        duplicatePayment.setPayeeId(222);
        duplicatePayment.setPaymentMethod("PayPal");
        duplicatePayment.setTransactionId("TXN-DUPLICATE");
        duplicatePayment.setDescription("Duplicate Payment Test");

        assertThrows(PaymentSaveException.class, () -> {
            createPaymentUseCase.execute(duplicatePayment);
        });
    }

    @Test
    @Transactional
    void executeShouldSaveMultiplePayments() {
        Payment payment1 = new Payment();
        payment1.setId(32);
        payment1.setAmount(new BigDecimal("50.00"));
        payment1.setCurrency("USD");
        payment1.setPaymentDate(LocalDateTime.now());
        payment1.setStatus("Completed");
        payment1.setPayerId(123);
        payment1.setPayeeId(223);
        payment1.setPaymentMethod("Credit Card");
        payment1.setTransactionId("TXN-MULTI-1");
        payment1.setDescription("Multiple Payment Test 1");

        Payment payment2 = new Payment();
        payment2.setId(33);
        payment2.setAmount(new BigDecimal("75.00"));
        payment2.setCurrency("USD");
        payment2.setPaymentDate(LocalDateTime.now());
        payment2.setStatus("Pending");
        payment2.setPayerId(124);
        payment2.setPayeeId(224);
        payment2.setPaymentMethod("Debit Card");
        payment2.setTransactionId("TXN-MULTI-2");
        payment2.setDescription("Multiple Payment Test 2");

        Payment result1 = createPaymentUseCase.execute(payment1);
        Payment result2 = createPaymentUseCase.execute(payment2);

        assertNotNull(result1);
        assertNotNull(result2);

        Payment savedPayment1 = entityManager.find(Payment.class, payment1.getId());
        Payment savedPayment2 = entityManager.find(Payment.class, payment2.getId());

        assertNotNull(savedPayment1);
        assertNotNull(savedPayment2);
        assertEquals(new BigDecimal("50.00"), savedPayment1.getAmount());
        assertEquals(new BigDecimal("75.00"), savedPayment2.getAmount());
        assertEquals("TXN-MULTI-1", savedPayment1.getTransactionId());
        assertEquals("TXN-MULTI-2", savedPayment2.getTransactionId());
    }

    @Test
    @Transactional
    void executeShouldPreserveAllPaymentData() {
        LocalDateTime specificDate = LocalDateTime.of(2023, 10, 15, 14, 30, 0);
        payment.setPaymentDate(specificDate);

        createPaymentUseCase.execute(payment);

        Payment savedPayment = entityManager.find(Payment.class, payment.getId());
        assertNotNull(savedPayment);

        assertEquals(payment.getId(), savedPayment.getId());
        assertEquals(payment.getAmount(), savedPayment.getAmount());
        assertEquals(payment.getCurrency(), savedPayment.getCurrency());

        assertEquals(specificDate.getYear(), savedPayment.getPaymentDate().getYear());
        assertEquals(specificDate.getMonth(), savedPayment.getPaymentDate().getMonth());
        assertEquals(specificDate.getDayOfMonth(), savedPayment.getPaymentDate().getDayOfMonth());
        assertEquals(specificDate.getHour(), savedPayment.getPaymentDate().getHour());
        assertEquals(specificDate.getMinute(), savedPayment.getPaymentDate().getMinute());

        assertEquals(payment.getStatus(), savedPayment.getStatus());
        assertEquals(payment.getPayerId(), savedPayment.getPayerId());
        assertEquals(payment.getPayeeId(), savedPayment.getPayeeId());
        assertEquals(payment.getPaymentMethod(), savedPayment.getPaymentMethod());
        assertEquals(payment.getTransactionId(), savedPayment.getTransactionId());
        assertEquals(payment.getDescription(), savedPayment.getDescription());
    }

}
