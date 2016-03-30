package com.fisheradelakin.daysbefore;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

public class NewDayActivity extends AppCompatActivity {

    @Bind(R.id.occasion_edit) EditText occasion;
    @Bind(R.id.datePicker) DatePicker mDatePicker;
    @Bind(R.id.new_layout) RelativeLayout newLayout;

    private int mColor;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_day);
        ButterKnife.bind(this);

        mRealm = Realm.getDefaultInstance();

        Colors colors = new Colors();
        mColor = colors.getColor();

        newLayout.setBackgroundColor(mColor);

        // generate a new color based on the background color for the status bar
        // using hsv because it makes it super easy bruh.
        float[] hsv = new float[3];
        Color.colorToHSV(mColor, hsv);
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

    private void saveDay() {
        Day day = new Day();

        day.setColor(mColor);
        day.setOccasion(occasion.getText().toString());

        int dayOfMonth = mDatePicker.getDayOfMonth();
        int month = mDatePicker.getMonth();
        int year = mDatePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth); // with calendar, i can set hour min and secs as well. :/

        day.setDate(calendar.getTime());

        day.setTimestamp(new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()));

        mRealm.beginTransaction();
        mRealm.copyToRealm(day);
        mRealm.commitTransaction();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Toast.makeText(NewDayActivity.this, "Date saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewDayActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}
