package com.example.moappfinal_jachuisochui;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

public class room  implements Parcelable{


    private String name;
    private String location;
    private String description;
    private String tel;


    public room(String name, String location, String description, String tel)
    {
        this.name = name;
        this.location = location;
        this.description = description;
        this.tel = tel;
    }


    protected room(Parcel in) {
        name = in.readString();
        location = in.readString();
        description = in.readString();
        tel = in.readString();
    }

    public static final Creator<room> CREATOR = new Creator<room>() {
        @Override
        public room createFromParcel(Parcel in) {
            return new room(in);
        }

        @Override
        public room[] newArray(int size) {
            return new room[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(description);
        dest.writeString(tel);
    }

    public String getName() {
        return name;
    }

    public String getLocation(){return location;}

    public String getDescription(){return description;}

    public String getTel() {
        return tel;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location){this.location = location;}

    public void setDescription(String description){this.description = description;}

    public void setTel(String tel) {
        this.tel = tel;
    }


}
