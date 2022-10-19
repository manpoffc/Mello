package com.example.mello;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterIncome extends RecyclerView.Adapter<MyAdapterIncome.MyViewHolder>{
    Context context;
    ArrayList<Income> incomes;

    public MyAdapterIncome(Context context, ArrayList<Income> incomes) {
        this.context = context;
        this.incomes = incomes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_items,parent,false);
        return new MyAdapterIncome.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Income income = incomes.get(position);
        holder.category.setText(income.category);
        holder.categoryValue.setText(String.valueOf(income.category_value));
    }

    @Override
    public int getItemCount() {
        return incomes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView category, categoryValue;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.category);
            categoryValue=itemView.findViewById(R.id.categoryValue);
        }
    }
}
