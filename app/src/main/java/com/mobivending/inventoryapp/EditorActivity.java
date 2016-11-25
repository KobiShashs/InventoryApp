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
package com.mobivending.inventoryapp;


import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobivending.inventoryapp.data.InventoryContract.InventoryEntry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static com.mobivending.inventoryapp.data.InventoryContract.InventoryEntry.COLUMN_INVENTORY_IMAGE;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PET_LOADER = 0;
    private static final int FILE_SELECT_CODE = 2;


    private Uri mCurrentPetUri;

    private EditText mNameEditText;

    private EditText mQuantityEditText;

    private EditText mPriceEditText;
    private Button mImageButton;
    private ImageView mImageView;


    private boolean mInventoryHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mInventoryHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new pet or editing an existing one.
        Intent intent = getIntent();
        mCurrentPetUri = intent.getData();
        // If the intent DOES NOT contain a pet content URI, then we know that we are
        // creating a new pet.
        if (mCurrentPetUri == null) {

            setTitle(getString(R.string.editor_activity_title_new_pet));


            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_pet));

            getLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);
        }

        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mQuantityEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mPriceEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mImageButton = (Button) findViewById(R.id.image_button);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonImageClick();
            }
        });


        mImageView = (ImageView) findViewById(R.id.imageSelected);

        mNameEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);

        mImageView.setOnTouchListener(mTouchListener);


    }

    private void buttonImageClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), FILE_SELECT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                    mImageView.setImageBitmap(bitmap);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saleButton(View v) {
        mQuantityEditText = (EditText) findViewById(R.id.edit_pet_breed);
        int quantity = Integer.valueOf(mQuantityEditText.getText().toString());
        if (quantity > 0) {
            quantity--;
            mQuantityEditText.setText(String.valueOf(quantity));
            Toast.makeText(this, "Quantity decreased", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "There is not enough quantity", Toast.LENGTH_SHORT).show();
        }

    }


    public void orderButton(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0123456789"));
        startActivity(intent);
    }


    private void savePet() {

        String nameString = mNameEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        ImageView image = (ImageView) findViewById(R.id.imageSelected);


        if (mCurrentPetUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(quantityString) &&
                TextUtils.isEmpty(priceString)) {// && mGender == PetEntry.GENDER_UNKNOWN) {

            return;
        }


        if (mNameEditText.getText().toString().length() == 0) {
            Toast.makeText(this, "invalid name", Toast.LENGTH_LONG).show();
            mNameEditText.setError("Name can\'t be empty");
            return;
        } else if (mQuantityEditText.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "invalid Quantity", Toast.LENGTH_LONG).show();

            return;
        } else if (mPriceEditText.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "invalid price", Toast.LENGTH_LONG).show();

            return;
        } else if (image.getDrawable() == null) {
            Toast.makeText(getApplicationContext(), "Upload an image", Toast.LENGTH_LONG).show();
            return;
        } else {


        }

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_INVENTORY_NAME, nameString);

        values.put(InventoryEntry.COLUMN_INVENTORY_PRICE, priceString);


        int quantity = 0;
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }


        values.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, quantity);


        Bitmap imageBitMap = ((BitmapDrawable) image.getDrawable()).getBitmap();

        byte[] imageByteArray;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        imageBitMap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        imageByteArray = bos.toByteArray();
        values.put(InventoryEntry.COLUMN_INVENTORY_IMAGE, imageByteArray);


        if (mCurrentPetUri == null) {

            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);


            if (newUri == null) {

                Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(mCurrentPetUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void btnImageOnClick(View view) {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    private void saveToInternalStorage(Bitmap bmp, String filename) {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File appDirectory = contextWrapper.getFilesDir();

        File currentPath = new File(appDirectory, filename);

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(currentPath);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentPetUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:

                savePet();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mInventoryHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };


                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        if (!mInventoryHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    ///***********

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INVENTORY_NAME,
                InventoryEntry.COLUMN_INVENTORY_QUANTITY,
                InventoryEntry.COLUMN_INVENTORY_PRICE,
                InventoryEntry.COLUMN_INVENTORY_IMAGE};


        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentPetUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }


        if (cursor.moveToFirst()) {

            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);
            byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex(COLUMN_INVENTORY_IMAGE));
            Drawable drawable = new BitmapDrawable(this.getResources(), BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length));

            String name = cursor.getString(nameColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            int price = cursor.getInt(priceColumnIndex);


            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mQuantityEditText.setText(quantity);
            mPriceEditText.setText(Integer.toString(price));

            mImageView = (ImageView) findViewById(R.id.imageSelected);

            mImageView.setImageDrawable(drawable);

//
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mNameEditText.setText("");
        mQuantityEditText.setText("");
        mPriceEditText.setText("");
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deletePet() {
        if (mCurrentPetUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentPetUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

}
