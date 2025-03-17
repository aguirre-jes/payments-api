package jeaguirre.me.adapters.inputs.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jeaguirre.me.adapters.inputs.rest.dto.CreatePaymentRequest;
import jeaguirre.me.adapters.inputs.rest.dto.PaymentResponse;
import jeaguirre.me.utils.OracleDbContainerExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

@HelidonTest
@ExtendWith(OracleDbContainerExtension.class)
@Testcontainers
class PaymentResourceTest {

    @Inject
    private WebTarget webTarget;
        
    @Test
    void shouldCreatePaymentAndReturnCreatedStatus() {
        CreatePaymentRequest requestDto = createSampleRequest();
        
        Response response = webTarget.path("/payments")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(requestDto));
        
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        PaymentResponse actualResponse = response.readEntity(PaymentResponse.class);
        assertNotNull(actualResponse);
        assertEquals(55, actualResponse.getId());
        assertEquals(new BigDecimal("100.00"), actualResponse.getAmount());
        assertEquals("USD", actualResponse.getCurrency());
    }
    
    private CreatePaymentRequest createSampleRequest() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setId(55);
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency("USD");
        request.setPaymentDate(LocalDateTime.now());
        request.setStatus("Pending");
        request.setPayerId(101);
        request.setPayeeId(201);
        request.setPaymentMethod("Credit Card");
        request.setTransactionId("TX12345");
        request.setDescription("Test payment");
        return request;
    }
}