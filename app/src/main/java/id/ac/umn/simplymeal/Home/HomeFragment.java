package id.ac.umn.simplymeal.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import id.ac.umn.simplymeal.AccountFragment;
import id.ac.umn.simplymeal.CategoryView.Menu;
import id.ac.umn.simplymeal.History.HistoryActivity;
import id.ac.umn.simplymeal.LoginRegister.Preferences;
import id.ac.umn.simplymeal.R;
import id.ac.umn.simplymeal.SearchActivity;

public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }


    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private StorageReference mStorage;
    private ImageView catImageView;
    private View    catName;
    private RecyclerView catRecycler;
    private AdapterCategory adapter;
    private List<Category> catList;
    private  List<Menu> catMenu;
    private ViewPager catView;
    private TextView searchBar;
    private List<String> suggestList = new ArrayList<>();
    private Button location;
    private ImageButton history_icon;


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Pindah fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        view.findViewById(R.id.shimmerCategory).setVisibility(view.VISIBLE);
        view.findViewById(R.id.shimmerFood).setVisibility(view.VISIBLE);

        searchBar = view.findViewById(R.id.searchBar);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchScreen  = new Intent(view.getContext(), SearchActivity.class);
                startActivity(searchScreen);
            }
        });

        //history icon
        history_icon = view.findViewById(R.id.history_notif);
        history_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Preferences.getLoggedInStatus(v.getContext()) == true)
                {
                    Intent intent = new Intent(view.getContext(), HistoryActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(v.getContext(), "Please Login First!", Toast.LENGTH_LONG).show();
                }

            }
        });

        catView = view.findViewById(R.id.viewPagerHead);
        catRecycler = view.findViewById(R.id.recyclerCategory);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //view pager
        catView = view.findViewById(R.id.viewPagerHead);
        catMenu = new ArrayList<>();
        Query queryMenu = mDatabase.child("Menu").orderByChild("idMenu");
        queryMenu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Menu menues = dataSnapshot1.getValue(Menu.class);
                        catMenu.add(menues);
                    }

                    ViewPagerMenu viewPager = new ViewPagerMenu(catMenu, view.getContext());
                    catView.setAdapter(viewPager);
                    catView.setPadding(20,0,150,0);
                    viewPager.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //card view
        catRecycler.setHasFixedSize(true);
        catRecycler.setLayoutManager(new GridLayoutManager(view.getContext(), 2,GridLayoutManager.VERTICAL, false));
        catRecycler.setClipToPadding(false);
        catRecycler.setNestedScrollingEnabled(true);

        //get Data
         catList= new ArrayList<>();
         mStorage = FirebaseStorage.getInstance().getReference();

        Query query = mDatabase.child("Category").orderByChild("idCategory");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Category category = dataSnapshot1.getValue(Category.class);
                        catList.add(category);
                    }

                    adapter = new AdapterCategory(view.getContext(), catList);
                    adapter.notifyDataSetChanged();
                    catRecycler.setAdapter(adapter);
                    view.findViewById(R.id.shimmerCategory).setVisibility(view.INVISIBLE);
                    view.findViewById(R.id.shimmerFood).setVisibility(view.INVISIBLE);
                }
                else{
                    Toast.makeText(view.getContext(), "Data Not Found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(view.getContext(), "Database Error",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}