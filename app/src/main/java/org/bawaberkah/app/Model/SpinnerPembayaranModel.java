package org.bawaberkah.app.Model;

public class SpinnerPembayaranModel {

    private  String metode;
    private  int imgMetode;

    public SpinnerPembayaranModel(String metode, int imgMetode){
        this.metode = metode;
        this.imgMetode = imgMetode;
    }

    public String getMetode() {
        return metode;
    }

    public void setMetode(String metode) {
        this.metode = metode;
    }

    public int getImgMetode() {
        return imgMetode;
    }

    public void setImgMetode(int imgMetode) {
        this.imgMetode = imgMetode;
    }

}
