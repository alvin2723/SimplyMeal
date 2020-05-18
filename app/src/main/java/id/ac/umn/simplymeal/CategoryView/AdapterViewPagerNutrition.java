package id.ac.umn.simplymeal.CategoryView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import id.ac.umn.simplymeal.R;

public class AdapterViewPagerNutrition extends PagerAdapter{
    private List<Portion> menus;
    private Context context;
    private static View.OnClickListener clickListener;

    public AdapterViewPagerNutrition(List<Portion>menus, Context context){
        this.menus = menus;
        this.context = context;
    }
    public void setOnItemClickListener(View.OnClickListener clickListener) {
        AdapterViewPagerNutrition.clickListener = clickListener;
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
                R.layout.pager_nutrition,
                container,
                false
        );

        ImageView imageNutrition = view.findViewById(R.id.nutrition);
        TextView menuTitle = view.findViewById(R.id.portionName);

        String viewMeal = menus.get(position).getNutrition();
        Picasso.get().load(viewMeal).into(imageNutrition);

        String strPortionName = menus.get(position).getPortionName();
        menuTitle.setText(strPortionName);

        container.addView(view,0);
        return view;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
