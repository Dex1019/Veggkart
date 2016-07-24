package com.example.prince.vegkart;

/**
 * Created by aniPC on 16-Jun-16.
 */
public class Product {




    public String proName;
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
        this.proName = _productName;
        this.description = _discription;
        this.price = _price;
        this.quantity = "0";

    }

    public void setProname(String proname) {
        this.proName = proname;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

