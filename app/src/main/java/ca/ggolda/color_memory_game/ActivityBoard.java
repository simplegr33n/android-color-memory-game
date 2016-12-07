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
    private String PatternString = "";
    private String cappedPattern;
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

                // Add a Random value to the end of the Pattern String
                Random r = new Random();
                int i1 = r.nextInt(Max - Min + 1) + Min;
                PatternString = PatternString + i1;

                // Display updated PatternString
                TextView patternString = (TextView) findViewById(R.id.pattern_textview);
                patternString.setText(PatternString);

                // Reset current index and
                // Create animation ready pattern with an 0 to cap the end
                currentIndex = 0;
                cappedPattern = PatternString + "0";

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

            for (int i = 0; i < cappedPattern.length(); i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final View redButton = (View) findViewById(R.id.red_button);
                final View yellowButton = (View) findViewById(R.id.yellow_button);
                final View blueButton = (View) findViewById(R.id.blue_button);
                final LinearLayout greenButton = (LinearLayout) findViewById(R.id.green_button);
                final TextView startButton = (TextView) findViewById(R.id.start_button);

                patternHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // get digit at currentIndex and display
                        char patternChar = cappedPattern.charAt(currentIndex);
                        currentIndex = currentIndex + 1;
                        TextView currentSquare = (TextView) findViewById(R.id.current_textview);
                        currentSquare.setText(String.valueOf(patternChar));

                        // light up button corresponding to patternChar
                        if (String.valueOf(patternChar).equals("1")) {
                            redButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            greenButton.setBackgroundColor(Color.parseColor("#49f436"));
                            blueButton.setBackgroundColor(Color.parseColor("#0000FF"));
                            yellowButton.setBackgroundColor(Color.parseColor("#FFFB00"));

                        } else if (String.valueOf(patternChar).equals("2")) {

                            redButton.setBackgroundColor(Color.parseColor("#FF0000"));
                            greenButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            blueButton.setBackgroundColor(Color.parseColor("#0000FF"));
                            yellowButton.setBackgroundColor(Color.parseColor("#FFFB00"));

                        } else if (String.valueOf(patternChar).equals("3")) {

                            redButton.setBackgroundColor(Color.parseColor("#FF0000"));
                            greenButton.setBackgroundColor(Color.parseColor("#49f436"));
                            blueButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            yellowButton.setBackgroundColor(Color.parseColor("#FFFB00"));

                        } else if (String.valueOf(patternChar).equals("4")) {

                            redButton.setBackgroundColor(Color.parseColor("#FF0000"));
                            greenButton.setBackgroundColor(Color.parseColor("#49f436"));
                            blueButton.setBackgroundColor(Color.parseColor("#0000FF"));
                            yellowButton.setBackgroundColor(Color.parseColor("#FFFFFF"));

                        } else if (String.valueOf(patternChar).equals("0")) {

                            redButton.setBackgroundColor(Color.parseColor("#FF0000"));
                            greenButton.setBackgroundColor(Color.parseColor("#49f436"));
                            blueButton.setBackgroundColor(Color.parseColor("#0000FF"));
                            yellowButton.setBackgroundColor(Color.parseColor("#FFFB00"));

                            startButton.setVisibility(View.VISIBLE);

                        }

                    }
                });
            }
        }
    }

}

