package com.tonkia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tonkia.utils.EditTextUtils;
import com.tonkia.view.StateButton;
import com.tonkia.vo.DealItem;
import com.tonkia.vo.DealRecord;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.shaohui.bottomdialog.BottomDialog;


public class UpdateActivity extends AppCompatActivity {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年mm月dd日 E");
    private int id;
    private TextView tvTitle;
    private TextView tvType;
    private TextView tvCost;
    private TextView tvDesc;
    private TextView tvDate;

    private DealRecord dealRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        id = getIntent().getIntExtra("id", -1);
        if (id == -1)
            finish();

        tvTitle = findViewById(R.id.tv_title);
        tvType = findViewById(R.id.tv_type);
        tvCost = findViewById(R.id.tv_cost);
        tvDesc = findViewById(R.id.tv_desc);
        tvDate = findViewById(R.id.tv_date);

        dealRecord = (DealRecord) LitePal.where("id=?", id + "").find(DealRecord.class).get(0);

        tvTitle.setText(dealRecord.getType() == DealItem.OUTPUT ? "支出" : "收入");
        tvType.setText(dealRecord.getItemName());
        tvCost.setText(String.format("%.2f", dealRecord.getCost()));
        tvDesc.setText(dealRecord.getDesc());
        tvDate.setText(sdf.format(new Date(dealRecord.getTime())));

    }

    public void back(View view) {
        finish();
    }

    public void delete(View view) {
        final BottomDialog bd = BottomDialog.create(getSupportFragmentManager());
        bd.setViewListener(new BottomDialog.ViewListener() {
            @Override
            public void bindView(View v) {
                TextView tv = v.findViewById(R.id.title);
                StateButton btnDel = v.findViewById(R.id.btn_ok);
                StateButton btnCancel = v.findViewById((R.id.btn_cancel));
                tv.setText("你确定要删除这条记录吗？");
                btnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dealRecord.delete();
                        setResult(1);
                        finish();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bd.dismiss();
                    }
                });
            }
        }).setLayoutRes(R.layout.dialog_delete).setDimAmount(0.5f).setCancelOutside(true).setTag("DeleteDialog").show();
    }

    public void update(View view) {
        final BottomDialog bd = BottomDialog.create(getSupportFragmentManager());
        bd.setViewListener(new BottomDialog.ViewListener() {
            @Override
            public void bindView(View v) {
                TextView tv = v.findViewById(R.id.title);
                final EditText edDesc = v.findViewById(R.id.ed_desc);
                final EditText edCost = v.findViewById(R.id.ed_cost);
                EditTextUtils.setPoint(edCost);
                StateButton btnOK = v.findViewById(R.id.btn_ok);
                StateButton btnCancel = v.findViewById((R.id.btn_cancel));
                tv.setText(dealRecord.getItemName() + "收入");
                btnOK.setText("修改");
                edDesc.setText(dealRecord.getDesc());
                edCost.setText(dealRecord.getCost() + "");
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String desc = edDesc.getText().toString().trim();
                        float cost = 0;
                        try {
                            cost = Float.parseFloat(edCost.getText().toString().trim());
                        } catch (Exception e) {
                            return;
                        }
                        if (TextUtils.isEmpty(desc) || cost <= 0) {
                            return;
                        }
                        dealRecord.setDesc(desc);
                        dealRecord.setCost(cost);
                        dealRecord.save();
                        setResult(1);
                        finish();
                        //最后应该调转到MainActivity下Detail
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bd.dismiss();
                    }
                });
            }
        }).setLayoutRes(R.layout.dialog_add).setDimAmount(0.5f).setCancelOutside(true).setTag("UpdateDialog").show();
    }


}
