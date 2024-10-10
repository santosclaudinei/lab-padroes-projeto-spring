package one.digitalinnovation.gof.dto;

public class ClienteUpdate {

    protected ClienteUpdate(String nome, String cep) {
        this.nome = nome;
        this.cep = cep;
    }

    private final String nome;
    private final String cep;

    public String getNome() {
        return nome;
    }

    public String getCep() {
        return cep;
    }

}
