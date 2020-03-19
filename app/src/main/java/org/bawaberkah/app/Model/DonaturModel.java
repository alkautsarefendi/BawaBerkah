package org.bawaberkah.app.Model;

public class DonaturModel {

    String catatan, id_campaign, id_donatur, nama_donatur, nominal;

    public DonaturModel(String catatan, String id_campaign, String id_donatur, String nama_donatur, String nominal) {
        this.catatan = catatan;
        this.id_campaign = id_campaign;
        this.id_donatur = id_donatur;
        this.nama_donatur = nama_donatur;
        this.nominal = nominal;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getId_campaign() {
        return id_campaign;
    }

    public void setId_campaign(String id_campaign) {
        this.id_campaign = id_campaign;
    }

    public String getId_donatur() {
        return id_donatur;
    }

    public void setId_donatur(String id_donatur) {
        this.id_donatur = id_donatur;
    }

    public String getNama_donatur() {
        return nama_donatur;
    }

    public void setNama_donatur(String nama_donatur) {
        this.nama_donatur = nama_donatur;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

}
