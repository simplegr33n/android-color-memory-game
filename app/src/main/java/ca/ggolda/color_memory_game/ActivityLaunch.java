package ca.ggolda.color_memory_game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by gcgol on 12/08/2016.
 */

public class ActivityLaunch extends AppCompatActivity {

    SharedPreferences sharedPref;

    private TextView highscoreTextview;

    private RelativeLayout centerButton;
    private LinearLayout swipeView;

    private ImageView centerImageview;
    private ImageView leftImageview;
    private ImageView rightImageview;

    private ImageView centerLocked;
    private ImageView rightLocked;
    private ImageView leftLocked;


    private int lastPlay;

    private int currentView = 1;

    private int scoreClassic = 0;
    private int scoreDestijl = 0;
    private int scorePi = 0;

    private ImageView levelOne;
    private ImageView levelTwo;
    private ImageView levelThree;
    private ImageView levelFour;
    private ImageView levelFive;
    private ImageView levelSix;
    private ImageView levelSeven;

    private ImageView twoLocked;
    private ImageView threeLocked;
    private ImageView fourLocked;
    private ImageView fiveLocked;
    private ImageView sixLocked;
    private ImageView sevenLocked;


    private int unlockedLevels;

    private float x1, x2;
    static final int MIN_DISTANCE = 150;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        highscoreTextview = (TextView) findViewById(R.id.highscore);
        sharedPref = getSharedPreferences("ggco_colormem_values", MODE_PRIVATE);


        centerButton = (RelativeLayout) findViewById(R.id.center_button);
        swipeView = (LinearLayout) findViewById(R.id.swipeview);


        // Set view on click of small icons
        //
        levelOne = (ImageView) findViewById(R.id.classic_small);
        levelTwo = (ImageView) findViewById(R.id.destijl_small);
        levelThree = (ImageView) findViewById(R.id.calc_small);

        levelOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setViews(1);
            }
        });

        levelTwo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setViews(2);
            }
        });

        levelThree.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setViews(3);
            }
        });



        twoLocked = (ImageView) findViewById(R.id.two_locked);
        threeLocked = (ImageView) findViewById(R.id.three_locked);
        fourLocked = (ImageView) findViewById(R.id.four_locked);
        fiveLocked = (ImageView) findViewById(R.id.five_locked);
        sixLocked = (ImageView) findViewById(R.id.six_locked);
        sevenLocked = (ImageView) findViewById(R.id.seven_locked);

        centerImageview = (ImageView) findViewById(R.id.center_icon);
        leftImageview = (ImageView) findViewById(R.id.left_imageview);
        rightImageview = (ImageView) findViewById(R.id.right_imageview);

        centerLocked = (ImageView) findViewById(R.id.center_locked);
        rightLocked = (ImageView) findViewById(R.id.right_locked);
        leftLocked = (ImageView) findViewById(R.id.left_locked);


        // set Get values, set views, and set small icons
        getValues();

    }


    @Override
    protected void onResume() {
        super.onResume();

        getValues();

    }

    private void getValues() {
        sharedPref = getSharedPreferences("ggco_colormem_values", MODE_PRIVATE);

        lastPlay = sharedPref.getInt("last_play", 1);

        unlockedLevels = sharedPref.getInt("unlocked_level", 1);

        scoreClassic = sharedPref.getInt("highscore_classic", 0);
        scoreDestijl = sharedPref.getInt("highscore_destijl", 0);
        scorePi = sharedPref.getInt("highscore_pi", 0);
        // set high score to high score views if not 0
        if (scoreClassic != 0) {
            highscoreTextview.setText(String.valueOf(scoreClassic));
        }

        //TODO: conditions for unlocking
        // Set conditions for unlocking De Stijl
        if (scoreClassic >= 5 && unlockedLevels == 1) {
            twoLocked.setVisibility(View.GONE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("unlocked_level", 2);
            editor.apply();
        }

        // Set conditions for unlocking Pi
        // TODO: make the requirement a sum of 31
        if (scoreDestijl + scoreClassic >= 10 && unlockedLevels == 2) {
            threeLocked.setVisibility(View.GONE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("unlocked_level", 3);
            editor.apply();
        }

        // Set conditions for unlocking ?
        if (scorePi >= 31 && unlockedLevels == 3) {
            fourLocked.setVisibility(View.GONE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("unlocked_level", 4);
            editor.apply();
        }
        unlockedLevels = sharedPref.getInt("unlocked_level", 1);

        // Set Small Buttons and Views after getting values
        setSmalls();
        setViews(lastPlay);

    }


    // set small icons
    private void setSmalls() {
        switch (unlockedLevels) {
            case 1:

                break;
            case 2:
                twoLocked.setVisibility(View.GONE);

                break;
            case 3:
                twoLocked.setVisibility(View.GONE);
                threeLocked.setVisibility(View.GONE);

                break;
            case 4:
                twoLocked.setVisibility(View.GONE);
                threeLocked.setVisibility(View.GONE);
                fourLocked.setVisibility(View.GONE);

                break;

        }
    }

    // Set views and locks
    private void setViews(int setView) {
        switch (setView) {
            case 1:
                // set highscore to scoreClassic
                highscoreTextview.setText("");
                if (scoreClassic != 0) {
                    highscoreTextview.setText(String.valueOf(scoreClassic));
                }

                leftLocked.setVisibility(View.GONE);

                centerLocked.setVisibility(View.GONE);
                centerImageview.setImageResource(R.drawable.board_classic);
                leftImageview.setImageResource(R.drawable.rectangle);
                rightImageview.setImageResource(R.drawable.board_destijl);

                centerButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Log as last_play
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("last_play", 1);
                        editor.apply();

                        Intent intent = new Intent(ActivityLaunch.this, BoardClassic.class);
                        startActivity(intent);
                    }
                });

                rightImageview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        setViews(2);
                    }
                });

                if (unlockedLevels >= 2) {
                    rightLocked.setVisibility(View.GONE);
                } else {
                    rightLocked.setVisibility(View.VISIBLE);
                }

                break;
            case 2:

                // set highscore to scoreClassic
                highscoreTextview.setText("");
                if (scoreDestijl != 0) {
                    highscoreTextview.setText(String.valueOf(scoreDestijl));
                }

                leftLocked.setVisibility(View.GONE);

                centerImageview.setImageResource(R.drawable.board_destijl);
                leftImageview.setImageResource(R.drawable.board_classic);
                rightImageview.setImageResource(R.drawable.board_pi);

                leftImageview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        setViews(1);
                    }
                });

                rightImageview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        setViews(3);
                    }
                });

                if (unlockedLevels >= 2) {
                    centerLocked.setVisibility(View.GONE);
                    twoLocked.setVisibility(View.GONE);
                    centerButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // Log as last_play
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("last_play", 2);
                            editor.apply();

                            Intent intent = new Intent(ActivityLaunch.this, BoardDeStijl.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    centerButton.setOnClickListener(null);
                    centerLocked.setVisibility(View.VISIBLE);
                }

                if (unlockedLevels >= 3) {
                    rightLocked.setVisibility(View.GONE);
                } else {
                    rightLocked.setVisibility(View.VISIBLE);
                }


                break;
            case 3:

                // set highscore to scoreClassic
                highscoreTextview.setText("");
                if (scorePi != 0) {
                    highscoreTextview.setText(String.valueOf(scorePi));
                }


                // TODO: make pi resource
                centerImageview.setImageResource(R.drawable.board_pi);
                leftImageview.setImageResource(R.drawable.board_destijl);
                rightImageview.setImageResource(R.drawable.rectangle);

                leftImageview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        setViews(2);
                    }
                });

                if (unlockedLevels < 2) {
                    leftLocked.setVisibility(View.VISIBLE);
                } else {
                    leftLocked.setVisibility(View.GONE);
                }


                if (unlockedLevels >= 3) {
                    centerLocked.setVisibility(View.GONE);
                    twoLocked.setVisibility(View.GONE);
                    threeLocked.setVisibility(View.GONE);
                    centerButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // Log as last_play
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("last_play", 3);
                            editor.apply();

                            Intent intent = new Intent(ActivityLaunch.this, BoardPi.class);
                            startActivity(intent);
                        }
                    });

                } else {
                    centerButton.setOnClickListener(null);
                    centerLocked.setVisibility(View.VISIBLE);
                }

                if (unlockedLevels >= 4) {
                    rightLocked.setVisibility(View.GONE);
                } else {
                    rightLocked.setVisibility(View.VISIBLE);
                }


                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;


        }
    }
}

