/*
Author: 20175165 서민주
Last Modification date: 19.12.03
Function: Custum Info Window
 */

package com.bungae1112.final_proj.mainActivity.mapView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bungae1112.final_proj.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


public class InfoWindowCustom implements GoogleMap.InfoWindowAdapter {
    Context context;
    LayoutInflater inflater;

    public InfoWindowCustom(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Log.d("InfoWindowCustom", "getInfoContents");

        inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.info_window, null);

        String img_url = "http://54.180.153.64:3000/images/" + marker.getTitle() + ".jpg";
        Log.d("IMAGE LOAD", "IMAGE LOADING FROM" + img_url);


        ImageView imageView = (ImageView) v.findViewById(R.id.storeImg);
        TextView title = (TextView) v.findViewById(R.id.nameTxt);
        TextView seats = (TextView) v.findViewById(R.id.seatsTxt);

        Glide.with( inflater.getContext() ).load(img_url).into(imageView);

        title.setText(marker.getTitle());
        seats.setText(marker.getSnippet());

        return v;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Log.d("InfoWindowCustom", "getInfoWindow");

        return null;
    }
}

