package ca.ggolda.color_memory_game;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;


public class ActivityBoard extends AppCompatActivity {

    private String PatternString = "123414";
    private int Min = 1;
    private int Max = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_layout);

        final LinearLayout gameBoard = (LinearLayout) findViewById(R.id.GameBoard);

        final TextView patternString = (TextView) findViewById(R.id.PatternString);
        patternString.setText(PatternString);

        // Do stuff inside layout when pressed
        // Note the use of OnTouchListener instead of OnClick for the
        // Responsive behaviour we're going to be looking for
        LinearLayout greenButton = (LinearLayout) findViewById(R.id.green_button);
        greenButton.setOnTouchListener(new View.OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    // Unnecessary Log
                    Log.d("Pressed", "Button pressed");

                    //Make background white on-pressed
                    gameBoard.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    // Add a Random value to the end of the Pattern String
                    Random r = new Random();
                    int i1 = r.nextInt(Max - Min + 1) + Min;
                    PatternString = PatternString + i1;
                    patternString.setText(PatternString);

                } else if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL)// && flag==true)
                {
                    // Another unnecessary Log
                    Log.d("Released", "Button released");
                    //Return background black on-released
                    gameBoard.setBackgroundColor(Color.parseColor("#000000"));
                    
                } else return false;


                //Must return true here in order to pick up ACTION_UP
                return true;
            }
        });

    }
}

