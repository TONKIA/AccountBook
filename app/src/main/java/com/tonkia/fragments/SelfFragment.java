package com.tonkia.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.tonkia.utils.EditTextUtils;
import com.tonkia.utils.HttpUtils;
import com.tonkia.view.StateButton;
import com.tonkia.vo.DealRecord;

import org.litepal.LitePal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class SelfFragment extends Fragment {

    private ImageView ivBg;
    private CircleImageView profile;
    private TextView tvDay;
    private TextView tvDeal;
    private TextView tvUsername;

    private LinearLayout login;
    private LinearLayout menu;

    private EditText edPhone;
    private EditText edCode;

    private StateButton btnCode;
    private StateButton btnLogin;

    private LinearLayout btnUpload;
    private LinearLayout btnDownload;
    private LinearLayout btnAbout;
    private LinearLayout btnUnlogin;

    private TextView tvUploadTime;

    private SharedPreferences sp;

    private String phone;
    private String secretcode;

    private boolean hasRequestCode = false;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private long time;


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
        edPhone = view.findViewById(R.id.tv_phone);
        edCode = view.findViewById(R.id.tv_code);
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
        tvUploadTime.setText("");
        tvUsername.setText("未登录");
        login.setVisibility(View.INVISIBLE);
        menu.setVisibility(View.INVISIBLE);

        sp = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        initPerson();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tvDay.setText("");
        tvDeal.setText("" + getDealCount());
        int recordDay = sp.getInt("recordDay", 0);
        tvDay.setText("" + recordDay);
    }

    private void initPerson() {

        phone = sp.getString("phone", null);
        secretcode = sp.getString("secretcode", null);

        //没有登录过
        if (phone != null || secretcode != null) {
            indentify(phone, secretcode);
        } else {
            unloginUi();
        }

    }

    private void unloginUi() {

        System.out.println(menu.toString());

        tvUploadTime.setText("");
        tvUsername.setText("未登录");
        login.setVisibility(View.VISIBLE);
        menu.setVisibility(View.VISIBLE);
        edCode.setText("");
        edPhone.setText("");

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "About 小凯账本 Designer TONKIA", Toast.LENGTH_SHORT).show();
            }
        });
        btnUnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            }
        });

        //---------------------验证码------------------
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String iphone = edPhone.getText().toString().trim();
                if (EditTextUtils.isMobileNO(iphone)) {
                    hasRequestCode = true;
                    phone = iphone;
                    btnCode.setEnabled(false);
                    mc.start();
                    HttpUtils.requestIdentifyCode(iphone, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            hasRequestCode = false;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.code() == 200) {
                                //todo 占时返回验证码
                                final String res = response.body().string();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "验证码：" + res, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //---------------------登录键------------------
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasRequestCode) {
                    hasRequestCode = false;
                    String icode = edCode.getText().toString().trim();
                    if (icode.length() == 4) {
                        HttpUtils.loginWithCode(phone, icode, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.code() == 200) {
                                    secretcode = response.body().string();
                                    if (secretcode.length() > 0) {
                                        //获取到secretcode
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("phone", phone);
                                        editor.putString("secretcode", secretcode);
                                        editor.commit();
                                        initPerson();
                                    } else {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "请先获取验证码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUi() {
        tvUsername.setText("用户：" + phone);
        login.setVisibility(View.GONE);
        menu.setVisibility(View.VISIBLE);
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
                Toast.makeText(getContext(), "About 小凯账本 Designer TONKIA", Toast.LENGTH_SHORT).show();
            }
        });
        btnUnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("phone");
                editor.remove("secretcode");
                editor.commit();
                initPerson();
            }
        });
        if (time > 0) {
            tvUploadTime.setText("上次备份时间" + sdf.format(time));
        } else {
            tvUploadTime.setText("暂无备份");
        }
    }

    private void indentify(String phone, String code) {

        HttpUtils.login(phone, secretcode, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String time = response.body().string();
                    if (time.length() > 0) {
                        SelfFragment.this.time = Long.parseLong(time);
                        System.out.println("time" + time);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loginUi();
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                unloginUi();
                                Toast.makeText(getContext(), "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private int getDealCount() {
        List<DealRecord> list = LitePal.findAll(DealRecord.class);
        return list.size();
    }

    //第一个参数总计时时间，第二个是间隔时间，单位都是毫秒值
    private CountTime mc = new CountTime(60000, 1000);

    public class CountTime extends CountDownTimer {
        public CountTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //开始计时调用的函数
            btnCode.setText("重新获取(" + millisUntilFinished / 1000 + "秒)");
            //其中millisUntilFinished已经是倒计时的时间了
        }

        @Override
        public void onFinish() {    //完成计时调用的函数
            btnCode.setText("获取验证码");
            btnCode.setEnabled(true);
        }
    }
}
