package to.dtech.rotadasaguas.domain;

import java.util.List;

public class Rota {

    String idRota;
    String cidade;
    List<String> marcadores;

    public Rota(String userId, List<String> lista, String cidade){
        this.setIdRota(userId);
        this.setCidade(cidade);
        this.setMarcadores(lista);
    }

    public String getIdRota() {
        return idRota;
    }

    public void setIdRota(String idRota) {
        this.idRota = idRota;
    }

    public List<String> getMarcadores() {
        return marcadores;
    }

    public void setMarcadores(List<String> marcadores) {
        this.marcadores = marcadores;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}
