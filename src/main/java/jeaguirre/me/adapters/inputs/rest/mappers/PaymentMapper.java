package jeaguirre.me.adapters.inputs.rest.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jeaguirre.me.adapters.inputs.rest.dto.CreatePaymentRequest;
import jeaguirre.me.adapters.inputs.rest.dto.PaymentResponse;
import jeaguirre.me.adapters.outputs.persistence.entities.Payment;

@ApplicationScoped
public class PaymentMapper {

    public Payment toEntity(CreatePaymentRequest requestDto) {
        Payment payment = new Payment();
        payment.setAmount(requestDto.getAmount());
        payment.setCurrency(requestDto.getCurrency());
        payment.setPaymentDate(requestDto.getPaymentDate());
        payment.setStatus(requestDto.getStatus());
        payment.setPayerId(requestDto.getPayerId());
        payment.setPayeeId(requestDto.getPayeeId());
        payment.setPaymentMethod(requestDto.getPaymentMethod());
        payment.setTransactionId(requestDto.getTransactionId());
        payment.setDescription(requestDto.getDescription());
        return payment;
    }

    public PaymentResponse toResponse(Payment payment) {
        PaymentResponse responseDto = new PaymentResponse();
        responseDto.setId(payment.getId());
        responseDto.setAmount(payment.getAmount());
        responseDto.setCurrency(payment.getCurrency());
        responseDto.setPaymentDate(payment.getPaymentDate());
        responseDto.setStatus(payment.getStatus());
        responseDto.setPayerId(payment.getPayerId());
        responseDto.setPayeeId(payment.getPayeeId());
        responseDto.setPaymentMethod(payment.getPaymentMethod());
        responseDto.setTransactionId(payment.getTransactionId());
        responseDto.setDescription(payment.getDescription());
        return responseDto;
    }

}
