package com.fisheradelakin.daysbefore;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DetailActivity extends AppCompatActivity {

    private static final int WRITE_STORAGE_PERMISSION = 1;
    public static final String FRAGMENT_DIALOG = "dialog";

    private Realm mRealm;

    @Bind(R.id.detail_layout) RelativeLayout detailLayout;
    private Day mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRealm = Realm.getDefaultInstance();

        // get the timestamp for the day passed in and try to find that day
        String timeStamp = (String) getIntent().getSerializableExtra("time");
        if (timeStamp != null) {

            RealmQuery<Day> dayRealmQuery = mRealm.where(Day.class);
            dayRealmQuery.equalTo("timestamp", timeStamp);
            RealmResults<Day> realmResults = dayRealmQuery.findAll();
            if (realmResults.size() > 0) {
                mDay = realmResults.get(0);
            }
        }


        setColor();
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

}
