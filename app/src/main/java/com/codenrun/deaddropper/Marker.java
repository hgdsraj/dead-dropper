package com.codenrun.deaddropper;

/**
 * Created by salma on 2017-01-08.
 */

public class Marker {

    // private variables
    double _latitude;
    double _longitude;
    String _name;
    String _description;

    // default constructor
    public Marker(){

    }

    // constructor
    public Marker(double latitude, double longitude, String name, String description){
        this._latitude = latitude;
        this._longitude = longitude;
        this._name = name;
        this._description = description;
    }

    public double get_latitude() {
        return _latitude;
    }

    public void set_latitude(double _latitude) {
        this._latitude = _latitude;
    }

    public double get_longitude() {
        return _longitude;
    }

    public void set_longitude(double _longitude) {
        this._longitude = _longitude;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }
}
