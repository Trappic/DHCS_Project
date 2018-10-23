package in.ac.iiitd.dhcs_project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class HonkPlayActivity extends AppCompatActivity {
    private static final String TAG = "HonkPlayActivity";

    private Integer images[] = {R.drawable.aachen_000000_000019_gtfine_color,R.drawable.aachen_000006_000019_gtfine_color,R.drawable.aachen_000018_000019_original, R.drawable.aachen_000036_000019_original, R.drawable.aachen_000098_000019_original, R.drawable.aachen_000105_000019_original, R.drawable.aachen_000156_000019_original, R.drawable.aachen_000164_000019_original, R.drawable.aachen_000173_000019_original,
            R.drawable.bochum_000000_006026_original};
           /* , R.drawable.bochum_000000_026634_original, R.drawable.bochum_000000_027057_original, R.drawable.bremen_000001_000019_original, R.drawable.bremen_000004_000019_original, R.drawable.bremen_000025_000019_original, R.drawable.bremen_000030_000019_original, R.drawable.bremen_000031_000019_original,
            R.drawable.bremen_000035_000019_original, R.drawable.bremen_000041_000019_original, R.drawable.bremen_000049_000019_original, R.drawable.bremen_000053_000019_original, R.drawable.bremen_000065_000019_original, R.drawable.bremen_000073_000019_original, R.drawable.bremen_000157_000019_original, R.drawable.cologne_000021_000019_original,
            R.drawable.cologne_000030_000019_original, R.drawable.cologne_000032_000019_original, R.drawable.darmstadt_000012_000019_original, R.drawable.dusseldorf_000011_000019_original, R.drawable.dusseldorf_000016_000019_original, R.drawable.dusseldorf_000088_000019_original, R.drawable.dusseldorf_000133_000019_original, R.drawable.erfurt_000004_000019_original,
            R.drawable.erfurt_000014_000019_original, R.drawable.erfurt_000019_000019_original, R.drawable.erfurt_000031_000019_original, R.drawable.erfurt_000045_000019_original, R.drawable.erfurt_000058_000019_original};
*/
    private Integer labels[] = {R.drawable.aachen_000018_000019_label, R.drawable.aachen_000036_000019_label, R.drawable.aachen_000098_000019_label, R.drawable.aachen_000105_000019_label, R.drawable.aachen_000156_000019_label, R.drawable.aachen_000164_000019_label, R.drawable.aachen_000173_000019_label,
            R.drawable.bochum_000000_006026_label, R.drawable.bochum_000000_026634_label, R.drawable.bochum_000000_027057_label, R.drawable.bremen_000001_000019_label, R.drawable.bremen_000004_000019_label, R.drawable.bremen_000025_000019_label, R.drawable.bremen_000030_000019_label, R.drawable.bremen_000031_000019_label,
            R.drawable.bremen_000035_000019_label, R.drawable.bremen_000041_000019_label, R.drawable.bremen_000049_000019_label, R.drawable.bremen_000053_000019_label, R.drawable.bremen_000065_000019_label, R.drawable.bremen_000073_000019_label, R.drawable.bremen_000157_000019_label, R.drawable.cologne_000021_000019_label,
            R.drawable.cologne_000030_000019_label, R.drawable.cologne_000032_000019_label, R.drawable.darmstadt_000012_000019_label, R.drawable.dusseldorf_000011_000019_label, R.drawable.dusseldorf_000016_000019_label, R.drawable.dusseldorf_000088_000019_label, R.drawable.dusseldorf_000133_000019_label, R.drawable.erfurt_000004_000019_label,
            R.drawable.erfurt_000014_000019_label, R.drawable.erfurt_000019_000019_label, R.drawable.erfurt_000031_000019_label, R.drawable.erfurt_000045_000019_label, R.drawable.erfurt_000058_000019_label};

    private String assets[] = {"aachen_000000_000019_gtfine_polygons.json","aachen_000006_000019_gtfine_polygons.json","aachen_000018_000019.json", "aachen_000036_000019.json", "aachen_000098_000019.json", "aachen_000105_000019.json", "aachen_000156_000019.json", "aachen_000164_000019.json", "aachen_000173_000019.json",
            "bochum_000000_006026.json", "bochum_000000_026634.json", "bochum_000000_027057.json", "bremen_000001_000019.json", "bremen_000004_000019.json", "bremen_000025_000019.json", "bremen_000030_000019.json", "bremen_000031_000019.json",
            "bremen_000035_000019.json", "bremen_000041_000019.json", "bremen_000049_000019.json", "bremen_000053_000019.json", "bremen_000065_000019.json", "bremen_000073_000019.json", "bremen_000157_000019.json", "cologne_000021_000019.json",
            "cologne_000030_000019.json", "cologne_000032_000019.json", "darmstadt_000012_000019.json", "dusseldorf_000011_000019.json", "dusseldorf_000016_000019.json", "dusseldorf_000088_000019.json", "dusseldorf_000133_000019.json", "erfurt_000004_000019.json",
            "erfurt_000014_000019.json", "erfurt_000019_000019.json", "erfurt_000031_000019.json", "erfurt_000045_000019.json", "erfurt_000058_000019.json"};



    String jsonString = readFromFile("");



    private int currImage;

    Random rand;

    final private int totalImages = 10;
    private String answers[] = new String[totalImages];
    private int numOfClicks = 0;
    private int correctAnswers = 0;

    private MediaPlayer mp;

    private String accountID;
    private String displayName;
    private int score = 0;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honk_play);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();

//        databaseReference.setValue("Hello World!");
        final SharedClass obj = getObject();

        final TextView scoreText = findViewById(R.id.scoreTextBox1);
        scoreText.setText("Score: " + Integer.toString(obj.score));

        rand = new Random();
        currImage = rand.nextInt(totalImages - 1);

        setAnswers();
        setCurrentImage();

        class ProgressBarTimer extends CountDownTimer {
            private ProgressBar progressBar;
            private int finish = 0;
            private ProgressBarTimer(long millisInFuture, long countDownInterval, ProgressBar progressBar) {
                super(millisInFuture, countDownInterval);
                this.progressBar = progressBar;
            }

            @Override
            public void onTick(long millisUntilFinished) {

                int progress = (int) (millisUntilFinished/100);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                finish = 1;
                mp = null;
                accountID = getIntent().getStringExtra("id");
                displayName = getIntent().getStringExtra("name");
                score = getIntent().getIntExtra("score", obj.score);
                if (correctAnswers >= obj.difficultyLevel+1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HonkPlayActivity.this);
                    builder.setMessage("Your Total Score:" + Integer.toString(obj.score))
                            .setCancelable(false)
                            .setPositiveButton("Next Level", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                    startQuestionActivity(obj);
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setCanceledOnTouchOutside(false);
                    alert.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);
                    alert.show();
                    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(Color.parseColor("#448AFF"));
//                    displayName = getIntent().getStringExtra("displayName");
//                    databaseReference.child("users").child(displayName).setValue(obj.score);
                }
                else {
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(HonkPlayActivity.this).create();
                    alertDialog.setTitle("Time's Up");
                    alertDialog.setMessage("Sorry, You haven't done enough to go to the next level!");
                    alertDialog.setIcon(R.drawable.wrong);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);
                    alertDialog.setButton("HOME", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(HonkPlayActivity.this, HomeActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                    Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setTextColor(Color.parseColor("#448AFF"));
                    Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(Color.parseColor("#448AFF"));
                    databaseReference.child("users").child(accountID).child("score").setValue(obj.score);
                }
            }
        }

        final ProgressBarTimer progressBarTimer;
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(100);
        progressBarTimer = new ProgressBarTimer(10000, 1, progressBar);
        final Button honkButton = findViewById(R.id.honkButton);
        honkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numOfClicks++;
                mp = MediaPlayer.create(HonkPlayActivity.this, R.raw.car_honk);
                mp.start();
            }
        });


        final Button rotateButton = findViewById(R.id.go);
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                accountID = getIntent().getStringExtra("id");
                displayName = getIntent().getStringExtra("name");
                score = getIntent().getIntExtra("score", obj.score);
                if (String.valueOf(numOfClicks).equals(answers[currImage])) {
                    setNewImage();
//                    addToast("Correct Answer");
                    correctAnswers++;
                    obj.incrementScore();
                    scoreText.setText("Score: " + Integer.toString(obj.score));

                }

                else {
                    progressBarTimer.cancel();
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(HonkPlayActivity.this).create();
                    alertDialog.setTitle("Wrong Answer");
                    alertDialog.setMessage("Sorry, You Lost!");
                    alertDialog.setIcon(R.drawable.wrong);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);
                    alertDialog.setButton("HOME", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(HonkPlayActivity.this, HomeActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                    Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setTextColor(Color.parseColor("#448AFF"));
                    Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(Color.parseColor("#448AFF"));
                    databaseReference.child("users").child(accountID).child("score").setValue(obj.score);
                }
                numOfClicks = 0;
                setCurrentImage();
                if (mp != null) {
                    mp.release();
                }
            }
        });

        progressBarTimer.start();



    }

    @Override
    public void onBackPressed() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ExitActivity.exitApplicationAndRemoveFromRecent(HonkPlayActivity.this);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCanceledOnTouchOutside(false);
            alert.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);
            alert.show();
            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(Color.parseColor("#448AFF"));
            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(Color.parseColor("#448AFF"));
        } catch (WindowManager.BadTokenException bte) {
            bte.printStackTrace();
        }
    }

    private SharedClass getObject() {
        Intent i = getIntent();
        return (SharedClass) i.getSerializableExtra("sharedObject");
    }

    public void startQuestionActivity(SharedClass sharedObject) {
        Intent intent;
        intent = new Intent(this, QuestionPageActivity.class);
        intent.putExtra("sharedObject", sharedObject);
        intent.putExtra("id", accountID);
        intent.putExtra("name", displayName);
        intent.putExtra("score", score);
        startActivity(intent);

    }

    private void setNewImage() {
        int prevcurrImage = currImage;
        while (prevcurrImage == currImage) {
            currImage = rand.nextInt(totalImages - 1);
        }
    }

    private void setCurrentImage() {
        final ImageView imageView = findViewById(R.id.imageDisplay);
//        imageView.setImageResource(images[currImage]);
//        Log.i(TAG, storageReference.toString());
        Glide.with(this)
                .load(images[currImage])
                .into(imageView);
    }

    private void addToast(String value) {
        CharSequence text = value;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();
    }

    //REmoving harcoding functions start from below here
    private void setAnswers() {
        for (int i = 0; i < totalImages; i++) {
            answers[i] = String.valueOf(getCarNum(assets[i]));
        }
    }

    public int getCarNum(String s) {
        int carNum = 0;
        JSONObject obj = null;
        try {
            obj = new JSONObject(loadJSONFromAsset(s));
            JSONArray m_jArry = obj.getJSONArray("objects");
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Log.d("Details-->", jo_inside.getString("label"));
                String label_value = jo_inside.getString("label");
                if (label_value.equals("car")) {
                    carNum++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return carNum;
    }

    public String loadJSONFromAsset(String s) {
        String json = null;
        try {
            InputStream is = getAssets().open(s);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public static String readFromFile(String path) {
        String ret = "";
        try {
            InputStream inputStream = new FileInputStream(new File(path));

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("FileToJson", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("FileToJson", "Can not read file: " + e.toString());
        }
        return ret;
    }

/*
    private String stringToJson(String jsonString) {

        try {

            JSONArray jarray = new JSONArray(jsonString);
        } catch (Exception ex) {
            Log.i("Json Error", ex.getMessage());
        }
    }*/

}
