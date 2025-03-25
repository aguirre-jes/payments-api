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
class FindPaymentsByDateRangeUseCaseIntegrationTest {

    @Inject
    private CreatePaymentUseCase createPaymentUseCase;
    
    @Inject
    private FindPaymentsByDateRangeUseCase findPaymentsByDateRangeUseCase;

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    private Payment paymentJanuary;
    private Payment paymentFebruary;
    private Payment paymentMarch;
    private Payment paymentApril;

    @BeforeEach
    void setUp() {
        // Create payments with different dates throughout the year
        paymentJanuary = new Payment();
        paymentJanuary.setId(86);
        paymentJanuary.setAmount(new BigDecimal("100.00"));
        paymentJanuary.setCurrency("USD");
        paymentJanuary.setPaymentDate(LocalDateTime.of(2023, 1, 15, 12, 0));
        paymentJanuary.setStatus("COMPLETED");
        paymentJanuary.setPayerId(175);
        paymentJanuary.setPayeeId(275);
        paymentJanuary.setPaymentMethod("Credit Card");
        paymentJanuary.setTransactionId("TXN-JAN-2023");
        paymentJanuary.setDescription("January Payment");
        
        paymentFebruary = new Payment();
        paymentFebruary.setId(87);
        paymentFebruary.setAmount(new BigDecimal("150.00"));
        paymentFebruary.setCurrency("USD");
        paymentFebruary.setPaymentDate(LocalDateTime.of(2023, 2, 10, 14, 30));
        paymentFebruary.setStatus("COMPLETED");
        paymentFebruary.setPayerId(176);
        paymentFebruary.setPayeeId(276);
        paymentFebruary.setPaymentMethod("Bank Transfer");
        paymentFebruary.setTransactionId("TXN-FEB-2023");
        paymentFebruary.setDescription("February Payment");
        
        paymentMarch = new Payment();
        paymentMarch.setId(88);
        paymentMarch.setAmount(new BigDecimal("200.00"));
        paymentMarch.setCurrency("EUR");
        paymentMarch.setPaymentDate(LocalDateTime.of(2023, 3, 22, 9, 45));
        paymentMarch.setStatus("PENDING");
        paymentMarch.setPayerId(177);
        paymentMarch.setPayeeId(277);
        paymentMarch.setPaymentMethod("PayPal");
        paymentMarch.setTransactionId("TXN-MAR-2023");
        paymentMarch.setDescription("March Payment");
        
        paymentApril = new Payment();
        paymentApril.setId(89);
        paymentApril.setAmount(new BigDecimal("250.00"));
        paymentApril.setCurrency("GBP");
        paymentApril.setPaymentDate(LocalDateTime.of(2023, 4, 5, 16, 15));
        paymentApril.setStatus("COMPLETED");
        paymentApril.setPayerId(178);
        paymentApril.setPayeeId(278);
        paymentApril.setPaymentMethod("Credit Card");
        paymentApril.setTransactionId("TXN-APR-2023");
        paymentApril.setDescription("April Payment");
    }

    @Test
    @Transactional
    void executeShouldReturnPaymentsWithinDateRange() {
        // Create test payments
        createPaymentUseCase.execute(paymentJanuary);
        createPaymentUseCase.execute(paymentFebruary);
        createPaymentUseCase.execute(paymentMarch);
        createPaymentUseCase.execute(paymentApril);
        
        // Find payments for Q1 (Jan-Mar)
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 3, 31, 23, 59);
        
        List<Payment> q1Payments = findPaymentsByDateRangeUseCase.execute(startDate, endDate);
        
        // Verify results
        assertNotNull(q1Payments);
        assertEquals(3, q1Payments.size());
        
        // Verify correct payments are returned
        boolean containsJanuaryPayment = false;
        boolean containsFebruaryPayment = false;
        boolean containsMarchPayment = false;
        
        for (Payment payment : q1Payments) {
            if (payment.getId() == paymentJanuary.getId()) {
                containsJanuaryPayment = true;
            } else if (payment.getId() == paymentFebruary.getId()) {
                containsFebruaryPayment = true;
            } else if (payment.getId() == paymentMarch.getId()) {
                containsMarchPayment = true;
            }
        }
        
        assertTrue(containsJanuaryPayment, "Results should contain January payment");
        assertTrue(containsFebruaryPayment, "Results should contain February payment");
        assertTrue(containsMarchPayment, "Results should contain March payment");
        
        // Find payments for a specific month (Feb only)
        LocalDateTime febStartDate = LocalDateTime.of(2023, 2, 1, 0, 0);
        LocalDateTime febEndDate = LocalDateTime.of(2023, 2, 28, 23, 59);
        
        List<Payment> febPayments = findPaymentsByDateRangeUseCase.execute(febStartDate, febEndDate);
        
        // Verify results
        assertNotNull(febPayments);
        assertEquals(1, febPayments.size());
        assertEquals(paymentFebruary.getId(), febPayments.get(0).getId());
    }
    
    @Test
    @Transactional
    void executeShouldReturnEmptyListWhenNoPaymentsInRange() {
        // Create test payments
        createPaymentUseCase.execute(paymentJanuary);
        createPaymentUseCase.execute(paymentFebruary);
        
        // Find payments for a range with no payments (May-June)
        LocalDateTime startDate = LocalDateTime.of(2023, 5, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 6, 30, 23, 59);
        
        List<Payment> noPayments = findPaymentsByDateRangeUseCase.execute(startDate, endDate);
        
        // Verify results
        assertNotNull(noPayments);
        assertTrue(noPayments.isEmpty());
    }
    
    @Test
    @Transactional
    void executeShouldHandleSameDayRange() {
        // Create test payment
        createPaymentUseCase.execute(paymentJanuary);
        
        // Find payments for just January 15th
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 15, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 15, 23, 59);
        
        List<Payment> sameDayPayments = findPaymentsByDateRangeUseCase.execute(startDate, endDate);
        
        // Verify results
        assertNotNull(sameDayPayments);
        assertEquals(1, sameDayPayments.size());
        assertEquals(paymentJanuary.getId(), sameDayPayments.get(0).getId());
    }
    
    @Test
    @Transactional
    void executeShouldHandleExactTimeRanges() {
        // Create test payment
        createPaymentUseCase.execute(paymentFebruary); // Payment time is 14:30
        
        // Find payments for February 10th, morning only
        LocalDateTime morningStart = LocalDateTime.of(2023, 2, 10, 0, 0);
        LocalDateTime morningEnd = LocalDateTime.of(2023, 2, 10, 12, 0);
        
        List<Payment> morningPayments = findPaymentsByDateRangeUseCase.execute(morningStart, morningEnd);
        
        // Should find no payments (February payment is at 14:30)
        assertNotNull(morningPayments);
        assertTrue(morningPayments.isEmpty());
        
        // Find payments for February 10th, afternoon only
        LocalDateTime afternoonStart = LocalDateTime.of(2023, 2, 10, 12, 1);
        LocalDateTime afternoonEnd = LocalDateTime.of(2023, 2, 10, 23, 59);
        
        List<Payment> afternoonPayments = findPaymentsByDateRangeUseCase.execute(afternoonStart, afternoonEnd);
        
        // Should find the February payment (at 14:30)
        assertNotNull(afternoonPayments);
        assertEquals(1, afternoonPayments.size());
        assertEquals(paymentFebruary.getId(), afternoonPayments.get(0).getId());
    }
    
    @Test
    @Transactional
    void executeShouldHandleInvertedDateRange() {
        // Create test payment
        createPaymentUseCase.execute(paymentMarch);
        
        // Find payments with end date before start date
        LocalDateTime endDate = LocalDateTime.of(2023, 2, 1, 0, 0);
        LocalDateTime startDate = LocalDateTime.of(2023, 4, 1, 0, 0);
        
        List<Payment> invertedRangePayments = findPaymentsByDateRangeUseCase.execute(startDate, endDate);
        
        // Should return empty list for inverted date range
        assertNotNull(invertedRangePayments);
        assertTrue(invertedRangePayments.isEmpty());
    }
}
