package jeaguirre.me.adapters.inputs.rest.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jeaguirre.me.adapters.inputs.rest.dto.CreatePaymentRequest;
import jeaguirre.me.adapters.inputs.rest.dto.PaymentResponse;
import jeaguirre.me.adapters.outputs.persistence.entities.Payment;

class PaymentMapperTest {

    private PaymentMapper paymentMapper;
    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        paymentMapper = new PaymentMapper();
        testDate = LocalDateTime.of(2023, 10, 15, 14, 30);
    }

    @Test
    void shouldMapRequestDtoToEntity() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setAmount(new BigDecimal("100.50"));
        request.setCurrency("USD");
        request.setPaymentDate(testDate);
        request.setStatus("Pending");
        request.setPayerId(101);
        request.setPayeeId(201);
        request.setPaymentMethod("Credit Card");
        request.setTransactionId("TXN12345");
        request.setDescription("Test payment");

        Payment result = paymentMapper.toEntity(request);

        assertNotNull(result);
        assertEquals(new BigDecimal("100.50"), result.getAmount());
        assertEquals("USD", result.getCurrency());
        assertEquals(testDate, result.getPaymentDate());
        assertEquals("Pending", result.getStatus());
        assertEquals(101, result.getPayerId());
        assertEquals(201, result.getPayeeId());
        assertEquals("Credit Card", result.getPaymentMethod());
        assertEquals("TXN12345", result.getTransactionId());
        assertEquals("Test payment", result.getDescription());
    }

    @Test
    void shouldMapEntityToResponseDto() {
        Payment payment = new Payment();
        payment.setId(1);
        payment.setAmount(new BigDecimal("200.75"));
        payment.setCurrency("EUR");
        payment.setPaymentDate(testDate);
        payment.setStatus("Completed");
        payment.setPayerId(102);
        payment.setPayeeId(202);
        payment.setPaymentMethod("Bank Transfer");
        payment.setTransactionId("TXN67890");
        payment.setDescription("Test response");

        PaymentResponse result = paymentMapper.toResponse(payment);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(new BigDecimal("200.75"), result.getAmount());
        assertEquals("EUR", result.getCurrency());
        assertEquals(testDate, result.getPaymentDate());
        assertEquals("Completed", result.getStatus());
        assertEquals(102, result.getPayerId());
        assertEquals(202, result.getPayeeId());
        assertEquals("Bank Transfer", result.getPaymentMethod());
        assertEquals("TXN67890", result.getTransactionId());
        assertEquals("Test response", result.getDescription());
    }

    @Test
    void shouldHandleNullValuesWhenMappingRequestToEntity() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setAmount(new BigDecimal("150.25"));
        request.setCurrency("GBP");
        request.setPaymentDate(testDate);
        request.setStatus("Processing");
        request.setPayerId(103);
        request.setPayeeId(203);
        request.setPaymentMethod(null);
        request.setTransactionId(null);
        request.setDescription(null);

        Payment result = paymentMapper.toEntity(request);

        assertNotNull(result);
        assertEquals(new BigDecimal("150.25"), result.getAmount());
        assertEquals("GBP", result.getCurrency());
        assertEquals("Processing", result.getStatus());
        assertNull(result.getPaymentMethod());
        assertNull(result.getTransactionId());
        assertNull(result.getDescription());
    }

    @Test
    void shouldHandleNullValuesWhenMappingEntityToResponse() {
        Payment payment = new Payment();
        payment.setId(2);
        payment.setAmount(new BigDecimal("300.00"));
        payment.setCurrency("USD");
        payment.setPaymentDate(testDate);
        payment.setStatus("Failed");
        payment.setPayerId(104);
        payment.setPayeeId(204);
        payment.setPaymentMethod(null);
        payment.setTransactionId(null);
        payment.setDescription(null);

        PaymentResponse result = paymentMapper.toResponse(payment);

        assertNotNull(result);
        assertEquals(2, result.getId());
        assertEquals(new BigDecimal("300.00"), result.getAmount());
        assertEquals("Failed", result.getStatus());
        assertNull(result.getPaymentMethod());
        assertNull(result.getTransactionId());
        assertNull(result.getDescription());
    }

    @Test
    void shouldPreserveDecimalPrecisionWhenMapping() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setAmount(new BigDecimal("123.45"));

        Payment entity = paymentMapper.toEntity(request);
        PaymentResponse response = paymentMapper.toResponse(entity);

        assertEquals(new BigDecimal("123.45"), entity.getAmount());
        assertEquals(new BigDecimal("123.45"), response.getAmount());
    }

    @Test
    void shouldMapEmptyRequestToEntity() {
        CreatePaymentRequest emptyRequest = new CreatePaymentRequest();

        Payment result = paymentMapper.toEntity(emptyRequest);

        assertNotNull(result);
        assertNull(result.getAmount());
        assertNull(result.getCurrency());
        assertNull(result.getPaymentDate());
        assertNull(result.getStatus());
        assertEquals(0, result.getPayerId());
        assertEquals(0, result.getPayeeId());
        assertNull(result.getPaymentMethod());
        assertNull(result.getTransactionId());
        assertNull(result.getDescription());
    }

    @Test
    void shouldMapEmptyEntityToResponse() {
        Payment emptyPayment = new Payment();

        PaymentResponse result = paymentMapper.toResponse(emptyPayment);

        assertNotNull(result);
        assertEquals(0, result.getId());
        assertNull(result.getAmount());
        assertNull(result.getCurrency());
        assertNull(result.getPaymentDate());
        assertNull(result.getStatus());
        assertEquals(0, result.getPayerId());
        assertEquals(0, result.getPayeeId());
        assertNull(result.getPaymentMethod());
        assertNull(result.getTransactionId());
        assertNull(result.getDescription());
    }

}
