package com.example.john.sqlitedatabasedemo;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.ScriptGroup;
public class SQLiteDatabaseHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "grocerystore";//name of db
    private static final int DB_VERSION = 1;//version of db
    //-originally(when app is first release)the db version will be 1, but say later on when we
    //    update and release a new version of our app & we want to change the db, we wil call
    //    that new db version 2, and the next one after that will be 3, and so on
    //-Say you released your app, and now everyone has a specific version of the database, then later
    //    you want to change the db, like add a new column. Inorder to make sure all users have the
    //    most recent version of the db(as in not running any old database), you have to
    //    change the DB_VERSION field, then release the new app, all users will download this new code
    //    into their app and get this new DB_VERSION
    //Two scenarios happen when you need to make changes to your database(like adding a new column)
    //    -user first time using app - doesn't have the db installed on their device, calls onCreate()
    //    -user already has app installed-but it's an out of date db-it needs new column,calls onUpgrade()
    //SQLite helper uses a db version number to determine wether to simply create or upgrade the db
    //The SQLIte helper code has a db version number, and the actual db thats stored in the device
    //   has a version number too. This version number is used to ensure that the db is up to date
    //   if the sqlite helper code has a number greater/less than the db, then it will call the
    //   upgrade() or downgrade() method, then it will set the db number to the number the sqlite
    //   helper has
    //*****Scenario 1: user first time runs app, no db exists
    //First time user runs app, checks if db exists, database doesn't exist, so SQLite helper creates it.
    //   SQLite helper gives the database the name and version number
    //   specified in the SQLite helper code(the constructor) when it's created
    public SQLiteDatabaseHelper(Context context) {
        //Before this call to super(), the db version would be by default 0, i think
        super(context, DB_NAME, null, DB_VERSION);//gives the db name and version
        //the super() method creates the db with the info provided to it
    }
    //after the db is created, it calls the onCreate() method
    //onCreate() includes code to populate the db
    //SQLiteDatabase db parameter is the db that was just created by SQLiteDatabaseHelper
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create a table in the new database
        db.execSQL("CREATE TABLE ITEM (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT, " +
                "DESCRIPTION TEXT, " +
                "IMAGE_RESOURCE_ID INTEGER);");
        //insert some data in with .put(columnName, columnValue)
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME","chocolate");//NAME is column name, chocolate is column value
        contentValues.put("DESCRIPTION","SUPER TASTY, CHEWY AND SWEET!");
        contentValues.put("IMAGE_RESOURCE_ID", R.drawable.chocolate);
        db.insert("ITEM", null,contentValues);
        //Summary of onCreate()-1.create tables, 2.ContentValues  3.db.insert() the content values
    }
    //****Scenario 2: existing user has the app installed, but has an old version of the db
    //User runs new version of the app, checks wether db exists, it does, so it doesn't recreate it.
    //    SQLite helper checks version number of existing db vs the number in the SQLite helper code
    //if SQLite helper version(DB_VERSION) is higher than db version, it calls onUpdate()
    //if it's lower, it calls onDowngrade() - you want to downgrade() when you want to remove a column
    //    that you no longer want in the db and want to revert back to a prevous version of the db
    //3 Parameters- the actual db itself being used by the device, the old db version that's out of date
    //    and the new version(described in the SQLite helper code)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("DESCRIPTION", "Super gross and old!");
        //update the row in the table ITEM, with the values(contentValues) where NAME equals Chocolate
        db.update("ITEM",contentValues,"NAME = ?", new String[]{"Chocolate"});

        //db.execSQL("ALTER TABLE ITEM ADD COLUMN CHECKED NUMERIC");
    }
    //Method is not used, but would make inserting new rows into the table easier
    public void insertItem(SQLiteDatabase db, String itemName, String description, int resourceId){
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", itemName);
        contentValues.put("DESCRIPTION", description);
        contentValues.put("IMAGE_RESOURCE_ID", resourceId);
        //update
        db.insert("ITEM", null,contentValues);
    }
    //makes updating the db a lot easier by placing all code in one place
    public void updateMyDb(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {//if the db version is 0 or less, which means db is empty(just got created)
            //create a table in the new database
            db.execSQL("CREATE TABLE ITEM (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAME TEXT, " +
                    "DESCRIPTION TEXT, " +
                    "IMAGE_RESOURCE_ID INTEGER);");
            //insert some data in
            ContentValues contentValues = new ContentValues();
            contentValues.put("NAME","chocolate");
            contentValues.put("DESCRIPTION","SUPER TASTY, CHEWY AND SWEET!");
            contentValues.put("IMAGE_RESOURCE_ID", R.drawable.chocolate);
            db.insert("ITEM", null,contentValues);
        }
        if(oldVersion < 2){//if db version is 1, it means that the db needs to be upgraded
            //CHECKED field is boolean, it means "has user checked this item as bought or not"
            db.execSQL("ALTER TABLE ITEM ADD COLUMN CHECKED NUMERIC");
        }  //When app needs access to database, the SQLiteHelper will check if db is there
    }      //if not, it will create a new db with that name and version number
}          //when database is created, the onCreate() method is called to create it





