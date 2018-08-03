package com.example.ysh.hyena.Context;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ysh.hyena.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ContextAdapter extends RecyclerView.Adapter<ContextAdapter.ViewHolder> {

    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    List<DataForm> mContext;
    String stEmail;
    Context context;

    public ContextAdapter(List<DataForm> mContext, String email, Context context) {
        this.mContext = mContext;
        this.stEmail = email;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {


        if (mContext.get(position).getEmail().equals(stEmail)) {
            return 1;
        } else
            return 0;

    }

    // 각각의 아이템안에 들어가 있는 텍스트나 버튼들을 참조하기 위한 메서드
    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_title;
        public TextView tv_price;
        public TextView tv_phone;
        public TextView tv_date;
        public TextView tv_time;
        public Button btnChat;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_item_title);    // 제목
            tv_price = itemView.findViewById(R.id.tv_item_price2);   // 가격
            tv_phone = itemView.findViewById(R.id.tv_item_phone2);   // 전화번호
            tv_date = itemView.findViewById(R.id.tv_item_date);      // 등록시간
            tv_time = itemView.findViewById(R.id.tv_item_time);
            btnChat = itemView.findViewById(R.id.btn_chat);
/*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "click " , Toast.LENGTH_SHORT).show();
                }
            });
*/
        }
    }

    @Override
    public ContextAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.context_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataForm data = mContext.get(position);


        long now = System.currentTimeMillis();
        long regTime = Long.parseLong(data.getTime());
        String k = formatTimeString(regTime);
        Toast.makeText(context, k, Toast.LENGTH_SHORT).show();


        holder.tv_title.setText(data.getTitle());     // 제목
        holder.tv_price.setText(data.getPrice());   // 가격
        holder.tv_phone.setText(data.getPhone());   // 전화번호
        holder.tv_date.setText(data.getDate());    // 등록시간
        holder.tv_time.setText(k);

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                String stContext = mContext.get(position).getKey();
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("email",stContext);
                context.startActivity(intent);
                */
            }
        });
    }

    public static String formatTimeString(long regTime) {
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - regTime) / 1000;
        String msg = null;
        if (diffTime < SEC) {
            msg = "방금 전";
        } else if ((diffTime /= SEC) < MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= MIN) < HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= HOUR) < DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= DAY) < MONTH) {
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mContext.size();
    }
}
