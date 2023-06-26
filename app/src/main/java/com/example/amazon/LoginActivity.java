package com.example.amazon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazon.Model.Users;
import com.example.amazon.prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity
{
    private TextView Admins,NotAdmins;
    private EditText Email,Password;
    private Button Login,gotoRegister;

    private FirebaseAuth mAuth;
    private  String ParentDBname="Users";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);
        Email=(EditText)findViewById(R.id.Login_Email);
        Password=(EditText)findViewById(R.id.Login_Password);
        Login=(Button)findViewById(R.id.Login_button);
        gotoRegister=(Button)findViewById(R.id.gotologin_activity_button);
        Admins=(TextView)findViewById(R.id.Admin);
        NotAdmins=(TextView)findViewById(R.id.NotAdmin);


        Admins.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
              Login.setText("Login as Admin");
              Admins.setVisibility(View.VISIBLE);
              NotAdmins.setVisibility(View.INVISIBLE);
              ParentDBname="Users";
            }
        });

        NotAdmins.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Login.setText("Login User");
                Admins.setVisibility(View.INVISIBLE);
                NotAdmins.setVisibility(View.VISIBLE);
                ParentDBname="Users";
            }

        });



        gotoRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });


        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
             Login();
            }
        });



    }

    private void Login()
    {

        String email=Email.getText().toString();
        String password=Password.getText().toString();


        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(),"please Provide Password",Toast.LENGTH_LONG).show();
        }

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(),"please Provide Valid Email",Toast.LENGTH_LONG).show();
        }
        else
        {
            AllowAccessToUserAccount(email,password);

        }


    }

/*
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }


*/


    private void AllowAccessToUserAccount(String email, String password)
    {

        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevelant.UserPhoneKey, email);
            Paper.book().write(Prevelant.UserPasswordKey, password);
        }


        final DatabaseReference RootRef;

       RootRef= FirebaseDatabase.getInstance().getReference();

       RootRef.addListenerForSingleValueEvent(new ValueEventListener()
       {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot)
           {
                   if((snapshot.child(ParentDBname).child(email).exists()))
                   {
                       Users  usersData=snapshot.child(ParentDBname).child(email).getValue(Users.class);

                       if(usersData.getPhone().equals(email))
                       {

                           if(usersData.getPassword().equals(password))
                           {

                               if (ParentDBname.equals("Users"))
                               {
                                   Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                                   Prevelant.CurrentOnlineUser=usersData;
                                   startActivity(intent);

                               }

                               /*
                               if (ParentDBname.equals("Users"))
                               {
                                   Intent intent = new Intent(getApplicationContext(),AdminCategoryActivity.class);
                                   startActivity(intent);

                               }


                                if (ParentDBname.equals("Users") )
                               {
                                   Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                   Prevelant.CurrentOnlineUser=usersData;
                                   startActivity(intent);
                               }
                             */
                           }
                       }



                   }
                   else
                   {
                       Toast.makeText(getApplicationContext(),"Account With this Phone  "+email+" not exists.",Toast.LENGTH_LONG).show();
                   }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error)
           {

           }

       });


    }




}