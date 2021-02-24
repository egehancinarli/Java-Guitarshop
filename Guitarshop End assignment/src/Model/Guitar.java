package Model;

import java.io.Serializable;

public class Guitar  implements Serializable {
    private String brand;
    private String Model;
    private boolean isAcoustic;
    private GuitarType type;
    private double price;
    private int stock;

    // for not providing a stock
    public Guitar(String brand, String model, boolean isAcoustic, GuitarType type, double price) {
       this(brand,model,isAcoustic,type,price,0);
    }



    public Guitar(String brand, String model, boolean isAcoustic, GuitarType type, double price, int stock) {
        this.brand = brand;
        Model = model;
        this.isAcoustic = isAcoustic;
        this.type = type;
        this.price = price;
        this.stock = stock;
    }

// Getters and setters


    public int getStock() { return stock; }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return Model;
    }

    public boolean getIsAcoustic() { return isAcoustic; }

    public GuitarType getType() {
        return type;
    }

    public void setStock(int stock) { this.stock = stock; }

    public double getPrice() {
        return price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        Model = model;
    }

    public void setAcoustic(boolean acoustic) {
        isAcoustic = acoustic;
    }

    public void setType(GuitarType type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
