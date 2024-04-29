package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Activity to display and manage high scores
public class HighScoreActivity extends AppCompatActivity {

    private Button return_to_home;  // Button to navigate back to the main activity
    private TextView free_mode_scores;  // TextView to display high scores for free mode
    private TextView timed_mode_scores;  // TextView to display high scores for timed mode
    private int score;  // Score passed from the previous activity
    private String name;  // Player name passed from the previous activity
    private String mode;  // Game mode passed from the previous activity
    private SharedPreferences sharedPreferences;  // To store and retrieve high scores

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Enable edge-to-edge layout
        setContentView(R.layout.activity_high_score);  // Set the layout for the activity

        // Handle window insets for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            // Initialize SharedPreferences with a private mode
            sharedPreferences = getSharedPreferences("HighScores", MODE_PRIVATE);
            // Initialize UI components
            return_to_home = findViewById(R.id.return_to_home);
            free_mode_scores = findViewById(R.id.free_mode_scores);
            timed_mode_scores = findViewById(R.id.timed_mode_scores);

            // Set onClickListener for the return button to navigate back to MainActivity
            return_to_home.setOnClickListener(v1 -> returnToMainActivity());

            // Retrieve extras from intent
            score = getIntent().getIntExtra("SCORE", 0);
            mode = getIntent().getStringExtra("MODE");
            name = getIntent().getStringExtra("NAME");

            // Update high scores with new score entry
            updateHighScores(mode, name, score);
            // Display the updated high scores
            displayHighScores();

            return insets;
        });
    }

    // Navigate back to MainActivity
    private void returnToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Update high scores for the specified mode
    private void updateHighScores(String mode, String name, int score) {
        String scores = sharedPreferences.getString(mode, "");  // Fetch current scores
        List<ScoreEntry> scoreList = parseScores(scores);  // Parse scores into list
        scoreList.add(new ScoreEntry(name, score));  // Add new score entry

        // Sort scores in descending order
        Collections.sort(scoreList, (s1, s2) -> Integer.compare(s2.score, s1.score));

        // Build a string from sorted score list
        StringBuilder newScores = new StringBuilder();
        for (ScoreEntry entry : scoreList) {
            newScores.append(entry.name).append(":").append(entry.score).append(";");
        }

        // Save the updated scores back to SharedPreferences
        sharedPreferences.edit().putString(mode, newScores.toString()).apply();
    }

    // Parse the score string into a list of ScoreEntry
    private List<ScoreEntry> parseScores(String scores) {
        List<ScoreEntry> scoreList = new ArrayList<>();
        if (!scores.isEmpty()) {
            String[] entries = scores.split(";");
            for (String entry : entries) {
                String[] details = entry.split(":");
                if (details.length == 2) {
                    scoreList.add(new ScoreEntry(details[0], Integer.parseInt(details[1])));
                }
            }
        }
        return scoreList;
    }

    // Display high scores in TextViews
    private void displayHighScores() {
        // Retrieve scores for both modes
        String freeScores = sharedPreferences.getString("Free Mode", "");
        String timedScores = sharedPreferences.getString("Timed Mode", "");

        // Format and set text for high scores
        free_mode_scores.setText(formatScores(freeScores));
        timed_mode_scores.setText(formatScores(timedScores));
    }

    // Format score strings for display
    private String formatScores(String scores) {
        StringBuilder formatted = new StringBuilder();
        String[] entries = scores.split(";");
        for (String entry : entries) {
            if (!entry.isEmpty()) {
                formatted.append(entry.replace(":", " - ")).append("\n");
            }
        }
        return formatted.toString();
    }


    // class to handle the scores passed from the previous activity
    private class ScoreEntry {
        String name;
        int score;

        ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}


