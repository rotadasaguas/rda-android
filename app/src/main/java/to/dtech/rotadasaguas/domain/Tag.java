package to.dtech.rotadasaguas.domain;


import com.like.LikeButton;

public class Tag{

    private String titulo;
    private String numero;
    private Boolean ativo;

    public Tag(){}
    public Tag(String t, Boolean i, String n){
        titulo = t;
        ativo = i;
        numero = n;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
