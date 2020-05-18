package id.ac.umn.simplymeal.LocalDatabase;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class CartDataSource implements ICartDataSource{

    private CartDAO cartDAO;
    private static CartDataSource instance;

    public CartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    public static CartDataSource getInstance(CartDAO cartDAO) {
        if(instance == null)
            instance = new CartDataSource(cartDAO);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getAllCartItems() {
        return cartDAO.getAllCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(String cartItemId) {
        return cartDAO.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItems() {
        return cartDAO.countCartItems();
    }

    @Override
    public int sumPrice() {
        return cartDAO.sumPrice();
    }

    @Override
    public void emptyCart() {
        cartDAO.emptyCart();
    }

    @Override
    public void insertToCart(Cart... cartItems) {
        cartDAO.insertToCart(cartItems);
    }

    @Override
    public void updateCart(Cart... cartItems) {
        cartDAO.updateCart(cartItems);
    }

    @Override
    public void deleteCartItem(Cart cart) {
        cartDAO.deleteCartItem(cart);
    }
    @Override
    public Single<Cart> getItemWithAllOptionsInCart(String portionName, String menuName){
        return cartDAO.getItemWithAllOptionsInCart(portionName, menuName);
    }
    @Override
    public int amountCartItems() {
        return cartDAO.amountCartItems();
    }
}
