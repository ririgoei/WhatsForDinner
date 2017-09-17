package edu.sjsu.rmarcelita.whatsfordinner.dummy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sjsu.rmarcelita.whatsfordinner.SQLiteIngredientsHelper;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class RecipeNames {

    private static SQLiteDatabase database;
    private static SQLiteIngredientsHelper db;
    private static Context context;

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Recipe> ITEMS = new ArrayList<Recipe>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Recipe> ITEM_MAP = new HashMap<String, Recipe>();

//    private static final int COUNT = 25;

//    static {
//        db = new SQLiteIngredientsHelper();
//        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createDummyItem(i));
//        }
//    }

    public static void setContext(Context c) {
        ITEMS.clear();
        ITEM_MAP.clear();
        if(db == null) db = new SQLiteIngredientsHelper(c);
        database = new SQLiteIngredientsHelper(c).getWritableDatabase();
        ArrayList<String> recipes = db.getAllRecipeName();
        Recipe item;
        for(int i = 0; i < recipes.size(); i++) {
            item = new Recipe(i+1+"", recipes.get(i).toString());
            addItem(item);
        }
        database.close();
    }

    private static void addItem(Recipe item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }


    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class Recipe {
        public final String id;
        public final String content;

        public Recipe(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
