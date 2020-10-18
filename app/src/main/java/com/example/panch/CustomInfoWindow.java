package com.example.panch;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter{

    private Context context;

    public CustomInfoWindow (Context ctx) {
        this.context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.info_window,null);

        TextView countryName = view.findViewById(R.id.countryName);
        TextView weatherInfo = view.findViewById(R.id.weatherInfo);
        TextView phyDens = view.findViewById(R.id.physInfo);
        TextView bedDens = view.findViewById(R.id.bedInfo);
        countryName.setText(marker.getTitle());
        if (marker.getSnippet() != null);
            String[] info = marker.getSnippet().split("-");
    
        for (int i = 0; i < info.length; i++) {
            switch (i) {
                case 0:
                    weatherInfo.setText(info[i]);
                    break;
                case 1:
                    phyDens.setText(info[i]);
                    break;
                case 2:
                    bedDens.setText(info[i]);
                    break;
                default:
                    break;
            }

        }

        return view;
    }
}
