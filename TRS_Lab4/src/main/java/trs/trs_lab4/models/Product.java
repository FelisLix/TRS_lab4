package trs.trs_lab4.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;

public class Product {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleDoubleProperty price;
    private final SimpleIntegerProperty quantity;
    private final SimpleStringProperty createdAt;
    private final SimpleStringProperty type;

    public Product(int id, String name, double price, int quantity, String createdAt, String type) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.createdAt = new SimpleStringProperty(createdAt);
        this.type = new SimpleStringProperty(type);
    }

    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public double getPrice() {
        return price.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public SimpleStringProperty createdAtProperty() {
        return createdAt;
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }
}
