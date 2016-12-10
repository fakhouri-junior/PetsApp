package com.example.android.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by salim on 20/10/2016.
 */
public final class PetContract {

    private PetContract(){}


    /** Content_Authority for the content provider

     */
    public static final String CONTENT_AUTHORITY = "com.example.android.pets";

    /**
     * Next, we concatonate the CONTENT_AUTHORITY constant with the scheme “content://”
     * we will create the BASE_CONTENT_URI which will be shared by every URI associated with PetContract:
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * This constants stores the path for each of the
     * tables which will be appended to the base content URI.
     */

    public static final String PATH_PETS = "pets";



    /**
     * pets table constants and values
     */
    public static final class PetEntry implements BaseColumns{

        /**
         * CONTENT URI FOR THIS CLASS OR "TABLE"
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /**
         * Constants for the table "pets"
         */

        public static final String TABLE_NAME = "pets";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_GENDER = "gender";
        public static final String COLUMN_PET_WEIGHT = "weight";

        /**
         * Possible values for the column gender
         * 0 for unknown
         * 1 for male
         * 2 for female
         */
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
    }
}
