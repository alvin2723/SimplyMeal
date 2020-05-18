package id.ac.umn.simplymeal.ShoppingCart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import id.ac.umn.simplymeal.LoginRegister.Preferences;
import id.ac.umn.simplymeal.R;
import id.ac.umn.simplymeal.Utils.Commons;

public class CheckOut extends AppCompatActivity {
    TextView address;
    TextView totalPayment, deliveryfee;
    Button payment;
    CheckBox mon, tue, wed, thu, fri, sat, sun;
    String monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    int feedelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        // HEADER
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Detail Pembelian");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Address
        address = findViewById(R.id.AlamatLine);
        address.setText(Preferences.getLoggedInAddress(getApplicationContext()));

        //delivery fee
        deliveryfee = findViewById(R.id.deliveryfee);
        deliveryfee.setText("Rp10000");

        //Total Pembayaran
        totalPayment = findViewById(R.id.totalPayment);
        totalPayment.setText(getIntent().getStringExtra("total"));

        feedelivery = 10000;
        totalPayment.setText(String.valueOf(Commons.cartRepository.sumPrice()+feedelivery));

        //Check box
        mon = (CheckBox) findViewById(R.id.Mon);
        tue = (CheckBox) findViewById(R.id.Tue);
        wed = (CheckBox) findViewById(R.id.Wed);
        thu = (CheckBox) findViewById(R.id.Thu);
        fri = (CheckBox) findViewById(R.id.Fri);
        sat = (CheckBox) findViewById(R.id.Sat);
        sun = (CheckBox) findViewById(R.id.Sun);

        //intent payment method
        payment = findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOut.this, PaymentActivity.class);
                if(mon.isChecked()) {
                    monday = "Monday";
                    intent.putExtra("monday", monday);
                }if(tue.isChecked()){
                    tuesday = "Tuesday";
                    intent.putExtra("tuesday", tuesday);
                }if(wed.isChecked()){
                    wednesday = "Wednesday";
                    intent.putExtra("wednesday", wednesday);
                }if(thu.isChecked()){
                    thursday = "Thursday";
                    intent.putExtra("thursday", thursday);
                }if(fri.isChecked()){
                    friday = "Friday";
                    intent.putExtra("friday", friday);
                }if(sat.isChecked()){
                    saturday = "Saturday";
                    intent.putExtra("saturday", saturday);
                }if(sun.isChecked()) {
                    sunday = "Sunday";
                    intent.putExtra("sunday", sunday);
                }
                intent.putExtra("totalPayment", totalPayment.getText().toString());
                startActivity(intent);
            }
        });
    }
}
