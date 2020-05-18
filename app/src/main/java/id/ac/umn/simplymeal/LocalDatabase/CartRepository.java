package id.ac.umn.simplymeal.LocalDatabase;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class CartRepository implements ICartDataSource {

    private ICartDataSource iCartDataSource;

    public CartRepository(ICartDataSource iCartDataSource) {
        this.iCartDataSource = iCartDataSource;
    }

    private static CartRepository instance;
    public static CartRepository getInstance(ICartDataSource iCartDataSource){
        if(instance == null)
            instance = new CartRepository(iCartDataSource);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getAllCartItems() {
        return iCartDataSource.getAllCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(String cartItemId) {
        return iCartDataSource.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItems() {
        return iCartDataSource.countCartItems();
    }

    @Override
    public int sumPrice() {
        return iCartDataSource.sumPrice();
    }

    @Override
    public void emptyCart() {
        iCartDataSource.emptyCart();
    }

    @Override
    public void insertToCart(Cart... cartItems) {
        iCartDataSource.insertToCart(cartItems);
    }

    @Override
    public void updateCart(Cart... cartItems) {
        iCartDataSource.updateCart(cartItems);
    }

    @Override
    public void deleteCartItem(Cart cart) {
        iCartDataSource.deleteCartItem(cart);
    }
    @Override
    public Single<Cart> getItemWithAllOptionsInCart(String portionName, String menuName){
        return iCartDataSource.getItemWithAllOptionsInCart(portionName, menuName);
    }
    @Override
    public int amountCartItems() {
        return iCartDataSource.amountCartItems();
    }
}
