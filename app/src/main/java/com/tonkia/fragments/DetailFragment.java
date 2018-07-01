package com.tonkia.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tonkia.R;
import com.tonkia.vo.DealRecord;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DetailFragment extends Fragment {
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<DealRecord> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        recyclerView = view.findViewById(R.id.recycle);
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        initList();
        return view;
    }

    private void initList() {
        mList = LitePal.findAll(DealRecord.class);
        Collections.reverse(mList);
        myAdapter.notifyDataSetChanged();

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_record_item, viewGroup, false);
            if (i == mList.size() - 1) {

            }
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
            myHolder.tvTime.setText(sdf.format(new Date(mList.get(i).getTime())));
            myHolder.tvCost.setText(String.format("%.2f", mList.get(i).getCost()));
            myHolder.tvItem.setText(mList.get(i).getItemName());
            myHolder.tvDesc.setText(mList.get(i).getDesc());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            public TextView tvTime;
            public TextView tvCost;
            public TextView tvItem;
            public TextView tvDesc;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                tvTime = itemView.findViewById(R.id.tv_time);
                tvCost = itemView.findViewById(R.id.tv_cost);
                tvItem = itemView.findViewById(R.id.tv_item);
                tvDesc = itemView.findViewById(R.id.tv_desc);
            }
        }
    }

}
