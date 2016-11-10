package to.dtech.rotadasaguas.domain;

public class Alimentacao {
    private String model;
    private String brand;
    private String photo;


    public Alimentacao(){}
    public Alimentacao(String m, String b, String p){
        model = m;
        brand = b;
        photo = p;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
