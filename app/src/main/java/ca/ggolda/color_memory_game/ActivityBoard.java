package ca.ggolda.color_memory_game;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private TextView patternString;
    private TextView currentSquare;
    private TextView guessTextview;

    private LinearLayout gameBoard;
    private TextView startButton;
    private View redButton;
    private View yellowButton;
    private View blueButton;
    private LinearLayout greenButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_layout);

        patternString = (TextView) findViewById(R.id.pattern_textview);
        currentSquare = (TextView) findViewById(R.id.current_textview);
        guessTextview = (TextView) findViewById(R.id.guess_textview);

        gameBoard = (LinearLayout) findViewById(R.id.GameBoard);
        startButton = (TextView) findViewById(R.id.start_button);
        redButton = (View) findViewById(R.id.red_button);
        yellowButton = (View) findViewById(R.id.yellow_button);
        blueButton = (View) findViewById(R.id.blue_button);
        greenButton = (LinearLayout) findViewById(R.id.green_button);

        patternHandler = new Handler();

        patternString.setText(PatternString);

        //Display pattern to user
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startPattern();
            }
        });
    }


    public void startPattern() {
        // Disable color buttons for pattern display duration
        disableButtons();

        //Return background black pattern start
        gameBoard.setBackgroundColor(Color.parseColor("#000000"));
        startButton.setVisibility(View.GONE);

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
                    Thread.sleep(1000);
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

                        Log.e("CurrentIndex", String.valueOf(currentIndex));

                        // get digit at currentIndex and display
                        char patternChar = cappedPattern.charAt(currentIndex);
                        currentSquare.setText(String.valueOf(patternChar));


                        // light up button corresponding to patternChar
                        if (String.valueOf(patternChar).equals("1")) {
                            greenButton.setBackgroundColor(Color.parseColor("#49f436"));
                            blueButton.setBackgroundColor(Color.parseColor("#0000FF"));
                            yellowButton.setBackgroundColor(Color.parseColor("#FFFB00"));


                            // Animate from white back to color in flashTime milliseconds
                            int colorFrom = Color.parseColor("#FFFFFF");
                            int colorTo = Color.parseColor("#FF0000");
                            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                            colorAnimation.setDuration(flashTime); // milliseconds
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                @Override
                                public void onAnimationUpdate(ValueAnimator animator) {
                                    redButton.setBackgroundColor((int) animator.getAnimatedValue());
                                }
                            });
                            colorAnimation.start();

                        } else if (String.valueOf(patternChar).equals("2")) {

                            redButton.setBackgroundColor(Color.parseColor("#FF0000"));
                            greenButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            blueButton.setBackgroundColor(Color.parseColor("#0000FF"));
                            yellowButton.setBackgroundColor(Color.parseColor("#FFFB00"));

                            // Animate from white back to color in flashTime milliseconds
                            int colorFrom = Color.parseColor("#FFFFFF");
                            int colorTo = Color.parseColor("#49f436");
                            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                            colorAnimation.setDuration(flashTime); // milliseconds
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                @Override
                                public void onAnimationUpdate(ValueAnimator animator) {
                                    greenButton.setBackgroundColor((int) animator.getAnimatedValue());
                                }
                            });
                            colorAnimation.start();

                        } else if (String.valueOf(patternChar).equals("3")) {

                            redButton.setBackgroundColor(Color.parseColor("#FF0000"));
                            greenButton.setBackgroundColor(Color.parseColor("#49f436"));
                            blueButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            yellowButton.setBackgroundColor(Color.parseColor("#FFFB00"));

                            // Animate from white back to color in flashTime milliseconds
                            int colorFrom = Color.parseColor("#FFFFFF");
                            int colorTo = Color.parseColor("#0000FF");
                            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                            colorAnimation.setDuration(flashTime); // milliseconds
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                @Override
                                public void onAnimationUpdate(ValueAnimator animator) {
                                    blueButton.setBackgroundColor((int) animator.getAnimatedValue());
                                }
                            });
                            colorAnimation.start();

                        } else if (String.valueOf(patternChar).equals("4")) {

                            redButton.setBackgroundColor(Color.parseColor("#FF0000"));
                            greenButton.setBackgroundColor(Color.parseColor("#49f436"));
                            blueButton.setBackgroundColor(Color.parseColor("#0000FF"));
                            yellowButton.setBackgroundColor(Color.parseColor("#FFFFFF"));

                            // Animate from white back to color in flashTime milliseconds
                            int colorFrom = Color.parseColor("#FFFFFF");
                            int colorTo = Color.parseColor("#FFFB00");
                            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                            colorAnimation.setDuration(flashTime); // milliseconds
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                @Override
                                public void onAnimationUpdate(ValueAnimator animator) {
                                    yellowButton.setBackgroundColor((int) animator.getAnimatedValue());
                                }
                            });
                            colorAnimation.start();

                        } else if (String.valueOf(patternChar).equals("0")) {

                            redButton.setBackgroundColor(Color.parseColor("#FF0000"));
                            greenButton.setBackgroundColor(Color.parseColor("#49f436"));
                            blueButton.setBackgroundColor(Color.parseColor("#0000FF"));
                            yellowButton.setBackgroundColor(Color.parseColor("#FFFB00"));

                            // Allow User to Guess
                            guessPlay();

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
                    //Make background white on-pressed
                    gameBoard.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    if (patternGuess.length() == (PatternString.length() - 1) && (patternGuess + "1").equals(PatternString)) {
                        //Disable buttons while start menu up
                        disableButtons();
                        patternGuess = patternGuess + "1";
                        userScore = patternGuess.length();
                        startButton.setText("Good Job!\nYour Score: " + userScore + "\nNext!");
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                //Display pattern to user
                                startPattern();
                            }
                        });

                        // TODO: remove this temp
                        guessTextview.setText(patternGuess);

                    } else if ((String.valueOf(PatternString.charAt(guessIndex))).equals("1")) {
                        patternGuess = patternGuess + "1";
                        guessIndex = guessIndex + 1;

                        // TODO: remove this temp
                        guessTextview.setText(patternGuess);

                    } else {
                        //Disable buttons while start menu up
                        disableButtons();
                        startButton.setText("Good Game!\nYour Score: " + userScore + "\nTry Again?");
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                resetGame();
                            }
                        });
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return background black on-released
                    gameBoard.setBackgroundColor(Color.parseColor("#000000"));

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
                    gameBoard.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    if (patternGuess.length() == (PatternString.length() - 1) && (patternGuess + "2").equals(PatternString)) {
                        //Disable buttons while start menu up
                        disableButtons();
                        patternGuess = patternGuess + "2";
                        userScore = patternGuess.length();
                        startButton.setText("Good Job!\nYour Score: " + userScore + "\nNext!");
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                //Display pattern to user
                                startPattern();
                            }
                        });

                        // TODO: remove this temp
                        guessTextview.setText(patternGuess);

                    } else if ((String.valueOf(PatternString.charAt(guessIndex))).equals("2")) {
                        patternGuess = patternGuess + "2";
                        guessIndex = guessIndex + 1;

                        // TODO: remove this temp
                        guessTextview.setText(patternGuess);

                    } else {
                        //Disable buttons while start menu up
                        disableButtons();
                        startButton.setText("Good Game!\nYour Score: " + userScore + "\nTry Again?");
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                resetGame();
                            }
                        });
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return background black on-released
                    gameBoard.setBackgroundColor(Color.parseColor("#000000"));

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
                    gameBoard.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    if (patternGuess.length() == (PatternString.length() - 1) && (patternGuess + "3").equals(PatternString)) {
                        //Disable buttons while start menu up
                        disableButtons();
                        patternGuess = patternGuess + "3";
                        userScore = patternGuess.length();
                        startButton.setText("Good Job!\nYour Score: " + userScore + "\nNext!");
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                //Display pattern to user
                                startPattern();
                            }
                        });

                        // TODO: remove this temp
                        guessTextview.setText(patternGuess);

                    } else if ((String.valueOf(PatternString.charAt(guessIndex))).equals("3")) {
                        patternGuess = patternGuess + "3";
                        guessIndex = guessIndex + 1;

                        // TODO: remove this temp
                        guessTextview.setText(patternGuess);

                    } else {
                        //Disable buttons while start menu up
                        disableButtons();
                        startButton.setText("Good Game!\nYour Score: " + userScore + "\nTry Again?");
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                resetGame();
                            }
                        });
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return background black on-released
                    gameBoard.setBackgroundColor(Color.parseColor("#000000"));

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
                    gameBoard.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    if (patternGuess.length() == (PatternString.length() - 1) && (patternGuess + "4").equals(PatternString)) {
                        //Disable buttons while start menu up
                        disableButtons();
                        patternGuess = patternGuess + "4";
                        userScore = patternGuess.length();
                        startButton.setText("Good Job!\nYour Score: " + userScore + "\nNext!");
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                //Display pattern to user
                                startPattern();
                            }
                        });

                        // TODO: remove this temp
                        guessTextview.setText(patternGuess);

                    } else if ((String.valueOf(PatternString.charAt(guessIndex))).equals("4")) {
                        patternGuess = patternGuess + "4";
                        guessIndex = guessIndex + 1;

                        // TODO: remove this temp
                        guessTextview.setText(patternGuess);

                    } else {
                        //Disable buttons while start menu up
                        disableButtons();
                        startButton.setText("Good Game!\nYour Score: " + userScore + "\nTry Again?");
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                resetGame();
                            }
                        });
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return background black on-released
                    gameBoard.setBackgroundColor(Color.parseColor("#000000"));

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

    }

    private void resetGame() {
        PatternString = "";
        guessIndex = 0;

        startButton.setText("Go!");
        startButton.setVisibility(View.VISIBLE);
        //Display pattern to user
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startPattern();
            }
        });

    }

    private void disableButtons() {
        //Disable color buttons
        redButton.setOnTouchListener(null);
        yellowButton.setOnTouchListener(null);
        blueButton.setOnTouchListener(null);
        greenButton.setOnTouchListener(null);
    }
}

