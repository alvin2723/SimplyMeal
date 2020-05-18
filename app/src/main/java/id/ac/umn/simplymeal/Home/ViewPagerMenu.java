package id.ac.umn.simplymeal.Home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import id.ac.umn.simplymeal.CategoryView.CategoryTab;
import id.ac.umn.simplymeal.CategoryView.CategoryTabActivity;
import id.ac.umn.simplymeal.CategoryView.DetailMenuActivity;
import id.ac.umn.simplymeal.CategoryView.Menu;
import id.ac.umn.simplymeal.R;

public class ViewPagerMenu extends PagerAdapter {
    private List<Menu> menus;
    private Context context;
    private static View.OnClickListener clickListener;

    public ViewPagerMenu(List<Menu>menus, Context context){
        this.menus = menus;
        this.context = context;
    }
    public void setOnItemClickListener(View.OnClickListener clickListener) {
        ViewPagerMenu.clickListener = clickListener;
    }
    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position){
        final View view = LayoutInflater.from(context).inflate(
                R.layout.item_view_pager_header,
                container,
                false
        );
        ImageView imageMenu = view.findViewById(R.id.menuThumb);
        final TextView menuTitle = view.findViewById(R.id.menuName);

        String viewMeal = menus.get(position).getidImage();
        Picasso.get().load(viewMeal).into(imageMenu);

        String strMealName = menus.get(position).getMenuName();
        menuTitle.setText(strMealName);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailscreen = new Intent(view.getContext(), DetailMenuActivity.class);
                detailscreen.putExtra("menuTitle",menus.get(position).getMenuName()).putExtra("menuBackground",menus.get(position).getidImageBackground()).putExtra("idMenu", menus.get(position).getIdMenu());
                view.getContext().startActivity(detailscreen);
            }
        });
        container.addView(view,0);
        return view;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
    public interface OnClickListener {
        void onClick(View v, int position);
    }


}
