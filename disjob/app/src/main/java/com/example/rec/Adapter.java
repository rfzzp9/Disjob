package com.example.rec;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.CustomViewHolder> {

    private ArrayList<JobListData> jobArrayList;
    private OnItemClickListener itemClickListener;
    private Context context;
    private int position;



//    @Override           //recyclerview 재사용 막음
//    public int getItemViewType(int position) {
//        return position;
//    }

    public ArrayList<JobListData> setFilteredList(ArrayList<JobListData> filteredList)
    {
        this.jobArrayList = filteredList;
        notifyDataSetChanged();
        return jobArrayList;
    }

    //인터페이스 선언
    public interface OnItemClickListener{
        //클릭시 동작할 함수
        void onItemClick(View v, int pos);
        void onButtonClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.itemClickListener = listener;
    }


    public Adapter(ArrayList<JobListData> jobArrayList) {
        this.jobArrayList = jobArrayList;
    }
    @NonNull
    @Override
    public Adapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.job_list2,viewGroup,false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull Adapter.CustomViewHolder customViewHolder, int i) {

        customViewHolder.job.setText(jobArrayList.get(i).getStar_img());
        customViewHolder.job.setText(jobArrayList.get(i).getJob_name());
        customViewHolder.hour_money.setText(jobArrayList.get(i).getHour_money());
        customViewHolder.area.setText(jobArrayList.get(i).getArea());
        customViewHolder.hireType.setText(jobArrayList.get(i).getHireType());
    }


    @Override
    public int getItemCount() {
        return jobArrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageButton star;
        TextView job;
        TextView hour_money;
        TextView area;
        TextView hireType;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.star = (ImageButton) itemView.findViewById(R.id.starBtn);
            this.job = (TextView) itemView.findViewById(R.id.jobName);
            this.hour_money = (TextView) itemView.findViewById(R.id.hourlyMoney);
            this.area = (TextView) itemView.findViewById(R.id.area);
            this.hireType = (TextView) itemView.findViewById(R.id.job);

            star.setTag(R.drawable.img_1); //repair 88

            itemView.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        //동작 호출 (onItemClick 함수 호출)
                        if (itemClickListener != null) {
                            itemClickListener.onItemClick(v, pos);
                        }
                    }
                }
            });

            star.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("recyclerview 11111", "!!!!!!!!!!!!!!!!!");
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION)
                    {
                        Log.e("recyclerview 22222", "!!!!!!!!!!!!!!!!!");
                        if(itemClickListener != null) {
                            itemClickListener.onButtonClick(view, pos);
                        }
                    }
                }


            });
        }

    }


}
