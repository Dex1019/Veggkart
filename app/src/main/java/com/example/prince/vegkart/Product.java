package com.example.prince.vegkart;

/**
 * Created by aniPC on 16-Jun-16.
 */
public class Product {




    public String productName;
    public String description;
    public String price;
    public String quantity;


    //Cunstructor
    public Product()
    {
        this.quantity = "0";

    }

    public Product(String _productName, String _discription, String _price)
    {
        this.productName = _productName;
        this.description = _discription;
        this.price = _price;
        this.quantity = "0";

    }

    //function to fetch all data
    /*public TableQuery<Product> FetchAllData()
    {
        var db = bl.openConnection();
        var table = db.Table<Product>();
        return table;
    }

    public string insertRecord()
    {
        try
        {
            var db = bl.openConnection();
            bl.creatDB();
            bl.createTable();
            db.Insert(this);
            return "record added";

        }
        catch (Exception e) { return "error "; }
    }*/
}

