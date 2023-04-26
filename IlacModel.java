package com.example.alzheimerhastatakip;

public class IlacModel {
    private String ID;
    private String ilacAdi;
    private String ilacGun;
    private String ilacSaat;
    private String ilacDoz;
    private String ilacNasil;
    private int ilacResult;
    public IlacModel(){

    }
    public IlacModel(String ilacAdi, String ilacGun, String ilacSaat, String ilacDoz,String ilacNasil, int ilacResult){
        this.ilacAdi=ilacAdi;
        this.ilacGun=ilacGun;
        this.ilacSaat=ilacSaat;
        this.ilacDoz=ilacDoz;
        this.ilacNasil=ilacNasil;
        this.ilacResult=ilacResult;
    }

    public String getIlacAdi(){
        return ilacAdi;
    }
    public void setIlacAdi(String ilacAdi){
        this.ilacAdi=ilacAdi;
    }

    public String getIlacGun(){
        return ilacGun;
    }
    public void setIlacGun(String ilacGun){
        this.ilacGun=ilacGun;
    }

    public String getIlacSaat(){
        return ilacSaat;
    }
    public void setIlacSaat(String ilacSaat){
        this.ilacSaat=ilacSaat;
    }

    public String getIlacDoz(){
        return ilacDoz;
    }
    public void setIlacDoz(String ilacDoz){
        this.ilacDoz=ilacDoz;
    }

    public String getIlacNasil(){
        return ilacNasil;
    }
    public void setIlacNasil(String ilacNasil){
        this.ilacNasil=ilacNasil;
    }

    public int getIlacResult(){
        return ilacResult;
    }
    public void setIlacResult(int ilacResult){
        this.ilacResult=ilacResult;
    }

}
