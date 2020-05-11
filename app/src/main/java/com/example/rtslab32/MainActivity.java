package com.example.rtslab32;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final double P = 4.0;
    private static final int[][] points = {{0, 6},
            {1, 5},
            {3, 3},
            {2, 4}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view) {
        final double learningSpeed = Double.parseDouble(
                ((Spinner) findViewById(R.id.lab32Spinner)).getSelectedItem().toString());
        final double timeDeadline = Double.parseDouble(
                ((Spinner) findViewById(R.id.lab32Spinner2)).getSelectedItem().toString());
        final int iterationsDeadline = Integer.parseInt(
                ((Spinner) findViewById(R.id.lab32Spinner3)).getSelectedItem().toString());

        double w1 = 0;
        double w2 = 0;
        double y;
        double delta;
        int iterations = 0;
        boolean completed = false;
        long start = System.nanoTime();

        int index = 0;
        while (iterations++ < iterationsDeadline && (System.nanoTime() - start) < timeDeadline) {

            index %= 4;

            y = points[index][0] * w1 + points[index][1] * w2;

            if (managedToFit(w1, w2)) {
                completed = true;
                break;
            }

            delta = P - y;
            w1 += delta * points[index][0] * learningSpeed;
            w2 += delta * points[index][1] * learningSpeed;
            index++;
        }

        TextView textViewW1 = findViewById(R.id.lab32W1);
        TextView textViewW2 = findViewById(R.id.lab32W2);
        TextView textViewIterations = findViewById(R.id.lab32Iterations);

        if (completed) {
            textViewW1.setText(String.format("w1 = %-6.3f = ", w1));
            textViewW2.setText(String.format("w2 = %-6.3f = ", w2));
            textViewIterations.setText("Iterations: " + iterations);

        } else {
            String result = "\tFailed to train model for specified deadline!";
            if (iterations >= iterationsDeadline) {
                result += "\n\tMore iterations are needed!";
            } else {
                result += "\n\tIt takes longer!";
            }
            textViewW1.setText(result);
            textViewW2.setText("");
        }

    }

    private boolean managedToFit(double w1, double w2) {
        return P < points[0][0] * w1 + points[0][1] * w2
                && P < points[1][0] * w1 + points[1][1] * w2
                && P > points[2][0] * w1 + points[2][1] * w2
                && P > points[3][0] * w1 + points[3][1] * w2;
    }
}