package to.dtech.rotadasaguas.domain;


public class Comentario {

    private String autor;
    private String comentario;
    private String data;

    public Comentario(){}
    public Comentario(String a, String d, String c){
        autor = a;
        data = d;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
