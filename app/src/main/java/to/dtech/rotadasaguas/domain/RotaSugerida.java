package to.dtech.rotadasaguas.domain;

public class RotaSugerida {

    private String nome;
    private String valores;
    private String icone;

    public RotaSugerida(){}

    public RotaSugerida(String n, String v ,String i){
        nome = n;
        valores = v;
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

    public String getValores() {
        return valores;
    }

    public void setValores(String valores) {
        this.valores = valores;
    }
}
