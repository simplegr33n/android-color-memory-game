package ca.ggolda.color_memory_game;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;
import java.util.StringTokenizer;


public class BoardClassic extends AppCompatActivity {

    private Handler patternHandler;

    private String PatternString = "";
    private String cappedPattern = "";
    private int currentIndex = -1;

    private String patternGuess = "";
    private int guessIndex = 0;

    private int userScore = 0;

    private int Min = 1;
    private int Max = 4;
    private int flashTime = 350;

    private int sleepTime;

    private TextView patternString;
    private TextView currentSquare;
    private TextView guessTextview;

    private LinearLayout gameBoard;
    private RelativeLayout breakLayout;

    private TextView messageTextview;
    private TextView scoreTextview;
    private TextView highscoreTextview;

    private View redButton;
    private View yellowButton;
    private View blueButton;
    private LinearLayout greenButton;

    private View targetQuad;
    private View offQuadOne;
    private View offQuadTwo;
    private View offQuadThree;

    private String targetColor;
    private String offColorOne;
    private String offColorTwo;
    private String offColorThree;

    SharedPreferences sharedPref;
    private int highScore;

    private MediaPlayer mp;

    private boolean sound_on;

    // create audio focus change listener
    final AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Should pause, but too many errors, so just releasing. Fuck it. File is small
                        // Can be called again quite easily.
                        releaseAudio();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Resume playback
                        // Actually I don't have this, because it seems that
                        // Audio gets released and then causes a crash because there's nothing
                        // to call .start() on.

                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Stop playback
                        releaseAudio();
                    }
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_classic);

        // initialize MobileAds
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6262252551936427~5382545595");

        // Request add and load into adView
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // get current high score from shared preferences
        sharedPref = getSharedPreferences("ggco_colormem_values", MODE_PRIVATE);
        highScore = sharedPref.getInt("highscore_classic", 0);
        sleepTime = sharedPref.getInt("sleep_time", 850);
        sound_on = sharedPref.getBoolean("sound_on", false);

        patternString = (TextView) findViewById(R.id.pattern_textview);
        currentSquare = (TextView) findViewById(R.id.current_textview);
        guessTextview = (TextView) findViewById(R.id.guess_textview);

        gameBoard = (LinearLayout) findViewById(R.id.GameBoard);
        breakLayout = (RelativeLayout) findViewById(R.id.BreakLayout);

        messageTextview = (TextView) findViewById(R.id.message);

        scoreTextview = (TextView) findViewById(R.id.score);
        scoreTextview.setText("GO");

        highscoreTextview = (TextView) findViewById(R.id.highscore);
        if (highScore != 0) {
            highscoreTextview.setText("Highscore: "  + String.valueOf(highScore));
        }


        redButton = (View) findViewById(R.id.red_button);
        yellowButton = (View) findViewById(R.id.yellow_button);
        blueButton = (View) findViewById(R.id.blue_button);
        greenButton = (LinearLayout) findViewById(R.id.green_button);

        patternHandler = new Handler();

        patternString.setText(PatternString);


        //Display pattern to user
        breakLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startPattern();
            }
        });
    }


    public void startPattern() {
        // Disable color buttons for pattern display duration
        disableButtons();

        // Set background black for pattern display
        gameBoard.setBackgroundColor(Color.parseColor("#3C3B3B"));
        breakLayout.setVisibility(View.GONE);

        // Add a Random value to the end of the Pattern String
        Random r = new Random();
        int i1 = r.nextInt(Max - Min + 1) + Min;
        PatternString = PatternString + i1;

        // Display updated PatternString
        patternString.setText(PatternString);

        // Reset current index value, guess index, patternGuess and
        // Create animation ready pattern with an 0 to cap the end
        currentIndex = -1;
        guessIndex = 0;
        patternGuess = "";
        cappedPattern = PatternString + "0";

        // TODO: remove this temp
        guessTextview.setText(patternGuess);

        // Start new thread for animating sequence
        new Thread(new Task()).start();
    }

    class Task implements Runnable {

        @Override
        public void run() {

            for (int i = 0; i < (cappedPattern.length() + 1); i++) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if ((currentIndex + 1) == cappedPattern.length()) {
                    guessPlay();
                } else {
                    currentIndex = currentIndex + 1;
                }

                patternHandler.post(new Runnable() {

                    @Override
                    public void run() {

                        // get digit at currentIndex and display
                        char patternChar = cappedPattern.charAt(currentIndex);
                        currentSquare.setText(String.valueOf(patternChar));

                        // light up button corresponding to patternChar
                        if (String.valueOf(patternChar).equals("1")) {
                            flashButton(1);
                        } else if (String.valueOf(patternChar).equals("2")) {
                            flashButton(2);
                        } else if (String.valueOf(patternChar).equals("3")) {
                            flashButton(3);
                        } else if (String.valueOf(patternChar).equals("4")) {
                            flashButton(4);
                        } else if (String.valueOf(patternChar).equals("0")) {
                            flashButton(0);

                        }
                    }
                });
            }
        }
    }

    // function for testing and responding to guess
    private void guessPress(int press) {

        if (patternGuess.length() == (PatternString.length() - 1) && (patternGuess + press).equals(PatternString)) {

            //Disable buttons while start menu up
            disableButtons();

            patternGuess = patternGuess + press;
            userScore = patternGuess.length();

            // if new usesScore beats old high score, change value in shared preferences
            if (userScore > highScore) {

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("highscore_classic", userScore);
                editor.apply();

                highScore = sharedPref.getInt("highscore_classic", 0);
            }


            messageTextview.setText("Good\nJob!");
            messageTextview.setTextColor(Color.parseColor("#49ff89"));
            scoreTextview.setText(String.valueOf(userScore));
            highscoreTextview.setText("Highscore: "  + String.valueOf(highScore));

            breakLayout.setVisibility(View.VISIBLE);
            breakLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Display pattern to user
                    startPattern();
                }
            });

            // TODO: remove this temp
            guessTextview.setText(patternGuess);


        } else if ((String.valueOf(PatternString.charAt(guessIndex))).equals(String.valueOf(press))) {
            patternGuess = patternGuess + press;
            guessIndex = guessIndex + 1;

            // TODO: remove this temp
            guessTextview.setText(patternGuess);

        } else {
            //Disable buttons while start menu up
            disableButtons();
            PatternString = "";

            messageTextview.setTextColor(Color.parseColor("#D2DBF9"));
            messageTextview.setText("Good\nGame!");


            scoreTextview.setTextColor(Color.parseColor("#FF0000"));
            scoreTextview.setText(String.valueOf(userScore));

            breakLayout.setVisibility(View.VISIBLE);
            breakLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    PatternString = "";
                    guessIndex = 0;
                    userScore = 0;

                    startPattern();
                }
            });

        }

    }


    private void guessPlay() {

        redButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background white on-pressed
                    redButton.setBackgroundColor(Color.parseColor("#FFFFFF"));


                    if (sound_on == true) {
                        // Play sound on press
                        // TODO: REFACTOR
                        // TODO: remove lag
                        releaseAudio();
                        // Start playback.
                        mp = MediaPlayer.create(BoardClassic.this, R.raw.note2);
                        mp.start();

                        //OnCompletionListener to release the audio file after playback
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                                releaseAudio();
                            }
                        });
                    }


                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    redButton.setBackgroundColor(Color.parseColor("#FF0000"));

                    // send guess to guessPress() function
                    guessPress(1);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        greenButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background white on-pressed
                    greenButton.setBackgroundColor(Color.parseColor("#FFFFFF"));


                    if (sound_on == true) {
                        // Play sound on press
                        // TODO: REFACTOR
                        // TODO: remove lag
                        releaseAudio();
                        // Start playback.
                        mp = MediaPlayer.create(BoardClassic.this, R.raw.note2);
                        mp.start();

                        //OnCompletionListener to release the audio file after playback
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                                releaseAudio();
                            }
                        });
                    }



                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    greenButton.setBackgroundColor(Color.parseColor("#49f436"));

                    // send guess to guessPress() function
                    guessPress(2);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        blueButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background white on-pressed
                    blueButton.setBackgroundColor(Color.parseColor("#FFFFFF"));


                    if (sound_on == true) {
                        // Play sound on press
                        // TODO: REFACTOR
                        // TODO: remove lag
                        releaseAudio();
                        // Start playback.
                        mp = MediaPlayer.create(BoardClassic.this, R.raw.note3);
                        mp.start();

                        //OnCompletionListener to release the audio file after playback
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                                releaseAudio();
                            }
                        });
                    }



                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    blueButton.setBackgroundColor(Color.parseColor("#0000FF"));

                    // send guess to guessPress() function
                    guessPress(3);


                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        yellowButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background white on-pressed
                    yellowButton.setBackgroundColor(Color.parseColor("#FFFFFF"));


                    if (sound_on == true) {
                        // Play sound on press
                        // TODO: REFACTOR
                        // TODO: remove lag
                        releaseAudio();
                        // Start playback.
                        mp = MediaPlayer.create(BoardClassic.this, R.raw.note4);
                        mp.start();

                        //OnCompletionListener to release the audio file after playback
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                                releaseAudio();
                            }
                        });
                    }


                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    yellowButton.setBackgroundColor(Color.parseColor("#FFFB00"));

                    // send guess to guessPress() function
                    guessPress(4);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

    }


    // Set views for animateFlash() function
    private void flashButton(int quad) {

        if (quad == 1) {
            targetQuad = (View) findViewById(R.id.red_button);
            offQuadOne = (View) findViewById(R.id.green_button);
            offQuadTwo = (View) findViewById(R.id.blue_button);
            offQuadThree = (View) findViewById(R.id.yellow_button);

            targetColor = "#FF0000";
            offColorOne = "#49f436";
            offColorTwo = "#0000FF";
            offColorThree = "#FFFB00";

            animateFlash();

        } else if (quad == 2) {
            targetQuad = (View) findViewById(R.id.green_button);
            offQuadOne = (View) findViewById(R.id.red_button);
            offQuadTwo = (View) findViewById(R.id.blue_button);
            offQuadThree = (View) findViewById(R.id.yellow_button);

            targetColor = "#49f436";
            offColorOne = "#FF0000";
            offColorTwo = "#0000FF";
            offColorThree = "#FFFB00";

            animateFlash();

        } else if (quad == 3) {
            targetQuad = (View) findViewById(R.id.blue_button);
            offQuadOne = (View) findViewById(R.id.green_button);
            offQuadTwo = (View) findViewById(R.id.red_button);
            offQuadThree = (View) findViewById(R.id.yellow_button);

            targetColor = "#0000FF";
            offColorOne = "#49f436";
            offColorTwo = "#FF0000";
            offColorThree = "#FFFB00";

            animateFlash();

        } else if (quad == 4) {
            targetQuad = (View) findViewById(R.id.yellow_button);
            offQuadOne = (View) findViewById(R.id.green_button);
            offQuadTwo = (View) findViewById(R.id.blue_button);
            offQuadThree = (View) findViewById(R.id.red_button);

            targetColor = "#FFFB00";
            offColorOne = "#49f436";
            offColorTwo = "#0000FF";
            offColorThree = "#FF0000";

            animateFlash();

        } else if (quad == 0) {
            redButton.setBackgroundColor(Color.parseColor("#FF0000"));
            greenButton.setBackgroundColor(Color.parseColor("#49f436"));
            blueButton.setBackgroundColor(Color.parseColor("#0000FF"));
            yellowButton.setBackgroundColor(Color.parseColor("#FFFB00"));

            // Set background to ready color
            gameBoard.setBackgroundColor(Color.parseColor("#000000"));

            // Allow User to Guess
            guessPlay();
        }
    }

    private void animateFlash() {
        offQuadOne.setBackgroundColor(Color.parseColor(offColorOne));
        offQuadTwo.setBackgroundColor(Color.parseColor(offColorTwo));
        offQuadThree.setBackgroundColor(Color.parseColor(offColorThree));

        // Animate from white back to color in flashTime milliseconds
        int colorFrom = Color.parseColor("#FFFFFF");
        int colorTo = Color.parseColor(targetColor);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(flashTime); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                targetQuad.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    private void disableButtons() {
        //Disable color buttons
        redButton.setOnTouchListener(null);
        yellowButton.setOnTouchListener(null);
        blueButton.setOnTouchListener(null);
        greenButton.setOnTouchListener(null);

        //return all buttons to base color
        redButton.setBackgroundColor(Color.parseColor("#FF0000"));
        greenButton.setBackgroundColor(Color.parseColor("#49f436"));
        blueButton.setBackgroundColor(Color.parseColor("#0000FF"));
        yellowButton.setBackgroundColor(Color.parseColor("#FFFB00"));
    }

    public void releaseAudio() {
        if (mp != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mp.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mp = null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BoardClassic.this, ActivityLaunch.class);
        startActivity(intent);

        return;
    }
}

