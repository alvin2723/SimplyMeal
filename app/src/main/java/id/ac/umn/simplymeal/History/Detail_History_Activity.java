package id.ac.umn.simplymeal.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.simplymeal.LoginRegister.Preferences;
import id.ac.umn.simplymeal.R;
import id.ac.umn.simplymeal.ShoppingCart.Payment;

public class Detail_History_Activity extends AppCompatActivity {
    private String trans_number, item_count, trans_date, payment_method, total;
    private List<Carts> listCarts = new ArrayList<>();
    private AdapterDetailHistory adapterDetailHistory;
    private RecyclerView cartView;
    TextView transaction_num, total_product, transaction_date, paymentMethod, address, total_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__history);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        trans_number = getIntent().getStringExtra("trans_num");
        transaction_num = findViewById(R.id.trans_num);
        transaction_num.setText(trans_number);
      //  RatingBar ratingBars = findViewById(R.id.rating_rating_bar);
//        if(trans_number == Preferences.getLoggedInTrans(Detail_History_Activity.this)){
//
//            Preferences.setLoggedInRate(Detail_History_Activity.this, "true");
//        }

        cartView = findViewById(R.id.item_products);
        cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cartView.setHasFixedSize(true);

        item_count = getIntent().getStringExtra("item_count");
        total_product = findViewById(R.id.count_item);
        total_product.setText(item_count);

        trans_date = getIntent().getStringExtra("date");
        transaction_date = findViewById(R.id.trans_date);
        transaction_date.setText(trans_date);

        payment_method = getIntent().getStringExtra("payment");
        paymentMethod = findViewById(R.id.PaymentMethod);
        paymentMethod.setText(payment_method);

        total = getIntent().getStringExtra("total");
        total_price = findViewById(R.id.total);
        total_price.setText(total);

        address = findViewById(R.id.AlamatLine);
        address.setText(Preferences.getLoggedInAddress(getApplicationContext()));

        Query query = FirebaseDatabase.getInstance().getReference().child("Transactions").child(trans_number).child("cartMenu");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    Carts cartData = dataSnapshot1.getValue(Carts.class);
                    listCarts.add(cartData);
                }
                    adapterDetailHistory = new AdapterDetailHistory(Detail_History_Activity.this, listCarts);
                    cartView.setAdapter(adapterDetailHistory);
                    adapterDetailHistory.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
