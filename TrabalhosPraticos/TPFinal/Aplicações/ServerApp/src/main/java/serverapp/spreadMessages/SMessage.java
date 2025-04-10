package serverapp.spreadMessages;

public class SMessage {

    private final long id;
    private SMessageValues value;
    private final String exchange;
    private String tipo;

    public SMessage(long id, SMessageValues value, String exchange, String tipo){
        this.id = id;
        this.value = value;
        this.exchange = exchange;
        this.tipo = tipo;
    }

    public long getId() {
        return id;
    }

    public SMessageValues getValue() {
        return value;
    }

    public String getExchange() {
        return exchange;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setValue(SMessageValues value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Message(" + id + ", " + value +  ")";
    }
}
