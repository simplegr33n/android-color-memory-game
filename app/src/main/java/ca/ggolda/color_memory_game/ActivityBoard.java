package ca.ggolda.color_memory_game;


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
    private String PatternString = "123414";
    private int currentIndex = 0;
    private int Min = 1;
    private int Max = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_layout);

        patternHandler = new Handler();

        final LinearLayout gameBoard = (LinearLayout) findViewById(R.id.GameBoard);

        final TextView patternString = (TextView) findViewById(R.id.pattern_textview);
        patternString.setText(PatternString);


        // Do stuff inside layout when pressed
        // Note the use of OnTouchListener instead of OnClick for the
        // Responsive behaviour we're going to be looking for
        LinearLayout greenButton = (LinearLayout) findViewById(R.id.green_button);
        greenButton.setOnTouchListener(new View.OnTouchListener() {

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

        final TextView startButton = (TextView) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Hide button once it's been pressed
                startButton.setVisibility(View.GONE);

                //Display pattern to user
                startPattern();

            }
        });
    }


    public void startPattern() {
        new Thread(new Task()).start();
    }

    class Task implements Runnable {

        @Override
        public void run() {

            for (int i = 0; i < PatternString.length(); i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                patternHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // get digit at currentIndex and display 
                        char currentDigit = PatternString.charAt(currentIndex);
                        currentIndex = currentIndex + 1;

                        TextView currentSquare = (TextView) findViewById(R.id.current_textview);
                        currentSquare.setText(String.valueOf(currentDigit));



                    }
                });
            }
        }
    }

}

