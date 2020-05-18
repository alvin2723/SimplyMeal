
package id.ac.umn.simplymeal.CategoryView;

import android.content.Context;
        import android.content.Intent;
        import android.media.Image;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.cardview.widget.CardView;
        import androidx.recyclerview.widget.RecyclerView;

        import com.squareup.picasso.Callback;
        import com.squareup.picasso.Picasso;
        import java.util.List;

        import id.ac.umn.simplymeal.R;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.ViewHolder> {

    private Context mContext;
    private LayoutInflater layoutinflater;
    private List<Menu> data;


    public AdapterMenu(Context mContext, List<Menu> data){
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutinflater.from(mContext).inflate(R.layout.content_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Menu menuData = data.get(position);
        holder.menuTitle.setText(menuData.getMenuName());



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailMenuActivity.class);
                intent.putExtra("menuTitle",menuData.getMenuName()).putExtra("menuBackground",menuData.getidImageBackground()).putExtra("idMenu", menuData.getIdMenu());

                v.getContext().startActivity(intent);
            }
        });

        Picasso.get().load(menuData.getidImage()).into(holder.image, new Callback(){
            @Override
            public void onSuccess() {
                Log.i("image berhasil","image");
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(mContext,"Could not get the image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView menuTitle;
        ImageView image;
        CardView cardView;
        TextView catTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            menuTitle = itemView.findViewById(R.id.menuTitle);
            cardView = itemView.findViewById(R.id.cardview);
            image = itemView.findViewById(R.id.menuImage);
            catTitle = itemView.findViewById(R.id.categoryTitle);
        }
    }
}