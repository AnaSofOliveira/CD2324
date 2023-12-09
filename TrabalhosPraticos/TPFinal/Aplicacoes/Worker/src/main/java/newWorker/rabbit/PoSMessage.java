package newWorker.rabbit;

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
}
