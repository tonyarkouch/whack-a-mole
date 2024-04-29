package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.EditText;

// EnterNameActivity is used for collecting user names after a game session
public class EnterNameActivity extends AppCompatActivity {
    private int score; // Score from the game, passed from MainActivity
    private String mode; // Game mode, passed from MainActivity
    private Button go_to_high_score; // Button to submit name and score
    private EditText take_name; // Text field to enter the user's name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display to utilize full screen for layouts
        EdgeToEdge.enable(this);
        // Set the layout for this activity
        setContentView(R.layout.activity_enter_name);

        // Apply window insets for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply padding to accommodate system navigation bars
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            // Retrieve score and mode from the intent extras
            score = getIntent().getIntExtra("SCORE", 0);
            mode = getIntent().getStringExtra("MODE");

            // Initialize the button and text field from the layout
            go_to_high_score = findViewById(R.id.go_to_high_score);
            take_name = findViewById(R.id.enter_name_for_high_score);

            // Set up a click listener for the button to handle high score submission
            go_to_high_score.setOnClickListener(v1 -> {
                String name = take_name.getText().toString(); // Get the name entered by the user
                goToHighScoreActivity(name); // Navigate to HighScoreActivity with the collected name
            });

            return insets;
        });
    }

    // Method to navigate to HighScoreActivity, passing along the collected name, score, and mode
    private void goToHighScoreActivity(String name) {
        Intent intent = new Intent(this, HighScoreActivity.class);
        intent.putExtra("SCORE", score); // Pass score
        intent.putExtra("MODE", mode); // Pass mode
        intent.putExtra("NAME", name); // Pass user-entered name
        startActivity(intent); // Start HighScoreActivity
    }
}
