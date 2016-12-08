package ca.ggolda.color_memory_game;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;


public class ActivityBoard extends AppCompatActivity {

    private Handler patternHandler;

    private String PatternString = "134";
    private String cappedPattern = "";
    private int currentIndex = -1;

    private String patternGuess = "";
    private int guessIndex = 0;

    private int userScore = 0;

    private int Min = 1;
    private int Max = 4;
    private int flashTime = 350;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_layout);

        patternHandler = new Handler();

        final TextView patternString = (TextView) findViewById(R.id.pattern_textview);
        patternString.setText(PatternString);

        final TextView startButton = (TextView) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Display pattern to user
                startPattern();
            }
        });
    }


    public void startPattern() {
        // Disable color buttons for pattern display duration
        disableButtons();

        TextView startButton = (TextView) findViewById(R.id.start_button);
        startButton.setVisibility(View.GONE);

        // Add a Random value to the end of the Pattern String
        Random r = new Random();
        int i1 = r.nextInt(Max - Min + 1) + Min;
        PatternString = PatternString + i1;

        // Display updated PatternString
        TextView patternString = (TextView) findViewById(R.id.pattern_textview);
        patternString.setText(PatternString);

        // Reset current index value, guess index, patternGuess and
        // Create animation ready pattern with an 0 to cap the end
        currentIndex = -1;
        guessIndex = 0;
        patternGuess = "";
        cappedPattern = PatternString + "0";

        // TODO: remove this temp
        TextView guessTextview = (TextView) findViewById(R.id.guess_textview);
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

                final View redButton = (View) findViewById(R.id.red_button);
                final View yellowButton = (View) findViewById(R.id.yellow_button);
                final View blueButton = (View) findViewById(R.id.blue_button);
                final LinearLayout greenButton = (LinearLayout) findViewById(R.id.green_button);

                patternHandler.post(new Runnable() {

                    @Override
                    public void run() {

                        Log.e("CurrentIndex", String.valueOf(currentIndex));

                        // get digit at currentIndex and display

                        char patternChar = cappedPattern.charAt(currentIndex);
                        TextView currentSquare = (TextView) findViewById(R.id.current_textview);
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

        final View redButton = (View) findViewById(R.id.red_button);
        final View yellowButton = (View) findViewById(R.id.yellow_button);
        final View blueButton = (View) findViewById(R.id.blue_button);
        final LinearLayout greenButton = (LinearLayout) findViewById(R.id.green_button);
        final TextView guessTextview = (TextView) findViewById(R.id.guess_textview);
        final TextView startButton = (TextView) findViewById(R.id.start_button);

        redButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (patternGuess.length() == (PatternString.length() - 1) && (patternGuess + "1").equals(PatternString)) {
                    patternGuess = patternGuess + "1";
                    userScore = patternGuess.length();
                    startButton.setText("Good Job!\nYour Score: " + userScore + "\nNext!");
                    startButton.setVisibility(View.VISIBLE);
                    startButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

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
                    startButton.setText("Good Game!\nYour Score: " + userScore + "\nTry Again?");
                    startButton.setVisibility(View.VISIBLE);
                    startButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            resetGame();
                        }
                    });
                }

            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (patternGuess.length() == (PatternString.length() - 1) && (patternGuess + "2").equals(PatternString)) {
                    patternGuess = patternGuess + "2";
                    userScore = patternGuess.length();
                    startButton.setText("Good Job!\nYour Score: " + userScore + "\nNext!");
                    startButton.setVisibility(View.VISIBLE);
                    startButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
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
                    startButton.setText("Good Game!\nYour Score: " + userScore + "\nTry Again?");
                    startButton.setVisibility(View.VISIBLE);
                    startButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            resetGame();
                        }
                    });
                }

            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (patternGuess.length() == (PatternString.length() - 1) && (patternGuess + "3").equals(PatternString)) {
                    patternGuess = patternGuess + "3";
                    userScore = patternGuess.length();
                    startButton.setText("Good Job!\nYour Score: " + userScore + "\nNext!");
                    startButton.setVisibility(View.VISIBLE);
                    startButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
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
                    startButton.setText("Good Game!\nYour Score: " + userScore + "\nTry Again?");
                    startButton.setVisibility(View.VISIBLE);
                    startButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            resetGame();
                        }
                    });
                }

            }
        });

        yellowButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (patternGuess.length() == (PatternString.length() - 1) && (patternGuess + "4").equals(PatternString)) {
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
                    startButton.setText("Good Game!\nYour Score: " + userScore + "\nTry Again?");
                    startButton.setVisibility(View.VISIBLE);
                    startButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            resetGame();
                        }
                    });
                }

            }
        });
    }

    private void resetGame() {
        PatternString = "";
        guessIndex = 0;

        final TextView startButton = (TextView) findViewById(R.id.start_button);
        startButton.setText("Go!");
        startButton.setVisibility(View.VISIBLE);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Display pattern to user
                startPattern();
            }
        });

    }

    private void disableButtons() {
        //Disable color buttons
        final View redButton = (View) findViewById(R.id.red_button);
        final View yellowButton = (View) findViewById(R.id.yellow_button);
        final View blueButton = (View) findViewById(R.id.blue_button);
        final LinearLayout greenButton = (LinearLayout) findViewById(R.id.green_button);
        redButton.setOnClickListener(null);
        yellowButton.setOnClickListener(null);
        blueButton.setOnClickListener(null);
        greenButton.setOnClickListener(null);
    }
}

