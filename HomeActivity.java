package in.ac.iiitd.dhcs_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.GamesClientStatusCodes;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.android.gms.auth.api.signin.GoogleSignIn.getClient;

public class HomeActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "HomeActivity";
    private static final int RC_SIGN_IN = 9001;
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_WAITING_ROOM = 10002;
    final static int RC_REQUEST_PERMISSIONS = 1003;

    private int currentLevel = 0;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView mStatusTextView;

    private String displayName;

    private String accountID;
    private int clickNum;
    private int score = 0;
    SharedClass sharedObject;// = new SharedClass();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    GoogleSignInAccount result1 ;
    GoogleSignInApi signInClient = null;
    GoogleApiClient googleApiClient = null;

    Random rand;

    int totalImages = 20;
    String answers[] = new String[totalImages];
    int numOfClicks = 0;
    int correctAnswers = 0;
    MediaPlayer mp;

//for quick game**************
    private RealTimeMultiplayerClient mRealTimeMultiplayerClient = null;
    RoomConfig mRoomConfig;
    String mRoomId=null;
    boolean mMultiplayer=false;
    ArrayList<Participant> mParticipants=null;
    String mMyId=null;
    byte[] mMsgBuf=new byte[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        switchToScreen(R.id.main_layout);

        sharedObject = new SharedClass();
        /*findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.quick_play_button).setOnClickListener(this);
        findViewById(R.id.startGameButton).setOnClickListener(this);
        findViewById(R.id.StartButton).setOnClickListener(this);*/

        for(int id:CLICKABLE){
            findViewById(id).setOnClickListener(this);
        }


        final Button honkButton = findViewById(R.id.honkButton);
        honkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numOfClicks++;
                mp = MediaPlayer.create(HomeActivity.this, R.raw.car_honk);
                mp.start();
            }
        });




//        findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                //.requestEmail()
                .requestScopes(Games.SCOPE_GAMES_LITE)
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
//        signInButton.setVisibility(View.GONE);
        // [END customize_button]
    }


   /* @Override
    protected void onResume() {
        super.onResume();
        signInSilently();
    }*/

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        System.exit(0);
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
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        // [END on_start_sign_in]
    }

    // [START onActivityResult]
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            //Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
           // handleSignInResult(task);
            //***********************
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // The signed in account is stored in the result.
                GoogleSignInAccount signedInAccount = result.getSignInAccount();
                updateUI(signedInAccount);
            }


        }else if (requestCode == RC_WAITING_ROOM) {
            // we got the result from the "waiting room" UI.
            if (resultCode == Activity.RESULT_OK) {
                // ready to start playing
                Log.i(TAG, "Starting game (waiting room returned OK).");
               //switchToScreen();
                //goToHonkPlay();
                //gameWelcomeScreen();
                gotoHonkPlayMultiPlayer();
            } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                // player indicated that they want to leave the room
                //leaveRoom();
                setContentView(R.layout.activity_home);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Dialog was cancelled (user pressed back key, for instance). In our game,
                // this means leaving the room too. In more elaborate games, this could mean
                // something else (like minimizing the waiting room UI).
                //leaveRoom();
                setContentView(R.layout.activity_home);

            }
        }else if(requestCode==RC_REQUEST_PERMISSIONS){
            if(resultCode==Activity.RESULT_OK){
                onConnected(GoogleSignIn.getLastSignedInAccount(this));
                Log.i(TAG, "Rc request code permission");
            }
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.i(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        /*Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(signInIntent, RC_SIGN_IN);*/
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_SIGN_IN);
        mGoogleSignInClient = signInClient;

        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }
    // [END signIn]

    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    // [START signOut]
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
           // final CircleImageView imgProfilePic = findViewById(R.id.profile_image);
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));
            displayName = account.getDisplayName();
            accountID = account.getId();
            Log.i(TAG, "display name: " + displayName);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.child("users").child(accountID).exists()) {
                        Log.i(TAG, "display name: Did not exist");
                        reference.child("users").child(accountID).child("name").setValue(displayName);
                        reference.child("users").child(accountID).child("score").setValue(score);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.i(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            });
//            String personPhotoUrl = account.getPhotoUrl().toString();
//
//            try {
//                Glide.with(this)
//                        .load(account.getPhotoUrl().toString())
//                        .apply(new RequestOptions()
//                                .placeholder(R.drawable.person2))
//                        .into(imgProfilePic);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            try {
//                new ImageLoadTask(account.getPhotoUrl().toString(), imgProfilePic).execute();
//            } catch (NullPointerException npe) {
//                new ImageLoadTask("https://soygrowers.com/wp-content/uploads/2017/02/default_bio_600x600.jpg", imgProfilePic).execute();
//            }

            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        } else {
           // mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.quick_play_button:
              //goToHonkPlay(v);
                //onConnected(result1);
                signInSilently();
                break;
            case R.id.startGameButton:
                Log.i(TAG,"start game onclick");
                goToHonkPlay();
                break;
            case R.id.StartButton:
                gotoHonkPlayMultiPlayer();
                break;
//           case R.id.disconnect_button:
//                revokeAccess();
//                break;
        }
    }

    public void goToHonkPlay() {
        Log.i(TAG, "gotoHonk called");
        if (isSignedIn()) {
            Intent intent = new Intent(this, QuestionPageActivity.class);
            intent.putExtra("sharedObject", sharedObject);
            intent.putExtra("id", accountID);
            intent.putExtra("name", displayName);
            intent.putExtra("score", score);
            startActivity(intent);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need to sign-in to start the game.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);
            alertDialog.show();
            Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(Color.parseColor("#448AFF"));
        }
    }

   /* public void goToHonkPlayMultiPlayer(){

        Intent intent = new Intent(this, HonkPlayMultiPlayer.class);
        intent.putExtra("sharedObject", sharedObject);
        intent.putExtra("clickNum", clickNum);
        startActivity(intent);


    }*/
    public void goToLeaderboard(View view) {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }

// for Quick start***********************************************************************************************************

    private String mPlayerId;
    GoogleSignInAccount mSignedInAccount = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void onConnected(GoogleSignInAccount googleSignInAccount){
        if(mSignedInAccount!= googleSignInAccount){
            mSignedInAccount = googleSignInAccount;

            mRealTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(this , Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount((this))));

            PlayersClient playersClient=Games.getPlayersClient(this,googleSignInAccount);

            playersClient.getCurrentPlayer()
                    .addOnSuccessListener(new OnSuccessListener<Player>() {
                        @Override
                        public void onSuccess(Player player) {
                            mPlayerId = player.getPlayerId();
                            Log.i(TAG, mPlayerId);
                           // switchToMainScreen();
                        }
                    })
                    .addOnFailureListener(createFailureListener("There was a problem getting the player id!"));
        }
       // Log.i(TAG, "hello world!");
        startQuickGame();
       // Log.i(TAG, "hello");




    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void signInSilently() {
        Log.i(TAG, "signInSilently()");

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), Games.SCOPE_GAMES_LITE)) {
            GoogleSignIn.requestPermissions(HomeActivity.this, RC_REQUEST_PERMISSIONS, GoogleSignIn.getLastSignedInAccount(this), Games.SCOPE_GAMES_LITE);
            Log.i(TAG, "has permission seccessfull");
        }

        if (GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), Games.SCOPE_GAMES_LITE)) {
            Log.i(TAG, "scope lite access permission");
            onConnected(GoogleSignIn.getLastSignedInAccount(this));
        }
    }
//        GoogleSignInClient signInClient =  GoogleSignIn.getClient(this,
//                GoogleSignInOptions.DEFAULT_SIGN_IN);
//
//        signInClient.silentSignIn().addOnCompleteListener(this,
//                new OnCompleteListener<GoogleSignInAccount>() {
//                    @Override
//                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "signInSilently(): success");
//                            onConnected(task.getResult());
//                        } else {
//                            Log.d(TAG, "signInSilently(): failure", task.getException());
//                            onDisconnected();
//                        }
//                    }
//                });
//

    public void onDisconnected() {
        Log.i(TAG, "onDisconnected()");

        mRealTimeMultiplayerClient = null;
       // mInvitationsClient = null;

        //switchToMainScreen();
    }

    final static int[] CLICKABLE={
      R.id.quick_play_button,R.id.sign_in_button, R.id.sign_out_button, R.id.startGameButton,R.id.StartButton
    };



    final static int[] SCREENS = {
           // R.id.screen_game, R.id.screen_main, R.id.screen_sign_in,
            R.id.screen_wait, R.id.HonkPlayWelcomeScreen,R.id.main_layout,R.id.HonkPlayActivity
    };
    int mCurScreen = -1;

    void switchToScreen(int screenId) {
        // make the requested screen visible; hide all others.
        for (int id : SCREENS) {
            findViewById(id).setVisibility(screenId == id ? View.VISIBLE : View.GONE);
        }
        mCurScreen = screenId;
    }

    void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private RoomUpdateCallback mRoomUpdateCallback = new RoomUpdateCallback() {

        // Called when room has been created
        @Override
        public void onRoomCreated(int statusCode, Room room) {
            Log.i(TAG, "onRoomCreated(" + statusCode + /*", " + room +*/ ")");
            if (statusCode != GamesCallbackStatusCodes.OK) {
                Log.i(TAG, "*** Error: onRoomCreated, status " + statusCode);
                showGameError();
                return;
            }

            // save room ID so we can leave cleanly before the game starts.
            mRoomId = room.getRoomId();

            // show the waiting room UI
            showWaitingRoom(room);

        }

        // Called when room is fully connected.
        @Override
        public void onRoomConnected(int statusCode, Room room) {
            Log.i(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");

            if (statusCode != GamesCallbackStatusCodes.OK) {
                Log.i(TAG, "*** Error: onRoomConnected, status " + statusCode);
                showGameError();
                return;
            }


            updateRoom(room);
        }

        @Override
        public void onJoinedRoom(int statusCode, Room room) {
            Log.i(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
            if (statusCode != GamesCallbackStatusCodes.OK) {
                Log.i(TAG, "*** Error: onRoomConnected, status " + statusCode);
                showGameError();
                return;
            }

            // show the waiting room UI
            showWaitingRoom(room);
        }

        // Called when we've successfully left the room (this happens a result of voluntarily leaving
        // via a call to leaveRoom(). If we get disconnected, we get onDisconnectedFromRoom()).
        @Override
        public void onLeftRoom(int statusCode, @NonNull String roomId) {
            // we have left the room; return to main screen.
            Log.i(TAG, "onLeftRoom, code " + statusCode);
            //switchToMainScreen();
        }
    };

    Map<String, Integer> mParticipantScore = new HashMap<>();

    // Participants who sent us their final score.
    Set<String> mFinishedParticipants = new HashSet<>();


   // private String mPlayerId;

    private RoomStatusUpdateCallback mRoomStatusUpdateCallback = new RoomStatusUpdateCallback() {
        // Called when we are connected to the room. We're not ready to play yet! (maybe not everybody
        // is connected yet).
        @Override
        public void onConnectedToRoom(Room room) {
            Log.i(TAG, "onConnectedToRoom.");

            //get participants and my ID:
            mParticipants = room.getParticipants();
            mMyId = room.getParticipantId(mPlayerId);

            // save room ID if its not initialized in onRoomCreated() so we can leave cleanly before the game starts.
            if (mRoomId == null) {
                mRoomId = room.getRoomId();
            }

            // print out the list of participants (for debug purposes)
            Log.i(TAG, "Room ID: " + mRoomId);
            Log.i(TAG, "My ID " + mMyId);
            Log.i(TAG, "<< CONNECTED TO ROOM>>");
        }

        // Called when we get disconnected from the room. We return to the main screen.
        @Override
        public void onDisconnectedFromRoom(Room room) {
            mRoomId = null;
            mRoomConfig = null;
            Log.i(TAG, "disconnected from room");
            showGameError();
        }


        // We treat most of the room update callbacks in the same way: we update our list of
        // participants and update the display. In a real game we would also have to check if that
        // change requires some action like removing the corresponding player avatar from the screen,
        // etc.
        @Override
        public void onPeerDeclined(Room room, @NonNull List<String> arg1) {
            updateRoom(room);
        }

        @Override
        public void onPeerInvitedToRoom(Room room, @NonNull List<String> arg1) {
            updateRoom(room);
        }

        @Override
        public void onP2PDisconnected(@NonNull String participant) {
        }

        @Override
        public void onP2PConnected(@NonNull String participant) {
        }

        @Override
        public void onPeerJoined(Room room, @NonNull List<String> arg1) {
            updateRoom(room);
        }

        @Override
        public void onPeerLeft(Room room, @NonNull List<String> peersWhoLeft) {
            updateRoom(room);
            Log.i(TAG,"On peer left");
        }

        @Override
        public void onRoomAutoMatching(Room room) {
            updateRoom(room);
        }

        @Override
        public void onRoomConnecting(Room room) {
            updateRoom(room);
        }

        @Override
        public void onPeersConnected(Room room, @NonNull List<String> peers) {
            updateRoom(room);
        }

        @Override
        public void onPeersDisconnected(Room room, @NonNull List<String> peers) {
            updateRoom(room);
        }
    };

    void updateRoom(Room room) {
        if (room != null) {
            mParticipants = room.getParticipants();
            //Log.i(TAG, mParticipants);
        }
        if (mParticipants != null) {
            //updatePeerScoresDisplay();
        }
    }

    private void handleException(Exception exception, String details) {
        int status = 0;

        if (exception instanceof ApiException) {
            ApiException apiException = (ApiException) exception;
            status = apiException.getStatusCode();
        }

        String errorString = null;
        switch (status) {
            case GamesCallbackStatusCodes.OK:
                break;
            case GamesClientStatusCodes.MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER:
                errorString = getString(R.string.status_multiplayer_error_not_trusted_tester);
                break;
            case GamesClientStatusCodes.MATCH_ERROR_ALREADY_REMATCHED:
                errorString = getString(R.string.match_error_already_rematched);
                break;
            case GamesClientStatusCodes.NETWORK_ERROR_OPERATION_FAILED:
                errorString = getString(R.string.network_error_operation_failed);
                break;
            case GamesClientStatusCodes.INTERNAL_ERROR:
                errorString = getString(R.string.internal_error);
                break;
            case GamesClientStatusCodes.MATCH_ERROR_INACTIVE_MATCH:
                errorString = getString(R.string.match_error_inactive_match);
                break;
            case GamesClientStatusCodes.MATCH_ERROR_LOCALLY_MODIFIED:
                errorString = getString(R.string.match_error_locally_modified);
                break;
            default:
                errorString = getString(R.string.unexpected_status, GamesClientStatusCodes.getStatusCodeString(status));
                break;
        }

        if (errorString == null) {
            return;
        }

        String message = getString(R.string.status_exception_error, details, status, exception);

        new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Error")
                .setMessage(message + "\n" + errorString)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }




    void showWaitingRoom(Room room) {
        // minimum number of players required for our game
        // For simplicity, we require everyone to join the game before we start it
        // (this is signaled by Integer.MAX_VALUE).
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        mRealTimeMultiplayerClient.getWaitingRoomIntent(room, MIN_PLAYERS)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        // show waiting room UI
                        startActivityForResult(intent, RC_WAITING_ROOM);
                    }
                })
                .addOnFailureListener(createFailureListener("There was a problem getting the waiting room!"));
    }

    private OnFailureListener createFailureListener(final String string) {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handleException(e, string);
            }
        };
    }
    private  static final long ROLE_HOST = 0x1;
    private static final long ROLE_PARTNER = 0x2;

    void startQuickGame() {
        // quick-start a game with 1 randomly selected opponent
        final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS,
                MAX_OPPONENTS,0);
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        //resetGameVars();
        Log.i(TAG, "start Quick");
        mRoomConfig = RoomConfig.builder(mRoomUpdateCallback)
                .setOnMessageReceivedListener(mOnRealTimeMessageReceivedListener)
                .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                .setAutoMatchCriteria(autoMatchCriteria)
                .build();
        Games.getRealTimeMultiplayerClient(this,GoogleSignIn.getLastSignedInAccount(this)).create(mRoomConfig);
        Log.i(TAG, "start Quick2");
    }


    void showGameError() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.game_problem))
                .setNeutralButton(android.R.string.ok, null).create();

       // switchToMainScreen();
    }


    // Activity is going to the background. We have to leave the current room.
    @Override
    public void onStop() {
        Log.d(TAG, "**** got onStop");

        // if we're in a room, leave it.
        leaveRoom();

        // stop trying to keep the screen on
        stopKeepingScreenOn();

        //switchToMainScreen();

        super.onStop();
    }

    // Clears the flag that keeps the screen on.
    void stopKeepingScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // Leave the room.
    void leaveRoom() {
        Log.d(TAG, "Leaving room.");
       // mSecondsLeft = 0;
        stopKeepingScreenOn();
        if (mRoomId != null) {
            mRealTimeMultiplayerClient.leave(mRoomConfig, mRoomId)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mRoomId = null;
                            mRoomConfig = null;
                        }
                    });
            switchToScreen(R.id.screen_wait);
        } else {
           // switchToMainScreen();
        }
    }


//***********************************************************************************************


    public void gameWelcomeScreen(){

        switchToScreen(R.id.HonkPlayWelcomeScreen);

    }
int mSecondLeft ;
public void gameTick(){
        if(mSecondLeft > 0){
            --mSecondLeft;
        }

    ((TextView )findViewById(R.id.countDown)).setText("0:"+ (mSecondLeft<10 ? "0": "")+ String.valueOf(mSecondLeft) );
        if(mSecondLeft<0){

            if(mParticipants.get(1).getParticipantId().equals(mMyId)){

                mMsgBuf[0] = (byte)numOfClicks;
                sendToAllReliably1(mMsgBuf);
                Log.i(TAG, "call 2");
                //intermediateHonkPlay();
               // mSecondLeft=10;
            }
        }
        //mSecondLeft=10;
}




    int currImage;


    Integer images[] = {R.drawable.aachen_000018_000019_original, R.drawable.aachen_000036_000019_original, R.drawable.aachen_000098_000019_original, R.drawable.aachen_000105_000019_original, R.drawable.aachen_000156_000019_original, R.drawable.aachen_000164_000019_original, R.drawable.aachen_000173_000019_original,
            R.drawable.bochum_000000_006026_original, R.drawable.bochum_000000_026634_original, R.drawable.bochum_000000_027057_original, R.drawable.bremen_000001_000019_original, R.drawable.bremen_000004_000019_original, R.drawable.bremen_000025_000019_original, R.drawable.bremen_000030_000019_original, R.drawable.bremen_000031_000019_original,
            R.drawable.bremen_000035_000019_original, R.drawable.bremen_000041_000019_original, R.drawable.bremen_000049_000019_original, R.drawable.bremen_000053_000019_original, R.drawable.bremen_000065_000019_original, R.drawable.bremen_000073_000019_original, R.drawable.bremen_000157_000019_original, R.drawable.cologne_000021_000019_original,
            R.drawable.cologne_000030_000019_original, R.drawable.cologne_000032_000019_original, R.drawable.darmstadt_000012_000019_original, R.drawable.dusseldorf_000011_000019_original, R.drawable.dusseldorf_000016_000019_original, R.drawable.dusseldorf_000088_000019_original, R.drawable.dusseldorf_000133_000019_original, R.drawable.erfurt_000004_000019_original,
            R.drawable.erfurt_000014_000019_original, R.drawable.erfurt_000019_000019_original, R.drawable.erfurt_000031_000019_original, R.drawable.erfurt_000045_000019_original, R.drawable.erfurt_000058_000019_original};



    public void gotoHonkPlayMultiPlayer(){
    switchToScreen(R.id.HonkPlayActivity);
    numOfClicks = 0;

        //mSecondLeft=10;



    if(mParticipants.get(0).getParticipantId().equals(mMyId)){
        rand = new Random();
        currImage = rand.nextInt(totalImages -1);
        mMsgBuf[0] = (byte)currImage;
        sendToAllReliably(mMsgBuf);
        intermediateHonkPlay();
    }




}


public void intermediateHonkPlay(){

    mSecondLeft=10;
    setCurrentImage();
    final Handler h = new Handler();
    h.postDelayed(new Runnable() {
        @Override
        public void run() {
            if(mSecondLeft<=0){
                if(mParticipants.get(1).getParticipantId().equals(mMyId)) {
                    Log.i(TAG,"call 1");
                    mMsgBuf[0] = (byte) numOfClicks;
                    sendToAllReliably1(mMsgBuf);
                }
            }
            gameTick();
            h.postDelayed(this,1000);
        }
    },1000);


}
public void setCurrentImage(){

    ImageView imageView=findViewById(R.id.imageDisplay);
    Glide.with(this)
            .load(images[currImage])
            .into(imageView);
}



    void sendToAllReliably(byte[] message) {
        mRealTimeMultiplayerClient.sendReliableMessage(mMsgBuf, mRoomId, mParticipants.get(1).getParticipantId(), new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
            @Override
            public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientParticipantId) {

                Log.d(TAG, "RealTime message sent");
                Log.d(TAG, "  statusCode: " + statusCode);
                Log.d(TAG, "  tokenId: " + tokenId);
                Log.d(TAG, "  recipientParticipantId: " + recipientParticipantId);
            }
        })
                .addOnSuccessListener(new OnSuccessListener<Integer>() {
                    @Override
                    public void onSuccess(Integer tokenId) {
                        Log.d(TAG, "Created a reliable message with tokenId: " + tokenId);
                    }
                });

    }

    void sendToAllReliably1(byte[] message) {
        mRealTimeMultiplayerClient.sendReliableMessage(mMsgBuf, mRoomId, mParticipants.get(0).getParticipantId(), new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
            @Override
            public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientParticipantId) {

                Log.d(TAG, "RealTime message sent");
                Log.d(TAG, "  statusCode: " + statusCode);
                Log.d(TAG, "  tokenId: " + tokenId);
                Log.d(TAG, "  recipientParticipantId: " + recipientParticipantId);
            }
        })
                .addOnSuccessListener(new OnSuccessListener<Integer>() {
                    @Override
                    public void onSuccess(Integer tokenId) {
                        Log.d(TAG, "Created a reliable message with tokenId: " + tokenId);
                    }
                });

    }





    OnRealTimeMessageReceivedListener mOnRealTimeMessageReceivedListener = new OnRealTimeMessageReceivedListener() {
        @Override
        public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
            byte[] buf = realTimeMessage.getMessageData();
            String sender = realTimeMessage.getSenderParticipantId();
            Log.i(TAG, "Message received: " + (char) buf[0]);

            if (mParticipants.get(1).getParticipantId().equals(mMyId)) {
                // score update.
                currImage=(int)buf[0];
                Log.i(TAG, "currentimage");
                intermediateHonkPlay();

            }
           // intermediateHonkPlay();

            else if (mParticipants.get(0).getParticipantId().equals(mMyId)) {
                // score update.
                int clicker=(int)buf[0];
                Log.i(TAG, "cicker");
                if(clicker==numOfClicks){
                   // mSecondLeft=10;
                    gotoHonkPlayMultiPlayer();
                }
                else
                {
                    switchToScreen(R.id.main_layout);
                }

            }

        }
    };













//*****************************************





}





class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;

    public ImageLoadTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }

}

class SharedClass implements Serializable {
    public int score;
    public int clickNum;
    public int currentLevel;
    public int maxLevel;
    public String[] levelInfo;
    public String[] extraInfo;
    public int[] minImages;
    public int difficultyLevel;

    public SharedClass() {
        score = 0;
        currentLevel = 0;
        maxLevel = 4;
        difficultyLevel = 0;
        clickNum = 0;

        levelInfo = new String[maxLevel];
        extraInfo = new String[maxLevel];
        minImages = new int[maxLevel];

        minImages[0] = -1;
        minImages[1] = 3;
        minImages[2] = 1;
        minImages[3] = 1;

        levelInfo[1] = "Honk once per car!";
        levelInfo[2] = "Tap on all the cars!";
        levelInfo[3] = "Scratch to find the cars!";

        extraInfo[1] = "Press the horn as many number of cars in the image.";
        extraInfo[2] = "Tap on as many number of cars present in the image.";
        extraInfo[3] = "Scratch the image and then pick the right option from the available set of options.";
    }

    public void incrementCurrentLevel() {
        this.currentLevel++;

        if (currentLevel == maxLevel) {

            currentLevel = 1;
            this.difficultyLevel++;
        }
    }

    public void incrementScore() {
        this.score+=10;
    }

    public void incrementScore(int score) {
        this.score+=score;
    }

    public int getScore() {
        return score;
    }
}




