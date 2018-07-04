package com.tonkia.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tonkia.MainActivity;
import com.tonkia.R;
import com.tonkia.utils.AudioPlayUtils;
import com.tonkia.vo.AudioInfo;
import com.tonkia.vo.DealRecord;

import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class DealFragment extends Fragment {

    private SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");
    private List<AudioInfo> mList;
    private RecyclerView rv;
    private MyAdapt adapt;
    private AudioPlayUtils audioPlayUtils;

    private RoundCornerProgressBar progressBar;
    private int total;
    private TextView tvEmpty;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal, container, false);
        rv = view.findViewById(R.id.recycle);
        tvEmpty = view.findViewById(R.id.tv_empty);
        adapt = new MyAdapt();
        rv.setAdapter(adapt);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        audioPlayUtils = new AudioPlayUtils();
        audioPlayUtils.setAudioPlayerListener(new MyAudioPlayerListenr());
        freshList();
        return view;
    }

    private void freshList() {
        MainActivity activity = (MainActivity) (getActivity());
        activity.freshDeal();
    }

    private class MyAudioPlayerListenr implements AudioPlayUtils.AudioPlayerListener {

        @Override
        public void onError() {
            System.out.println("*******onError" + progressBar);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "播放失败！", Toast.LENGTH_SHORT).show();
                    progressBar.setProgress(0);
                    progressBar = null;
                    total = 0;
                }
            });

        }

        @Override
        public void onComplete() {
            System.out.println("*******onComplete");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(0);
                    progressBar = null;
                    total = 0;
                }
            });

        }

        @Override
        public void currentPosition(final int position) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(100 * position / total);
                }
            });
        }

        @Override
        public void onStart(int total) {
            DealFragment.this.total = total;
        }
    }

    public void freshList(List<AudioInfo> mList) {
        this.mList = mList;
        adapt.notifyDataSetChanged();
        if (mList.size() > 0) {
            tvEmpty.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.VISIBLE);
        }

    }

    class MyAdapt extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_audio, viewGroup, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
            final AudioInfo ai = mList.get(i);
            ((MyViewHolder) viewHolder).tvName.setText(sfd.format(new Date(ai.getTime())));
            ((MyViewHolder) viewHolder).rpb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (DealFragment.this.progressBar == null) {
                        DealFragment.this.progressBar = ((MyViewHolder) viewHolder).rpb;
                        audioPlayUtils.playAudio(new File(ai.getPath()));
                    }
                }
            });
            ((MyViewHolder) viewHolder).btnDel.setVisibility(View.GONE);
            ((MyViewHolder) viewHolder).rpb.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((MyViewHolder) viewHolder).tvName.setText("删除该条语音          ");
                    ((MyViewHolder) viewHolder).btnDel.setVisibility(View.VISIBLE);

                    ((MyViewHolder) viewHolder).rpb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((MyViewHolder) viewHolder).tvName.setText(sfd.format(new Date(ai.getTime())));
                            ((MyViewHolder) viewHolder).rpb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (DealFragment.this.progressBar == null) {
                                        DealFragment.this.progressBar = ((MyViewHolder) viewHolder).rpb;
                                        audioPlayUtils.playAudio(new File(ai.getPath()));
                                    }
                                }
                            });
                            ((MyViewHolder) viewHolder).btnDel.setVisibility(View.GONE);
                        }
                    });
                    ((MyViewHolder) viewHolder).btnDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            File f = new File(ai.getPath());
                            f.delete();
                            ai.delete();
                            freshList();
                        }
                    });

                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView tvName;
            public RoundCornerProgressBar rpb;
            public ImageButton btnDel;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_name);
                rpb = itemView.findViewById(R.id.progressBar);
                btnDel = itemView.findViewById(R.id.btn_del);
            }
        }
    }


}
