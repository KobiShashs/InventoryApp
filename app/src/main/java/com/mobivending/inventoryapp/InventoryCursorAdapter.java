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

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobivending.inventoryapp.data.InventoryContract.InventoryEntry;

import static com.mobivending.inventoryapp.data.InventoryContract.InventoryEntry.COLUMN_INVENTORY_IMAGE;

/**
 * Created by kobishasha on 10/25/16.
 * /**
 * {@link InventoryCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of inventory data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */


public class InventoryCursorAdapter  extends CursorAdapter{
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView)view.findViewById(R.id.price);
        ImageView imageImageView = (ImageView)view.findViewById(R.id.image);

        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);
       // int blobColumnIndex = cursor.getColumnIndex((InventoryEntry.COLUMN_INVENTORY_IMAGE));

        // Read the pet attributes from the Cursor for the current pet
        String inventoryName = cursor.getString(nameColumnIndex);
        String inventoryQuantity = cursor.getString(quantityColumnIndex);
        String inventoryPrice = cursor.getString(priceColumnIndex);


        byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex(COLUMN_INVENTORY_IMAGE));
        Drawable drawable =  new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length));
//        // If the pet breed is empty string or null, then use some default text
//        // that says "Unknown breed", so the TextView isn't blank.
//        if (TextUtils.isEmpty(petBreed)) {
//            petBreed = context.getString(R.string.unknown_breed);
//        }

        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(inventoryName);
        quantityTextView.setText(inventoryQuantity);
        priceTextView.setText(inventoryPrice);
        imageImageView.setImageDrawable(drawable);
    }

}
