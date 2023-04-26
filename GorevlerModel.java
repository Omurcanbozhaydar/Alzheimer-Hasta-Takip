package com.example.alzheimerhastatakip;

public class GorevlerModel {
    private String ID;
    private String gorevAdi;
    private String gorevGun;
    private String gorevSaat;
    private String gorevTekrar;
    public GorevlerModel(){

    }
    public GorevlerModel(String gorevAdi, String gorevGun, String gorevSaat, String gorevTekrar){
        this.gorevAdi=gorevAdi;
        this.gorevGun=gorevGun;
        this.gorevSaat=gorevSaat;
        this.gorevTekrar=gorevTekrar;
    }

    public String getGorevAdi(){
        return gorevAdi;
    }
    public void setGorevAdi(String gorevAdi){
        this.gorevAdi=gorevAdi;
    }

    public String getGorevGun(){
        return gorevGun;
    }
    public void setGorevGun(String gorevGun){
        this.gorevGun=gorevGun;
    }
    public String getGorevSaat(){
        return gorevSaat;
    }
    public void setGorevSaat(String gorevSaat){
        this.gorevSaat=gorevSaat;
    }
    public String getGorevTekrar(){
        return gorevTekrar;
    }
    public void setGorevTekrar(String gorevTekrar){
        this.gorevSaat=gorevTekrar;
    }


}
