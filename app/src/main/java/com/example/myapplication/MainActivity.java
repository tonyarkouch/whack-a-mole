/*CC0 1.0 Universal
By marking the work with a CC0 public domain dedication,
the creator is giving up their copyright and allowing reusers to distribute, remix, adapt,
and build upon the material in any medium or format, even for commercial purposes.
No Copyright
The person who associated a work with this deed has dedicated the work to the public domain by waiving all of his or her rights to the work
worldwide under copyright law, including all related and neighboring rights, to the extent allowed by law.
You can copy, modify, distribute and perform the work, even for commercial purposes, all without asking permission. See Other Information below.
Other Information
In no way are the patent or trademark rights of any person affected by CC0, nor are the rights that other persons may
have in the work or in how the work is used, such as publicity or privacy rights.
Unless expressly stated otherwise, the person who associated a work with this deed makes no warranties about the work,
and disclaims liability for all uses of the work, to the fullest extent permitted by applicable law.
When using or citing the work, you should not imply endorsement by the author or the affirmer.
CC0: This work has been marked as dedicated to the public domain.
* */


package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import java.util.Random;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    private Button start_button;
    private Button stop_button;
    private Button timer_button;
    private TextView score_counter;
    private TextView timer_counter;
    private ImageView mole_image1;
    private ImageView mole_image2;
    private ImageView mole_image3;
    private boolean gameActive;  // Flag to check if the game is active
    private Handler handler;
    private Random random;
    private int score;
    private int secondsPassed = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            start_button = findViewById(R.id.start_button);
            stop_button = findViewById(R.id.stop_button);
            timer_button = findViewById(R.id.timer);
            score_counter = findViewById(R.id.counter);
            timer_counter = findViewById(R.id.display_time);
            mole_image1 = findViewById(R.id.mole1);
            mole_image2 = findViewById(R.id.mole2);
            mole_image3 = findViewById(R.id.mole3);

            start_button.setOnClickListener(v1 -> startGame());
            stop_button.setOnClickListener(v1 -> stopGame());
            timer_button.setOnClickListener(v1 -> startTimerMode());

            mole_image1.setOnClickListener(v1 -> hitMole(mole_image1));
            mole_image2.setOnClickListener(v1 -> hitMole(mole_image2));
            mole_image3.setOnClickListener(v1 -> hitMole(mole_image3));

            handler = new Handler();
            random = new Random();
            score = 0;
            timer_counter.setVisibility(View.INVISIBLE);
            score_counter.setText("Score: " + score);

            return insets;
        });
    }



    private void startGame() {
        score = 0;
        score_counter.setText("Score: " + score);
        gameActive = true;
        mole_image1.setVisibility(View.VISIBLE);
        mole_image2.setVisibility(View.VISIBLE);
        mole_image3.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gameActive) {
                    toggleMoles();
                    handler.postDelayed(this, 500);
                }
            }
        }, 1000);
    }

    private void stopGame() {
        if (gameActive) {  // Check if the game is currently active
            gameActive = false;
            mole_image1.setVisibility(View.INVISIBLE);
            mole_image2.setVisibility(View.INVISIBLE);
            mole_image3.setVisibility(View.INVISIBLE);
            score_counter.setText("Score: " + score);
            goToEnterNameActivity("Free Mode");  // Transition to HighScoreActivity only when game was active
        }
    }


    private void toggleMoles() {
        mole_image1.setVisibility(random.nextBoolean() ? View.VISIBLE : View.INVISIBLE);
        mole_image2.setVisibility(random.nextBoolean() ? View.VISIBLE : View.INVISIBLE);
        mole_image3.setVisibility(random.nextBoolean() ? View.VISIBLE : View.INVISIBLE);
    }

    private void hitMole(View mole) {
        if (gameActive && mole.getVisibility() == View.VISIBLE) {
            score++;
            score_counter.setText("Score: " + score);
        }
    }

    private void startTimerMode() {
        score = 0;
        score_counter.setText("0");
        timer_counter.setText(String.format("Time: %d", secondsPassed));
        timer_counter.setVisibility(View.VISIBLE);
        gameActive = true;
        secondsPassed = 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gameActive) {
                    toggleMoles();
                    updateTimer();
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);

        handler.postDelayed(() -> {
            stopGame();
            score_counter.setText("Score: " + score);;
            timer_counter.setVisibility(View.INVISIBLE);
            goToEnterNameActivity("Timed Mode");
            handler.postDelayed(() -> {
                score = 0;
                score_counter.setText("Score: " + score);
            }, 5000);
        }, 5000);
    }

    private void updateTimer() {
        secondsPassed++;
        timer_counter.setText(String.format("Time: %d", secondsPassed));
    }
    private void goToEnterNameActivity(String mode) {
        Intent intent = new Intent(this, EnterNameActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("MODE", mode); // Pass the game mode as well
        startActivity(intent);
    }

}

