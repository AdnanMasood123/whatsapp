package com.example.amazon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{

    private EditText Name,Email,Password;
    private Button Register;

    private FirebaseAuth mAuth;
    private String currentUserId;
   // private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
       //  currentUserId=mAuth.getCurrentUser().getUid();
       // RootRef= FirebaseDatabase.getInstance().getReference().child("Users");




        Name=(EditText)findViewById(R.id.Register_UserName);
        Email=(EditText)findViewById(R.id.Register_Email);
        Password=(EditText)findViewById(R.id.Login_Password);
        Register=(Button)findViewById(R.id.Register_button);



        Register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Register();
            }
        });


    }

    private void Register()
    {
        String name=Name.getText().toString();
        String email=Email.getText().toString();
        String password=Password.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(getApplicationContext(),"please Provide UserName",Toast.LENGTH_LONG).show();
        }

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
                ValidatePhoneNumver(name,email,password);

        }



    }


    private void ValidatePhoneNumver(String name, String email, String password)
    {
           final DatabaseReference RootRef;
           RootRef=FirebaseDatabase.getInstance().getReference();

           RootRef.addListenerForSingleValueEvent(new ValueEventListener()
           {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot)
               {
                         if(!(snapshot.child("Users").child(email).exists()))
                         {
                             HashMap<String,Object>  UsersMap=new HashMap<>();
                             UsersMap.put("uid",currentUserId);
                             UsersMap.put("name",name);
                             UsersMap.put("phone",email);
                             UsersMap.put("password",password);

                             RootRef.child("Users")
                                     .child(email)
                                     .updateChildren(UsersMap)
                                     .addOnCompleteListener(new OnCompleteListener<Void>()
                                     {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task)
                                         {
                                              if(task.isSuccessful())
                                              {
                                                  SendToLoginActivity();
                                                  Toast.makeText(getApplicationContext(),"Account createc successfully",Toast.LENGTH_LONG).show();
                                              }
                                              else
                                              {
                                                  String error=task.getException().toString();
                                                  Toast.makeText(getApplicationContext(),""+error,Toast.LENGTH_LONG).show();
                                              }
                                         }

                                     });


                         }
                         else
                         {
                             Toast.makeText(getApplicationContext(),"This "+email+" already exists.",Toast.LENGTH_LONG).show();
                         }

               }

               @Override
               public void onCancelled(@NonNull DatabaseError error)
               {

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

}