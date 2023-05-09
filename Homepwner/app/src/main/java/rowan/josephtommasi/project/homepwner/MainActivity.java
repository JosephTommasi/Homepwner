package rowan.josephtommasi.project.homepwner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends SingleFragmentActivity {

    private static final String EXTRA_ITEM_SERIAL = "rowan.josephtommasi.project.homepwner.item_serial";

    public static Intent newIntent(Context packageContext, String itemSerial){
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(EXTRA_ITEM_SERIAL, itemSerial);
        return intent;
    }

    @Override
    protected Fragment createFragment(){
        String itemSerial = (String) getIntent().getSerializableExtra(EXTRA_ITEM_SERIAL);
        return ItemFragment.newInstance(itemSerial);
    }
}