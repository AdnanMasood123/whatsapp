package com.example.amazon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("ALL")
public class SettingsActivity extends AppCompatActivity
{
    private CircleImageView ProfileImage;
    private EditText FullName,Phone,Address;
    private Button Save;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private StorageReference ProfileImageRef;

    private String Name,phone,address;
    private static final int  galleryPic=1;
    Uri ImageUri;
    private  String SaveCurrentDate,SaveCurrentTime,RandomKey,DownloadImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth= FirebaseAuth.getInstance();
        RootRef=FirebaseDatabase.getInstance().getReference().child("Profile Data");
        ProfileImageRef= FirebaseStorage.getInstance().getReference().child("Profile Images");

        ProfileImage=(CircleImageView)findViewById(R.id.Setup_Profile_Image);
        FullName=(EditText) findViewById(R.id.Setup_Profile_Name);
        Phone=(EditText) findViewById(R.id.Setup_Profile_Country);
        Address=(EditText) findViewById(R.id.Setp_Profile_FullName);
        Save=(Button) findViewById(R.id.Setup_Profile_button);




        Save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                 StoreProfileImageToStorage();
            }
        });


        ProfileImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,galleryPic);
            }

        });





    }


    private void SaveInfo()
    {
           Name=FullName.getText().toString();
           phone=Phone.getText().toString();
           address=Address.getText().toString();


           if(TextUtils.isEmpty(Name))
           {
               Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
           }

           else if(TextUtils.isEmpty(phone))
           {
            Toast.makeText(getApplicationContext(), "Please Enter phone number", Toast.LENGTH_SHORT).show();
           }

           else if(TextUtils.isEmpty(address))
           {
            Toast.makeText(getApplicationContext(), "Please Enter Address", Toast.LENGTH_SHORT).show();
           }
           else
           {

               HashMap<Object,String>  ProfileMap=new HashMap<>();
               ProfileMap.put("phone ", phone);
               ProfileMap.put("name ", Name);
               ProfileMap.put("address ", address);
               ProfileMap.put("profileimage ", DownloadImageUri);

               RootRef.child(phone)
                       .setValue(ProfileMap)
                       .addOnCompleteListener(new OnCompleteListener<Void>()
                       {
                   @Override
                   public void onComplete(@NonNull Task<Void> task)
                   {
                           if(task.isSuccessful())
                           {
                               Toast.makeText(getApplicationContext(), "Information Save Successfully", Toast.LENGTH_SHORT).show();
                               DisplayProfileImage();
                           }
                           else
                           {
                               String message=task.getException().toString();
                               Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
                           }

                   }

               });


           }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==galleryPic && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            ProfileImage.setImageURI(ImageUri);


        }

    }
    private void StoreProfileImageToStorage()
    {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        SaveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        SaveCurrentTime = currentTime.format(calendar.getTime());

        RandomKey=SaveCurrentDate+SaveCurrentTime;


        final StorageReference filePath = ProfileImageRef.child(ImageUri.getLastPathSegment() + RandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(getApplicationContext(), "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {

                            Toast.makeText(getApplicationContext(), ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }


                        DownloadImageUri = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            DownloadImageUri = task.getResult().toString();

                            Toast.makeText(getApplicationContext(), "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveInfo();
                        }
                    }
                });
            }
        });
    }



    private void DisplayProfileImage()
    {

        RootRef.child(phone).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                  if(snapshot.hasChild("profileimage"))
                  {
                      String Image=snapshot.getValue().toString();

                      Picasso.with(SettingsActivity.this).load(Image).into(ProfileImage);


                  }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }




}








