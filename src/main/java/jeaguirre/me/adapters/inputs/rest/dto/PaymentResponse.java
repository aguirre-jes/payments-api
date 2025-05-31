package jeaguirre.me.adapters.inputs.rest.dto;

public class PaymentResponse extends PaymentDetails {
    private int id;
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public void setId(int id) {
        this.id = id;
    }

}
