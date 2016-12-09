package ca.ggolda.color_memory_game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
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


    private TextView classicScore;
    private TextView destijlScore;

    private int scoreClassic;
    private int scoreDestijl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        RelativeLayout classicButton = (RelativeLayout) findViewById(R.id.classic_button);
        classicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLaunch.this, BoardClassic.class);;
                startActivity(intent);

            }
        });

        RelativeLayout destijlButton = (RelativeLayout) findViewById(R.id.destijl_button);
        destijlButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLaunch.this, BoardDeStijl.class);;
                startActivity(intent);
            }
        });




        // get current high score from shared preferences
        sharedPref = getSharedPreferences("ggco_colormem_values", MODE_PRIVATE);
        scoreClassic = sharedPref.getInt("highscore_classic", 0);
        scoreDestijl = sharedPref.getInt("highscore_destijl", 0);
        // set high score to high score views
        String classicHigh = "High Score: " + scoreClassic;
        String destijlHigh = "High Score: " + scoreDestijl;
        classicScore = (TextView) findViewById(R.id.classic_score);
        classicScore.setText(classicHigh);
        destijlScore = (TextView) findViewById(R.id.destijl_score);
        destijlScore.setText(destijlHigh);


    }


    @Override
    protected void onResume() {
        super.onResume();

        // get current high score from shared preferences
        scoreClassic = sharedPref.getInt("highscore_classic", 0);
        scoreDestijl = sharedPref.getInt("highscore_destijl", 0);
        String classicHigh = "High Score: " + scoreClassic;
        String destijlHigh = "High Score: " + scoreDestijl;
        // set high score to high score views
        classicScore.setText(classicHigh);
        destijlScore.setText(destijlHigh);


    }
}
