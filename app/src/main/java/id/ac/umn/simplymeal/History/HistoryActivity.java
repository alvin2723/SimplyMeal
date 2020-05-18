package id.ac.umn.simplymeal.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.simplymeal.CategoryView.AdapterCategoryTab;
import id.ac.umn.simplymeal.CategoryView.CategoryTab;
import id.ac.umn.simplymeal.CategoryView.CategoryTabActivity;
import id.ac.umn.simplymeal.LoginRegister.Preferences;
import id.ac.umn.simplymeal.R;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<History> HistoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recylcer view shop cart
        recyclerView = findViewById(R.id.recyclerview_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        //get data history
        HistoryList= new ArrayList<>();
        String usrEmail = Preferences.getLoggedInEmail(HistoryActivity.this);
        Query queryCat =  FirebaseDatabase.getInstance().getReference().child("Transactions").orderByChild("userEmail").equalTo(usrEmail);
        queryCat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        History history = dataSnapshot1.getValue(History.class);
                        HistoryList.add(history);
                    }

                    AdapterHistory adapterHistory = new AdapterHistory(HistoryActivity.this, HistoryList);
                    recyclerView.setAdapter(adapterHistory);
                    adapterHistory.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(HistoryActivity.this, "Data Not Found",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HistoryActivity.this, "Database Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
