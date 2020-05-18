package id.ac.umn.simplymeal.CategoryView;

import android.os.Bundle;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AdapterCategoryTab extends FragmentPagerAdapter {
    private List<CategoryTab> categories;

    public AdapterCategoryTab(@NonNull FragmentManager fm, List<CategoryTab>categories) {
        super(fm);
        this.categories = categories;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        FragmentMenu fragment = new FragmentMenu();
        Bundle args = new Bundle();
        args.putString("categoryId",categories.get(position).getIdCategory());
        args.putString("categoryName",categories.get(position).getCategoryName());
        args.putString("categoryDesc",categories.get(position).getDesc());
        args.putString("categoryImage",categories.get(position).getImage());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position){
        return categories.get(position).getCategoryName();
    }
}
