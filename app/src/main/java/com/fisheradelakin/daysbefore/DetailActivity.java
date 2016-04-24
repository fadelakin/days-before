package com.fisheradelakin.daysbefore;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DetailActivity extends AppCompatActivity {

    // TODO: implement runnable for hours, mins, and secs
    // TODO: rework ui and how it looks
    // TODO: add edit option for date and title

    @Bind(R.id.detail_layout) RelativeLayout detailLayout;
    @Bind(R.id.occasion_tv) TextView occasion;
    @Bind(R.id.days_until_tv) TextView daysUntil;
    @Bind(R.id.counting_tv) TextView counting;
    @Bind(R.id.fab) FloatingActionButton share;

    private static final int WRITE_STORAGE_PERMISSION = 1;
    public static final String FRAGMENT_DIALOG = "dialog";

    private Realm mRealm;
    private Day mDay;
    private Bitmap mBitmap;

    private static final String TAG = "Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mRealm = Realm.getDefaultInstance();

        // get the timestamp for the day passed in and try to find that day
        String timeStamp = (String) getIntent().getSerializableExtra("time");
        if (timeStamp != null) {

            RealmQuery<Day> dayRealmQuery = mRealm.where(Day.class);
            dayRealmQuery.equalTo("timestamp", timeStamp);
            RealmResults<Day> realmResults = dayRealmQuery.findAll();
            if (realmResults.size() > 0) {
                mDay = realmResults.get(0);

                occasion.setText(mDay.getOccasion());
                Date date = mDay.getDate();
                DateTime dateTime = new DateTime(date);
                int days = Days.daysBetween(new DateTime(), dateTime).getDays();

                String daysBefore = days + " days";
                daysUntil.setText(daysBefore);

                SimpleDateFormat sdf = new SimpleDateFormat("LLLL dd, yyyy", Locale.getDefault());
                String countingDown = "Counting down to\n " + sdf.format(date);
                counting.setText(countingDown);
            }
        }

        setColor();
    }

    @OnClick(R.id.fab)
    public void share() {
        takeScreenshot();
    }

    private void setColor() {
        detailLayout.setBackgroundColor(mDay.getColor());

        // generate a new color based on the background color for the status bar
        // using hsv because it makes it super easy bruh.
        float[] hsv = new float[3];
        Color.colorToHSV(mDay.getColor(), hsv);
        hsv[2] *= 0.75f; // value component
        int statusBarColor = Color.HSVToColor(hsv);

        // set status bar to a darker color of the background
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.setStatusBarColor(statusBarColor);
        }
    }

    private void takeScreenshot() {

        share.setVisibility(View.GONE);

        detailLayout.setDrawingCacheEnabled(true);
        mBitmap = detailLayout.getDrawingCache();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestWriteStoragePermission();
        } else {
            takeScreenshot(mBitmap);
        }
    }

    private void takeScreenshot(Bitmap bitmap) {
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Days Before");

        if (!folder.exists()) {
            folder.mkdir();
            Log.i(TAG, "Folder doesn't exist but it does now.");
        } else {
            Log.i(TAG, "Folder exists");
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

        File file = new File(folder, "days_before" + timeStamp + ".jpeg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            file.setReadable(true, false);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/jpeg");

            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        share.setVisibility(View.VISIBLE);
    }

    private void requestWriteStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new StorageConfirmationDialog().show(getFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_STORAGE_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ErrorDialog.newInstance(getString(R.string.write_permission))
                        .show(getFragmentManager(), FRAGMENT_DIALOG);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            takeScreenshot(mBitmap);
        }
    }
}
