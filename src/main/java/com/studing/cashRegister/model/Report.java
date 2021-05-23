package com.studing.cashRegister.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Model class of report.
 * @author tHolubets
 */
public class Report {
    private String name;

    private int checkQuantity;
    private double totalAmount;
    private double cardAmount;
    private double cashAmount;
    private double taxAmount;

    private int returnedCheckQuantity;
    private double returnedTotalAmount;
    private double returnedCardAmount;
    private double returnedCashAmount;
    private double returnedTaxAmount;

    private double cashReplenishment;
    private double cashWithdrawal;
    private LocalTime time;

    /**
     * Constructor of report class with small list of parameters
     * @param name name of report
     * @param checkQuantity quantity of checks for report, should be non-negative
     * @param totalAmount total amount of checks for report, should be non-negative
     */
    public Report(String name, int checkQuantity, double totalAmount) {
        this.name = name;
        this.checkQuantity = checkQuantity;
        time = LocalTime.now();
        returnedCheckQuantity = (int) (Math.random() * (checkQuantity));
        if(returnedCheckQuantity>0){
            returnedTotalAmount = Math.random() * totalAmount;
            returnedCardAmount = returnedTotalAmount * 0.4;
            returnedCashAmount = returnedTotalAmount - returnedCardAmount;
            returnedTaxAmount = returnedTotalAmount * 0.2;
        }
        this.totalAmount = totalAmount - returnedTotalAmount;
        cardAmount = this.totalAmount*0.45;
        cashAmount = this.totalAmount - cardAmount;
        taxAmount = this.totalAmount * 0.2;
        cashReplenishment = Math.random() * totalAmount*0.2;
        cashWithdrawal = Math.random() * totalAmount*0.5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getTime() {
        return time.truncatedTo(ChronoUnit.SECONDS);
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public int getCheckQuantity() {
        return checkQuantity;
    }

    public void setCheckQuantity(int checkQuantity) {
        this.checkQuantity = checkQuantity;
    }

    public double getTotalAmount() {
        return round(totalAmount, 2);
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getCardAmount() {
        return round(cardAmount, 2);
    }

    public void setCardAmount(double cardAmount) {
        this.cardAmount = cardAmount;
    }

    public double getCashAmount() {
        return round(cashAmount, 2);
    }

    public void setCashAmount(double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public double getTaxAmount() {
        return round(taxAmount, 2);
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public int getReturnedCheckQuantity() {
        return returnedCheckQuantity;
    }

    public void setReturnedCheckQuantity(int returnedCheckQuantity) {
        this.returnedCheckQuantity = returnedCheckQuantity;
    }

    public double getReturnedTotalAmount() {
        return round(returnedTotalAmount, 2);
    }

    public void setReturnedTotalAmount(double returnedTotalAmount) {
        this.returnedTotalAmount = returnedTotalAmount;
    }

    public double getReturnedCardAmount() {
        return round(returnedCardAmount, 2);
    }

    public void setReturnedCardAmount(double returnedCardAmount) {
        this.returnedCardAmount = returnedCardAmount;
    }

    public double getReturnedCashAmount() {
        return round(returnedCashAmount, 2);
    }

    public void setReturnedCashAmount(double returnedCashAmount) {
        this.returnedCashAmount = returnedCashAmount;
    }

    public double getReturnedTaxAmount() {
        return round(returnedTaxAmount, 2);
    }

    public void setReturnedTaxAmount(double returnedTaxAmount) {
        this.returnedTaxAmount = returnedTaxAmount;
    }

    public double getCashReplenishment() {
        return round(cashReplenishment, 2);
    }

    public void setCashReplenishment(double cashReplenishment) {
        this.cashReplenishment = cashReplenishment;
    }

    public double getCashWithdrawal() {
        return round(cashWithdrawal, 2);
    }

    public void setCashWithdrawal(double cashWithdrawal) {
        this.cashWithdrawal = cashWithdrawal;
    }

    @Override
    public String toString() {
        return "Report{" +
                "checkQuantity=" + checkQuantity +
                ", totalAmount=" + totalAmount +
                ", cardAmount=" + cardAmount +
                ", cashAmount=" + cashAmount +
                ", taxAmount=" + taxAmount +
                ", returnedCheckQuantity=" + returnedCheckQuantity +
                ", returnedTotalAmount=" + returnedTotalAmount +
                ", returnedCardAmount=" + returnedCardAmount +
                ", returnedCashAmount=" + returnedCashAmount +
                ", returnedTaxAmount=" + returnedTaxAmount +
                ", cashReplenishment=" + cashReplenishment +
                ", cashWithdrawal=" + cashWithdrawal +
                ", time=" + time +
                '}';
    }
}
