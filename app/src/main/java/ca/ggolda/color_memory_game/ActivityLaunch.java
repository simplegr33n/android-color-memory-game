package ca.ggolda.color_memory_game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageView leftLocked;
    private ImageView rightLocked;


    private int lastPlay = -1;

    private int currentView = 1;

    private int scoreClassic = 0;
    private int scoreDestijl = 0;
    private int scorePi = 0;

    private boolean twoUnlocked = false;
    private boolean threeUnlocked = false;
    private boolean fourUnlocked = false;
    private boolean fiveUnlocked = false;
    private boolean sixUnlocked = false;
    private boolean sevenUnlocked = false;

    private float x1, x2;
    static final int MIN_DISTANCE = 150;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        highscoreTextview = (TextView) findViewById(R.id.highscore);
        sharedPref = getSharedPreferences("ggco_colormem_values", MODE_PRIVATE);
        getValues();

        centerButton = (RelativeLayout) findViewById(R.id.center_button);
        swipeView = (LinearLayout) findViewById(R.id.swipeview);




        centerImageview = (ImageView) findViewById(R.id.center_icon);
        leftImageview = (ImageView) findViewById(R.id.left_imageview);
        rightImageview = (ImageView) findViewById(R.id.right_imageview);

        centerLocked = (ImageView) findViewById(R.id.center_locked);
        leftLocked = (ImageView) findViewById(R.id.left_locked);
        rightLocked = (ImageView) findViewById(R.id.right_locked);


        //set ontouch to swipeview
        swipeView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;

                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            // Left to Right swipe action
                            if (x2 > x1) {
                                Toast.makeText(ActivityLaunch.this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show();
                            }

                            // Right to left swipe action
                            else {
                                Toast.makeText(ActivityLaunch.this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // consider as something else - a screen tap for example
                        }
                        break;
                }
                return onTouchEvent(event);
            }

        });


        // set views
        setViews();


        // get current high scores from shared preferences
        getValues();

    }


    @Override
    protected void onResume() {
        super.onResume();

        getValues();

    }

    private void getValues() {
        sharedPref = getSharedPreferences("ggco_colormem_values", MODE_PRIVATE);

        lastPlay = sharedPref.getInt("last_play", -1);

        scoreClassic = sharedPref.getInt("highscore_classic", 0);
        scoreDestijl = sharedPref.getInt("highscore_destijl", 0);
        scorePi = sharedPref.getInt("highscore_pi", 0);
        // set high score to high score views if not 0
        if (scoreClassic != 0) {
            highscoreTextview.setText(String.valueOf(scoreClassic));
        }

        //TODO: conditions for unlocking
        // Set conditions for unlocking De Stijl
        if (scoreClassic == 5) {
            twoUnlocked = true;
        }

        // Set conditions for unlocking Pi
        if (scoreDestijl == 5) {
            threeUnlocked = true;
        }

    }


    // Set views and locks
    private void setViews() {
        switch (currentView) {
            case 1:
                // set highscore to scoreClassic
                if (scoreClassic != 0) {
                    String classicHigh = String.valueOf(scoreClassic);
                    highscoreTextview.setText(classicHigh);
                }

                centerImageview.setImageResource(R.drawable.board_classic);
                leftImageview.setImageResource(R.drawable.rectangle);
                rightImageview.setImageResource(R.drawable.board_destijl);

                if (twoUnlocked == true) {
                    rightLocked.setVisibility(View.GONE);
                }

                centerLocked.setVisibility(View.GONE);
                leftLocked.setVisibility(View.GONE);

                centerButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(ActivityLaunch.this, BoardClassic.class);
                        startActivity(intent);
                    }
                });


                break;
            case 2:
                // set highscore to scoreClassic
                highscoreTextview.setText(scoreDestijl);

                centerImageview.setImageResource(R.drawable.board_destijl);
                leftImageview.setImageResource(R.drawable.board_classic);
                rightImageview.setImageResource(R.drawable.rectangle);

                if (twoUnlocked == true) {
                    centerLocked.setVisibility(View.GONE);
                    centerButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(ActivityLaunch.this, BoardDeStijl.class);
                            startActivity(intent);
                        }
                    });
                }

                if (threeUnlocked == true) {
                    rightLocked.setVisibility(View.GONE);
                }

                leftLocked.setVisibility(View.GONE);


                break;
            case 3:
                // set highscore to scoreClassic
                highscoreTextview.setText(scorePi);

                // TODO: make pi resource
                centerImageview.setImageResource(R.drawable.rectangle);
                leftImageview.setImageResource(R.drawable.board_destijl);
                rightImageview.setImageResource(R.drawable.rectangle);

                if (twoUnlocked == true) {
                    leftLocked.setVisibility(View.GONE);
                }

                if (threeUnlocked == true) {
                    centerLocked.setVisibility(View.GONE);
                    centerButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(ActivityLaunch.this, BoardPi.class);
                            startActivity(intent);
                        }
                    });
                }
                if (fourUnlocked == true) {
                    rightLocked.setVisibility(View.GONE);
                }

                leftLocked.setVisibility(View.GONE);


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

