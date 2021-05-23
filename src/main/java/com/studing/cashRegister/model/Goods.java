package com.studing.cashRegister.model;

/**
 * Model class of goods.
 * @author tHolubets
 */
public class Goods {
    private long id;
    private String name;
    private String description;
    private double price;
    private int quantity;

    /**
     * Constructor of goods class with all parameters
     * @param id goods identifier
     * @param name goods name
     * @param description goods description
     * @param price goods price, should be non-negative
     * @param quantity goods available quantity, should be non-negative
     */
    public Goods(long id, String name, String description, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
