package rowan.josephtommasi.project.homepwner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class ItemPagerActivity extends AppCompatActivity implements ItemFragment.Callbacks {

    private static final String EXTRA_ITEM_SERIAL = "rowan.josephtommasi.project.homepwner.item_serial";

    private ViewPager mViewPager;
    private List<Item> mItems;

    public static Intent newIntent(Context packageContext, String itemSerial){
        Intent intent = new Intent(packageContext, ItemPagerActivity.class);
        intent.putExtra(EXTRA_ITEM_SERIAL, itemSerial);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_pager);

        String itemSerial = (String) getIntent().getSerializableExtra(EXTRA_ITEM_SERIAL);

        mViewPager = (ViewPager) findViewById(R.id.item_view_pager);

        mItems = ItemCatalog.get(this).getItems();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Item item = mItems.get(position);
                return ItemFragment.newInstance(item.getSerialNum());
            }

            @Override
            public int getCount() {
                return mItems.size();
            }
        });

        for (int i = 0; i < mItems.size(); i++){
            if (mItems.get(i).getSerialNum().equals(itemSerial)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onItemUpdated(Item item){

    }
}
