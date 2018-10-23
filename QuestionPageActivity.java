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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class QuestionPageActivity extends AppCompatActivity {
    private static final String TAG = "QuestionPageActivity";

    int score = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_page);

        final SharedClass sharedObject = getObject();
        setActivityText(sharedObject);

        final TextView textView = findViewById(R.id.levelText);
        final TextView textView2 = findViewById(R.id.levelInformation);
        final Button button = findViewById(R.id.StartButton);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                startNewActivity(sharedObject);
            }
        });

        final VideoView mVideoview = findViewById(R.id.videoView3);
        mVideoview.setVisibility(View.GONE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedObject.currentLevel == 1) {
                    textView.setText("Tutorial");
                    textView2.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    mVideoview.setVisibility(View.VISIBLE);
                    String path = "android.resource://" + getPackageName() + "/" + R.raw.tutorial1;
                    mVideoview.setMediaController(null);
                    mVideoview.setVideoURI(Uri.parse(path));
                    mVideoview.setZOrderOnTop(true);
                    mVideoview.start();
                    mVideoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mVideoview.setVisibility(View.GONE);
                            textView.setText("Level " + Integer.toString(sharedObject.difficultyLevel*(sharedObject.maxLevel-1) + sharedObject.currentLevel));
                            textView2.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                        }
                    });
                }
                else if (sharedObject.currentLevel == 2) {
                    textView.setText("Tutorial");
                    textView2.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    mVideoview.setVisibility(View.VISIBLE);
                    String path = "android.resource://" + getPackageName() + "/" + R.raw.tutorial2;
                    mVideoview.setMediaController(null);
                    mVideoview.setVideoURI(Uri.parse(path));
                    mVideoview.setZOrderOnTop(true);
                    mVideoview.start();
                    mVideoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            textView.setText("Level " + Integer.toString(sharedObject.difficultyLevel*(sharedObject.maxLevel-1) + sharedObject.currentLevel));
                            mVideoview.setVisibility(View.GONE);
                            textView2.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                        }
                    });
                }
                else {
                    textView.setText("Tutorial");
                    textView2.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    mVideoview.setVisibility(View.VISIBLE);
                    String path = "android.resource://" + getPackageName() + "/" + R.raw.tutorial3;
                    mVideoview.setMediaController(null);
                    mVideoview.setVideoURI(Uri.parse(path));
                    mVideoview.setZOrderOnTop(true);
                    mVideoview.start();
                    mVideoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            textView.setText("Level " + Integer.toString(sharedObject.difficultyLevel*(sharedObject.maxLevel-1) + sharedObject.currentLevel));
                            mVideoview.setVisibility(View.GONE);
                            textView2.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ExitActivity.exitApplicationAndRemoveFromRecent(QuestionPageActivity.this);
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
    }

    public void startNewActivity(SharedClass sharedObject) {
        Intent intent;
        //share level info to download class
        /*intent = new Intent(this, ImageDownload.class);
        intent.putExtra("sharedObject", sharedObject);
        Log.e(TAG, "displayName: " + getDisplayName());
        intent.putExtra("id", getAccountID());
        intent.putExtra("name", getDisplayName());
        intent.putExtra("score", getScore());
        intent.putExtra("currentLevel",sharedObject.currentLevel);
*/

        if (sharedObject.currentLevel == 1) {
            intent = new Intent(this, HonkPlayActivity.class);
            intent.putExtra("sharedObject", sharedObject);
            Log.e(TAG, "displayName: " + getDisplayName());
            intent.putExtra("id", getAccountID());
            intent.putExtra("name", getDisplayName());
            intent.putExtra("score", getScore());
        }
        else if (sharedObject.currentLevel == 2) {
            intent = new Intent(this, TapPlayActivity.class);
            intent.putExtra("sharedObject", sharedObject);
            Log.e(TAG, "displayName: " + getDisplayName());
            intent.putExtra("id", getAccountID());
            intent.putExtra("name", getDisplayName());
            intent.putExtra("score", getScore());
        }
        else  {
            intent = new Intent(this, ScratchPlayActivity.class);
            intent.putExtra("sharedObject", sharedObject);
            Log.e(TAG, "name: " + getDisplayName());
            intent.putExtra("id", getAccountID());
            intent.putExtra("name", getDisplayName());
            intent.putExtra("score", getScore());
        }
        startActivity(intent);

    }

    private void setActivityText(SharedClass sharedObject) {
        TextView levelText = findViewById(R.id.levelText);
        TextView levelInformation = findViewById(R.id.levelInformation);


        sharedObject.incrementCurrentLevel();

        levelText.setText("Level " + Integer.toString(sharedObject.difficultyLevel*(sharedObject.maxLevel-1) + sharedObject.currentLevel));
        levelInformation.setText(sharedObject.levelInfo[sharedObject.currentLevel]);
    }

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
        return getIntent().getIntExtra("score", score);
    }
}
