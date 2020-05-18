package id.ac.umn.simplymeal.CategoryView;

public class Portion {
    private String amountPortion;
    private String idPortion;
    private String nutrition;
    private String portionName;
    private Integer price;

    public Portion() {

    }

    public Portion(String amountPortion, String idPortion, String nutrition, String portionName, Integer price) {
        this.amountPortion = amountPortion;
        this.idPortion = idPortion;
        this.nutrition = nutrition;
        this.portionName = portionName;
        this.price = price;
    }

    public String getAmountPortion() {
        return amountPortion;
    }

    public void setAmountPortion(String amountPortion) {
        this.amountPortion = amountPortion;
    }

    public String getIdPortion() {
        return idPortion;
    }

    public void setIdPortion(String idPortion) {
        this.idPortion = idPortion;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public String getPortionName() {
        return portionName;
    }

    public void setPortionName(String portionName) {
        this.portionName = portionName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
