package com.example.ysh.hyena;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ysh.hyena.Fragment.TabActiviy;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

public class AddActivity extends AppCompatActivity {

    private StorageReference mStorageRef;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference mdatabaseReference;

    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    public static final int REQUEST_CODE = 10;

    private Spinner spinner;
    private int spinner_number;

    private EditText et_title;
    private EditText et_price;
    private EditText et_phone;
    private ImageView iv_image;

    private String upload_title;
    private String upload_price;
    private String upload_phone;
    private String upload_category;
    private String upload_date;
    private String upload_time;
    private long upload_reg;
    private String email;
    private Uri imgUri;
    private String url;

    Hashtable<String, String >context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        spinner = findViewById(R.id.spinner_field);
        iv_image = findViewById(R.id.iv_image);

        et_title = findViewById(R.id.et_title);
        et_price = findViewById(R.id.et_price);
        et_phone = findViewById(R.id.et_phone);

        if (user != null) {
            email = user.getEmail();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    upload_category = null;
                } else {
                    upload_category = parent.getItemAtPosition(position).toString();
                    spinner_number = position;
                    Toast.makeText(AddActivity.this, "" + spinner_number, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddActivity.this, "반드시 하나를 선택하세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                iv_image.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void btn_image(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select umage"), REQUEST_CODE);
    }

    public void btn_upload(View view) {

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        upload_date = df.format(date);
        upload_reg = System.currentTimeMillis();
        upload_time = String.valueOf(upload_reg);
        upload_title = et_title.getText().toString();
        upload_price = et_price.getText().toString();
        upload_phone = et_phone.getText().toString();

        if (upload_title == null) {
            Toast.makeText(AddActivity.this, "제목을 입력하세요!", Toast.LENGTH_SHORT).show();
        } else if (upload_price == null) {
            Toast.makeText(AddActivity.this, "가격을 입력하세요!", Toast.LENGTH_SHORT).show();
        } else if (upload_phone == null) {
            Toast.makeText(AddActivity.this, "핸드폰 번호를 입력하세요!", Toast.LENGTH_SHORT).show();
        } else if (upload_category == null) {
            Toast.makeText(AddActivity.this, "카테고리를 선택하세요!", Toast.LENGTH_SHORT).show();
        } else if(imgUri == null){
            Toast.makeText(getApplicationContext(), "이미지를 선택하세요", Toast.LENGTH_SHORT).show();
        } else {

            final StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));

            final UploadTask uploadTask = ref.putFile(imgUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        url = task.getResult().toString();

                        context = new Hashtable<>();
                        context.put("email", email);
                        context.put("title", upload_title);
                        context.put("price", upload_price);
                        context.put("category", upload_category);
                        context.put("phone", upload_phone);
                        context.put("date", upload_date);
                        context.put("time", upload_time);
                        context.put("imageUrl", url);
                        context.put("key", user.getUid());
                        Toast.makeText(AddActivity.this, "타이틀 : " + upload_title
                                        + "\n가격 : " + upload_price
                                        + "\n전화번호 : " + upload_phone
                                        + "\n카테고리 : " + upload_category
                                        + "\n올린날짜 : " + upload_date
                                , Toast.LENGTH_SHORT).show();

                        if (spinner_number == 1) {
                            mdatabaseReference = database.getReference().child("product").child(upload_date);
                        } else if (spinner_number == 2) {
                            mdatabaseReference = database.getReference().child("room").child(upload_date);
                        } else if (spinner_number == 3) {
                            mdatabaseReference = database.getReference().child("book").child(upload_date);
                        }

                        mdatabaseReference.setValue(context);
                        Toast.makeText(getApplicationContext(), "업로드 성공", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
  
/*
    public void btn_upload(View view) {

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        upload_date = df.format(date);
        upload_reg = System.currentTimeMillis();
        upload_time = String.valueOf(upload_reg);
        upload_title = et_title.getText().toString();
        upload_price = et_price.getText().toString();
        upload_phone = et_phone.getText().toString();

        if (upload_title == null) {
            Toast.makeText(AddActivity.this, "제목을 입력하세요!", Toast.LENGTH_SHORT).show();
        } else if (upload_price == null) {
            Toast.makeText(AddActivity.this, "가격을 입력하세요!", Toast.LENGTH_SHORT).show();
        } else if (upload_phone == null) {
            Toast.makeText(AddActivity.this, "핸드폰 번호를 입력하세요!", Toast.LENGTH_SHORT).show();
        } else if (upload_category == null) {
            Toast.makeText(AddActivity.this, "카테고리를 선택하세요!", Toast.LENGTH_SHORT).show();
        } else if(imgUri == null){
            Toast.makeText(getApplicationContext(), "이미지를 선택하세요", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog dialog = new ProgressDialog(AddActivity.this);
            dialog.setTitle("게시중입니다.....");
            dialog.show();

            StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));

            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Task<Uri> downUrl=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    url = downUrl.getResult().toString();


                    Log.i("url:",downUrl.getResult().toString());
                    // url = taskSnapshot.getStorage().getDownloadUrl().toString();
                   // url = taskSnapshot.getUploadSessionUri().toString();

                    context = new Hashtable<>();
                    context.put("email", email);
                    context.put("title", upload_title);
                    context.put("price", upload_price);
                    context.put("category", upload_category);
                    context.put("phone", upload_phone);
                    context.put("date", upload_date);
                    context.put("time", upload_time);
                    context.put("imageUrl", url);
                    context.put("key", user.getUid());
                    Toast.makeText(AddActivity.this, "타이틀 : " + upload_title
                                    + "\n가격 : " + upload_price
                                    + "\n전화번호 : " + upload_phone
                                    + "\n카테고리 : " + upload_category
                                    + "\n올린날짜 : " + upload_date
                            , Toast.LENGTH_SHORT).show();

                    if (spinner_number == 1) {
                        mdatabaseReference = database.getReference().child("product").child(upload_date);
                    } else if (spinner_number == 2) {
                        mdatabaseReference = database.getReference().child("room").child(upload_date);
                    } else if (spinner_number == 3) {
                        mdatabaseReference = database.getReference().child("book").child(upload_date);
                    }

                    mdatabaseReference.setValue(context);
                    Toast.makeText(getApplicationContext(), "업로드 성공", Toast.LENGTH_SHORT).show();

                    finish();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("업로드" + (int)progress);
                        }
                    });
        }

    }
*/

}
