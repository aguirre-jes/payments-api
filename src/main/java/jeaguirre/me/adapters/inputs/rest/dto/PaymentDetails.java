package jeaguirre.me.adapters.inputs.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public int getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public int getPayerId() {
        return payerId;
    }

    public int getPayeeId() {
        return payeeId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }

    public void setPayeeId(int payeeId) {
        this.payeeId = payeeId;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
