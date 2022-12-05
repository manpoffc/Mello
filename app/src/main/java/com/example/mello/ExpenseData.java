package com.example.mello;

import java.util.Date;

public class ExpenseData {

    String Name;
    Integer Amount;
    String Date;
    String Category;
    String Comment;


    public String getExpenseID() {
        return ExpenseID;
    }

    public void setExpenseID(String expenseID) {
        ExpenseID = expenseID;
    }

    String ExpenseID;



    public ExpenseData(String name, Integer amount, String date, String category, String comment) {
        Name = name;
        Amount = amount;
        Date = date;
        Category = category;
        Comment = comment;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getAmount() {
        return Amount;
    }

    public ExpenseData(){};

    public void setAmount(Integer amount) {
        Amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
