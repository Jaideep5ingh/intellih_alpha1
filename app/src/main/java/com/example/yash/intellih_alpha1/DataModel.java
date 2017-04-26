package com.example.yash.intellih_alpha1;

/**
 * Created by Admin on 13-04-2017.
 */

public class DataModel {String room;
    int image;

    public DataModel(String room, int image) {
        this.room = room;
        this.image = image;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
