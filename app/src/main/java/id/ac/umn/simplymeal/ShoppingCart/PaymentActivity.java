package id.ac.umn.simplymeal.ShoppingCart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import id.ac.umn.simplymeal.History.Carts;
import id.ac.umn.simplymeal.LocalDatabase.Cart;
import id.ac.umn.simplymeal.LocalDatabase.CartDataSource;
import id.ac.umn.simplymeal.LocalDatabase.CartDatabase;
import id.ac.umn.simplymeal.LocalDatabase.CartRepository;
import id.ac.umn.simplymeal.LoginRegister.Preferences;
import id.ac.umn.simplymeal.MainActivity;
import id.ac.umn.simplymeal.R;
import id.ac.umn.simplymeal.Utils.Commons;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PaymentActivity extends AppCompatActivity {
    String mon, tue, wed, thu, fri, sat, sun;
    Button confirm;
    ExpandableListView expandableListView;
    PaymentMethodAdapter expandableListAdapter;
    List expandableListTitle;
    HashMap expandableListDetail;
    String totalPayment;
    private CompositeDisposable compositeDisposable;
    private DatabaseReference databaseReference;
    ArrayList<String> deliveryday;
    List<Carts> cartList;
    private long id, ides;
    private List<Payment> paymentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // HEADER
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //delivery day
        deliveryday = new ArrayList<String>();


        //expand payment
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = PaymentMethodPump.getData();
        expandableListTitle = new ArrayList(expandableListDetail.keySet());
        expandableListAdapter = new PaymentMethodAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        initDB();

        //confirm
        confirm = findViewById(R.id.Confirm);
        loadCartItems();

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
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Transactions");
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String transDate = df.format(c);

        totalPayment = getIntent().getStringExtra("totalPayment");

        final String method[] = {"BCA", "BRI", "Mandiri"};
        Random random = new Random();
        final int paymentMethod = random.nextInt(method.length - 0) + 0;
        Integer data = 1;
        String usrEmail = Preferences.getLoggedInEmail(PaymentActivity.this);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //masukin order ke database
                mon = getIntent().getStringExtra("monday");
                tue = getIntent().getStringExtra("tuesday");
                wed = getIntent().getStringExtra("wednesday");
                thu = getIntent().getStringExtra("thursday");
                fri = getIntent().getStringExtra("friday");
                sat = getIntent().getStringExtra("saturday");
                sun = getIntent().getStringExtra("sunday");

                deliveryday.add(mon);
                deliveryday.add(tue);
                deliveryday.add(wed);
                deliveryday.add(thu);
                deliveryday.add(fri);
                deliveryday.add(sat);
                deliveryday.add(sun);

                Payment payment = new Payment();
                payment.setCartMenu(carts);
                payment.setUserEmail(usrEmail);
                payment.setTransDate(transDate);
                payment.setTotalPrice(totalPayment);
                payment.setOngkir("10000");
                payment.setDeliveryDay(deliveryday);
                payment.setPaymentMethod(method[paymentMethod]);
                payment.setTrans_number(String.valueOf(System.currentTimeMillis()));

                if(!carts.isEmpty()){
                    databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(payment);
                }

                Commons.cartRepository.emptyCart();
                Intent intent2 = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent2);
            }
        });



    }

    private void initDB() {
        Commons.cartDatabase = CartDatabase.getInstance(this);
        Commons.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Commons.cartDatabase.cartDAO()));
    }
}