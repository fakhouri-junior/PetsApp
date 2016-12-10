package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Created by salim on 25/10/2016.
 */

/**
 * {@link PetCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class PetCursorAdapter extends CursorAdapter {


    /**
     * Constructs a new {@link PetCursorAdapter}.
     *
     * @param context The context
     * @param cursor       The cursor from which to get the data.
     */
    public PetCursorAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);

    }



    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // create the empty view
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // fill and populate the view with data
        TextView nameTextView = (TextView)view.findViewById(R.id.name);
        TextView summayTextView = (TextView) view.findViewById(R.id.summary);

        // data now, using cursor
        String nameString = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME));
        String summaryString = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED));

        // If the pet breed is empty string or null, then use some default text
        // that says "Unknown breed", so the TextView isn't blank.
        if (TextUtils.isEmpty(summaryString)) {
            summaryString = context.getString(R.string.unknown_breed);
        }
        // attach data with views
        nameTextView.setText(nameString);
        summayTextView.setText(summaryString);

    }
}
