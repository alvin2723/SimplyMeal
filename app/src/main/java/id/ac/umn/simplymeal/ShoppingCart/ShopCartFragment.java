package id.ac.umn.simplymeal.ShoppingCart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.BreakIterator;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import id.ac.umn.simplymeal.AboutUs;
import id.ac.umn.simplymeal.LocalDatabase.Cart;
import id.ac.umn.simplymeal.R;
import id.ac.umn.simplymeal.LoginRegister.Preferences;
import id.ac.umn.simplymeal.Utils.Commons;
import id.ac.umn.simplymeal.Utils.deleteSwiperHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ShopCartFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    private AdapterCardViewCart adapter;
    private DatabaseReference databaseReference;
    private Button btnPlus, btnMinus;
    private RecyclerView recyclerView;
    private CompositeDisposable compositeDisposable;
    private List<Cart> data;

    Button checkout;
    public static TextView total;

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_shop_cart, container, false);

        //header
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Shopping Cart");
        toolbar.inflateMenu(R.menu.clear_shopcart);
        toolbar.setOnMenuItemClickListener(this);

        //recylcer view shop cart
        recyclerView = view.findViewById(R.id.recyclerview1);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        deleteSwiperHelper delSwipeHelper = new deleteSwiperHelper(getContext(), recyclerView,
                200) {
            @Override
            public void instanstiateDeleteButton(RecyclerView.ViewHolder viewHolder,
                                                 List<deleteButton> buf) {
                buf.add(new deleteButton(getContext(), "Delete", 30,0,
                        Color.parseColor("#FF3C30"), pos -> {
                    Cart cart = adapter.getItemAtPosition(pos);
                    Commons.cartRepository.deleteCartItem(cart);
                    sumAllCart();
                    Toast.makeText(getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
                }));
            }
        };
        loadCartItems();

        //set total harga
        total = view.findViewById(R.id.total);
        sumAllCart();

        //button checkout
        checkout = view.findViewById(R.id.checkout);



        return view;
    }

    private void loadCartItems() {
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                Commons.cartRepository.getAllCartItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> carts) throws Exception {
                                displayCartItem(carts);
                            }
                        })
        );
    }

    private void displayCartItem(List<Cart> carts) {
        adapter = new AdapterCardViewCart(getView().getContext(), carts);
        recyclerView.setAdapter(adapter);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carts.isEmpty()){
                    Toast.makeText(v.getContext(),"Please pick Your Menu First",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(v.getContext(), CheckOut.class);
                    intent.putExtra("total", total.getText().toString());
                    startActivity(intent);
                }

            }
        });
    }

    private void sumAllCart(){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        total.setText(String.format(" %s", formatRupiah.format(
                Commons.cartRepository.sumPrice())));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearCart:
                Commons.cartRepository.emptyCart();
                sumAllCart();
                Toast.makeText(getContext(), "All items deleted", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }
}
