package ca.ggolda.color_memory_game;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;


public class ActivityBoard extends AppCompatActivity {

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

    private TextView startTextview;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_layout);

        // initialize MobileAds
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6262252551936427~5382545595");

        // Request add and load into adView
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // get current high score from shared preferences
        sharedPref = this.getPreferences(this.MODE_PRIVATE);
        highScore = sharedPref.getInt("high_score", 0);
        sleepTime = sharedPref.getInt("sleep_time", 850);

        patternString = (TextView) findViewById(R.id.pattern_textview);
        currentSquare = (TextView) findViewById(R.id.current_textview);
        guessTextview = (TextView) findViewById(R.id.guess_textview);

        gameBoard = (LinearLayout) findViewById(R.id.GameBoard);
        breakLayout = (RelativeLayout) findViewById(R.id.BreakLayout);

        startTextview = (TextView) findViewById(R.id.start_textview);
        messageTextview = (TextView) findViewById(R.id.message_textview);
        scoreTextview = (TextView) findViewById(R.id.score_textview);
        highscoreTextview = (TextView) findViewById(R.id.highscore_textview);



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
        gameBoard.setBackgroundColor(Color.parseColor("#000000"));
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

    private void guessPlay() {

        redButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    // send guess to guessPress() function
                    guessPress(1);


                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return background black on-released
                    gameBoard.setBackgroundColor(Color.parseColor("#FFFFFF"));

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
                    // send guess to guessPress() function
                    guessPress(2);


                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return background black on-released
                    gameBoard.setBackgroundColor(Color.parseColor("#FFFFFF"));

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
                    // send guess to guessPress() function
                    guessPress(3);


                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return background black on-released
                    gameBoard.setBackgroundColor(Color.parseColor("#FFFFFF"));

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
                    // send guess to guessPress() function
                    guessPress(4);

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return background black on-released
                    gameBoard.setBackgroundColor(Color.parseColor("#FFFFFF"));

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

    }


    // function for testing and responding to guess
    private void guessPress (int press) {

        //Make background light green on-pressed
        gameBoard.setBackgroundColor(Color.parseColor("#000000"));

        if (patternGuess.length() == (PatternString.length() - 1) && (patternGuess + press).equals(PatternString)) {
            //Disable buttons while start menu up
            disableButtons();
            patternGuess = patternGuess + press;
            userScore = patternGuess.length();

            // if new usesScore beats old high score, change value in shared preferences
            if (userScore >= highScore ) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("high_score", userScore);
                editor.apply();

                highScore = sharedPref.getInt("high_score", 0);
            }

            startTextview.setText("Next!");
            messageTextview.setText("Good Job!");
            messageTextview.setTextColor(Color.parseColor("#49ff89"));
            scoreTextview.setText("Your Score: " + userScore);
            highscoreTextview.setText("High Score: " + highScore);

            scoreTextview.setText("Your Score: " + userScore);
            highscoreTextview.setText("High Score: " + highScore);

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


            startTextview.setText("Try Again?");
            messageTextview.setText("Good Game!");
            messageTextview.setTextColor(Color.parseColor("#FF8949!"));
            scoreTextview.setText("Your Score: " + userScore);
            highscoreTextview.setText("High Score: " + highScore);


            breakLayout.setVisibility(View.VISIBLE);
            breakLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    resetGame();
                }
            });

        }

    }


    private void resetGame() {
        PatternString = "";
        guessIndex = 0;

        startTextview.setText("Go!");
        startTextview.setVisibility(View.VISIBLE);
        //Display pattern to user
        startTextview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startPattern();
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
            gameBoard.setBackgroundColor(Color.parseColor("#FFFFFF"));

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
    }
}

