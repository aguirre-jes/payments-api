package jeaguirre.me.adapters.inputs.rest;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
}
