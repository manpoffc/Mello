package com.example.mello;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterGroup extends RecyclerView.Adapter<MyAdapterGroup.MyViewHolder>{
    Context context;
    ArrayList<GroupModel> groupModels;

    public MyAdapterGroup(Context context, ArrayList<GroupModel> groupModels) {
        this.context = context;
        this.groupModels = groupModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.group_list_items,parent,false);

        return new MyAdapterGroup.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GroupModel groupModel = groupModels.get(position);
        holder.setIsRecyclable(false);
        holder.GroupName.setText(String.valueOf(groupModel.groupName));
    }

    @Override
    public int getItemCount() {
        return groupModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView GroupName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            GroupName = itemView.findViewById(R.id.name_group);
        }
    }
}
