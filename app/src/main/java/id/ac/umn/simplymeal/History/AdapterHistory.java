package id.ac.umn.simplymeal.History;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import id.ac.umn.simplymeal.LoginRegister.Preferences;
import id.ac.umn.simplymeal.R;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {
    private Context mContext;
    private LayoutInflater layoutinflater;
    private List<History> data;

    public AdapterHistory(Context mContext, List<History> data){
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutinflater.from(mContext).inflate(R.layout.content_trans_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final History historyData = data.get(position);
        holder.trans_num.setText(data.get(position).getTrans_number());
        holder.trans_date.setText(data.get(position).getTransDate());
        holder.total.setText(data.get(position).getTotalPrice());
        holder.count_item.setText(String.valueOf(data.get(position).getCartMenu().size()));
        holder.view_history_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Detail_History_Activity.class);
                Preferences.setLoggedInTrans(mContext,data.get(position).getTrans_number());
                intent.putExtra("trans_num", data.get(position).getTrans_number()).putExtra("item_count", String.valueOf(data.get(position).getCartMenu().size())).putExtra("payment", data.get(position).getPaymentMethod()).putExtra("total", data.get(position).getTotalPrice()).putExtra("date", data.get(position).getTransDate()).putExtra("trans_num",data.get(position).getTrans_number());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView trans_num;
        TextView trans_date;
        TextView total;
        TextView count_item;
        Button view_history_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trans_num = itemView.findViewById(R.id.trans_number);
            trans_date = itemView.findViewById(R.id.trans_date);
            total = itemView.findViewById(R.id.total);
            view_history_detail = itemView.findViewById(R.id.view_detail);
            count_item = itemView.findViewById(R.id.count_item);
        }
    }
}
