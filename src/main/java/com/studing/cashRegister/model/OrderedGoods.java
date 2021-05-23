package com.studing.cashRegister.model;

/**
 * Model class of ordered goods.
 * @author tHolubets
 */
public class OrderedGoods {
    private long id;
    private long goodsId;
    private String name;
    private double price;
    private int quantity;

    /**
     * Constructor of ordered goods class with all parameters
     * @param id ordered goods record id
     * @param goodsId goods id
     * @param name goods name
     * @param price ordered goods price, should be non-negative
     * @param quantity ordered goods quantity, should be non-negative
     */
    public OrderedGoods(long id, long goodsId, String name, double price, int quantity) {
        this.id = id;
        this.goodsId = goodsId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Constructor of ordered goods class with reduced list of parameters. Other methods of created object should be used with caution when applying
     * @param id ordered goods id
     * @param price ordered goods price, should be non-negative
     * @param quantity ordered goods quantity, should be non-negative
     */
    public OrderedGoods(long id, double price, int quantity) {
        this.id = id;
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

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "OrderedGoods{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    public String forShow() {
        return "<b>" + name + "</b> (" + price + "₴) - quantity: " + quantity;
    }

    public String forShowEdit() {
        return "<b>" + name + "</b> (" + price + "₴) - quantity: ";
    }
}
