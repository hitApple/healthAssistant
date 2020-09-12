package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.List;

public class CalorieResult extends BaseActivity {

    private int sumCalorieAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calorie_finished);

        TextView calorieResult = findViewById(R.id.today_result);
        calorieResult.setText(getCaloriesText());
        TextView sumCalorie = findViewById(R.id.sum_calorie);
        String sumCalorieString = "总共：" + sumCalorieAmount + " 卡路里";
        sumCalorie.setText(sumCalorieString);
        TextView exceptCalorie = findViewById(R.id.exception_calorie);
        String exceptCalorieString = "期望值为：";
        exceptCalorie.setText(exceptCalorieString);

        Button continueWriteDown = findViewById(R.id.continue_write_down);
        continueWriteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CalorieResult.this, CalorieView.class));
            }
        });
        Button backToHomePage = findViewById(R.id.back_to_homepage);
        backToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CalorieResult.this, HomePage_find.class));
            }
        });

    }

    private String getCaloriesText(){
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        List<Calorie> calories = LitePal.select("foodName", "CalorieAmount")
                                        .where("year = ? and month = ? and day = ?", year, month, day)
                                        .find(Calorie.class);
        StringBuilder builder = new StringBuilder();
        for (Calorie calorie : calories){
            builder.append(calorie.getFoodName());
            builder.append("     ");
            builder.append(calorie.getCalorieAmount());
            builder.append("\n");
            sumCalorieAmount += calorie.getCalorieAmount();
        }
        return builder.toString();
    }

}