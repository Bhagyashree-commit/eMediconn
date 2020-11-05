package com.example.emediconn.Model;

public class CategoryModel {
public String speciality_id;

    public String getSpeciality_id() {
        return speciality_id;
    }

    public void setSpeciality_id(String speciality_id) {
        this.speciality_id = speciality_id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String speciality ;
public String imagepath;

    public CategoryModel() {
        this.speciality_id = speciality_id;
        this.speciality = speciality;
        this.imagepath = imagepath;
    }


}
