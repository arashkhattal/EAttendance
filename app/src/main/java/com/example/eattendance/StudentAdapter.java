package com.example.eattendance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder>{
    ArrayList<Studentitem> studentitems;
    Context context;

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public StudentAdapter(Context context, ArrayList<Studentitem> studentitems) {
        this.studentitems = studentitems;
        this.context = context;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView number;
        TextView status;
        CardView cardView;

        public StudentViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.studentname_tv);
            number = itemView.findViewById(R.id.studentnumber_tv);
            status = itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.cardview);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,parent,false);
        return new StudentViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.name.setText(studentitems.get(position).getname());
        holder.number.setText(studentitems.get(position).getRoll()+"");
        holder.status.setText(studentitems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getcolor(position));

    }

    private int getcolor(int position) {
        String status = studentitems.get(position).getStatus();
        if (status.equals("PRESENT"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.present)));
        else if (status.equals("ABSENT"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.absent)));

        return 0;
    }

    @Override
    public int getItemCount() {  return studentitems.size();}

}
