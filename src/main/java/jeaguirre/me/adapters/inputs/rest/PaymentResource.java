package jeaguirre.me.adapters.inputs.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jeaguirre.me.adapters.inputs.rest.dto.CreatePaymentRequest;
import jeaguirre.me.adapters.inputs.rest.dto.PaymentResponse;
import jeaguirre.me.adapters.inputs.rest.mappers.PaymentMapper;
import jeaguirre.me.adapters.outputs.persistence.entities.Payment;
import jeaguirre.me.applications.PaymentService;

@Path("/payments")
public class PaymentResource {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @Inject
    public PaymentResource(PaymentService paymentService, PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new payment", description = "Creates a new payment and returns the details of the created payment")
    @APIResponse(description = "Details of the created payment", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)))
    public Response createPayment(CreatePaymentRequest request) {
        Payment payment = paymentMapper.toEntity(request);
        paymentService.savePayment(payment);
        PaymentResponse response = paymentMapper.toResponse(payment);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get payment by ID", description = "Returns the payment details for the specified ID")
    @APIResponse(description = "The payment details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)))
    @APIResponse(responseCode = "404", description = "Payment not found")
    public Response getPaymentById(@PathParam("id") int id) {
        try {
            Payment payment = paymentService.getPaymentById(id);
            PaymentResponse response = paymentMapper.toResponse(payment);
            return Response.ok(response).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update payment", description = "Updates an existing payment with the provided details")
    @APIResponse(description = "The updated payment details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)))
    @APIResponse(responseCode = "404", description = "Payment not found")
    public Response updatePayment(@PathParam("id") int id, CreatePaymentRequest request) {
        try {
            // Verify payment exists
            paymentService.getPaymentById(id);

            // Map request to entity
            Payment payment = paymentMapper.toEntity(request);
            payment.setId(id);

            // Update payment
            paymentService.update(payment);

            // Return updated payment
            PaymentResponse response = paymentMapper.toResponse(payment);
            return Response.ok(response).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete payment", description = "Deletes the payment with the specified ID")
    @APIResponse(responseCode = "204", description = "Payment successfully deleted")
    @APIResponse(responseCode = "404", description = "Payment not found")
    public Response deletePayment(@PathParam("id") int id) {
        try {
            // Verify payment exists
            paymentService.getPaymentById(id);

            // Delete payment
            paymentService.deleteById(id);

            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/status/{status}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Find payments by status", description = "Returns all payments with the specified status")
    @APIResponse(description = "List of payments", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)))
    public Response findByStatus(@PathParam("status") String status) {
        List<Payment> payments = paymentService.findByStatus(status);
        List<PaymentResponse> responseList = payments.stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
        return Response.ok(responseList).build();
    }

    @GET
    @Path("/payer/{payerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Find payments by payer ID", description = "Returns all payments made by the specified payer")
    @APIResponse(description = "List of payments", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)))
    public Response findByPayerId(@PathParam("payerId") int payerId) {
        List<Payment> payments = paymentService.findByPayerId(payerId);
        List<PaymentResponse> responseList = payments.stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
        return Response.ok(responseList).build();
    }

    @GET
    @Path("/payee/{payeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Find payments by payee ID", description = "Returns all payments received by the specified payee")
    @APIResponse(description = "List of payments", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)))
    public Response findByPayeeId(@PathParam("payeeId") int payeeId) {
        List<Payment> payments = paymentService.findByPayeeId(payeeId);
        List<PaymentResponse> responseList = payments.stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
        return Response.ok(responseList).build();
    }

    @GET
    @Path("/date-range")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Find payments by date range", description = "Returns all payments made between the specified dates")
    @APIResponse(description = "List of payments", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)))
    public Response findByPaymentDateBetween(
            @QueryParam("startDate") @Parameter(description = "Start date (ISO format)") String startDateStr,
            @QueryParam("endDate") @Parameter(description = "End date (ISO format)") String endDateStr) {

        LocalDateTime startDate = LocalDateTime.parse(startDateStr);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr);

        List<Payment> payments = paymentService.findByPaymentDateBetween(startDate, endDate);
        List<PaymentResponse> responseList = payments.stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
        return Response.ok(responseList).build();
    }
}
