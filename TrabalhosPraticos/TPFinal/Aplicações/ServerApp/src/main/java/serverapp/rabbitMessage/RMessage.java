package serverapp.rabbitMessage;

public class RMessage {

    private final long id;
    private final String nomeFicheiro;

    public RMessage(long id, String nomeFicheiro){
        this.id = id; 
        this.nomeFicheiro = nomeFicheiro;
    }

    public long getId() {
        return id;
    }

    public String getNomeFicheiro() {
        return nomeFicheiro;
    }

    @Override
    public String toString() {
        return "RMessage( id='" + id + "', nomeFicheiro='" + nomeFicheiro + "')";
    }
}
