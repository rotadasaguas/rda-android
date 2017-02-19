package to.dtech.rotadasaguas.domain;

import java.util.List;

public class Rota {

    String idRota;
    String cidade;
    List<String> alimentacao;
    List<String> lazer;
    List<String> acomodacao;

    public Rota(String idRota, String cidade, List<String> alimentacao, List<String> lazer, List<String> acomodacao) {
        this.idRota = idRota;
        this.cidade = cidade;
        this.alimentacao = alimentacao;
        this.lazer = lazer;
        this.acomodacao = acomodacao;
    }

    public List<String> getAlimentacao() {
        return alimentacao;
    }

    public void setAlimentacao(List<String> alimentacao) {
        this.alimentacao = alimentacao;
    }

    public List<String> getLazer() {
        return lazer;
    }

    public void setLazer(List<String> lazer) {
        this.lazer = lazer;
    }

    public List<String> getAcomodacao() {
        return acomodacao;
    }

    public void setAcomodacao(List<String> acomodacao) {
        this.acomodacao = acomodacao;
    }

    public String getIdRota() {
        return idRota;
    }

    public void setIdRota(String idRota) {
        this.idRota = idRota;
    }


    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}
