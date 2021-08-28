package com.example.eattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    ArrayList<Classitem> classitems;
    Context context;

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ClassAdapter(Context context, ArrayList<Classitem> classitems) {
        this.classitems = classitems;
        this.context = context;
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder{
        TextView ClassName;
        TextView SubjectName;
        ImageButton btn;

        public ClassViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            ClassName = itemView.findViewById(R.id.class_tv);
            SubjectName = itemView.findViewById(R.id.subject_tv);

            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));


        }
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item,parent,false);
        return new ClassViewHolder(itemView,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        holder.ClassName.setText(classitems.get(position).getClassname());
        holder.SubjectName.setText(classitems.get(position).getSubjectname());

    }

    @Override
    public int getItemCount() {
        return classitems.size();
    }
}
