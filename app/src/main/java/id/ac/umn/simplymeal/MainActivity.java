package id.ac.umn.simplymeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import id.ac.umn.simplymeal.Home.HomeFragment;
import id.ac.umn.simplymeal.LocalDatabase.CartDataSource;
import id.ac.umn.simplymeal.LocalDatabase.CartDatabase;
import id.ac.umn.simplymeal.LocalDatabase.CartRepository;
import id.ac.umn.simplymeal.LoginRegister.Preferences;
import id.ac.umn.simplymeal.ShoppingCart.ShopCartFragment;
import id.ac.umn.simplymeal.Utils.Commons;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView navigation;
    private long backPressed;
    private Toast text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDB();
        loadFragment(new HomeFragment());

        if(getIntent().hasExtra("keyAccount")){
                loadFragment(new AccountFragment());
        }
        else if(getIntent().hasExtra("toShopcart") && Preferences.getLoggedInStatus(getApplicationContext()) == true){
            loadFragment(new ShopCartFragment());
        }
        getlocationnow();
    }
    private void initDB(){
        Commons.cartDatabase = CartDatabase.getInstance(this);
        Commons.cartRepository =
                CartRepository.getInstance(CartDataSource.getInstance(Commons.cartDatabase.cartDAO()));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment=null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_account:
                    fragment = new AccountFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_carts:
                    if(Preferences.getLoggedInStatus(MainActivity.this) == false)
                    {
                        Toast.makeText(MainActivity.this,"You need to Logged in First!", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    else{
                        fragment = new ShopCartFragment();
                        loadFragment(fragment);
                        return true;
                    }

            }
            return false;

        }
    };

    private void getlocationnow(){
        final Context context = getApplicationContext();
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double longitude = location.getLongitude();
                            double latitude = location.getLatitude();

                            Geocoder geocoder;
                            final List<Address> addresses;
                            geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                Preferences.setLoggedInAddress(context,addresses.get(0).getAddressLine(0));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {


        if(backPressed + 1000 > System.currentTimeMillis()){
            text.cancel();
            super.onBackPressed();
            this.finish();
            return;

        }
        else{
            text = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            text.show();
        }
        backPressed = System.currentTimeMillis();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
