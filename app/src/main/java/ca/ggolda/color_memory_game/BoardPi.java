package ca.ggolda.color_memory_game;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public class BoardPi extends AppCompatActivity {

    private String pi = "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679";
    private String guess = "" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_pi);

    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BoardPi.this, ActivityLaunch.class);
        startActivity(intent);

        return;
    }
}
