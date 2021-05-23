package com.studing.cashRegister.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Model class of order.
 * @author tHolubets
 */
public class Order implements Serializable {
    private long id;
    private LocalDateTime dateTime;
    private long cashierId;
    private String status;
    private List<OrderedGoods> goodsList;

    /**
     * Constructor of order class with all parameters
     * @param id order id
     * @param dateTime time and date of order creation
     * @param cashierId id of cashier who has created the order
     * @param status could be 'open', 'closed', 'registered'
     * @param goodsList list of ordered goods
     */
    public Order(long id, LocalDateTime dateTime, long cashierId, String status, List<OrderedGoods> goodsList) {
        this.id = id;
        this.dateTime = dateTime;
        this.cashierId = cashierId;
        this.status = status;
        this.goodsList = goodsList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime.truncatedTo(ChronoUnit.SECONDS);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public long getCashierId() {
        return cashierId;
    }

    public void setCashierId(long cashierId) {
        this.cashierId = cashierId;
    }

    public List<OrderedGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderedGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public String getTotalAmount(){
        double result = goodsList.stream()
                    .map(goods -> goods.getPrice()*goods.getQuantity())
                    .reduce(0.0, (subtotal, element) -> subtotal+element);
        return round(result, 2) + " ₴";
    }

    public double getTotalAmountDouble(){
        double result = goodsList.stream()
                .map(goods -> goods.getPrice()*goods.getQuantity())
                .reduce(0.0, (subtotal, element) -> subtotal+element);
        return round(result, 2);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", cashierId=" + cashierId +
                ", status='" + status + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }
}
