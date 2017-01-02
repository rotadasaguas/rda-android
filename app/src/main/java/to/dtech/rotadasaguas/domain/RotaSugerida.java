package to.dtech.rotadasaguas.domain;

public class RotaSugerida {

    private String nome;
    private String icone;

    public RotaSugerida(){}

    public RotaSugerida(String n, String i){
        nome = n;
        icone = i;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }
}
