package com.tonkia.fragments.add;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tonkia.AddActivity;
import com.tonkia.R;
import com.tonkia.utils.EditTextUtils;
import com.tonkia.view.StateButton;
import com.tonkia.vo.DealItem;
import com.tonkia.vo.DealRecord;

import org.litepal.LitePal;

import java.util.List;

import me.shaohui.bottomdialog.BottomDialog;

public class OutputFragment extends Fragment {
    private List<DealItem> itemList;
    private RecyclerView rv;
    private MyRecycleAdapter myRecycleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_output, container, false);
        RecyclerView rv = view.findViewById(R.id.recycle);
        myRecycleAdapter = new MyRecycleAdapter();
        rv.setAdapter(myRecycleAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        initList();
        return view;
    }

    public void initList() {
        //查询数据库里的item数据
        itemList = LitePal.where("type=?", "" + DealItem.OUTPUT).order("id desc").find(DealItem.class);
        myRecycleAdapter.notifyDataSetChanged();
    }

    private class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder> {

        class MyViewHolder extends RecyclerView.ViewHolder {
            public StateButton btnItem;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                btnItem = itemView.findViewById(R.id.btn_item);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_deal_item, viewGroup, false);
            MyViewHolder mvh = new MyViewHolder(view);
            return mvh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            myViewHolder.btnItem.setText(itemList.get(i).getItemName());
            myViewHolder.btnItem.setOnClickListener(new MyOnClickListener(i));
            myViewHolder.btnItem.setOnLongClickListener(new MyOnLongClickListener(i));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        class MyOnClickListener implements View.OnClickListener {
            private int i;

            public MyOnClickListener(int i) {
                this.i = i;
            }

            @Override
            public void onClick(View view) {
                addDeal(i);

            }
        }

        class MyOnLongClickListener implements View.OnLongClickListener {
            private int i;

            public MyOnLongClickListener(int i) {
                this.i = i;
            }

            @Override
            public boolean onLongClick(View view) {
                deleteItem(i);
                return true;
            }
        }
    }

    private void deleteItem(final int i) {
        final BottomDialog bd = BottomDialog.create(getFragmentManager());
        bd.setViewListener(new BottomDialog.ViewListener() {
            @Override
            public void bindView(View v) {
                TextView tv = v.findViewById(R.id.title);
                Button btnDel = v.findViewById(R.id.btn_ok);
                Button btnCancel = v.findViewById((R.id.btn_cancel));
                tv.setText("你确定要删除<" + itemList.get(i).getItemName() + ">这条支出项吗？");
                btnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemList.get(i).delete();
                        initList();
                        bd.dismiss();
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

    private void addDeal(final int i) {
        final BottomDialog bd = BottomDialog.create(getFragmentManager());
        bd.setViewListener(new BottomDialog.ViewListener() {
            @Override
            public void bindView(View v) {
                TextView tv = v.findViewById(R.id.title);
                final EditText edDesc = v.findViewById(R.id.ed_desc);
                final EditText edCost = v.findViewById(R.id.ed_cost);
                EditTextUtils.setPoint(edCost);
                StateButton btnOK = v.findViewById(R.id.btn_ok);
                StateButton btnCancel = v.findViewById((R.id.btn_cancel));
                tv.setText(itemList.get(i).getItemName() + "消费");
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
                        DealRecord dr = new DealRecord();
                        dr.setCost(cost);
                        dr.setItemName(itemList.get(i).getItemName());
                        dr.setDesc(desc);
                        dr.setTime(System.currentTimeMillis());
                        dr.setType(itemList.get(i).getType());
                        dr.initTime();
                        dr.save();
                        getActivity().setResult(1);
                        getActivity().finish();
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
        }).setLayoutRes(R.layout.dialog_add).setDimAmount(0.5f).setCancelOutside(true).setTag("AddDialog").show();
    }
}
