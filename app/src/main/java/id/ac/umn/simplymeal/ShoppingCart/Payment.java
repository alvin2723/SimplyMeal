package id.ac.umn.simplymeal.ShoppingCart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.ac.umn.simplymeal.LocalDatabase.Cart;

public class Payment {
    private String trans_number;
    private String userEmail;
    private String transDate;
    private List<Cart> cartMenu;
    private String totalPrice;
    private ArrayList<String> deliveryDay;
    private String ongkir;
    private String paymentMethod;

    public Payment(String trans_number,String userEmail, String transDate, List<Cart> cartMenu, String totalPrice, ArrayList<String> deliveryDay, String ongkir, String paymentMethod) {
        this.trans_number = trans_number;
        this.userEmail = userEmail;
        this.transDate = transDate;
        this.cartMenu = cartMenu;
        this.totalPrice = totalPrice;
        this.deliveryDay = deliveryDay;
        this.ongkir = ongkir;
        this.paymentMethod = paymentMethod;
    }

    public Payment() {
    }

    public String getTrans_number() {
        return trans_number;
    }

    public void setTrans_number(String trans_number) {
        this.trans_number = trans_number;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public List<Cart> getCartMenu() {
        return cartMenu;
    }

    public void setCartMenu(List<Cart> cartMenu) {
        this.cartMenu = cartMenu;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<String> getDeliveryDay() {
        return deliveryDay;
    }

    public void setDeliveryDay(ArrayList<String> deliveryDay) {
        this.deliveryDay = deliveryDay;
    }

    public String getOngkir() {
        return ongkir;
    }

    public void setOngkir(String ongkir) {
        this.ongkir = ongkir;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}