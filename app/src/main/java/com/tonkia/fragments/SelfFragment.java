package com.tonkia.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tonkia.R;
import com.tonkia.view.StateButton;
import com.tonkia.vo.DealRecord;

import org.litepal.LitePal;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class SelfFragment extends Fragment {

    private ImageView ivBg;
    private CircleImageView profile;
    private TextView tvDay;
    private TextView tvDeal;
    private TextView tvUsername;

    private LinearLayout login;
    private LinearLayout menu;

    private EditText tvPhone;
    private EditText tvCode;

    private StateButton btnCode;
    private StateButton btnLogin;

    private LinearLayout btnUpload;
    private LinearLayout btnDownload;
    private LinearLayout btnAbout;
    private LinearLayout btnUnlogin;

    private TextView tvUploadTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self, container, false);
        ivBg = view.findViewById(R.id.img_bg);
        profile = view.findViewById(R.id.profile_image);
        tvUsername = view.findViewById(R.id.tv_username);
        tvDay = view.findViewById(R.id.tv_day_count);
        tvDeal = view.findViewById(R.id.tv_deal_count);
        login = view.findViewById(R.id.login_form);
        menu = view.findViewById(R.id.menu);
        tvPhone = view.findViewById(R.id.tv_phone);
        tvCode = view.findViewById(R.id.tv_code);
        btnCode = view.findViewById(R.id.btn_code);
        btnLogin = view.findViewById(R.id.btn_login);
        btnUpload = view.findViewById(R.id.btn_upload);
        btnDownload = view.findViewById(R.id.btn_download);
        btnAbout = view.findViewById(R.id.btn_about);
        btnUnlogin = view.findViewById(R.id.btn_unlogin);
        tvUploadTime = view.findViewById(R.id.tv_upload_time);

        RequestOptions myOptions = new RequestOptions()
                .centerCrop();
        Glide.with(this).load(R.drawable.bg).apply(myOptions).into(ivBg);
        Glide.with(this).load(R.drawable.profile).apply(myOptions).into(profile);

        //先隐藏起来
        tvUsername.setText("未登录");
        login.setVisibility(View.INVISIBLE);
        menu.setVisibility(View.INVISIBLE);

        initPerson();
        return view;
    }

    private void initPerson() {
        SharedPreferences sp = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String phone = sp.getString("phone", null);
        String code = sp.getString("code", null);
        //验证帐号密码
        if (indentify(phone, code)) {
            tvUsername.setText("用户：" + phone);
            login.setVisibility(View.GONE);
            menu.setVisibility(View.VISIBLE);
            menu.setOnClickListener(null);
            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "click on btnUpload", Toast.LENGTH_SHORT).show();
                }
            });
            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "click on btnDownload", Toast.LENGTH_SHORT).show();
                }
            });
            btnAbout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "click on btnAbout", Toast.LENGTH_SHORT).show();
                }
            });
            btnUnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "click on btnUnlogin", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            login.setVisibility(View.VISIBLE);
            menu.setVisibility(View.VISIBLE);
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean indentify(String phone, String code) {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        tvDay.setText("");
        tvDeal.setText("" + getDealCount());
        SharedPreferences sp = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        int recordDay = sp.getInt("recordDay", 0);
        tvDay.setText("" + recordDay);
    }

    private int getDealCount() {
        List<DealRecord> list = LitePal.findAll(DealRecord.class);
        return list.size();
    }
}
