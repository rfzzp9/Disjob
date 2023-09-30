package com.example.rec;

public class JobListData {

    private int star_img;
    private String job_name;
    private String hour_money;
    private String area;
    private String hireType;
    public boolean isImageChanged;

    public JobListData(int star_img, String job_name, String hour_money, String area, String hireType) {
        this.star_img = star_img;
        this.job_name = job_name;
        this.hour_money = hour_money;
        this.area = area;
        this.hireType = hireType;
    }

    public JobListData() {
    }


    public void setStar_img(int star_img) {
        this.star_img = star_img;
    }
    public int getStar_img(){return star_img;}

    public void setJob_name(String job_name) {this.job_name = job_name;}
    public String getJob_name(){return job_name;}

    public void setHour_money(String hour_money) { this.hour_money = hour_money; }
    public String getHour_money() {return hour_money;}

    public void setArea(String area) {this.area = area;}
    public String getArea() {return area; }

    public void setHireType() {this.hireType = hireType; }

    public String getHireType() { return hireType; }
}
