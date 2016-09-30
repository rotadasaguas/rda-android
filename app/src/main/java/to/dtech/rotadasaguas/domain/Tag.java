package to.dtech.rotadasaguas.domain;


import com.like.LikeButton;

public class Tag{

    private String titulo;
    private Boolean ativo;

    public Tag(){}
    public Tag(String t, Boolean i){
        titulo = t;
        ativo = i;
    }

    public String getTitulo() {
        return titulo;
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
