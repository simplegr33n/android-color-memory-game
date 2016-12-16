package ca.ggolda.color_memory_game;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Random;


public class BoardDeStijl extends AppCompatActivity {

    private Handler patternHandler;

    private ArrayList<Integer> patternList;
    private ArrayList<Integer> cappedPattern;
    private int currentIndex = -1;

    private ArrayList<Integer> guessList;
    private int guessIndex = 0;

    private int userScore = 0;

    private int Min = 1;
    private int Max = 17;
    private int flashTime = 350;
    private String flashColor = "#F438F7";

    private int sleepTime;

    private LinearLayout gameBoard;
    private RelativeLayout breakLayout;

    private TextView messageTextview;
    private TextView scoreTextview;
    private TextView highscoreTextview;

    private View button1;
    private View button2;
    private View button3;
    private View button4;
    private View button5;
    private View button6;
    private View button7;
    private View button8;
    private View button9;
    private View button10;
    private View button11;
    private View button12;
    private View button13;
    private View button14;
    private View button15;
    private View button16;
    private View button17;


    private View targetQuad;
    private View offQuad1;
    private View offQuad2;
    private View offQuad3;
    private View offQuad4;
    private View offQuad5;
    private View offQuad6;
    private View offQuad7;
    private View offQuad8;
    private View offQuad9;
    private View offQuad10;
    private View offQuad11;
    private View offQuad12;
    private View offQuad13;
    private View offQuad14;
    private View offQuad15;
    private View offQuad16;

    private String targetColor;
    private String offColor1;
    private String offColor2;
    private String offColor3;
    private String offColor4;
    private String offColor5;
    private String offColor6;
    private String offColor7;
    private String offColor8;
    private String offColor9;
    private String offColor10;
    private String offColor11;
    private String offColor12;
    private String offColor13;
    private String offColor14;
    private String offColor15;
    private String offColor16;


    SharedPreferences sharedPref;
    private int highScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_destijl);

        // initialize MobileAds
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6262252551936427~5382545595");

        // Request add and load into adView
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // get current high score and sleeptime from shared preferences
        sharedPref = getSharedPreferences("ggco_colormem_values", MODE_PRIVATE);
        highScore = sharedPref.getInt("highscore_destijl", 0);
        sleepTime = sharedPref.getInt("sleep_time", 850);

        gameBoard = (LinearLayout) findViewById(R.id.GameBoard);
        breakLayout = (RelativeLayout) findViewById(R.id.BreakLayout);


        messageTextview = (TextView) findViewById(R.id.message);
        scoreTextview = (TextView) findViewById(R.id.score);
        // TODO: Maybe replace this with a rounded star somehow... FontAwesome?
        scoreTextview.setTextSize(94);
        scoreTextview.setText("GO");
        highscoreTextview = (TextView) findViewById(R.id.highscore);
        if (highScore != 0) {
            highscoreTextview.setText("Highscore:"  + String.valueOf(highScore));
        }

        patternList = new ArrayList<Integer>();


        button1 = (View) findViewById(R.id.button1);
        button2 = (View) findViewById(R.id.button2);
        button3 = (View) findViewById(R.id.button3);
        button4 = (View) findViewById(R.id.button4);
        button5 = (View) findViewById(R.id.button5);
        button6 = (View) findViewById(R.id.button6);
        button7 = (View) findViewById(R.id.button7);
        button8 = (View) findViewById(R.id.button8);
        button9 = (View) findViewById(R.id.button9);
        button10 = (View) findViewById(R.id.button10);
        button11 = (View) findViewById(R.id.button11);
        button12 = (View) findViewById(R.id.button12);
        button13 = (View) findViewById(R.id.button13);
        button14 = (View) findViewById(R.id.button14);
        button15 = (View) findViewById(R.id.button15);
        button16 = (View) findViewById(R.id.button16);
        button17 = (View) findViewById(R.id.button17);


        patternHandler = new Handler();


        //Display pattern to user
        breakLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // increase textsize for number display
                scoreTextview.setTextSize(164);
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

        // Add a Random value to the end of the patternList
        Random r = new Random();
        int i1 = r.nextInt(Max - Min + 1) + Min;
        patternList.add(i1);

        // Reset current index value, guess index, guessList and
        // Create animation ready pattern with an 0 to cap the end
        currentIndex = -1;
        guessIndex = 0;
        guessList = new ArrayList<Integer>();

        // TODO: find another way than this
        // I imagine this cloning is not
        // great for performance
        patternList.add(0);

        Log.e("CAPPEDINFO", "" + patternList);


        // Start new thread for animating sequence
        new Thread(new Task()).start();
    }

    class Task implements Runnable {

        @Override
        public void run() {

            for (int i = 0; i < (patternList.size()); i++) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if ((currentIndex + 1) == patternList.size()) {
                    guessPlay();
                } else {
                    currentIndex = currentIndex + 1;
                }

                patternHandler.post(new Runnable() {

                    @Override
                    public void run() {

                        // get digit at currentIndex and display
                        int patternChar = patternList.get(currentIndex);

                        // light up button corresponding to patternChar
                        if (String.valueOf(patternChar).equals("1")) {
                            flashButton(1);
                        } else if (String.valueOf(patternChar).equals("2")) {
                            flashButton(2);
                        } else if (String.valueOf(patternChar).equals("3")) {
                            flashButton(3);
                        } else if (String.valueOf(patternChar).equals("4")) {
                            flashButton(4);
                        } else if (String.valueOf(patternChar).equals("5")) {
                            flashButton(5);
                        } else if (String.valueOf(patternChar).equals("6")) {
                            flashButton(6);
                        } else if (String.valueOf(patternChar).equals("7")) {
                            flashButton(7);
                        } else if (String.valueOf(patternChar).equals("8")) {
                            flashButton(8);
                        } else if (String.valueOf(patternChar).equals("9")) {
                            flashButton(9);
                        } else if (String.valueOf(patternChar).equals("10")) {
                            flashButton(10);
                        } else if (String.valueOf(patternChar).equals("11")) {
                            flashButton(11);
                        } else if (String.valueOf(patternChar).equals("12")) {
                            flashButton(12);
                        } else if (String.valueOf(patternChar).equals("13")) {
                            flashButton(13);
                        } else if (String.valueOf(patternChar).equals("14")) {
                            flashButton(14);
                        } else if (String.valueOf(patternChar).equals("15")) {
                            flashButton(15);
                        } else if (String.valueOf(patternChar).equals("16")) {
                            flashButton(16);
                        } else if (String.valueOf(patternChar).equals("17")) {
                            flashButton(17);
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

        if (guessList.size() == (patternList.size() - 1) && (patternList.get(patternList.size() - 1)).equals(press)) {
            //Disable buttons while start menu up
            disableButtons();

            scoreTextview.setTextColor(Color.parseColor("#FFFFFF"));

            guessList.add(press);

            userScore = guessList.size();

            // if new usesScore beats old high score, change value in shared preferences
            if (userScore >= highScore) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("highscore_destijl", userScore);
                editor.apply();

                highScore = sharedPref.getInt("highscore_destijl", 0);
            }


            messageTextview.setText("Great!");
            messageTextview.setTextColor(Color.parseColor("#e0ff8c"));
            scoreTextview.setText(String.valueOf(userScore));
            highscoreTextview.setText("Highscore:"  + String.valueOf(highScore));

            breakLayout.setVisibility(View.VISIBLE);
            breakLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Display pattern to user
                    startPattern();
                }
            });


        } else if (((patternList.get(guessIndex))).equals(press)) {
            guessList.add(press);
            guessIndex = guessIndex + 1;


        } else {
            //Disable buttons while start menu up
            disableButtons();
            patternList = new ArrayList<Integer>();

            messageTextview.setTextColor(Color.parseColor("#FF8949"));
            messageTextview.setText("Good\nGame!");

            scoreTextview.setTextColor(Color.parseColor("#FF0000"));
            scoreTextview.setText(String.valueOf(userScore));


            breakLayout.setVisibility(View.VISIBLE);
            breakLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    patternList = new ArrayList<Integer>();
                    guessIndex = 0;
                    userScore = 0;

                    startPattern();
                }
            });

        }

    }

    private void guessPlay() {

        button1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button1.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    // send guess to guessPress() function
                    guessPress(1);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button2.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    // send guess to guessPress() function
                    guessPress(2);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button3.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button3.setBackgroundColor(Color.parseColor("#FF0000"));
                    // send guess to guessPress() function
                    guessPress(3);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button4.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button4.setBackgroundColor(Color.parseColor("#4242AE"));
                    // send guess to guessPress() function
                    guessPress(4);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button5.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button5.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    // send guess to guessPress() function
                    guessPress(5);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button6.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button6.setBackgroundColor(Color.parseColor("#FFFB00"));
                    // send guess to guessPress() function
                    guessPress(6);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button7.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button7.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    // send guess to guessPress() function
                    guessPress(7);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button8.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button8.setBackgroundColor(Color.parseColor("#FF0000"));
                    // send guess to guessPress() function
                    guessPress(8);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button9.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button9.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button9.setBackgroundColor(Color.parseColor("#FFFB00"));
                    // send guess to guessPress() function
                    guessPress(9);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button10.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button10.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button10.setBackgroundColor(Color.parseColor("#FF0000"));
                    // send guess to guessPress() function
                    guessPress(10);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button11.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button11.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button11.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    // send guess to guessPress() function
                    guessPress(11);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button12.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button12.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button12.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    // send guess to guessPress() function
                    guessPress(12);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button13.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button13.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button13.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    // send guess to guessPress() function
                    guessPress(13);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button14.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button14.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button14.setBackgroundColor(Color.parseColor("#FF0000"));
                    // send guess to guessPress() function
                    guessPress(14);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button15.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button15.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button15.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    // send guess to guessPress() function
                    guessPress(15);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button16.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button16.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button16.setBackgroundColor(Color.parseColor("#4242AE"));
                    // send guess to guessPress() function
                    guessPress(16);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

        button17.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Make background on-pressed TODO: weird pink for now, pick better color
                    button17.setBackgroundColor(Color.parseColor(flashColor));

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    //Return button background on-released
                    button17.setBackgroundColor(Color.parseColor("#FF0000"));
                    // send guess to guessPress() function
                    guessPress(17);

                } else return false;

                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });


    }



    // Set views for animateFlash() function
    private void flashButton(int quad) {

        if (quad == 1) {
            targetQuad = (View) findViewById(R.id.button1);
            offQuad1 = (View) findViewById(R.id.button2);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#FFFFFF";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 2) {
            targetQuad = (View) findViewById(R.id.button2);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#FFFFFF";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 3) {
            targetQuad = (View) findViewById(R.id.button3);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button2);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#FF0000";
            offColor1 = "#FFFFFF";
            offColor2 = "#FFFFFF";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 4) {
            targetQuad = (View) findViewById(R.id.button4);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button2);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#4242AE";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#FFFFFF";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 5) {
            targetQuad = (View) findViewById(R.id.button5);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button2);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#FFFFFF";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 6) {
            targetQuad = (View) findViewById(R.id.button6);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button2);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#FFFB00";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFFFF";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 7) {
            targetQuad = (View) findViewById(R.id.button7);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button2);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#FFFFFF";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 8) {
            targetQuad = (View) findViewById(R.id.button8);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button2);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#FF0000";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FFFFFF";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 9) {
            targetQuad = (View) findViewById(R.id.button9);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button2);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#FFFB00";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFFFF";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 10) {
            targetQuad = (View) findViewById(R.id.button10);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button2);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#FF0000";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FFFFFF";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 11) {
            targetQuad = (View) findViewById(R.id.button11);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button2);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#FFFFFF";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 12) {
            targetQuad = (View) findViewById(R.id.button12);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button2);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#FFFFFF";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 13) {
            targetQuad = (View) findViewById(R.id.button13);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button2);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#FFFFFF";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 14) {
            targetQuad = (View) findViewById(R.id.button14);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button2);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#FF0000";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FFFFFF";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 15) {
            targetQuad = (View) findViewById(R.id.button15);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button2);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#FFFFFF";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 16) {
            targetQuad = (View) findViewById(R.id.button16);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button2);
            offQuad16 = (View) findViewById(R.id.button17);

            targetColor = "#4242AE";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#FFFFFF";
            offColor16 = "#FF0000";

            animateFlash();

        } else if (quad == 17) {
            targetQuad = (View) findViewById(R.id.button17);
            offQuad1 = (View) findViewById(R.id.button1);
            offQuad2 = (View) findViewById(R.id.button3);
            offQuad3 = (View) findViewById(R.id.button4);
            offQuad4 = (View) findViewById(R.id.button5);
            offQuad5 = (View) findViewById(R.id.button6);
            offQuad6 = (View) findViewById(R.id.button7);
            offQuad7 = (View) findViewById(R.id.button8);
            offQuad8 = (View) findViewById(R.id.button9);
            offQuad9 = (View) findViewById(R.id.button10);
            offQuad10 = (View) findViewById(R.id.button11);
            offQuad11 = (View) findViewById(R.id.button12);
            offQuad12 = (View) findViewById(R.id.button13);
            offQuad13 = (View) findViewById(R.id.button14);
            offQuad14 = (View) findViewById(R.id.button15);
            offQuad15 = (View) findViewById(R.id.button16);
            offQuad16 = (View) findViewById(R.id.button2);

            targetColor = "#FF0000";
            offColor1 = "#FFFFFF";
            offColor2 = "#FF0000";
            offColor3 = "#4242AE";
            offColor4 = "#FFFFFF";
            offColor5 = "#FFFB00";
            offColor6 = "#FFFFFF";
            offColor7 = "#FF0000";
            offColor8 = "#FFFB00";
            offColor9 = "#FF0000";
            offColor10 = "#FFFFFF";
            offColor11 = "#FFFFFF";
            offColor12 = "#FFFFFF";
            offColor13 = "#FF0000";
            offColor14 = "#FFFFFF";
            offColor15 = "#4242AE";
            offColor16 = "#FFFFFF";

            animateFlash();

        } else if (quad == 0) {

            //return all buttons to base color
            button1.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button2.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button3.setBackgroundColor(Color.parseColor("#FF0000"));
            button4.setBackgroundColor(Color.parseColor("#4242AE"));
            button5.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button6.setBackgroundColor(Color.parseColor("#FFFB00"));
            button7.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button8.setBackgroundColor(Color.parseColor("#FF0000"));
            button9.setBackgroundColor(Color.parseColor("#FFFB00"));
            button10.setBackgroundColor(Color.parseColor("#FF0000"));
            button11.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button12.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button13.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button14.setBackgroundColor(Color.parseColor("#FF0000"));
            button15.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button16.setBackgroundColor(Color.parseColor("#4242AE"));
            button17.setBackgroundColor(Color.parseColor("#FF0000"));

            // Remove cap to prepare for next run
            patternList.remove(patternList.size() - 1);


            // Set background to ready color
            gameBoard.setBackgroundColor(Color.parseColor("#000000"));

            // Allow User to Guess
            guessPlay();
        }
    }

    private void animateFlash() {
        offQuad1.setBackgroundColor(Color.parseColor(offColor1));
        offQuad2.setBackgroundColor(Color.parseColor(offColor2));
        offQuad3.setBackgroundColor(Color.parseColor(offColor3));
        offQuad4.setBackgroundColor(Color.parseColor(offColor4));
        offQuad5.setBackgroundColor(Color.parseColor(offColor5));
        offQuad6.setBackgroundColor(Color.parseColor(offColor6));
        offQuad7.setBackgroundColor(Color.parseColor(offColor7));
        offQuad8.setBackgroundColor(Color.parseColor(offColor8));
        offQuad9.setBackgroundColor(Color.parseColor(offColor9));
        offQuad10.setBackgroundColor(Color.parseColor(offColor10));
        offQuad11.setBackgroundColor(Color.parseColor(offColor11));
        offQuad12.setBackgroundColor(Color.parseColor(offColor12));
        offQuad13.setBackgroundColor(Color.parseColor(offColor13));
        offQuad14.setBackgroundColor(Color.parseColor(offColor14));
        offQuad15.setBackgroundColor(Color.parseColor(offColor15));
        offQuad16.setBackgroundColor(Color.parseColor(offColor16));

        // Animate from white back to color in flashTime milliseconds
        int colorFrom = Color.parseColor(flashColor);
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
        button1.setOnTouchListener(null);
        button2.setOnTouchListener(null);
        button3.setOnTouchListener(null);
        button4.setOnTouchListener(null);
        button5.setOnTouchListener(null);
        button6.setOnTouchListener(null);
        button7.setOnTouchListener(null);
        button8.setOnTouchListener(null);
        button9.setOnTouchListener(null);
        button10.setOnTouchListener(null);
        button11.setOnTouchListener(null);
        button12.setOnTouchListener(null);
        button13.setOnTouchListener(null);
        button14.setOnTouchListener(null);
        button15.setOnTouchListener(null);
        button16.setOnTouchListener(null);
        button17.setOnTouchListener(null);


        //return all buttons to base color
        button1.setBackgroundColor(Color.parseColor("#FFFFFF"));
        button2.setBackgroundColor(Color.parseColor("#FFFFFF"));
        button3.setBackgroundColor(Color.parseColor("#FF0000"));
        button4.setBackgroundColor(Color.parseColor("#4242AE"));
        button5.setBackgroundColor(Color.parseColor("#FFFFFF"));
        button6.setBackgroundColor(Color.parseColor("#FFFB00"));
        button7.setBackgroundColor(Color.parseColor("#FFFFFF"));
        button8.setBackgroundColor(Color.parseColor("#FF0000"));
        button9.setBackgroundColor(Color.parseColor("#FFFB00"));
        button10.setBackgroundColor(Color.parseColor("#FF0000"));
        button11.setBackgroundColor(Color.parseColor("#FFFFFF"));
        button12.setBackgroundColor(Color.parseColor("#FFFFFF"));
        button13.setBackgroundColor(Color.parseColor("#FFFFFF"));
        button14.setBackgroundColor(Color.parseColor("#FF0000"));
        button15.setBackgroundColor(Color.parseColor("#FFFFFF"));
        button16.setBackgroundColor(Color.parseColor("#4242AE"));
        button17.setBackgroundColor(Color.parseColor("#FF0000"));

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BoardDeStijl.this, ActivityLaunch.class);
        startActivity(intent);

        return;
    }
}

