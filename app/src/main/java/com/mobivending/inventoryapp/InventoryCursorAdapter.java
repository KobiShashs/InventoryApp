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

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobivending.inventoryapp.data.InventoryContract.InventoryEntry;

import static com.mobivending.inventoryapp.R.id.quantity;
import static com.mobivending.inventoryapp.data.InventoryContract.InventoryEntry.COLUMN_INVENTORY_IMAGE;

/**
 * Created by kobishasha on 10/25/16.
 * /**
 * {@link InventoryCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of inventory data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */


public class InventoryCursorAdapter extends CursorAdapter {

    CatalogActivity cat = new CatalogActivity();
    int quant;
    private boolean flagClicked = false;

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        final TextView nameTextView = (TextView) view.findViewById(R.id.name);
        final TextView quantityTextView = (TextView) view.findViewById(quantity);
        final TextView priceTextView = (TextView) view.findViewById(R.id.price);
        final ImageView imageImageView = (ImageView) view.findViewById(R.id.image);
        Button saleButton = (Button) view.findViewById(R.id.minus_button);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flagClicked = true;
                int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(InventoryEntry._ID));
                int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);
                String inventoryQuantity = cursor.getString(quantityColumnIndex);
                quant = Integer.parseInt(inventoryQuantity);
                if (quant > 0) {
                    ContentValues values = new ContentValues();
                    values.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, Integer.parseInt(inventoryQuantity) - 1);
                    context.getContentResolver().update(ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, itemId), values, null, null);
                    Toast.makeText(context, "minus 1", Toast.LENGTH_SHORT).show();
                    String calculated = String.valueOf(quant);
                    quantityTextView.setText(calculated);//inventoryQuantity
                    flagClicked = false;
                } else {
                    Toast.makeText(context, "Sorry no can do", Toast.LENGTH_SHORT).show();
                }
            }


        });


        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);

        String inventoryName = cursor.getString(nameColumnIndex);
        String inventoryQuantity = cursor.getString(quantityColumnIndex);
        String inventoryPrice = cursor.getString(priceColumnIndex);


        byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex(COLUMN_INVENTORY_IMAGE));
        Drawable drawable = new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length));

        nameTextView.setText(inventoryName);
        if (flagClicked) {
            String calculated = String.valueOf(quant);
            quantityTextView.setText(calculated);
            flagClicked = false;
        } else {
            quantityTextView.setText(inventoryQuantity);
        }

        priceTextView.setText(inventoryPrice);
        imageImageView.setImageDrawable(drawable);
    }


}


