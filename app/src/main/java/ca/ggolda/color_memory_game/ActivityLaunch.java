package ca.ggolda.color_memory_game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by gcgol on 12/08/2016.
 */

public class ActivityLaunch extends AppCompatActivity {

    SharedPreferences sharedPref;

    private LinearLayout classicButton;
    private LinearLayout destijlButton;

    private TextView classicScore;
    private TextView destijlScore;

    private int scoreClassic;
    private int scoreDestijl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        classicButton = (LinearLayout) findViewById(R.id.classic_button);
        classicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLaunch.this, BoardClassic.class);;
                startActivity(intent);

            }
        });

        destijlButton = (LinearLayout) findViewById(R.id.destijl_button);
        destijlButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLaunch.this, BoardDeStijl.class);;
                startActivity(intent);
            }
        });



        // get current high score from shared preferences
        // TODO: cross activity shared prefs so i can actually see scores here
        sharedPref = this.getPreferences(this.MODE_PRIVATE);
        scoreClassic = sharedPref.getInt("high_score_classic", 0);
        scoreDestijl = sharedPref.getInt("high_score_destijl", 0);
        // set high score to high score views
        classicScore = (TextView) findViewById(R.id.classic_score);
        classicScore.setText("High Score: " + scoreClassic);
        destijlScore = (TextView) findViewById(R.id.destijl_score);
        destijlScore.setText("High Score: " + scoreDestijl);

    }


}
