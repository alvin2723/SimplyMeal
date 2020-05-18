package id.ac.umn.simplymeal.LocalDatabase;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ICartDataSource {
    Flowable<List<Cart>> getAllCartItems();
    Flowable<List<Cart>> getCartItemById(String cartItemId);

    int countCartItems();

    int sumPrice();

    void emptyCart();
    void insertToCart(Cart... cartItems);
    void updateCart(Cart... cartItems);
    void deleteCartItem(Cart cart);
    Single<Cart> getItemWithAllOptionsInCart(String portionName, String menuName);
    int amountCartItems();
}
