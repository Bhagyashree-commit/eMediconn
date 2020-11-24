package com.example.emediconn.Doctor.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.emediconn.Patient.MyProfileFragment;
import com.example.emediconn.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyAccountFragment extends Fragment implements View.OnClickListener {

    ImageView ivBack;
    ImageView ivProfile;
    TextView tvName;
    TextView tvPhone;
    TextView tvEmail;
    TextView tvViewOrder;
    TextView tvWallet;
    TextView tvViewProfile;
    TextView tvViewAddress;
    RelativeLayout rlEdit;

    public Uri fileUri;

    private static final int SELECT_PICTURE = 1;
    public Uri picUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "VoterMonitor";
    public static final int MEDIA_TYPE_VIDEO = 2;

    String picturePath = "", filename = "", ext = "";
    String encodedString1="";
    String encodedString2="";
    String setPic = "";
    public static Bitmap bitmap;
    int PERMISSION_ALL = 4;

    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE ,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.activity_my_account, container, false);

        ivBack = v.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        rlEdit = v.findViewById(R.id.rlEdit);
        rlEdit.setOnClickListener(this);

        ivProfile = v. findViewById(R.id.ivProfile);
        tvName = v.findViewById(R.id.tvName);
        tvPhone = v.findViewById(R.id.tvPhone);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvViewOrder = v.findViewById(R.id.tvViewOrder);
        tvViewOrder.setOnClickListener(this);

        tvViewProfile = v.findViewById(R.id.tvViewProfile);
        tvViewAddress = v.findViewById(R.id.tvViewAddress);

        tvViewProfile.setOnClickListener(this);
        tvViewAddress.setOnClickListener(this);


        ivProfile.setOnClickListener(this);

        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        replaceFragmentWithAnimation(new PatientDashboard());
                        return true;
                    }
                }
                return false;
            }
        });

        //Setting text
//        tvName.setText(pref.get(AppSettings.firstName));
//        tvPhone.setText(pref.get(AppSettings.Phone1));
//        tvEmail.setText(pref.get(AppSettings.Email));
    return v;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvViewProfile:
                replaceFragmentWithAnimation(new MyProfileFragment());
                break;

            case R.id.ivProfile:
                if(!hasPermissions(getActivity(), PERMISSIONS)){
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    showCameraGalleryDialog();
                }

                break;
        }
    }



    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void showCameraGalleryDialog() {

        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.camera_gallery_popup);

        dialog.show();

        RelativeLayout rrCancel = (RelativeLayout) dialog.findViewById(R.id.rr_cancel);
        LinearLayout llCamera = (LinearLayout) dialog.findViewById(R.id.ll_camera);
        LinearLayout llGallery = (LinearLayout) dialog.findViewById(R.id.ll_gallery);

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("camera","camera");
                 captureImage();
                dialog.dismiss();
            }
        });


        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(photoPickerIntent, SELECT_PICTURE);

                dialog.dismiss();
            }
        });

        rrCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.nav_host_fragment, fragment);
        FragmentManager mFragmentManager=getFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // transaction.addToBackStack(null);
        transaction.commit();
    }


    //============================Code for Camera and Gallary===================================//

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        fileUri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", getOutputMediaFile(MEDIA_TYPE_IMAGE));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}
