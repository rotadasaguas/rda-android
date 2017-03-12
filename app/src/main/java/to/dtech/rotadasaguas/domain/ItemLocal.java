package to.dtech.rotadasaguas.domain;

public class ItemLocal {
    private String nome;
    private String descricao;
    private String photo;
    private String endereco;
    private String rating;


    public ItemLocal(){}
    public ItemLocal(String n, String d, String e, String f, String r){
        nome = n;
        descricao = d;
        endereco = e;
        photo = f;
        rating = r;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
