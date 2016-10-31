package com.mobivending.inventoryapp.data;

/**
 * Created by kobishasha on 10/25/16.
 * This is the contract
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Inventory app.
 */
public final class InventoryContract {
    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.mobivending.inventoryapp";
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_INVENTORY = "inventory";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InventoryContract() {
        throw new AssertionError("No InventoryContract instances for you!");
    }

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single inventory.
     */
    public static final class InventoryEntry implements BaseColumns {
        /**
         * The content URI to access the pet data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of inventories.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single inventory.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * Name of database table for pets
         */
        public final static String TABLE_NAME = "inventory";

        /**
         * Unique ID number for the inventory product (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the inventory product.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_INVENTORY_NAME = "name";

        /**
         * Quantity of the inventory product.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_INVENTORY_QUANTITY = "quantity";

        /**
         * Price of the inventory product.
         * <p>
         * Type: INTEGER
         */

        public final static String COLUMN_INVENTORY_PRICE = "price";
        /**
         * Image of the inventory product.
         * <p>
         * Type: BLOB
         */
        public final static String COLUMN_INVENTORY_IMAGE = "image";

    }


}
