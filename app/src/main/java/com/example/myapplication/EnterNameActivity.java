package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;

public class EnterNameActivity extends AppCompatActivity {
    private int score;
    private String mode;
    private Button go_to_high_score;
    private EditText take_name;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enter_name);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);


             score = getIntent().getIntExtra("SCORE", 0);
             mode = getIntent().getStringExtra("MODE");

            go_to_high_score = findViewById(R.id.go_to_high_score);

            take_name = findViewById(R.id.enter_name_for_high_score);

            go_to_high_score.setOnClickListener(v1 -> {
                String name = take_name.getText().toString(); // Get the name when the button is clicked
                goToHighScoreActivity(name);
            });



            return insets;

        });
    }

    private void goToHighScoreActivity(String name) {
        Intent intent = new Intent(this, HighScoreActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("MODE", mode);
        intent.putExtra("NAME", name);// Pass the game mode as well
        startActivity(intent);
    }
}