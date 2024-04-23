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
import android.widget.ImageView;
import android.content.Intent;
import android.app.AlertDialog;
import android.widget.EditText;

public class HighScoreActivity extends AppCompatActivity {

    private Button return_to_home;
    private TextView free_mode_scores;
    private TextView timed_mode_scores;
    private static final String DIALOG_SHOWN_KEY = "dialogShown";
    private boolean isDialogShown = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_high_score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return_to_home = findViewById(R.id.return_to_home);
            free_mode_scores = findViewById(R.id.free_mode_scores);
            timed_mode_scores = findViewById(R.id.timed_mode_scores);

            return_to_home.setOnClickListener(v1 -> returnToMainActivity());

            if (savedInstanceState != null) {
                isDialogShown = savedInstanceState.getBoolean(DIALOG_SHOWN_KEY, false);
            }
            if (!isDialogShown) {
                int score = getIntent().getIntExtra("SCORE", 0);
                String mode = getIntent().getStringExtra("MODE");
                showNamePrompt(score, mode);
            }


            return insets;
        });
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(DIALOG_SHOWN_KEY, isDialogShown);
    }

    private void showNamePrompt(int score, String mode) {
        if (!isDialogShown) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter Your Name");

            final EditText input = new EditText(this);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {
                String name = input.getText().toString();
                updateHighScoreDisplay(name, score, mode);
                dialog.cancel();
                isDialogShown = false;
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.cancel();
                isDialogShown = false;
            });

            builder.setOnDismissListener(dialog -> isDialogShown = false);

            builder.show();
            isDialogShown = true;
        }
    }

    private void updateHighScoreDisplay(String name, int score, String mode) {
        String scoreText = name + ": " + score + "\n"; // Prepare the score text to append

        if ("Free Mode".equals(mode)) {
            // Append new score text to the existing text
            free_mode_scores.append(scoreText + "\n");
        } else if ("Timed Mode".equals(mode)) {
            // Append new score text to the existing text
            timed_mode_scores.append(scoreText);
        }

        // Reset the dialog shown flag to allow new entries
        isDialogShown = true;
    }
}