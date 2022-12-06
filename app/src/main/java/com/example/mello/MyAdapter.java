package com.example.mello;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    Context context;
    String name;
    String amt;
    String date;
    String category;
    String comment;

    private EditText edtname,edtamount,edtdate,edtcomment;
    private AutoCompleteTextView edtcategory;
    private Button updatebutton;
    private Button deletebutton;
    private RadioButton individualBtn, groupButton;
    private TextInputLayout updateGroupDropdown;

    //ArrayList<Expense> expenses;
    ArrayList<ExpenseData> expensedata;

    //For categories in update dialog box
    ArrayAdapter<String> adapterItems;
    String[] categories = {"Food","Entertainment","Car","Fuel","Insurance","Clothes","Services","Gifts","Bills","Education","Liquor","Rent","Other"};

    public MyAdapter(Context context, ArrayList<ExpenseData> expensedata) {

        this.context = context;
       // this.expenses = expenses;
        this.expensedata=expensedata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // View v = LayoutInflater.from(context).inflate(R.layout.list_items,parent,false);
        View v = LayoutInflater.from(context).inflate(R.layout.list_expense,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //Expense expense = expenses.get(position);
       // holder.category.setText(expense.category);
        //holder.categoryValue.setText(String.valueOf(expense.category_value));
        ExpenseData expense = expensedata.get(position);
        holder.name.setText(expense.Name);
        holder.amount.setText(String.valueOf(expense.Amount));
        holder.date.setText(String.valueOf(expense.Date));
        holder.category.setText(expense.Category);
        holder.comment.setText(expense.Comment);
        String key=expense.getExpenseID();

        int pos = position;
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = holder.name.getText().toString();
                amt = holder.amount.getText().toString();
                date = holder.date.getText().toString();
                category = holder.category.getText().toString();
                comment = holder.comment.getText().toString();
                System.out.println(expense.getExpenseID()+"inside onclick");
                updateExpenseItem(name,amt,date,category,comment,pos,key);

            }
        });

    }

    //Update dialog box
    @SuppressLint("ResourceType")
    private void updateExpenseItem(String name, String amt, String date, String category, String comment, int position, String exid){

        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this.context);
        LayoutInflater inflater1 = LayoutInflater.from(this.context);
        View view1 = inflater1.inflate(R.layout.update_expense,null);
        dialog.setView(view1);

        edtname=view1.findViewById(R.id.editname);
        edtamount=view1.findViewById(R.id.editamount);
        edtdate=view1.findViewById(R.id.editdate);
        edtcategory=view1.findViewById(R.id.editcategory);
        edtcomment=view1.findViewById(R.id.editcomment);
        individualBtn=view1.findViewById(R.id.updateIndividual_button);
        groupButton = view1.findViewById(R.id.updateGroup_button);
        updateGroupDropdown=view1.findViewById(R.id.Update_GroupInputLayout);
        adapterItems = new ArrayAdapter<>(view1.getContext(), R.layout.list_categories,categories);

        edtname.setText(name);
        edtamount.setText(amt);
        edtdate.setText(date);
        edtcategory.setText(category);
        edtcomment.setText(comment);
        edtcategory.setAdapter(adapterItems);

        updatebutton=view1.findViewById(R.id.update_button);
        deletebutton=view1.findViewById(R.id.delete_button);

        androidx.appcompat.app.AlertDialog dialogm = dialog.create();

        groupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGroupDropdown.setVisibility(0);
            }
        });


        individualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateGroupDropdown.getVisibility()==0){
                    updateGroupDropdown.setVisibility(8);
                }
            }
        });

        //UPDATE BUTTON ONCLICK LISTENER

       updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String n=edtname.getText().toString();
                String a =edtamount.getText().toString();
                Integer am = Integer.parseInt(a);
                String d = edtdate.getText().toString();
                String c = edtcategory.getText().toString();
                String co = edtcomment.getText().toString();
                ExpenseData exp = new ExpenseData(n,am,d,c,co);
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Expenses").child(exid).setValue(exp).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        notifyDataSetChanged();
                        Toast.makeText(view.getContext(),"updated",Toast.LENGTH_SHORT).show();
                        dialogm.dismiss();
                    }
                });
            }
        });


       //DELETE BUTTON ONCLICK LISTENER

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Expenses").child(exid).removeValue();
                notifyItemRemoved(position);
                dialogm.dismiss();
            }
        });

        dialogm.show();
    }

    @Override
    public int getItemCount() {
        return expensedata.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

       // TextView category, categoryValue;
       // public MyViewHolder(@NonNull View itemView) {
       //     super(itemView);
        //    category = itemView.findViewById(R.id.category);
          //  categoryValue=itemView.findViewById(R.id.categoryValue);
        //}

        TextView name, amount, date, category, comment;
        private MaterialCardView parent;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.namelist);
            amount=itemView.findViewById(R.id.amountlist);
            date=itemView.findViewById(R.id.datelist);
            category=itemView.findViewById(R.id.categorylist);
            comment=itemView.findViewById(R.id.commentlist);
            parent=itemView.findViewById(R.id.parent_cardView);
        }

    }
}
