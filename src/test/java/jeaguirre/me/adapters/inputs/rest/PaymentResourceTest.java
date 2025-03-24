package jeaguirre.me.adapters.inputs.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
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

        @Test
        void shouldRetrievePaymentById() {
                Response response = webTarget.path("/payments/" + 1)
                                .request(MediaType.APPLICATION_JSON)
                                .get();

                assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
                PaymentResponse retrievedPayment = response.readEntity(PaymentResponse.class);
                assertNotNull(retrievedPayment);
                assertEquals(1, retrievedPayment.getId());
                assertEquals(new BigDecimal("100"), retrievedPayment.getAmount());
                assertEquals("USD", retrievedPayment.getCurrency());
        }

        @Test
        void shouldUpdateExistingPayment() {

                // Prepare update request
                CreatePaymentRequest updateRequest = createSampleRequest();
                updateRequest.setAmount(new BigDecimal("200.00"));
                updateRequest.setDescription("Updated test payment");

                // Update the payment
                Response updateResponse = webTarget.path("/payments/" + 2)
                                .request(MediaType.APPLICATION_JSON)
                                .put(Entity.json(updateRequest));

                assertEquals(Response.Status.OK.getStatusCode(), updateResponse.getStatus());
                PaymentResponse updatedPayment = updateResponse.readEntity(PaymentResponse.class);
                assertNotNull(updatedPayment);
                assertEquals(2, updatedPayment.getId());
                assertEquals(new BigDecimal("200.00"), updatedPayment.getAmount());
                assertEquals("Updated test payment", updatedPayment.getDescription());
        }

        @Test
        void shouldDeleteExistingPayment() {
                // Delete the payment
                Response deleteResponse = webTarget.path("/payments/" + 4)
                                .request()
                                .delete();

                assertEquals(Response.Status.NO_CONTENT.getStatusCode(), deleteResponse.getStatus());

                // Verify payment no longer exists
                Response getResponse = webTarget.path("/payments/" + 4)
                                .request(MediaType.APPLICATION_JSON)
                                .get();

                assertEquals(Response.Status.NOT_FOUND.getStatusCode(), getResponse.getStatus());
        }

        @Test
        void shouldFindPaymentsByStatus() {
                // Then find payments by status
                Response response = webTarget.path("/payments/status/Pending")
                                .request(MediaType.APPLICATION_JSON)
                                .get();

                assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
                List<PaymentResponse> payments = response.readEntity(new GenericType<List<PaymentResponse>>() {
                });
                assertNotNull(payments);
                assertTrue(payments.size() > 0);
                assertEquals("Pending", payments.get(0).getStatus());
        }

        @Test
        void shouldFindPaymentsByPayerId() {
                // Then find payments by payerId
                Response response = webTarget.path("/payments/payer/103")
                                .request(MediaType.APPLICATION_JSON)
                                .get();

                assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
                List<PaymentResponse> payments = response.readEntity(new GenericType<List<PaymentResponse>>() {
                });
                assertNotNull(payments);
                assertTrue(payments.size() > 0);
                assertEquals(103, payments.get(0).getPayerId());
        }

        @Test
        void shouldFindPaymentsByPayeeId() {
                // Then find payments by payeeId
                Response response = webTarget.path("/payments/payee/203")
                                .request(MediaType.APPLICATION_JSON)
                                .get();

                assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
                List<PaymentResponse> payments = response.readEntity(new GenericType<List<PaymentResponse>>() {
                });
                assertNotNull(payments);
                assertTrue(payments.size() > 0);
                assertEquals(203, payments.get(0).getPayeeId());
        }

        @Test
        void shouldFindPaymentsByDateRange() {

                LocalDateTime now = LocalDateTime.now();

                // Define date range (one day before and after)
                LocalDateTime startDate = now.minusDays(1);
                LocalDateTime endDate = now.plusDays(1);
                String startDateStr = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                String endDateStr = endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                // Then find payments by date range
                Response response = webTarget.path("/payments/date-range")
                                .queryParam("startDate", startDateStr)
                                .queryParam("endDate", endDateStr)
                                .request(MediaType.APPLICATION_JSON)
                                .get();

                assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
                List<PaymentResponse> payments = response.readEntity(new GenericType<List<PaymentResponse>>() {
                });
                assertNotNull(payments);
                assertTrue(payments.size() > 0);
                // Verify payment date is in range (should match the date we set)
                assertEquals(now.getYear(), payments.get(0).getPaymentDate().getYear());
                assertEquals(now.getMonth(), payments.get(0).getPaymentDate().getMonth());
                assertEquals(now.getDayOfMonth(), payments.get(0).getPaymentDate().getDayOfMonth());
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