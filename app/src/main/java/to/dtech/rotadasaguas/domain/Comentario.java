package to.dtech.rotadasaguas.domain;


public class Comentario {

    private String autor;
    private String comentario;

    public Comentario(){}
    public Comentario(String a, String c){
        autor = a;
        comentario = c;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
