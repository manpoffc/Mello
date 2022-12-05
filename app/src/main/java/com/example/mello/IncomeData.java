package com.example.mello;

public class IncomeData {
    String Name;
    Integer Amount;
    String Date;
    String Category;
    String Comment;


    public IncomeData(String name, Integer amount, String date, String category, String comment) {
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

    public void setAmount(Integer amount) {
        Amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public IncomeData() {};

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
