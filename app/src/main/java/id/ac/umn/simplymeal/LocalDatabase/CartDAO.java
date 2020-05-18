package id.ac.umn.simplymeal.LocalDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CartDAO {
    @Query("SELECT * FROM Cart")
    Flowable<List<Cart>> getAllCartItems();

    @Query("SELECT * FROM Cart WHERE menuName=:cartItemId")
    Flowable<List<Cart>> getCartItemById(String cartItemId);

    @Query("SELECT COUNT(*) FROM Cart")
    int countCartItems();

    @Query("SELECT SUM(amount * price) FROM Cart")
    int sumPrice();

    @Query("DELETE FROM Cart")
    void emptyCart();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertToCart(Cart... cartItems);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCart(Cart... cartItems);

    @Delete
    void deleteCartItem(Cart cart);

    @Query("SELECT * FROM Cart WHERE portionName=:portionName AND menuName=:menuName")
    Single<Cart> getItemWithAllOptionsInCart(String portionName, String menuName);

    @Query("SELECT SUM(amount) FROM Cart")
    int amountCartItems();
}
