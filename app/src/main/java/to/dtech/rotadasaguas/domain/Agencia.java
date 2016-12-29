package to.dtech.rotadasaguas.domain;

public class Agencia {

    private String nome;
    private String descricao;
    private String cidade;
    private String estado;
    private String cep;
    private String endereco;
    private String bairro;
    private String numero;
    private String telefone;

    public Agencia(){}

    public Agencia(String n, String d, String c, String e, String ed, String cp, String b, String nu, String t){
        nome = n;
        descricao = d;
        cidade = c;
        estado = e;
        endereco = ed;
        cep = cp;
        bairro = b;
        numero = nu;
        telefone = t;
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

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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
