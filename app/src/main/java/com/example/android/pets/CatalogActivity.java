/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetDbHelper;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_TAG = CatalogActivity.class.getName();

    private static final int LOADER_ID = 0;

    private PetCursorAdapter petCursorAdapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // call the loader
        /*
        Note: The method getLoaderManager()
         is only available in the Fragment class.
         To get a LoaderManager in a FragmentActivity,
         call getSupportLoaderManager().
         */
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        listView = (ListView) findViewById(R.id.listView);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        petCursorAdapter = new PetCursorAdapter(this,null);
        listView.setAdapter(petCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set up the Uri
                Uri uri = ContentUris.withAppendedId(PetEntry.CONTENT_URI,id);
                // make an intent to editors activity, and pass the uri along with it
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        //displayDatabaseInfo();
    }

    private void insertData(){

        // dummy data

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        // use content resolver insert method
        Uri uri = getContentResolver().insert(PetEntry.CONTENT_URI, values);


        /*
        SQLiteDatabase db = petDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        long rowId = db.insert(PetEntry.TABLE_NAME, null, values);
        if(rowId == -1){
            // something went wrong
            Log.e(LOG_TAG, "Error -1 while inserting a row in database");
        }
        */


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
                insertData();
                //displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllPets(){
        int rowsAffected = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
        if(rowsAffected == 0){
            // fail
            Toast.makeText(CatalogActivity.this, R.string.catalog_delete_all_pets_failed, Toast.LENGTH_SHORT).show();
        }else{
            // success
            Toast.makeText(CatalogActivity.this, R.string.catalog_delete_all_pets_successful, Toast.LENGTH_SHORT).show();
        }

    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete All Pets");
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteAllPets();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.

        // Create and/or open a database to read from it
        //SQLiteDatabase db = petDbHelper.getReadableDatabase();

        /** Note: there are many constructors
         * Cursor query (boolean distinct,
         String table,
         String[] columns, can be named projections
         String selection,  used as where statement (typically write the condition), use ? for placeholder
         String[] selectionArgs, fill in the ? placeholders in selection
         String groupBy,
         String having,
         String orderBy,
         String limit)
         */
        String[] projections = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };


        //Cursor cursor = db.query(PetEntry.TABLE_NAME, projections, null, null, null, null, null);

        // No more direct calls to the database
        // we will use the following architecture .. Activity -> Content Resolver -> Content Provider -> database
        Cursor cursor = getContentResolver().query(
                PetEntry.CONTENT_URI,
                projections,
                null,
                null,
                null
        );

        petCursorAdapter = new PetCursorAdapter(this,cursor);
        listView.setAdapter(petCursorAdapter);

        /*
        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        //Cursor cursor = db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
            displayView.setText("Number of rows in pets database table: " + cursor.getCount()+"\n\n");
            displayView.append(PetEntry._ID + " - "
                    + PetEntry.COLUMN_PET_NAME + " - "
                    + PetEntry.COLUMN_PET_GENDER + " - "
            + PetEntry.COLUMN_PET_WEIGHT + "\n");


            // figure out the indices here instead of using 0,1,2 ...

            // get the values
            while (cursor.moveToNext()){

                // can use getColumnIndex
                int id = cursor.getInt(cursor.getColumnIndex(PetEntry._ID));
                String name = cursor.getString(1);
                String breed = cursor.getString(2);
                int gender = cursor.getInt(3);
                int weight = cursor.getInt(4);

                displayView.append(String.valueOf(id) + " - "+
                name + " - " +
                breed + " - "+
                String.valueOf(gender) + " - "+
                String.valueOf(weight) + "\n");
            }


        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
        */
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projections = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

        switch (id){

            case LOADER_ID:
                return new CursorLoader(this,
                        PetEntry.CONTENT_URI,
                        projections,
                        null,
                        null,
                        null
                );
            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        petCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        petCursorAdapter.swapCursor(null);

    }


}
