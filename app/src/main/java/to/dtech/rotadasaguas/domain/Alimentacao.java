package to.dtech.rotadasaguas.domain;

/**
 * Created by siqueiradg on 21/09/2016.
 */
public class Alimentacao {
    private String model;
    private String brand;
    private int photo;


    public Alimentacao(){}
    public Alimentacao(String m, String b, int p){
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

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
