package ca.ggolda.color_memory_game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by gcgol on 12/08/2016.
 */

public class ActivityLaunch extends AppCompatActivity {


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mHighscoreDatabaseReference;

    SharedPreferences sharedPref;

    private TextView highscoreTextview;
    private TextView recordTextview;
    private TextView levelName;


    private ImageView centerImageview;
    private ImageView leftImageview;
    private ImageView rightImageview;

    private ImageView centerLocked;
    private ImageView rightLocked;
    private ImageView leftLocked;


    private int centerPlay;

    private int scoreClassic = 0;
    private int scoreDestijl = 0;
    private int scorePi = 0;

    private ImageView levelOne;
    private ImageView levelTwo;
    private ImageView levelThree;

    private ImageView twoLocked;
    private ImageView threeLocked;


    private Integer classic_record;
    private Integer de_stijl_record;
    private Integer pi_record;

    private int unlockedLevels;

    private float x1, x2;
    static final int MIN_DISTANCE = 150;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        levelName = (TextView) findViewById(R.id.level_name);

        // Get user highscore
        highscoreTextview = (TextView) findViewById(R.id.highscore);
        sharedPref = getSharedPreferences("ggco_colormem_values", MODE_PRIVATE);

        //get world records
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mHighscoreDatabaseReference = mFirebaseDatabase.getReference().child("highscore");
        recordTextview = (TextView) findViewById(R.id.record);


        ImageView settingsButton = (ImageView) findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ActivityLaunch.this, ActivitySettings.class);
                startActivity(intent);
            }
        });


        // Set view on click of small icons
        //
        levelOne = (ImageView) findViewById(R.id.classic_small);
        levelTwo = (ImageView) findViewById(R.id.destijl_small);
        levelThree = (ImageView) findViewById(R.id.calc_small);

        levelOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                centerPlay = 1;
                setViews(1);
            }
        });

        levelTwo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                centerPlay = 2;
                setViews(2);
            }
        });

        levelThree.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                centerPlay = 3;
                setViews(3);
            }
        });


        twoLocked = (ImageView) findViewById(R.id.two_locked);
        threeLocked = (ImageView) findViewById(R.id.three_locked);

        centerImageview = (ImageView) findViewById(R.id.center_icon);
        leftImageview = (ImageView) findViewById(R.id.left_imageview);
        rightImageview = (ImageView) findViewById(R.id.right_imageview);

        centerLocked = (ImageView) findViewById(R.id.center_locked);
        rightLocked = (ImageView) findViewById(R.id.right_locked);
        leftLocked = (ImageView) findViewById(R.id.left_locked);

        // set Get values, set views, and set small icons
        getValues();


        // get records and values again..
        // TODO: find better way of setting views properly the first time
        getRecords();
        getValues();

    }

    // Swipe functionality for lauch icons
    @Override
    public boolean onTouchEvent(MotionEvent event) {

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
                        if (centerPlay - 1 >= 1) {
                            centerPlay = centerPlay - 1;
                            setViews(centerPlay);
                        } else {
                            centerPlay = 3;
                            setViews(centerPlay);
                        }
                    }

                    // Right to left swipe action
                    else if (x2 < x1) {

                        if (centerPlay + 1 <= 3) {
                            centerPlay = centerPlay + 1;
                            setViews(centerPlay);
                        } else {
                            centerPlay = 1;
                            setViews(centerPlay);
                        }
                    } else {

                    }

                } else {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    // consider as something else - a screen tap for example
                    if (deltaX == 0) {


                        if (centerPlay == 1) {
                            // Log as last_play
                            editor.putInt("last_play", 1);
                            editor.apply();

                            Intent intent_classic = new Intent(ActivityLaunch.this, BoardClassic.class);
                            startActivity(intent_classic);
                        }

                        // check if level 2 unlocked
                        if (centerPlay == 2 && unlockedLevels >= 2) {
                            // Log as last_play
                            editor.putInt("last_play", 2);
                            editor.apply();

                            Intent intent_destijl = new Intent(ActivityLaunch.this, BoardDeStijl.class);
                            startActivity(intent_destijl);
                        }
                        // check if level 3 unlocked
                        if (centerPlay == 3 && unlockedLevels >= 3) {
                            // Log as last_play
                            editor.putInt("last_play", 3);
                            editor.apply();

                            Intent intent_pi = new Intent(ActivityLaunch.this, BoardPi.class);
                            startActivity(intent_pi);
                        }





                    }
                }

        }
        return super.onTouchEvent(event);

    }


    @Override
    protected void onResume() {
        super.onResume();

        getValues();

    }

    private void getValues() {
        sharedPref = getSharedPreferences("ggco_colormem_values", MODE_PRIVATE);

        // get centerPlay for int to give setView
        centerPlay = sharedPref.getInt("last_play", 1);

        unlockedLevels = sharedPref.getInt("unlocked_level", 1);

        scoreClassic = sharedPref.getInt("highscore_classic", 0);
        scoreDestijl = sharedPref.getInt("highscore_destijl", 0);
        scorePi = sharedPref.getInt("highscore_pi", 0);

        classic_record = sharedPref.getInt("classic_record", 0);
        de_stijl_record = sharedPref.getInt("de_stijl_record", 0);
        pi_record = sharedPref.getInt("pi_record", 0);

        if (scoreClassic > classic_record) {
            classic_record = scoreClassic;
        }

        if (scoreDestijl > de_stijl_record) {
            de_stijl_record = scoreDestijl;
        }

        if (scorePi > pi_record) {
            pi_record = scorePi;
        }

        // set high score to high score views if not 0
        if (scoreClassic != 0) {
            highscoreTextview.setText(String.valueOf(scoreClassic));
        }

        //TODO: conditions for unlocking
        // Set conditions for unlocking De Stijl
        if (scoreClassic >= 10 && unlockedLevels == 1) {
            twoLocked.setVisibility(View.GONE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("unlocked_level", 2);
            editor.apply();
        }

        // Set conditions for unlocking Pi
        // TODO: make the requirement a sum of 31
        if (scoreDestijl >= 10 && unlockedLevels == 2) {
            threeLocked.setVisibility(View.GONE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("unlocked_level", 3);
            editor.apply();
        }

        // Set conditions for unlocking ?
//        if (scorePi >= 31 && unlockedLevels == 3) {
//            fourLocked.setVisibility(View.GONE);
//
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putInt("unlocked_level", 4);
//            editor.apply();
//        }
//        unlockedLevels = sharedPref.getInt("unlocked_level", 1);

        //Set views after getting all values
        setViews(centerPlay);

    }

    // get world records
    private void getRecords() {
        mHighscoreDatabaseReference.child("classic").
                addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(DataSnapshot dataSnapshot) {
                                              classic_record = dataSnapshot.getValue(Integer.class);

                                              if (classic_record != null) {
                                                  SharedPreferences.Editor editor = sharedPref.edit();
                                                  editor.putInt("classic_record", classic_record);
                                                  editor.apply();
                                                  if (scoreClassic > classic_record) {
                                                      mHighscoreDatabaseReference.child("classic").setValue(scoreClassic);
                                                      getValues();
                                                  }

                                              } else if (classic_record == null || scoreClassic > 0) {
                                                  mHighscoreDatabaseReference.child("classic").setValue(scoreClassic);
                                                  getValues();

                                              } else {
                                                  classic_record = 0;
                                              }


                                              getValues();


                                              //Set current world record
                                              if (scoreClassic != 0 && classic_record == null || scoreClassic > Integer.valueOf(classic_record)) {
                                                  mHighscoreDatabaseReference.child("classic").setValue(scoreClassic);
                                                  getValues();
                                              }
                                          }

                                          @Override
                                          public void onCancelled(DatabaseError databaseError) {
                                              System.out.println("The read failed: " + databaseError.getCode());
                                          }
                                      }

                );

        mHighscoreDatabaseReference.child("de_stijl").
                addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(DataSnapshot dataSnapshot) {
                                              de_stijl_record = dataSnapshot.getValue(Integer.class);

                                              if (de_stijl_record != null) {
                                                  SharedPreferences.Editor editor = sharedPref.edit();
                                                  editor.putInt("de_stijl_record", de_stijl_record);
                                                  editor.apply();
                                                  if (scoreDestijl > de_stijl_record) {
                                                      mHighscoreDatabaseReference.child("de_stijl").setValue(scoreDestijl);
                                                      getValues();
                                                  }

                                              } else if (de_stijl_record == null || scoreDestijl > 0) {
                                                  mHighscoreDatabaseReference.child("de_stijl").setValue(scoreDestijl);
                                                  getValues();

                                              } else {
                                                  de_stijl_record = 0;
                                              }

                                              getValues();

                                              //Set current world record
                                              if (scoreDestijl != 0 && de_stijl_record == null || scoreDestijl > Integer.valueOf(de_stijl_record)) {
                                                  mHighscoreDatabaseReference.child("de_stijl").setValue(scoreDestijl);
                                                  getValues();
                                              }
                                          }

                                          @Override
                                          public void onCancelled(DatabaseError databaseError) {
                                              System.out.println("The read failed: " + databaseError.getCode());
                                          }
                                      }

                );

        mHighscoreDatabaseReference.child("pi").
                addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(DataSnapshot dataSnapshot) {
                                              pi_record = dataSnapshot.getValue(Integer.class);

                                              if (pi_record != null) {
                                                  SharedPreferences.Editor editor = sharedPref.edit();
                                                  editor.putInt("pi_record", pi_record);
                                                  editor.apply();
                                                  if (scorePi > pi_record) {
                                                      mHighscoreDatabaseReference.child("pi").setValue(scorePi);
                                                      getValues();
                                                  }

                                              } else if (pi_record == null || scorePi > 0) {
                                                  mHighscoreDatabaseReference.child("pi").setValue(scorePi);
                                                  getValues();

                                              } else {
                                                  pi_record = 0;
                                              }

                                              getValues();


                                          }

                                          @Override
                                          public void onCancelled(DatabaseError databaseError) {
                                              System.out.println("The read failed: " + databaseError.getCode());
                                          }
                                      }

                );
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

        }
    }

    // Set views and locks
    private void setViews(int setView) {
        // Set Small Buttons
        setSmalls();

        switch (setView) {
            case 1:
                // set highscore to scoreClassic
                highscoreTextview.setText("");
                if (scoreClassic != 0) {
                    highscoreTextview.setText(String.valueOf(scoreClassic));
                }

                levelName.setText("Classic");
                recordTextview.setText(String.valueOf(classic_record));
                centerLocked.setVisibility(View.GONE);
                centerImageview.setImageResource(R.drawable.board_classic);
                leftImageview.setImageResource(R.drawable.board_pi);
                rightImageview.setImageResource(R.drawable.board_destijl);
                if (unlockedLevels >= 3) {
                    leftLocked.setVisibility(View.GONE);
                } else {
                    leftLocked.setVisibility(View.VISIBLE);
                }

                leftImageview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        centerPlay = 3;
                        setViews(3);
                    }
                });
                rightImageview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        centerPlay = 2;
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

                // set highscore to scoreDestijl
                highscoreTextview.setText("");
                if (scoreDestijl != 0) {
                    highscoreTextview.setText(String.valueOf(scoreDestijl));
                }

                levelName.setText("De Stijl");

                // Set record
                recordTextview.setText(String.valueOf(de_stijl_record));

                leftLocked.setVisibility(View.GONE);

                centerImageview.setImageResource(R.drawable.board_destijl);
                leftImageview.setImageResource(R.drawable.board_classic);
                rightImageview.setImageResource(R.drawable.board_pi);

                leftImageview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        centerPlay = 1;
                        setViews(1);
                    }
                });

                rightImageview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        centerPlay = 3;
                        setViews(3);
                    }
                });

                if (unlockedLevels >= 2) {
                    centerLocked.setVisibility(View.GONE);
                    twoLocked.setVisibility(View.GONE);
                } else {
                    centerLocked.setVisibility(View.VISIBLE);
                }

                if (unlockedLevels >= 3) {
                    rightLocked.setVisibility(View.GONE);
                } else {
                    rightLocked.setVisibility(View.VISIBLE);
                }


                break;
            case 3:

                // set highscore to scorePi
                highscoreTextview.setText("");
                if (scorePi != 0) {
                    highscoreTextview.setText(String.valueOf(scorePi));
                }
                // Set record
                recordTextview.setText(String.valueOf(pi_record));

                levelName.setText("Pi Time");


                // TODO: make pi resource
                centerImageview.setImageResource(R.drawable.board_pi);
                leftImageview.setImageResource(R.drawable.board_destijl);
                rightImageview.setImageResource(R.drawable.board_classic);
                rightLocked.setVisibility(View.GONE);


                rightImageview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        centerPlay = 1;
                        setViews(1);
                    }
                });


                leftImageview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        centerPlay = 2;
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

                } else {
                    centerLocked.setVisibility(View.VISIBLE);
                }

                break;

        }
    }
}

