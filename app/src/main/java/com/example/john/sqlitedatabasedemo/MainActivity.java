package com.example.john.sqlitedatabasedemo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //call our custom SQLite database helper class
        SQLiteOpenHelper helper = new SQLiteDatabaseHelper(this);
        //To read or write to a db, we need to use a cursor
        //    Say we want to read data from our db
        //    -First get a reference to the db
        //    -Then create & use the cursor to read/ from the db
        //    -Navigate the cursor to the record we want, and read from it
        try{
            //get a reference to the db
            SQLiteDatabase db = helper.getReadableDatabase();
            //create the cursor using a database query
            //SQLiteDatabase query() method returns a cursor object
            //the query() method below says, get from ITEM table, the columns _id,NAME, DESCRIPTION
            //   - and get all the columns
            Cursor cursor = db.query("ITEM", new String[]{"_id","NAME","DESCRIPTION","IMAGE_RESOURCE_ID"},
                    null,null,null,null,null);
            //behind the scenes, android uses the query() method to construct a SQL SELECT statement
            //Boolean moveToFirst(), Boolean moveToLast(), Boolean moveToPrevious(), Boolean moveToNext()
            if(cursor.moveToFirst()){
                //Retrieve values from a cursor's current record using get*() methods:getString,getInt(),etc
                //    -Each method takes one paramter-index of the cursor's columns-starting at 0
                //0 is for _id, 1 for NAME, 2 for DESCRIPTION, 3 for IMAGE_RESOURCE_ID
                String itemName = cursor.getString(1);
                String description = cursor.getString(2);
                int imageResId = cursor.getInt(3);

                TextView itemNameTV = findViewById(R.id.itemName_txtView);
                TextView descTV = findViewById(R.id.description_txtView);
                ImageView imageView = findViewById(R.id.imageView);

                itemNameTV.setText(itemName);
                descTV.setText(description);
                imageView.setImageResource(imageResId);
            }
            cursor.close();
            db.close();
        }catch(SQLiteException e){
            Toast.makeText(this,"Database request error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
