package com.example.amazon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity
{

    private ImageView shoes,shirts,laptops,watches;
    private  ImageView headphones,tshirts,coats,purse;
    private  ImageView phones,femaleDressses,hats,glasses;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);


        shirts=(ImageView)findViewById(R.id.TShirts);
        laptops=(ImageView)findViewById(R.id.Laptop);
        watches=(ImageView)findViewById(R.id.Watches);
        headphones=(ImageView)findViewById(R.id.HeadPhones);
        tshirts=(ImageView)findViewById(R.id.TShirts);
        coats=(ImageView)findViewById(R.id.Dress);
        purse=(ImageView)findViewById(R.id.Purse);
        phones=(ImageView)findViewById(R.id.Phones);
        femaleDressses=(ImageView)findViewById(R.id.Female_Dress);
        hats=(ImageView)findViewById(R.id.Hats);
        glasses=(ImageView)findViewById(R.id.Glasses);
        shoes=(ImageView)findViewById(R.id.Shoes);


       shoes.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {
               Intent intent=new Intent(getApplicationContext(),AdminAddProductActivity.class);
               intent.putExtra("Category","shoes");
               startActivity(intent);
           }
       });


        shirts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("Category","shirts");
                startActivity(intent);
            }
        });



        laptops.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("Category","laptops");
                startActivity(intent);
            }
        });




        watches.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("Category","watches");
                startActivity(intent);
            }
        });



        headphones.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("Category","headphones");
                startActivity(intent);
            }
        });



        tshirts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("Category","tshirts");
                startActivity(intent);
            }
        });



        coats.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("Category","coats");
                startActivity(intent);
            }
        });



        purse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("Category","purse");
                startActivity(intent);
            }
        });



        phones.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("Category","phones");
                startActivity(intent);
            }
        });



        femaleDressses.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("Category","female dress");
                startActivity(intent);
            }
        });



        hats.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("Category","hats");
                startActivity(intent);
            }
        });



        glasses.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),AdminAddProductActivity.class);
                intent.putExtra("Category","glasses");
                startActivity(intent);
            }
        });

    }
}