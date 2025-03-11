package jeaguirre.me.adapters.inputs.rest;

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
import jeaguirre.me.applications.usescases.CreatePaymentUseCase;

@Path("/payments")
public class PaymentController {

    private final CreatePaymentUseCase createPaymentUseCase;
    private final PaymentMapper paymentMapper;

    @Inject
    public PaymentController(CreatePaymentUseCase createPaymentUseCase, PaymentMapper paymentMapper) {
        this.createPaymentUseCase = createPaymentUseCase;
        this.paymentMapper = new PaymentMapper();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPayment(CreatePaymentRequest requestDto) {
        Payment payment = paymentMapper.toEntity(requestDto);
        Payment createdPayment = createPaymentUseCase.execute(payment);
        PaymentResponse responseDto = paymentMapper.toResponse(createdPayment);
        return Response.status(Response.Status.CREATED).entity(responseDto).build();
    }

}
