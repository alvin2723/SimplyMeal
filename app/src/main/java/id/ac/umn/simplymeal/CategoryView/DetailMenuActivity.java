package id.ac.umn.simplymeal.CategoryView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.simplymeal.AccountFragment;
import id.ac.umn.simplymeal.LocalDatabase.Cart;
import id.ac.umn.simplymeal.LocalDatabase.CartDataSource;
import id.ac.umn.simplymeal.LocalDatabase.CartDatabase;
import id.ac.umn.simplymeal.LocalDatabase.CartRepository;
import id.ac.umn.simplymeal.LoginRegister.Preferences;
import id.ac.umn.simplymeal.MainActivity;
import id.ac.umn.simplymeal.R;
import id.ac.umn.simplymeal.Utils.Commons;
import id.ac.umn.simplymeal.ShoppingCart.ShopCartFragment;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailMenuActivity extends AppCompatActivity {
    BottomNavigationView navigation;
    private TextView TitleMenu;
    private Integer price;
    ImageView imageBackground;
    String namaMenu;
    String stringmenuBackground;
    private ProgressBar progressBar;
    TextView ingredients;
    ViewPager pagernutrition;
    FloatingActionButton btnAddtoCart;
    CollapsingToolbarLayout CollapsingToolbarLayout;
    NotificationBadge badge;
    ElegantNumberButton amountNumber;
    Integer amount;
    public int setAmount;
    ImageView cart_icon;
    Integer counter = 0;
    String idMenus;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);

        // HEADER
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        //CollapsingToolbarLayout
        CollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        CollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        CollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        // FOOTER NAVIGATION
        navigation = findViewById(R.id.bottom_navigation);

        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("position", 0);

        // nama menu
        TitleMenu = findViewById(R.id.namaMenu);
        TitleMenu.setText(getIntent().getStringExtra("menuTitle"));
//        TitleMenu.setTextColor(Color.parseColor("#FFFFFF"));

        // image background
        imageBackground = findViewById(R.id.menuImage);
        // get string dari intent

        stringmenuBackground = getIntent().getStringExtra("menuBackground");
        //picasso untuk load image; load(datanya apa).into(di tempat apa)
        Picasso.get().load(stringmenuBackground).into(imageBackground, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(DetailMenuActivity.this, "Could not get the image", Toast.LENGTH_SHORT).show();
            }
        });

        //get xml ingredient
        ingredients = findViewById(R.id.ingredients);
        //array list ingredient
        final List<String> listnama = new ArrayList<String>();
        //get ingredient
        idMenus = getIntent().getStringExtra("idMenu");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Menu");
        Query query = FirebaseDatabase.getInstance().getReference("Menu").orderByChild("idMenu").equalTo(idMenus);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnap.child("idIngredient").getChildren()) {
                        String namaIngredient = String.valueOf(dataSnapshot1.child("NamaIngredient").getValue());
                        listnama.add(namaIngredient);
                    }
                }

                //taro di xml textview ingredient
                String output = "";
                for (int i = 0; i < listnama.size(); i++) {
                    output += listnama.get(i) + "\n";
                }
                ingredients.setText(output);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get xml nutrition
        pagernutrition = findViewById(R.id.pagernutrition);
        //array list nutrition
        final List<Portion> listnutrition = new ArrayList<Portion>();

        //define context
        final Context context = this;

        //get nutrition
        //get id menu dari -> String idMenu = getIntent().getStringExtra("idMenu"); (sudah ada di atas)
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Menu");
        Query query2 = FirebaseDatabase.getInstance().getReference("Menu").orderByChild("idMenu").equalTo(idMenus);
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnap.child("idPortion").getChildren()) {
                        String imagenutrition = String.valueOf(dataSnapshot1.child("nutrition").getValue());
                        Portion nutrition = dataSnapshot1.getValue(Portion.class);
                        listnutrition.add(nutrition);
                    }

                    AdapterViewPagerNutrition viewPagerNutrition = new AdapterViewPagerNutrition(listnutrition, context);
                    pagernutrition.setAdapter(viewPagerNutrition);
                    pagernutrition.setPadding(20,0,150,0);
                    viewPagerNutrition.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Query query1 = FirebaseDatabase.getInstance().getReference("Category").orderByChild("idMenu/"+idMenus).equalTo(true);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    String categoryTitle = String.valueOf(dataSnap.child("categoryName").getValue());
                    TextView titleCategory = findViewById(R.id.category);
                    titleCategory.setText(categoryTitle);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        initDB();

        //portion xml
        final RadioGroup portionGroup = findViewById(R.id.portionGroup);

        //price xml
        final TextView singlePortion = findViewById(R.id.singlePrice);
        final TextView familyPortion = findViewById(R.id.familyPrice);

        Query query3 = FirebaseDatabase.getInstance().getReference("Menu").child(idMenus).child("idPortion");
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    String price = String.valueOf(dataSnap.child("price").getValue());
                    if(String.valueOf(dataSnap.child("portionName").getValue()).contains("Single Portion")){
                        singlePortion.setText(price);
                    }
                    else if(String.valueOf(dataSnap.child("portionName").getValue()).contains("Family Portion")){
                        familyPortion.setText(price);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //amount
        amountNumber = (ElegantNumberButton) findViewById(R.id.amount);
        amountNumber.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amountNumber.getNumber() == null){
                    amount = 1;
                }else{
                    amount = Integer.parseInt(amountNumber.getNumber());
                }


            }
        });

        //add cart FAB
        btnAddtoCart = findViewById(R.id.addToCart);

        btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Preferences.getLoggedInStatus(context) == true) {
                    updateCartCount();
                    try {
                        //cek portion
                        final RadioGroup portionGroup = findViewById(R.id.portionGroup);
                        String finalPortionName = "";
                        switch (portionGroup.getCheckedRadioButtonId()) {
                            case R.id.Single:
                                finalPortionName = "Single Portion";
                                String idMenu = getIntent().getStringExtra("idMenu");
                                Query query2 = FirebaseDatabase.getInstance().getReference("Menu").child(idMenu).child("idPortion").orderByChild("portionName").equalTo(finalPortionName);
                                final String finalPortionName1 = finalPortionName;
                                query2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                                            price = ((Long) dataSnap.child("price").getValue()).intValue();

                                            final Cart cartItem = new Cart();
                                            if(amount == null){
                                                amount=1;
                                            }

                                            cartItem.setAmount(amount);
                                            cartItem.setMenuName(getIntent().getStringExtra("menuTitle"));
                                            cartItem.setPortionName(finalPortionName1);
                                            cartItem.setPrice(price);
                                            cartItem.setImageMenu(getIntent().getStringExtra("menuBackground"));

                                            //add db
//                                            cartItem.setUserName(Preferences.getLoggedInUser(context));
                                            Commons.cartRepository.getItemWithAllOptionsInCart(cartItem.getPortionName(),cartItem.getMenuName())
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new SingleObserver<Cart>() {
                                                        @Override
                                                        public void onSubscribe(Disposable d) {

                                                        }

                                                        @Override
                                                        public void onSuccess(Cart cart) {
                                                            if(cart.equals(cartItem)){

                                                                cart.setPrice(cartItem.getPrice());
                                                                cart.setImageMenu(cartItem.getImageMenu());
                                                                cart.setPortionName(cartItem.getPortionName());
                                                                cart.setAmount(cart.getAmount() + cartItem.getAmount());

                                                                Commons.cartRepository.updateCart(cart);
                                                                updateCartCount();
                                                                Log.i("a", new Gson().toJson(cart));
                                                                Toast.makeText(context, "sukses", Toast.LENGTH_SHORT).show();

                                                            }
                                                            else{
                                                                Commons.cartRepository.insertToCart(cartItem);
                                                                Log.i("a", new Gson().toJson(cartItem));
                                                                Toast.makeText(context, "sukses", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {
                                                            if(e.getMessage().contains("empty")){
                                                                Commons.cartRepository.insertToCart(cartItem);
                                                                updateCartCount();
                                                            }
                                                        }
                                                    });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                                break;
                            case R.id.Family:
                                finalPortionName = "Family Portion";
                                String idMenu2 = getIntent().getStringExtra("idMenu");
                                Query query3 = FirebaseDatabase.getInstance().getReference("Menu").child(idMenu2).child("idPortion").orderByChild("portionName").equalTo(finalPortionName);
                                final String finalPortionName2 = finalPortionName;
                                query3.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                                            price = ((Long) dataSnap.child("price").getValue()).intValue();

                                            //set amount
                                            final Cart cartItem2 = new Cart();
                                            if(amount == null){
                                                amount = 1;
                                            }

                                            cartItem2.setAmount(amount);
                                            cartItem2.setMenuName(getIntent().getStringExtra("menuTitle"));
                                            cartItem2.setPortionName(finalPortionName2);
                                            cartItem2.setPrice(price);
                                            cartItem2.setImageMenu(getIntent().getStringExtra("menuBackground"));


                                            //add db
//                                            cartItem2.setUserName(Preferences.getLoggedInUser(context));
                                            Commons.cartRepository.getItemWithAllOptionsInCart(cartItem2.getPortionName(),cartItem2.getMenuName())
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new SingleObserver<Cart>() {
                                                        @Override
                                                        public void onSubscribe(Disposable d) {

                                                        }

                                                        @Override
                                                        public void onSuccess(Cart cart) {
                                                            if(cart.equals(cartItem2)){

                                                                cart.setPrice(cartItem2.getPrice());
                                                                cart.setImageMenu(cartItem2.getImageMenu());
                                                                cart.setPortionName(cartItem2.getPortionName());
                                                                cart.setAmount(cart.getAmount() + cartItem2.getAmount());

                                                                Commons.cartRepository.updateCart(cart);
                                                                updateCartCount();
                                                                Log.i("a", new Gson().toJson(cart));
                                                                Toast.makeText(context, "sukses", Toast.LENGTH_SHORT).show();

                                                            }
                                                            else{
                                                                Commons.cartRepository.insertToCart(cartItem2);
                                                                Log.i("a", new Gson().toJson(cartItem2));
                                                                Toast.makeText(context, "sukses", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {
                                                            if(e.getMessage().contains("empty")){
                                                                Commons.cartRepository.insertToCart(cartItem2);
                                                                updateCartCount();
                                                            }
                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                                break;
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "No", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(DetailMenuActivity.this,"Please Login First",Toast.LENGTH_LONG).show();
                    Intent loginScreen = new Intent(DetailMenuActivity.this, MainActivity.class);
                    loginScreen.putExtra("keyAccount","key");
                    startActivity(loginScreen);
                }
            }
        });

    }

    private void initDB() {
        Commons.cartDatabase = CartDatabase.getInstance(this);
        Commons.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Commons.cartDatabase.cartDAO()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        View view = menu.findItem(R.id.navigation_cart).getActionView();
        badge = view.findViewById(R.id.badge);
        cart_icon = view.findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginScreen = new Intent(DetailMenuActivity.this, MainActivity.class);
                loginScreen.putExtra("toShopcart","shops");
                startActivity(loginScreen);

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.navigation_cart){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateCartCount() {
        if(badge == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(Commons.cartRepository.amountCartItems() == 0)
                    badge.setVisibility(View.INVISIBLE);
                else{
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(Commons.cartRepository.amountCartItems()));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }
}
