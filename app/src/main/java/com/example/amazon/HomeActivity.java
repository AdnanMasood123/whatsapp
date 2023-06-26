package com.example.amazon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazon.Model.Products;
import com.example.amazon.Model.Users;
import com.example.amazon.prevelant.Prevelant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
{

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView PostList;
    private Toolbar mToolBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    private ImageButton go;
    private     CircleImageView Image;
    private Button logout;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef,ProductsInfoRef,ProfileImageRef;
    private String ParentDBname="Users";
    Context ctx;

    private String phone,password;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mAuth=FirebaseAuth.getInstance();

        mAuth=FirebaseAuth.getInstance();
        //currentUserId=mAuth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();
        ProductsInfoRef=FirebaseDatabase.getInstance().getReference().child("Product Information");
        ProfileImageRef=FirebaseDatabase.getInstance().getReference().child("Profile Data");

        mToolBar=(Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Home");

        drawerLayout=(DrawerLayout) findViewById(R.id.drawerlayout);
        Image=(CircleImageView) findViewById(R.id.nav_profile_image);
        //Like=(ImageButton)findViewById(R.id.dislike);
        // Comment=(ImageButton)findViewById(R.id.comment);
        //PutComment=(TextView)findViewById(R.id.dislike_post);
        PostList=(RecyclerView) findViewById(R.id.all_user_post);
        go=(ImageButton)findViewById(R.id.AddProduct);
        navigationView=(NavigationView) findViewById(R.id.navigation_view);


        actionBarDrawerToggle=new ActionBarDrawerToggle(HomeActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView.bringToFront();
        View view=navigationView.inflateHeaderView(R.layout.navigation_header);


        View headerView=navigationView.getHeaderView(0);
        TextView userNameText=headerView.findViewById(R.id.nav_Profile_name);
        userNameText.setText(Prevelant.CurrentOnlineUser.getName());






        PostList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        PostList.setLayoutManager(linearLayoutManager);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                ItemSelecter(item);
                return false;
            }
        });



        go.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(),AdminCategoryActivity.class);
                startActivity(intent);

            }
        });


    }






    private void SendToLoginActivity()
    {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void CheckUserExistence()
    {

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if ((snapshot.child(ParentDBname).child(phone).exists()))
                {
                    Users usersData = snapshot.child(ParentDBname).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {

                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }


                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }

        });
    }

    private void SendToRegisterActivity()
    {
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<Products,ProductsViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<Products, ProductsViewHolder>
                (Products.class,R.layout.display_products_layout,ProductsViewHolder.class,ProductsInfoRef)
        {

            @Override
            protected void populateViewHolder(ProductsViewHolder productsViewHolder, Products products, int i)
            {
                String PostKey = getRef(i).getKey();

                  productsViewHolder.setPname(products.getPname());;
                  productsViewHolder.setImage(products.getImage(),getApplicationContext());
                  productsViewHolder.setPrice(products.getPrice());
                  productsViewHolder.setDescription(products.getDescription());

                  productsViewHolder.view.setOnClickListener(new View.OnClickListener()
                  {
                      @Override
                      public void onClick(View view)
                      {
                           Intent intent=new Intent(getApplicationContext(),ProductsDetailsActivity.class);
                           intent.putExtra("postkey",PostKey);
                           intent.putExtra("name",products.getPname());
                           intent.putExtra("image",products.getImage());
                           intent.putExtra("price",products.getPrice());
                           intent.putExtra("description",products.getDescription());
                           intent.putExtra("date",products.getDate());
                           intent.putExtra("time",products.getTime());
                           startActivity(intent);
                      }
                  });


            }
        };

        PostList.setAdapter(recyclerAdapter);

    }

    public static  class  ProductsViewHolder extends  RecyclerView.ViewHolder
    {
        TextView ProductName,ProductPrice,ProductDescripion;
        ImageView ProductImage;

        View view;

        public ProductsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            view=itemView;
        }

        public void setPname(String pname)
        {
            ProductName=(TextView) view.findViewById(R.id.Details_Product_Name);
            ProductName.setText(pname);
        }

        public void setPrice(String price)
        {
            ProductPrice=(TextView) view.findViewById(R.id.Details_Product_Price);
            ProductPrice.setText("Price = "+price+ "$");
        }


        public void setDescription(String description)
        {
            ProductDescripion=(TextView) view.findViewById(R.id.Details_Product_Description);
            ProductDescripion.setText(description);
        }


        public void setImage(String image, Context ctx)
        {
            ProductImage=(ImageView) view.findViewById(R.id.Details_Product_Image);
            Picasso.with(ctx).load(image).into(ProductImage);
        }



    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void ItemSelecter(MenuItem item)
    {
        switch (item.getItemId())
        {
/*
            case R.id.nav_cart:

                break;

            case R.id.nav_orders:

                break;

            case R.id.nav_categories:
                Intent Mainintent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(Mainintent);
                break;

*/

            case R.id.nav_settings:
                Intent intent=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_Logout:
               mAuth.signOut();
                Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent1);
                break;

        }

    }








}