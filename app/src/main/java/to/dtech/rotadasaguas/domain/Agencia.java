package to.dtech.rotadasaguas.domain;

public class Agencia {

    private String nome;
    private String telefone;
    private String endereco;

    public Agencia(){}

    public Agencia(String n, String ed, String t){
        nome = n;
        endereco = ed;
        telefone = t;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
