package com.example.emediconn.Doctor;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Patient.AboutUsFragment;
import com.example.emediconn.Doctor.ui.ChangePasswordFragment;
import com.example.emediconn.Patient.ContactUsFragment;
import com.example.emediconn.Doctor.ui.DoctorListFragment;
import com.example.emediconn.Patient.MyAccountFragment;
import com.example.emediconn.Patient.PatientDashboardFragment;
import com.example.emediconn.Patient.TermsOfServiceFragment;
import com.example.emediconn.Patient.DoctorCategoryFragment;
import com.example.emediconn.R;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DrawerActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    PrefManager prefManager;
    ImageView iv_patienpropic;
    TextView tv_patientname;
    //other
    public static int backPressed = 0;

    public static String cat_id="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);

        drawer = findViewById(R.id.drawer_layout);
        iv_patienpropic = headerview.findViewById(R.id.iv_patientpropic);
        tv_patientname = headerview.findViewById(R.id.tv_patientname);
        prefManager=new PrefManager(DrawerActivity.this);
        String name=prefManager.get("full_name");

        Log.e("profileeeee",""+prefManager.get("profile_photo"));
        //prefManager.get("full_name");
           // tv_patientname.setText(prefManager.get("full_name"));
        if(prefManager.get("profile_photo").trim().length()>40)
        {
            //iv_patienpropic.setImageDrawable(getResources().getDrawable(R.drawable.userr));

            String image_url=prefManager.get("profile_photo");
            Log.e("url",image_url);
            Glide.with(getApplicationContext())
                    .load(image_url)
                    .into(iv_patienpropic);
        }
        else {
            iv_patienpropic.setImageDrawable(getResources().getDrawable(R.drawable.userr));
        }

  tv_patientname.setText(name);

        prefManager=new PrefManager(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        Intent in=getIntent();

        String called_from = getIntent().getStringExtra("page");

        if(called_from == null) {
            called_from = "empty string";
            Log.e("called",called_from);
        }
        if (called_from.equalsIgnoreCase("Search")) {
            // do whatever

            cat_id=getIntent().getStringExtra("cat_id");
            replaceFragmentWithAnimation(new DoctorListFragment());


        } else {
            replaceFragmentWithAnimation(new PatientDashboardFragment());
            // do whatever
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_patientdashboard) {
            replaceFragmentWithAnimation( new PatientDashboardFragment());
        }
        else if (id == R.id.myProfile) {
            replaceFragmentWithAnimation( new MyAccountFragment());
        }
        else if (id == R.id.nav_changepass) {
            replaceFragmentWithAnimation( new ChangePasswordFragment());
        }
        else if (id == R.id.nav_aboutus) {
            replaceFragmentWithAnimation(new AboutUsFragment());
        }
        else if (id == R.id.nav_category) {
            replaceFragmentWithAnimation(new DoctorCategoryFragment());
        }
        else if (id == R.id.nav_termsOfservice) {
            replaceFragmentWithAnimation(new TermsOfServiceFragment());
        }

        else if (id == R.id.nav_appshare) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, download this app!");
            startActivity(shareIntent);
        }
        else if (id == R.id.nav_contactus) {
            replaceFragmentWithAnimation(new ContactUsFragment());

        }

        else if (id == R.id.nav_logout) {
            logout();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.nav_host_fragment, fragment);
        FragmentManager mFragmentManager=getSupportFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
       // transaction.addToBackStack(null);
        transaction.commit();
    }

    public void logout() {
        //Dialog
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertyesno);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);


        //findId
        TextView tvYes = (TextView) dialog.findViewById(R.id.tvOk);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvcancel);
        TextView tvReason = (TextView) dialog.findViewById(R.id.textView22);
        TextView tvAlertMsg = (TextView) dialog.findViewById(R.id.tvAlertMsg);

        //set value
        tvAlertMsg.setText("Confirmation Alert..!!!");
        tvReason.setText("Are you sure want to logout?");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        //set listener
        tvYes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                prefManager.logoutUser();
                //startActivity(new Intent(DrawerActivity.this, ChooseRole.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
prefManager.set("user_type","");
prefManager.commit();

               // preferences.set("login","no");
              //  preferences.commit();
//                Toasty.success(DrawerActivity.this, "Logged out..!!!", Toast.LENGTH_SHORT).show();
                finishAffinity();
                dialog.dismiss();
            }
        });


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
        backPressed = backPressed + 1;
        if (backPressed == 1) {
            Toast.makeText(DrawerActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new CountDownTimer(5000, 1000) { // adjust the milli seconds here
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() { backPressed = 0;
                }
            }.start();
        }
        if (backPressed == 2) {
            backPressed = 0;
            finishAffinity();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


}