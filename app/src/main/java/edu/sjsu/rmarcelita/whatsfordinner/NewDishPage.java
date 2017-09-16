package edu.sjsu.rmarcelita.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class NewDishPage extends AppCompatActivity {

    private String imagePath;
    private SQLiteIngredientsHelper db;
    private Context context;
    private ArrayList<AutoCompleteTextView> ingredients;
    private ArrayList<String> ingredientsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish_page);

        TypedArray imgs = getResources().obtainTypedArray(R.array.images);

        ImageView userImage = (ImageView) findViewById(R.id.userImageView);

        context = getApplicationContext();

        db = new SQLiteIngredientsHelper(this);

//        Intent chooseIntent = getIntent();
//        String id = chooseIntent.getStringExtra(LocalFileImages.EXTRA_MESSAGE);
//        Log.v("", "ID: " + id);
//        Toast.makeText(NewDishPage.this, "ID: " + id, Toast.LENGTH_SHORT).show();
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String message = extras.getString(LocalFileImages.EXTRA_MESSAGE);
            int id = Integer.parseInt(message);
            Toast.makeText(NewDishPage.this, "Helloooo: " + message, Toast.LENGTH_SHORT).show();
            userImage.setImageResource(imgs.getResourceId(id, -1));
            imagePath = imgs.getText(id).toString();
        }

        //SQLiteDatabase database = new SQLiteIngredientsHelper(this).getWritableDatabase();
        //database.close();

        ImageView mealImage = (ImageView) findViewById(R.id.userImageView);


        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.newDishRelativeLayout);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.recipeNameLinearLayout);

        EditText recipeET = (EditText) findViewById(R.id.recipeNameET);

        Toast.makeText(getApplicationContext(), "Gets to onCreate NewDishPage", Toast.LENGTH_SHORT).show();

        final LinearLayout ingredientLayout = (LinearLayout) findViewById(R.id.ingredientsLinearLayout);
        Button addNewIngredient = (Button) findViewById(R.id.ingredientsButton);

        db.createIngredientsOnlyTable();
        ArrayList<String> allAvailableIngredients = new ArrayList<>();

        if(db.getAllIngredients() != null) {
            allAvailableIngredients = db.getAllIngredients();
        } else {
            allAvailableIngredients.add("Choose Ingredient");
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, allAvailableIngredients);

        ingredients = new ArrayList<>();

        addNewIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView newIngredient = new AutoCompleteTextView(getApplicationContext());
                newIngredient.setAdapter(adapter);
                newIngredient.setCompletionHint("Enter ingredient here");
                ingredientLayout.addView(newIngredient);
                ingredients.add(newIngredient);
            }
        });

        SQLiteDatabase database = new SQLiteIngredientsHelper(this).getWritableDatabase();

        Button submitIngredient = (Button) findViewById(R.id.submitRecipeButton);
        final EditText recipeName = (EditText) findViewById(R.id.recipeNameET);
        final EditText instructions = (EditText) findViewById(R.id.directionsText);

        submitIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientsArray = new ArrayList<String>();
                ArrayList<String> recipeNames = db.getAllRecipeName();
                for(AutoCompleteTextView autoComp : ingredients) {
                    db.insertIngredientsOnly(autoComp.getText().toString());
                    ingredientsArray.add(autoComp.getText().toString());
                }
                Gson gson = new Gson();
                String ingredientsString = gson.toJson(ingredientsArray);

                if(recipeNames.contains(recipeName.getText().toString())) {
                    Toast.makeText(context, "Recipe name already exists, please enter a new one",
                            Toast.LENGTH_LONG).show();
                } else {
                    db.insertIntoTable(recipeName.getText().toString(), ingredientsString,
                            instructions.getText().toString(), imagePath);
                    Toast.makeText(context, "New recipe " + recipeName.getText().toString() + " successfully added!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void onButtonShowPopupWindowClick(View view) {

        try {
            LinearLayout mainLayout = (LinearLayout) findViewById(R.id.addImageLinearLayout);

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = inflater.inflate(R.layout.popup_image_layout, null);

            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

            Button uploadURL = (Button) popupView.findViewById(R.id.uploadImageURLButton);
            uploadURL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popUpWindowImageURL(view);
                    popupWindow.dismiss();
                }
            });

            Button uploadLocal = (Button) popupView.findViewById(R.id.uploadImageLocalButton);
            final Intent intent = new Intent(this, LocalFileImages.class);

            uploadLocal.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    startActivity(intent);
                    popupWindow.dismiss();
                }

            });
        } catch(Exception e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }

    public void popUpWindowImageURL(View view) {
        LinearLayout imageURLLayout = (LinearLayout) findViewById(R.id.addImageLinearLayout);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupViewURL = inflater.inflate(R.layout.popup_image_url_layout, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindowURL = new PopupWindow(popupViewURL, width, height, focusable);

        popupWindowURL.showAtLocation(imageURLLayout, Gravity.CENTER, 0, 0);

        imagePath = "";

        Button uploadURLButton = (Button) popupViewURL.findViewById(R.id.imageURLUploadButton);

        uploadURLButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText imagePathEditText = (EditText) popupViewURL.findViewById(R.id.imageURLEditText);
                imagePath = imagePathEditText.getText().toString();
                Log.v("Image Path", imagePath);
                if(URLUtil.isValidUrl(imagePath)) {
                    ImageView userImage = (ImageView) findViewById(R.id.userImageView);
                    Picasso.with(context).load(imagePath).into(userImage);
                } else {
                    Toast.makeText(context, "URL not valid!", Toast.LENGTH_LONG).show();
                }
                popupWindowURL.dismiss();
                return true;

                //imageView.setImageResource(imagePath) imagePath == R.id.fjdka;l
            }
        });
    }
}
