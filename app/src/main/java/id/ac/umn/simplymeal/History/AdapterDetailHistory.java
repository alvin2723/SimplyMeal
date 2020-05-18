package id.ac.umn.simplymeal.History;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import id.ac.umn.simplymeal.LocalDatabase.Cart;
import id.ac.umn.simplymeal.LoginRegister.Preferences;
import id.ac.umn.simplymeal.R;

public class AdapterDetailHistory extends RecyclerView.Adapter<AdapterDetailHistory.ViewHolder> {
    private Context mContext;
    private LayoutInflater layoutinflater;
    private List<Carts> data;
    private  String trans_numbs;
    private double hasil;
    private int count = 0;
    public AdapterDetailHistory(Context mContext, List<Carts> data){
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutinflater.from(mContext).inflate(R.layout.item_product_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Carts cartsData = data.get(position);
        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.menu_name.setText(data.get(position).getMenuName());
        holder.qty.setText(data.get(position).getAmount().toString());
        holder.portion_name.setText(data.get(position).getPortionName());
        holder.price.setText(String.format(" %s", formatRupiah.format(data.get(position).getPrice()* data.get(position).getAmount())));
        Picasso.get().load(data.get(position).getImageMenu()).into(holder.image);
        holder.thanks.setVisibility(View.INVISIBLE);
        holder.ratingBar.setVisibility(View.VISIBLE);
        holder.rat_button.setVisibility(View.VISIBLE);
        trans_numbs = Preferences.getLoggedInTrans(mContext);



        Query query = FirebaseDatabase.getInstance().getReference().child("Transactions").orderByChild("trans_number").equalTo(trans_numbs);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    Long stat = dataSnap.child("status").getValue(Long.class);
                    if(stat == null){
                        holder.thanks.setVisibility(View.INVISIBLE);
                        holder.ratingBar.setVisibility(View.VISIBLE);
                        holder.rat_button.setVisibility(View.VISIBLE);
                    }
                    else if(stat == 1){
                        holder.ratingBar.setVisibility(View.INVISIBLE);
                        holder.thanks.setVisibility(View.VISIBLE);
                        holder.rat_button.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.rat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = holder.ratingBar.getRating();

                DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
                Query query = FirebaseDatabase.getInstance().getReference("Menu").orderByChild("menuName").equalTo(data.get(position).getMenuName());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot datasnap : dataSnapshot.getChildren()) {
                            double nilaiRating = datasnap.child("ratingStar").getValue(Double.class);


                            Preferences.setLoggedInIdMenu(mContext, datasnap.getKey());

                            if( nilaiRating  >= 3.0) {
                                firebase.child("Menu").child(datasnap.getKey()).child("idCategory").child("C04").setValue(true);
                            }
                            Intent intent = new Intent(mContext, HistoryActivity.class);
                            mContext.startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                firebase.child("Menu").child(Preferences.getLoggedInIdMenu(mContext)).child("ratingStar").setValue(Double.valueOf(rating));
                firebase.child("Transactions").child(trans_numbs).child("status").setValue(1);
            }
        });



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView menu_name;
        TextView qty;
        TextView portion_name;
        TextView price;
        ImageView image;
        RatingBar ratingBar;
        TextView thanks;
        Button rat_button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            menu_name = itemView.findViewById(R.id.menu_name);
            qty = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);
            portion_name = itemView.findViewById(R.id.portion);
            image = itemView.findViewById(R.id.imageMenu);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating_rating_bar);
            thanks = itemView.findViewById(R.id.thanks);
            rat_button = itemView.findViewById(R.id.rat_button);
        }
    }
}

