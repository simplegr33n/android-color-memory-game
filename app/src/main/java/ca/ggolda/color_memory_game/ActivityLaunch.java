package ca.ggolda.color_memory_game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by gcgol on 12/08/2016.
 */

public class ActivityLaunch extends AppCompatActivity {

    SharedPreferences sharedPref;

    private TextView highscoreTextview;

    private RelativeLayout centerButton;

    private ImageView centerImageview;
    private ImageView leftImageview;
    private ImageView rightImageview;

    private ImageView centerLocked;
    private ImageView leftLocked;
    private ImageView rightLocked;


    private int lastPlay = -1;

    private int scoreClassic = 0;
    private int scoreDestijl = 0;

    private boolean twoUnlocked = false;
    private boolean threeUnlocked = false;
    private boolean fourUnlocked = false;
    private boolean fiveUnlocked = false;
    private boolean sixUnlocked = false;
    private boolean sevenUnlocked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        sharedPref = getSharedPreferences("ggco_colormem_values", MODE_PRIVATE);
        getValues();

        centerButton = (RelativeLayout) findViewById(R.id.center_button);


        highscoreTextview = (TextView) findViewById(R.id.highscore);

        centerImageview = (ImageView) findViewById(R.id.center_icon);
        leftImageview = (ImageView) findViewById(R.id.left_imageview);
        rightImageview = (ImageView) findViewById(R.id.right_imageview);

        centerLocked = (ImageView) findViewById(R.id.center_locked);
        leftLocked = (ImageView) findViewById(R.id.left_locked);
        rightLocked = (ImageView) findViewById(R.id.right_locked);


        if (lastPlay != -1) {

            switch(lastPlay) {
                case 1:
                    // set highscore to scoreClassic
                    highscoreTextview.setText(scoreClassic);

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
                    }

                    if (threeUnlocked == true) {
                        rightLocked.setVisibility(View.GONE);
                    }

                    leftLocked.setVisibility(View.GONE);

                    centerButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(ActivityLaunch.this, BoardDeStijl.class);
                            startActivity(intent);
                        }
                    });


                    break;
                case 3:

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
        } else {
            // set highscore to ""
            highscoreTextview.setText("");

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
                    ;
                    startActivity(intent);
                }
            });


        }


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
        // set high score to high score views if not 0
        if (scoreClassic != 0) {
            String classicHigh = String.valueOf(scoreClassic);
            highscoreTextview = (TextView) findViewById(R.id.highscore);
            highscoreTextview.setText(classicHigh);
        }

        //TODO: conditions for unlocking
        // Set conditions for unlocking
        if (scoreClassic == 10) {
            twoUnlocked = true;
        }

    }
}
