package com.example.amazon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductsDetailsActivity extends AppCompatActivity
{
    public String date,description,image,pid,pname,price,time,postkey;
    TextView ProductName,ProductPrice,ProductDescripion,ProductDate,ProductTime;
    ImageView ProductImage;
    private DatabaseReference ProductsRef;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details);

        ProductImage=(ImageView)findViewById(R.id.Details_Product_Image);
        ProductName=(TextView) findViewById(R.id.Details_Product_Name);
        ProductPrice=(TextView) findViewById(R.id.Details_Product_Price);
        ProductDescripion=(TextView) findViewById(R.id.Details_Product_Description);
        ProductDate=(TextView) findViewById(R.id.Details_Product_Date);
        ProductTime=(TextView) findViewById(R.id.Details_Product_Time);

        date=(String) getIntent().getExtras().get("date");
        postkey=(String) getIntent().getExtras().get("postkey");
        time=(String) getIntent().getExtras().get("time");
        pname=(String) getIntent().getExtras().get("name");
       // image=(String) getIntent().getExtras().get("image");
        price=(String) getIntent().getExtras().get("price");
        description=(String) getIntent().getExtras().get("description");

        ProductsRef= FirebaseDatabase.getInstance().getReference().child("Product Information").child(postkey);


        ProductName.setText(""+pname);
        ProductPrice.setText(price);
        ProductDescripion.setText("Features"+description);
        ProductDate.setText("Added On  "+date);
        ProductTime.setText("Added At  "+time);

        setProductData();

    }

    private void setProductData()
    {
        ProductsRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                  if(snapshot.exists())
                  {
                      String Image=snapshot.child("image").getValue().toString();
                      Picasso.with(ctx).load(Image).into(ProductImage);
                  }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

    }
}