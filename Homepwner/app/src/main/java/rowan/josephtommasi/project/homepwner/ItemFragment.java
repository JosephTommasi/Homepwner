package rowan.josephtommasi.project.homepwner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.util.Date;
import java.util.List;

public class    ItemFragment extends Fragment {
    private Item mItem;
    private File mPhotoFile;
    private EditText mNameField;
    private Button mDate;
    private EditText mSerialField;
    private EditText mValueField;
    private Button mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Callbacks mCallbacks;

    private static final String ARG_ITEM_SERIAL = "item_serial";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;


    public interface Callbacks{
        void onItemUpdated(Item item);
    }
    public static ItemFragment newInstance(String itemSerial){
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_SERIAL, itemSerial);

        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String itemSerial = (String) getArguments().getSerializable(ARG_ITEM_SERIAL);
        mItem = ItemCatalog.get(getActivity()).getItem(itemSerial);
        mPhotoFile = ItemCatalog.get(getActivity()).getPhotoFile(mItem);
        //mItem = new Item();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item, container, false);
        mNameField = (EditText) v.findViewById(R.id.item_name);
        mNameField.setText(mItem.getName());
        mNameField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                mItem.setName(s.toString());
                updateItem();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                return;
            }

        });

        mValueField = (EditText) v.findViewById(R.id.item_value);
        mValueField.setText(Integer.toString(mItem.getValue()));
        mValueField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                mItem.setValue(Integer.valueOf(s.toString()));
                updateItem();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                return;
            }

        });

        mSerialField = (EditText) v.findViewById(R.id.item_serial);
        mSerialField.setText(mItem.getSerialNum().toString());


        //Date today = new Date();
        //mItem.setDateCreated(today);
        mDate = (Button) v.findViewById(R.id.date);
        updateDate();
        mDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mItem.getDateCreated());
                dialog.setTargetFragment(ItemFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();

        mReportButton = (Button) v.findViewById(R.id.reportButton);
        mReportButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getItemReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.item_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        mPhotoButton = (ImageButton) v.findViewById(R.id.item_camera);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        //System.out.println(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "rowan.josephtommasi.project.homepwner.fileprovider",mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });
        mPhotoView = (ImageView) v.findViewById(R.id.item_photo);
        updatePhotoView();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mItem.setDateCreated(date);
            updateItem();
            updateDate();
        } else if (requestCode == REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(), "rowan.josephtommasi.project.homepwner.fileprovider", mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updateItem();
            updatePhotoView();
        }
    }

    private void updateItem(){
        mCallbacks.onItemUpdated(mItem);
    }

    private void updateDate() {
        mDate.setText(mItem.getDateCreated().toString());
    }

    private String getItemReport(){
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mItem.getDateCreated()).toString();

        String report = getString(R.string.item_report,
                mItem.getName(), mItem.getSerialNum(), Integer.toString(mItem.getValue()), dateString);
        return report;
    }

    private void updatePhotoView(){
        if (mPhotoFile == null || !mPhotoFile.exists()){
            System.out.println("pass");
            //mPhotoButton.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
