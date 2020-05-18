package id.ac.umn.simplymeal.CategoryView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Menu {
    private String menuName;
    //private Map<String, Object> imageMenu ;
    private String category;
    private String idMenu;
    private  Map<String, Object> idIngredient;
    private Map<String, Object> idPortion;
    private String idimage;
    private String idimageBackground;
    private double rating;

    public Menu(String menuName, String image, String imageBackground, String idMenu, String category, Map<String, Object> idIngredient, Map<String, Object> idPortion, double rating) {
        this.menuName = menuName;
        this.idimage = image;
        this.category = category;
        this.idMenu = idMenu;
        this.idIngredient = idIngredient;
        this.idPortion = idPortion;
        this.idimageBackground = imageBackground;
        this.rating = rating;
    }

    public  Menu(){

    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public  Map<String, Object> getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(Map<String, Object> idIngredient) {
        this.idIngredient = idIngredient;
    }

    public  Map<String, Object> getIdPortion() {
        return idPortion;
    }

    public void setIdPortion( Map<String, Object> idPortion) {
        this.idPortion = idPortion;
    }

    public String getidImage() {
        return idimage;
    }

    public void setidImage(String idimage) {
        this.idimage = idimage;
    }

    public String getidImageBackground() {
        return idimageBackground;
    }

    public void setidImageBackground(String idimageBackground) { this.idimageBackground = idimageBackground; }

    public double getRatingStar() {
        return rating;
    }

    public void setRatingStar(double rating) {
        this.rating = rating;
    }
}
