package id.ac.umn.simplymeal.CategoryView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import id.ac.umn.simplymeal.AccountFragment;
import id.ac.umn.simplymeal.Location.UserLocation;
import id.ac.umn.simplymeal.LoginRegister.Preferences;
import id.ac.umn.simplymeal.MainActivity;
import id.ac.umn.simplymeal.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryTabActivity extends AppCompatActivity {
    BottomNavigationView navigation;

    private List<CategoryTab> catTabList;
    private TabLayout tabCat;
    private ViewPager viewPagerCat;
    private Context context;
    private Places address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_tab);


        final Integer position = getIntent().getIntExtra("position",0);
        // HEADER
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //get Data untuk tab Category
        catTabList= new ArrayList<>();
        tabCat = findViewById(R.id.tabLayout);
        viewPagerCat = findViewById(R.id.viewCategories);
        Query queryCat =  FirebaseDatabase.getInstance().getReference().child("Category").orderByChild("idCategory");
        queryCat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        String menuKey = dataSnapshot1.getKey();
                        CategoryTab category = dataSnapshot1.getValue(CategoryTab.class);
                        catTabList.add(category);
                    }

                    AdapterCategoryTab adapterCat = new AdapterCategoryTab(getSupportFragmentManager(), catTabList);
                    viewPagerCat.setAdapter(adapterCat);
                    tabCat.setupWithViewPager(viewPagerCat);
                    viewPagerCat.setCurrentItem(position, true);
                    adapterCat.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(CategoryTabActivity.this, "Data Not Found",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CategoryTabActivity.this, "Database Error",Toast.LENGTH_SHORT).show();
            }
        });

        final Button textLocation = findViewById(R.id.textLocation);
        LinearLayout bottomShet = findViewById(R.id.bottom_sheet);
        final BottomSheetBehavior layout = BottomSheetBehavior.from(bottomShet);
        Button currentLoc = findViewById(R.id.currentLoc);
        Button mapLoc = findViewById(R.id.locationMap);
        layout.setPeekHeight(0);
        layout.setHideable(true);

        if(Preferences.getLoggedInStatus(getApplicationContext()) == true){

            textLocation.setText(Preferences.getLoggedInAddress(getApplicationContext()));
            textLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    layout.setPeekHeight(50);
                    layout.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });
        }else{
            textLocation.setText("Your Location");
        }

        currentLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(Preferences.getLoggedInStatus(v.getContext()) == true){
                        textLocation.setText(Preferences.getLoggedInAddress(v.getContext()));
                    }else{
                        Toast.makeText(CategoryTabActivity.this,"Please Login First",Toast.LENGTH_SHORT).show();
                        Intent loginScreen = new Intent(v.getContext(), MainActivity.class);
                        loginScreen.putExtra("keyAccount","key");
                        startActivity(loginScreen);
                    }

                    layout.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        mapLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserLocation.class);
                startActivity(intent);
                layout.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        // Create a new Places client instance.

         if(!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCYKKIMZBp1KoS5wfBKjCa449TyC4rOGdg");
             PlacesClient placesClient = Places.createClient(this);
        }
            // Initialize the AutocompleteSupportFragment.

            AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                    getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
             autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
            ((EditText)autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).setHint("Enter your Address");
            //set Text size
            ((EditText)autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).setTextSize(14);


            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                   Toast.makeText(CategoryTabActivity.this, "Your Location Has been Selected!",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Status status) {
                    Toast.makeText(CategoryTabActivity.this, "An error occurred: " + status, Toast.LENGTH_LONG).show();
                }

            });

    }

}
