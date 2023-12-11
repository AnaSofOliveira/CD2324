package posapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SalesMessage {

    private final String produto;
    private final Date data;
    private final String categoria;
    private final int quantidade;
    private final double precoSIVA;
    private final double precoCIVA;

    public SalesMessage(String produto, String categoria, int quantidade, double preco){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        this.data = new Date(System.currentTimeMillis());
        this.produto = produto;
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.precoSIVA = preco;
        this.precoCIVA = preco + preco*0.23;

    }

    public String getProduto() {
        return produto;
    }

    public Date getData() {
        return data;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoSIVA() {
        return precoSIVA;
    }

    public double getPrecoCIVA() {
        return precoCIVA;
    }

    @Override
    public String toString() {
        return "PoSMessage(" +
                "produto='" + produto + '\'' +
                ", categoria='" + categoria + '\'' +
                ')';
    }
}
