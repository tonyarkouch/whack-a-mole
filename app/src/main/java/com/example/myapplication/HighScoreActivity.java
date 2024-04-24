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
import android.content.Intent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.SharedPreferences;

public class HighScoreActivity extends AppCompatActivity {

    private Button return_to_home;
    private TextView free_mode_scores;
    private TextView timed_mode_scores;
    private int score;
    private String name;
    private String mode;
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_high_score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            sharedPreferences = getSharedPreferences("HighScores", MODE_PRIVATE);
            return_to_home = findViewById(R.id.return_to_home);
            free_mode_scores = findViewById(R.id.free_mode_scores);
            timed_mode_scores = findViewById(R.id.timed_mode_scores);



            return_to_home.setOnClickListener(v1 -> returnToMainActivity());


                 score = getIntent().getIntExtra("SCORE", 0);
                 mode = getIntent().getStringExtra("MODE");
                 name = getIntent().getStringExtra("NAME");


            updateHighScores(mode, name, score);
            displayHighScores();

            return insets;
        });
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    private void updateHighScores(String mode, String name, int score) {
        // Fetch the current high scores list for the given mode
        String scores = sharedPreferences.getString(mode, "");
        List<ScoreEntry> scoreList = parseScores(scores);
        scoreList.add(new ScoreEntry(name, score));

        // Sort the list by scores in descending order
        Collections.sort(scoreList, (s1, s2) -> Integer.compare(s2.score, s1.score));

        // Convert the list back to a string and save it
        StringBuilder newScores = new StringBuilder();
        for (ScoreEntry entry : scoreList) {
            newScores.append(entry.name).append(":").append(entry.score).append(";");
        }

        sharedPreferences.edit().putString(mode, newScores.toString()).apply();
    }

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

    private void displayHighScores() {
        String freeScores = sharedPreferences.getString("Free Mode", "");
        String timedScores = sharedPreferences.getString("Timed Mode", "");

        free_mode_scores.setText(formatScores(freeScores));
        timed_mode_scores.setText(formatScores(timedScores));
    }

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

    private class ScoreEntry {
        String name;
        int score;

        ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}


