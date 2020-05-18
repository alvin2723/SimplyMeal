package id.ac.umn.simplymeal.CategoryView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.ac.umn.simplymeal.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMenu extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private AdapterMenu adapter;
    private List<Menu> menuList;
    DatabaseReference mDatabase;
    private TextView catTitle;
    private TextView descCategory;

    ImageView imageCategory;
    String stringimageCategory;


    // TODO: Rename and change types of parameters


    public FragmentMenu() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentMenu newInstance(String param1, String param2) {
        FragmentMenu fragment = new FragmentMenu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_menu, container, false);

        if(getArguments() != null) {
            progressBar = view.findViewById(R.id.progressBar);
            progressBar.setVisibility(view.VISIBLE);

            // get id dari xml
            catTitle = view.findViewById(R.id.categoryTitle);
            descCategory = view.findViewById(R.id.descCategory);

            recyclerView = view.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2, GridLayoutManager.VERTICAL, false));

            //get Arguments
            String categoryName = getArguments().getString("categoryName");
            String categoryId = getArguments().getString("categoryId");

            String descCat = getArguments().getString("categoryDesc");

            //set Category Text
            catTitle.setText(categoryName);
            //set desc Category
            descCategory.setText(descCat);

            // panggil data firebase
            menuList = new ArrayList<Menu>();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Menu");

            //query child dari data database
            Query query = FirebaseDatabase.getInstance().getReference("Menu").orderByChild("idCategory/" + categoryId).equalTo(true);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                        String namamenu = String.valueOf(dataSnap.child("menuName").getValue());
                        String idMenu = String.valueOf(dataSnap.child("idMenu").getValue());
                        String menuKey = dataSnap.getKey();

                        Menu menus = dataSnap.getValue(Menu.class);
                        menuList.add(menus);
                    }
                    adapter = new AdapterMenu(view.getContext(), menuList);
                    progressBar.setVisibility(view.INVISIBLE);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("dataCatCancel", "Tidak berhasil");
                }
            });

            //get image category
            imageCategory = view.findViewById(R.id.imageCategory);
            stringimageCategory = getArguments().getString("categoryImage");
            Picasso.get().load(stringimageCategory).into(imageCategory, new Callback() {
                @Override
                public void onSuccess() {
                    Log.i("image berhasil", "image");
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(view.getContext(), "Could not get the image", Toast.LENGTH_SHORT).show();
                }
            });
            //get nama category
        }
        return view;

    }
}
