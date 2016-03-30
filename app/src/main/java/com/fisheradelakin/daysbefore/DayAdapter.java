package com.fisheradelakin.daysbefore;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by temidayo on 3/29/16.
 */
public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private Context mContext;
    private RealmResults<Day> mDays;

    public DayAdapter(Context context, RealmResults<Day> days) {
        mContext = context;
        mDays = days;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.day_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Day day = mDays.get(position);

        String occasion = day.getOccasion();
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("day", mDays.get(getLayoutPosition()));
            mContext.startActivity(intent);
        }
    }
}
