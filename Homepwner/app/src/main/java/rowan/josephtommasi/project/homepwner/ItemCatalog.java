package rowan.josephtommasi.project.homepwner;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ItemCatalog {
    private static ItemCatalog sItemCatalog;

    private List<Item> mItems;

    private Context mContext;

    public static ItemCatalog get(Context context){
        if (sItemCatalog == null){
            sItemCatalog = new ItemCatalog(context);
        }
        return sItemCatalog;
    }

    private ItemCatalog(Context context) {
        mContext = context.getApplicationContext();
        mItems = new ArrayList<>();
        //keep it simple w/ 10
        //textbook removes this; i think i want to keep it...less work to test

        for (int i = 0; i < 10; i++){
            Item item = new Item();
            mItems.add(item);
        }

    }
    public void addItem(Item c){
        mItems.add(c);
    }
    public List<Item> getItems(){
        return mItems;
    }

    public Item getItem(String serial) {
        for (Item item : mItems){
            if (item.getSerialNum().equals(serial)){
                return item;
            }
        }
        return null;
    }
    public File getPhotoFile(Item item){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, item.getPhotoFilename());
    }
}
