package com.tonkia.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tonkia.R;
import com.tonkia.vo.DealRecord;

import org.litepal.LitePal;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class SelfFragment extends Fragment {

    private ImageView ivBg;
    private CircleImageView profile;
    private TextView tvDay;
    private TextView tvDeal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_self, container, false);
        ivBg = view.findViewById(R.id.img_bg);
        profile = view.findViewById(R.id.profile_image);

        tvDay = view.findViewById(R.id.tv_day_count);
        tvDeal = view.findViewById(R.id.tv_deal_count);

        RequestOptions myOptions = new RequestOptions()
                .centerCrop();
        Glide.with(this).load(R.drawable.bg).apply(myOptions).into(ivBg);
        Glide.with(this).load(R.drawable.profile).apply(myOptions).into(profile);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fresh();
    }

    private void fresh() {
        tvDay.setText("");
        tvDeal.setText("" + getDealCount());
    }

    private int getDealCount() {
        List<DealRecord> list = LitePal.findAll(DealRecord.class);
        return list.size();
    }
}
