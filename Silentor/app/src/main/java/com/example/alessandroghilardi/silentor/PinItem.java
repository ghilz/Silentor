package com.example.alessandroghilardi.silentor;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PinItem {

    private double Latitude;
    private double Longitude;
    private int id;

    public PinItem(double lat, double lng) {
        super();
        this.Latitude = lat;
        this.Longitude = lng;
    }

    public PinItem() {
        super();
    }

    public double getLatitude(){
        return this.Latitude;
    }

    public double getLongitude(){
        return this.Longitude;
    }


    public LatLng getCoordinates() {
        LatLng coordinates = new LatLng(this.Latitude,  this.Longitude);
        return coordinates;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCoordinates(LatLng coordinates) {
        this.Latitude = coordinates.latitude;
        this.Longitude = coordinates.longitude;
    }

    public int getId() {
        return id;
    }
}