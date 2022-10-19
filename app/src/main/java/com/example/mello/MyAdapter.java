package com.example.mello;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Expense> expenses;

    public MyAdapter(Context context, ArrayList<Expense> expenses) {

        this.context = context;
        this.expenses = expenses;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_items,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Expense expense = expenses.get(position);
        holder.category.setText(expense.category);
        holder.categoryValue.setText(String.valueOf(expense.category_value));

    }

    @Override
    public int getItemCount() {
        return expenses.size();
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
