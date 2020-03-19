package org.bawaberkah.app.Model;

public class Product {

    private int percentage;
    private String judul, target, terkumpul, path, start, end,id;


    public Product(String id, String judul, String target, String terkumpul, String path, int percentage, String start, String end) {
        this.id = id;
        this.judul = judul;
        this.target = target;
        this.terkumpul = terkumpul;
        this.path = path;
        this.percentage = percentage;
        this.start = start;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getTarget() {
        return target;
    }

    public String getTerkumpul() {
        return terkumpul;
    }

    public String getPath() {
        return path;
    }

    public int getPercentage() {
        return percentage;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

}
