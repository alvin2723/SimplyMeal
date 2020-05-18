package id.ac.umn.simplymeal.ShoppingCart;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Measure;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import id.ac.umn.simplymeal.LocalDatabase.Cart;
import id.ac.umn.simplymeal.R;
import id.ac.umn.simplymeal.Utils.Commons;
import io.reactivex.disposables.CompositeDisposable;

public class AdapterCardViewCart extends RecyclerView.Adapter<AdapterCardViewCart.ViewHolder> {

    private Context mContext;
    private LayoutInflater layoutinflater;
    private List<Cart> data;
    private double total;
    private CompositeDisposable compositeDisposable;

    public AdapterCardViewCart(Context mContext, List<Cart> data){
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutinflater.from(mContext).inflate(R.layout.content_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        compositeDisposable = new CompositeDisposable();
        final Cart cartsData = data.get(position);
        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        Picasso.get().load(data.get(position).getImageMenu()).into(holder.imageMenu);

        holder.menuTitle.setText(data.get(position).getMenuName());
        holder.portion.setText(data.get(position).getPortionName());
        holder.price.setText(String.format(" %s", formatRupiah.format(
                data.get(position).getPrice() * data.get(position).getAmount())));
        holder.qty.setNumber(String.valueOf(data.get(position).getAmount()));
        holder.qty.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart = data.get(position);

                if(newValue == 0){
                    Commons.cartRepository.deleteCartItem(cart);
                }
                cart.setAmount(newValue);

                Commons.cartRepository.updateCart(cart);

                int totalPrice = 0;
                for (Cart carts : data) {
                    totalPrice+= (carts.getPrice() * carts.getAmount());
                }

                ShopCartFragment.total.setText(String.format(" %s", formatRupiah.format(totalPrice)));
                holder.price.setText(String.format(" %s", formatRupiah.format(
                        data.get(position).getPrice() * data.get(position).getAmount())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Cart getItemAtPosition(int pos) {
        return data.get(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView menuTitle, portion, price;
        ElegantNumberButton qty;
        CardView cardView;
        ImageView imageMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            menuTitle = itemView.findViewById(R.id.ingredients);
            portion = itemView.findViewById(R.id.portion);
            qty = itemView.findViewById(R.id.txt_amount);
            price = itemView.findViewById(R.id.price);
            cardView = itemView.findViewById(R.id.cardview);
            imageMenu = itemView.findViewById(R.id.imageMenu);

        }
    }
}