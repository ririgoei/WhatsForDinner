package edu.sjsu.rmarcelita.whatsfordinner;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.icu.text.DateFormat;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class LocalFileImages extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "My Image ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_file_images);

        final TypedArray imgs = getResources().obtainTypedArray(R.array.images);

        LinearLayout rbLayout = (LinearLayout) findViewById(R.id.imageListLinearLayout);

        String imageName = "";
        String[] imageNameAr = new String[2];

        final RadioButton[] rb = new RadioButton[imgs.length()];
        final RadioGroup rg = new RadioGroup(this);
        rg.setOrientation(RadioGroup.VERTICAL);

        for(int i = 0; i < imgs.length(); i++) {
            rb[i] = new RadioButton(this);
            rg.addView(rb[i]);
            imageName = imgs.getText(i).toString();
            imageNameAr = imageName.split("/");
            rb[i].setText(imageNameAr[2]);
            ImageView currentImage = new ImageView(this);
            currentImage.setImageResource(imgs.getResourceId(i, -1));
            rg.addView(currentImage);
        }

        rbLayout.addView(rg);

        LinearLayout buttonsLinearLayout = new LinearLayout(this);
        buttonsLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        Button chooseImageButton = new Button(this);
        chooseImageButton.setText("Choose Image");
        final Intent chooseIntent = new Intent(this,NewDishPage.class);
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = (int) rg.getCheckedRadioButtonId()-1;
//                Toast.makeText(getApplicationContext(), "ID: " + imgs.getText(id).toString(),
                      //  Toast.LENGTH_LONG).show();
                chooseIntent.putExtra(EXTRA_MESSAGE, id + "");
                startActivity(chooseIntent);
            }
        });

        Button goBackButton = new Button(this);
        goBackButton.setText("Go Back");
        final Intent intent = new Intent(this, NewDishPage.class);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        buttonsLinearLayout.addView(chooseImageButton);
        buttonsLinearLayout.addView(goBackButton);

        rbLayout.addView(buttonsLinearLayout);
    }
}
