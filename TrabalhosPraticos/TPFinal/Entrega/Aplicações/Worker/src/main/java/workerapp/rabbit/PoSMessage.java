package workerapp.rabbit;

public class PoSMessage {

    private final String produto;
    private final String categoria;

    public PoSMessage(String produto, String categoria){
        this.produto = produto; 
        this.categoria = categoria;
    }

    public String getProduto() {
        return produto;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return "PoSMessage(" +
                "produto='" + produto + '\'' +
                ", categoria='" + categoria + '\'' +
                ')';
    }
}
