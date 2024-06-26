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

// Import necessary Android and view handling libraries
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

// MainActivity class for the application
public class MainActivity extends AppCompatActivity {
    // UI components
    private Button start_button;
    private Button stop_button;
    private Button timer_button;
    private TextView score_counter;
    private TextView timer_counter;
    private ImageView mole_image1;
    private ImageView mole_image2;
    private ImageView mole_image3;

    // Game state variables
    private boolean gameActive;  // Flag to check if the game is active
    private Handler handler;  // Handler for posting delayed tasks
    private Random random;  // Random number generator for game logic
    private int score;  // Tracks the current score
    private int secondsPassed = 1;  // Tracks time passed in timer mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout to be edge-to-edge on devices that support it
        EdgeToEdge.enable(this);
        // Set the content view to the main activity layout
        setContentView(R.layout.activity_main);

        // Setup UI elements with proper padding and listeners
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            // Initialize buttons and text views
            start_button = findViewById(R.id.start_button);
            stop_button = findViewById(R.id.stop_button);
            timer_button = findViewById(R.id.timer);
            score_counter = findViewById(R.id.counter);
            timer_counter = findViewById(R.id.display_time);
            mole_image1 = findViewById(R.id.mole1);
            mole_image2 = findViewById(R.id.mole2);
            mole_image3 = findViewById(R.id.mole3);

            // Set up button listeners to handle game interactions
            start_button.setOnClickListener(v1 -> startGame());
            stop_button.setOnClickListener(v1 -> stopGame());
            timer_button.setOnClickListener(v1 -> startTimerMode());

            // Set up listeners for mole images to handle game logic
            mole_image1.setOnClickListener(v1 -> hitMole(mole_image1));
            mole_image2.setOnClickListener(v1 -> hitMole(mole_image2));
            mole_image3.setOnClickListener(v1 -> hitMole(mole_image3));

            // Initialize handler and random generator
            handler = new Handler();
            random = new Random();
            score = 0;
            // Initially hide the timer and set the score text
            timer_counter.setVisibility(View.INVISIBLE);
            score_counter.setText("Score: " + score);

            return insets;
        });
    }

    // Method to start the game
    private void startGame() {
        score = 0;
        score_counter.setText("Score: " + score);
        gameActive = true;
        // Make all mole images visible
        mole_image1.setVisibility(View.VISIBLE);
        mole_image2.setVisibility(View.VISIBLE);
        mole_image3.setVisibility(View.VISIBLE);
        // Schedule a task to toggle moles' visibility
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

    // Method to stop the game
    private void stopGame() {
        if (gameActive) {  // Check if the game is currently active
            gameActive = false;
            mole_image1.setVisibility(View.INVISIBLE);
            mole_image2.setVisibility(View.INVISIBLE);
            mole_image3.setVisibility(View.INVISIBLE);
            score_counter.setText("Score: " + score);
            // Transition to name entry activity for high score
            goToEnterNameActivity("Free Mode");
        }
    }

    // Method to toggle visibility of moles randomly
    private void toggleMoles() {
        mole_image1.setVisibility(random.nextBoolean() ? View.VISIBLE : View.INVISIBLE);
        mole_image2.setVisibility(random.nextBoolean() ? View.VISIBLE : View.INVISIBLE);
        mole_image3.setVisibility(random.nextBoolean() ? View.VISIBLE : View.INVISIBLE);
    }

    // Method to handle mole hit action
    private void hitMole(View mole) {
        if (gameActive && mole.getVisibility() == View.VISIBLE) {
            score++;
            score_counter.setText("Score: " + score);
        }
    }

    // Method to start the game in timer mode
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

        // Schedule stopping the game after 5 seconds in timer mode
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

    // Method to update the timer display
    private void updateTimer() {
        secondsPassed++;
        timer_counter.setText(String.format("Time: %d", secondsPassed));
    }

    // Method to navigate to the EnterNameActivity for score submission
    private void goToEnterNameActivity(String mode) {
        Intent intent = new Intent(this, EnterNameActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("MODE", mode); // Pass the game mode as well
        startActivity(intent);
    }
}
