package jeaguirre.me.adapters.inputs.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentDetails {
    private int id;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime paymentDate;
    private String status;
    private int payerId;
    private int payeeId;
    private String paymentMethod;
    private String transactionId;
    private String description;
    
}
