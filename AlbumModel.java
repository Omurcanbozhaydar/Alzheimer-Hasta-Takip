package com.example.alzheimerhastatakip;

public class AlbumModel {
    private String ID;
    private String isim;
    private String resim;
    public AlbumModel(){

    }
    public AlbumModel(String isim, String resim){
        this.isim=isim;
        this.resim=resim;

    }

    public String getIsim(){
        return isim;
    }
    public void setIsim(String isim){
        this.isim=isim;
    }

    public String getResim(){
        return resim;
    }
    public void setresim(String resim){
        this.resim=resim;
    }

}
