package in.ac.iiitd.dhcs_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ImageDownload extends AppCompatActivity {


    String[] path = {"1.jpg", "2.jpeg", "3.jpg","4.jpg"};
    String[] down = new String[4];
    int count = 0;
    private String TAG = "Sample";

    final SharedClass sharedObject = getObject();
    int score=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_download);

        //FirebaseStorage storage = FirebaseStorage.getInstance();
            try {
                download();
            } catch (IOException e) {
                e.printStackTrace();
            }

        final Button goToHonk = findViewById(R.id.button2);
        goToHonk.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Intent intent = new Intent(ImageDownload.this, HonkPlayActivity.class);
                //startActivity(intent);
            }

        });



        }
        public void download() throws IOException {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            //StorageReference imageref= storageReference.child("Images");
            for (int i = 0; i < 4; i++) {
                StorageReference pic = storageReference.child(path[i]);

                final File localFile = File.createTempFile("images", ".png");

                pic.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Log.i(TAG, localFile.getAbsolutePath());
                        down[count] = localFile.getAbsolutePath();
                        count++;
                        // Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        //ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        //imageView.setImageBitmap(myBitmap);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
                //Bitmap bmImg = BitmapFactory.decodeFile(localFile.getAbsolutePath());



            }
        }

/*
    public void startNewActivity(SharedClass sharedObject) {
        Intent intent;

       // if(sharedObject.currentLevel == 1) {
            intent = new Intent(this, HonkPlayActivity.class);
            intent.putExtra("sharedObject", sharedObject);
            Log.e(TAG, "displayName: " + getDisplayName());
            intent.putExtra("id", getAccountID());
            intent.putExtra("name", getDisplayName());
            intent.putExtra("score", getScore());
            // intent.putExtra("currentLevel", sharedObject.currentLevel);
            //intent.putExtra("down", down);
            startActivity(intent);
       // }

    }*/




    private SharedClass getObject() {
        Intent i = getIntent();
        return (SharedClass) i.getSerializableExtra("sharedObject");
    }

    private String getAccountID() {
        return getIntent().getStringExtra("id");
    }

    private String getDisplayName() {
        return getIntent().getStringExtra("name");
    }

    private int getScore() {
        return getIntent().getIntExtra("score",score);
    }


}