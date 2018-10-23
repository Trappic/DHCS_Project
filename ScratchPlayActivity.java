package in.ac.iiitd.dhcs_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cooltechworks.utils.BitmapUtils;
import com.cooltechworks.views.ScratchImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class ScratchPlayActivity extends AppCompatActivity {
    private final static String TAG = "ScratchPlayActivity";

    private Integer images[] = {R.drawable.aachen_000000_000019_gtfine_color,R.drawable.aachen_000006_000019_gtfine_color};
   /* ,R.drawable.aachen_000018_000019_original, R.drawable.aachen_000036_000019_original, R.drawable.aachen_000098_000019_original, R.drawable.aachen_000105_000019_original, R.drawable.aachen_000156_000019_original, R.drawable.aachen_000164_000019_original, R.drawable.aachen_000173_000019_original,
            R.drawable.bochum_000000_006026_original, R.drawable.bochum_000000_026634_original, R.drawable.bochum_000000_027057_original, R.drawable.bremen_000001_000019_original, R.drawable.bremen_000004_000019_original, R.drawable.bremen_000025_000019_original, R.drawable.bremen_000030_000019_original, R.drawable.bremen_000031_000019_original,
            R.drawable.bremen_000035_000019_original, R.drawable.bremen_000041_000019_original, R.drawable.bremen_000049_000019_original, R.drawable.bremen_000053_000019_original, R.drawable.bremen_000065_000019_original, R.drawable.bremen_000073_000019_original, R.drawable.bremen_000157_000019_original, R.drawable.cologne_000021_000019_original,
            R.drawable.cologne_000030_000019_original, R.drawable.cologne_000032_000019_original, R.drawable.darmstadt_000012_000019_original, R.drawable.dusseldorf_000011_000019_original, R.drawable.dusseldorf_000016_000019_original, R.drawable.dusseldorf_000088_000019_original, R.drawable.dusseldorf_000133_000019_original, R.drawable.erfurt_000004_000019_original,
            R.drawable.erfurt_000014_000019_original, R.drawable.erfurt_000019_000019_original, R.drawable.erfurt_000031_000019_original, R.drawable.erfurt_000045_000019_original, R.drawable.erfurt_000058_000019_original};
*/
    private Integer labels[] = {R.drawable.aachen_000018_000019_label, R.drawable.aachen_000036_000019_label, R.drawable.aachen_000098_000019_label, R.drawable.aachen_000105_000019_label, R.drawable.aachen_000156_000019_label, R.drawable.aachen_000164_000019_label, R.drawable.aachen_000173_000019_label,
            R.drawable.bochum_000000_006026_label, R.drawable.bochum_000000_026634_label, R.drawable.bochum_000000_027057_label, R.drawable.bremen_000001_000019_label, R.drawable.bremen_000004_000019_label, R.drawable.bremen_000025_000019_label, R.drawable.bremen_000030_000019_label, R.drawable.bremen_000031_000019_label,
            R.drawable.bremen_000035_000019_label, R.drawable.bremen_000041_000019_label, R.drawable.bremen_000049_000019_label, R.drawable.bremen_000053_000019_label, R.drawable.bremen_000065_000019_label, R.drawable.bremen_000073_000019_label, R.drawable.bremen_000157_000019_label, R.drawable.cologne_000021_000019_label,
            R.drawable.cologne_000030_000019_label, R.drawable.cologne_000032_000019_label, R.drawable.darmstadt_000012_000019_label, R.drawable.dusseldorf_000011_000019_label, R.drawable.dusseldorf_000016_000019_label, R.drawable.dusseldorf_000088_000019_label, R.drawable.dusseldorf_000133_000019_label, R.drawable.erfurt_000004_000019_label,
            R.drawable.erfurt_000014_000019_label, R.drawable.erfurt_000019_000019_label, R.drawable.erfurt_000031_000019_label, R.drawable.erfurt_000045_000019_label, R.drawable.erfurt_000058_000019_label};

    private String assets[] = {"aachen_000000_000019_gtfine_polygons.json","aachen_000006_000019_gtfine_polygons.json"};
   /* ,"aachen_000018_000019.json", "aachen_000036_000019.json", "aachen_000098_000019.json", "aachen_000105_000019.json", "aachen_000156_000019.json", "aachen_000164_000019.json", "aachen_000173_000019.json",
            "bochum_000000_006026.json", "bochum_000000_026634.json", "bochum_000000_027057.json", "bremen_000001_000019.json", "bremen_000004_000019.json", "bremen_000025_000019.json", "bremen_000030_000019.json", "bremen_000031_000019.json",
            "bremen_000035_000019.json", "bremen_000041_000019.json", "bremen_000049_000019.json", "bremen_000053_000019.json", "bremen_000065_000019.json", "bremen_000073_000019.json", "bremen_000157_000019.json", "cologne_000021_000019.json",
            "cologne_000030_000019.json", "cologne_000032_000019.json", "darmstadt_000012_000019.json", "dusseldorf_000011_000019.json", "dusseldorf_000016_000019.json", "dusseldorf_000088_000019.json", "dusseldorf_000133_000019.json", "erfurt_000004_000019.json",
            "erfurt_000014_000019.json", "erfurt_000019_000019.json", "erfurt_000031_000019.json", "erfurt_000045_000019.json", "erfurt_000058_000019.json"};
*/
    private int currImage;

    SharedClass obj;
    TextView scoreText;


    Random rand;

    final private int totalImages = 2;
    private String answers[] = new  String[totalImages];
    private String displayName;
    private String accountID;
    private int score;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_play);

        setAnswers();
        rand = new Random();
        int randButton = rand.nextInt(4) + 1;

        currImage = rand.nextInt(totalImages - 1);


        obj = getObject();
        scoreText = findViewById(R.id.scoreTextBox3);
        scoreText.setText("Score: " + Integer.toString(obj.score));

        final ImageView imageView = findViewById(R.id.sample_image);
//        imageView.setImageResource(images[currImage]);
        setCurrentImage();

        Button button1 = findViewById(R.id.optionButton1);
        Button button2 = findViewById(R.id.optionButton2);
        Button button3 = findViewById(R.id.optionButton3);
        Button button4 = findViewById(R.id.optionButton4);

        int trueAnswer = Integer.parseInt(answers[currImage]);

        setButtons(button1, button2, button3, button4, randButton, trueAnswer);
    }


    private void setButtons(Button but1, Button but2, Button but3, Button but4, int randomButton, int trueAns) {
        accountID = getIntent().getStringExtra("id");
        displayName = getIntent().getStringExtra("name");
        score = getIntent().getIntExtra("score", obj.score);
        if (randomButton == 1) {
            but1.setText(String.valueOf(trueAns));
            but2.setText(String.valueOf(trueAns - 1));
            but3.setText(String.valueOf(trueAns + 2));
            but4.setText(String.valueOf(trueAns + 1));
            but1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(ScratchPlayActivity.this, "CORRECT", Toast.LENGTH_SHORT).show();
                    obj.incrementScore();
                    scoreText.setText("Score:" + Integer.toString(obj.score));
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScratchPlayActivity.this);
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
            });
            but2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlertDialog();
                }
            });
            but3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlertDialog();
                }
            });
            but4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlertDialog();
                }
            });
        }
        else if(randomButton == 2) {
            but1.setText(String.valueOf(trueAns - 1));
            but2.setText(String.valueOf(trueAns));
            but3.setText(String.valueOf(trueAns + 2));
            but4.setText(String.valueOf(trueAns + 1));
            but2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(ScratchPlayActivity.this, "CORRECT", Toast.LENGTH_SHORT).show();
                    obj.incrementScore();
                    scoreText.setText("Score:" + Integer.toString(obj.score));
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScratchPlayActivity.this);
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
            });
            but1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlertDialog();
                }
            });
            but3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlertDialog();
                }
            });
            but4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlertDialog();
                }
            });
        }
        else if(randomButton == 3) {
            but1.setText(String.valueOf(trueAns + 2));
            but2.setText(String.valueOf(trueAns - 1));
            but3.setText(String.valueOf(trueAns));
            but4.setText(String.valueOf(trueAns + 1));
            but3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(ScratchPlayActivity.this, "CORRECT", Toast.LENGTH_SHORT).show();
                    obj.incrementScore();
                    scoreText.setText("Score:" + Integer.toString(obj.score));
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScratchPlayActivity.this);
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
            });
            but1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlertDialog();
                }
            });
            but2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlertDialog();
                }
            });
            but4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlertDialog();
                }
            });
        }
        else if(randomButton == 4) {
            but1.setText(String.valueOf(trueAns - 1));
            but2.setText(String.valueOf(trueAns + 1));
            but3.setText(String.valueOf(trueAns + 2));
            but4.setText(String.valueOf(trueAns));
            but4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(ScratchPlayActivity.this, "CORRECT", Toast.LENGTH_SHORT).show();
                    obj.incrementScore();
                    scoreText.setText("Score:" + Integer.toString(obj.score));
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScratchPlayActivity.this);
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
            });
            but1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlertDialog();
                }
            });
            but2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlertDialog();
                }
            });
            but3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlertDialog();
                }
            });
        }
        else{
            Toast.makeText(ScratchPlayActivity.this, Integer.toString(randomButton), Toast.LENGTH_SHORT).show();
            Log.v(TAG, Integer.toString(randomButton));
        }
    }

    @Override
    public void onBackPressed() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ExitActivity.exitApplicationAndRemoveFromRecent(ScratchPlayActivity.this);
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

    private void setNewImage() {
        int prevcurrImage = currImage;
        while (prevcurrImage == currImage) {
            currImage = rand.nextInt(totalImages - 1);
        }
    }

    private void setCurrentImage() {
        final ScratchImageView imageView = findViewById(R.id.sample_image);
//        imageView.setImageResource(images[currImage]);
//        Log.i(TAG, storageReference.toString());
        Glide.with(this)
                .load(images[currImage])
                .into(imageView);
    }

    private void getAlertDialog() {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(ScratchPlayActivity.this).create();
        alertDialog.setTitle("Wrong Answer");
        alertDialog.setMessage("Sorry, You Lost!");
        alertDialog.setIcon(R.drawable.wrong);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.setButton("HOME", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ScratchPlayActivity.this, HomeActivity.class);
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
