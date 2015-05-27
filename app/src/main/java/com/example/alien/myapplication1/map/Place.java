package com.example.alien.myapplication1.map;

/**
 * Created by Alien on 2015-05-27.
 */
public class Place extends Point {
    private int place_id;
    private int account_id;
    private String title;

    public Place(int place_id, int account_id, String title, double coordX, double coordY) {
        super(coordX, coordY);

        this.place_id = place_id;
        this.account_id = account_id;
        this.title = title;
    }

    public int getPlaceID() {
        return place_id;
    }

    public int getAccountID() {
        return account_id;
    }

    public String getTitle() {
        return title;
    }
}
