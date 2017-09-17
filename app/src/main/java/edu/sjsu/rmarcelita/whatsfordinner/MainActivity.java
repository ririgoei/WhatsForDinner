package edu.sjsu.rmarcelita.whatsfordinner;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToMealsPage(View view) {
        Intent intent = new Intent(this, MealsPage.class);
        startActivity(intent);
    }

    public void goToRecipesPage(View view) {
        Intent intent = new Intent(this, RecipeListActivity.class);
        startActivity(intent);
    }

    public void goToGroceriesPage(View view) {
        Intent intent = new Intent(this, GroceriesPage.class);
        startActivity(intent);
    }

    public void goToNewDishPage(View view) {
        Intent intent = new Intent(this, NewDishPage.class);
        startActivity(intent);
    }

    public void onButtonShowPopupWindowClick(View view) {

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainPageRelativeLayout);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_layout, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

        Button ok_button = (Button)popupView.findViewById(R.id.okButton);

        ok_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
