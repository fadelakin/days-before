package com.fisheradelakin.daysbefore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    // TODO: Write code to update Day in case user wants to change date
    // TODO: > Day day = realm.where(Day.class).equals("timestamp", originalDay.getTimestamp()).findFirst();
    // TODO: > realm.beginTransaction();
    // TODO: > set the values of the updated day
    // TODO: > realm.commitTransaction();

    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.days_rv) RecyclerView daysRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Day> dayRealmQuery = realm.where(Day.class);
        RealmResults<Day> days = dayRealmQuery.findAll();

        DayAdapter adapter = new DayAdapter(this, days);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        daysRV.setLayoutManager(layoutManager);
        daysRV.setHasFixedSize(true);
        daysRV.setAdapter(adapter);
    }

    @OnClick(R.id.fab)
    public void addDay() {
        Intent intent = new Intent(this, NewDayActivity.class);
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
