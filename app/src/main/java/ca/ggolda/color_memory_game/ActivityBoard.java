package ca.ggolda.color_memory_game;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ActivityBoard extends AppCompatActivity {

    private String PatternString = "123414";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_layout);

        final LinearLayout gameBoard = (LinearLayout) findViewById(R.id.GameBoard);

        final TextView patternString = (TextView) findViewById(R.id.PatternString);
        patternString.setText(PatternString);

        // Do stuff inside layout when pressed
        // Note the use of OnTouchListener instead of OnClick for the
        // Responsive behaviour we're going to be
        // Looking for
        LinearLayout greenButton = (LinearLayout) findViewById(R.id.green_button);
        greenButton.setOnTouchListener(new View.OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Unnecessary Log
                    Log.d("Pressed", "Button pressed");

                    //Make background white on-pressed
                    gameBoard.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    // Add a value to the end of the Pattern String
                    PatternString = PatternString + "3";
                    patternString.setText(PatternString);

                }

                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Another unnecessary Log
                    Log.d("Released", "Button released");
                    //Return background black on-released
                    gameBoard.setBackgroundColor(Color.parseColor("#000000"));

                }

                // TODO Auto-generated method stub
                return false;
            }
        });

    }
}

