package id.ac.umn.simplymeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.ac.umn.simplymeal.CategoryView.AdapterMenu;
import id.ac.umn.simplymeal.CategoryView.Menu;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    List<Menu> suggestList;
    AdapterMenu searchAdapter;
    private Button btnSearch;
    private CardView cardSearch;
    private  RecyclerView  menusView;
    private  SearchView searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


         mDatabase = FirebaseDatabase.getInstance().getReference("Menu");
        suggestList = new ArrayList<Menu>();

        final Context context = this;
         searchBar = findViewById(R.id.search_bar);
        menusView = findViewById(R.id.recyclerSearch);
        menusView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2, GridLayoutManager.VERTICAL, false));

        cardSearch = findViewById(R.id.cardSearch);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Query queries = mDatabase.orderByChild("menuName").startAt(query.toLowerCase()).endAt(query.toLowerCase()+"\uf8ff");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    Menu item = dataSnap.getValue(Menu.class);
                    Log.i("search",dataSnap.getKey());
                    suggestList.add(item);
                }
                searchAdapter = new AdapterMenu(SearchActivity.this, suggestList);
                menusView.setAdapter(searchAdapter);
                menusView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(searchBar!= null){
            searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return false;
                }
            });
        }

    }
    private void search(String str){
        ArrayList<Menu> myList = new ArrayList<>();
            for(Menu searchMenu : suggestList){
                if(searchMenu.getMenuName().toLowerCase().contains(str.toLowerCase())){

                    myList.add(searchMenu);
                }
            }
        searchAdapter = new AdapterMenu(SearchActivity.this, myList);
        menusView.setAdapter(searchAdapter);
        if(str != null){
            menusView.setVisibility(View.VISIBLE);
        }
        else{
            menusView.setVisibility(View.INVISIBLE);
        }

    }



}
