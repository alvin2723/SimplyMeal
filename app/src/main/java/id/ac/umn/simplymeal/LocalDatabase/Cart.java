package id.ac.umn.simplymeal.LocalDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Cart")
public class Cart implements Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "menuName")
    private String menuName;

    @ColumnInfo(name = "imageMenu")
    private String imageMenu;

    @ColumnInfo(name = "amount")
    private int amount;

    @ColumnInfo(name = "price")
    private int price;

    @ColumnInfo(name = "portionName")
    private String portionName;



    public Cart() {
    }

    public String getPortionName() {
        return portionName;
    }

    public void setPortionName(String portionName) {
        this.portionName = portionName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageMenu() {
        return imageMenu;
    }

    public void setImageMenu(String imageMenu) {
        this.imageMenu = imageMenu;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this){
            return true;
        }
        if(!(obj instanceof Cart)){
            return false;
        }
        Cart cartItem = (Cart)obj;
        return cartItem.getMenuName().equals(this.menuName) &&
                cartItem.getPortionName().equals(this.portionName);
    }
}
