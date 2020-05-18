package id.ac.umn.simplymeal.History;

public class Carts {
    private String idMenus;
    private String menuName;
    private String imageMenu;
    private Long amount;
    private Long price;
    private String portionName;
    private float rating;

    public Carts(String idMenus, String menuName, String imageMenu, Long amount, Long price, String portionName, float rating) {
        this.idMenus = idMenus;
        this.menuName = menuName;
        this.imageMenu = imageMenu;
        this.amount = amount;
        this.price = price;
        this.portionName = portionName;

        this.rating = rating;
    }

    public Carts() {
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getPortionName() {
        return portionName;
    }

    public void setPortionName(String portionName) {
        this.portionName = portionName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getImageMenu() {
        return imageMenu;
    }

    public void setImageMenu(String imageMenu) {
        this.imageMenu = imageMenu;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getIdMenus() {
        return idMenus;
    }

    public void setIdMenus(String idMenu) {
        this.idMenus = idMenu;
    }

    public float getRatingStar() {
        return rating;
    }

    public void setRatingStar(float rating) {
        this.rating = rating;
    }
}
