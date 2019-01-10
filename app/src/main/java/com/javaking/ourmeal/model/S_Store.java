package com.javaking.ourmeal.model;


import java.io.Serializable;

public class S_Store implements Serializable {


    private String store_code;
    private String store_title;
    private String member_id;
    private String loc_code;
    private String zip_no;
    private String roadaddrpart1;
    private String addrdetail;
    private String roadaddrpart2;
    private String store_address;
    private String store_tel ;
    private String store_info;
    private String store_image;
    private String store_type;
    private String store_parking;
    private String store_o_time;
    private String store_b_time;
    private String store_website;
    private String store_c_date;
    private String store_u_date;
    private String store_d_date;
    private String score_avg;
    private int store_reviewCount;


    public S_Store() {}

    public S_Store(String store_code, String store_title, String member_id, String loc_code, String zip_no,
                   String roadaddrpart1, String addrdetail, String roadaddrpart2, String store_address, String store_tel,
                   String store_info, String store_image, String store_type, String store_parking, String store_o_time,
                   String store_b_time, String store_website, String store_c_date, String store_u_date, String store_d_date
            , String score_avg, int store_reviewCount) {

        this.store_code = store_code;
        this.store_title = store_title;
        this.member_id = member_id;
        this.loc_code = loc_code;
        this.zip_no = zip_no;
        this.roadaddrpart1 = roadaddrpart1;
        this.addrdetail = addrdetail;
        this.roadaddrpart2 = roadaddrpart2;
        this.store_address = store_address;
        this.store_tel = store_tel;
        this.store_info = store_info;
        this.store_image = store_image;
        this.store_type = store_type;
        this.store_parking = store_parking;
        this.store_o_time = store_o_time;
        this.store_b_time = store_b_time;
        this.store_website = store_website;
        this.store_c_date = store_c_date;
        this.store_u_date = store_u_date;
        this.store_d_date = store_d_date;
        this.score_avg = score_avg;
        this.store_reviewCount = store_reviewCount;

    }

    public int getStore_reviewCount() {
        return store_reviewCount;
    }

    public void setStore_reviewCount(int recordCount) {
        this.store_reviewCount = recordCount;
    }

    public String getScore_avg() {
        return score_avg;
    }

    public void setScore_avg(String score_avg) {
        this.score_avg = score_avg;
    }

    public String getStore_code() {
        return store_code;
    }

    public void setStore_code(String store_code) {
        this.store_code = store_code;
    }

    public String getStore_title() {
        return store_title;
    }

    public void setStore_title(String store_title) {
        this.store_title = store_title;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getLoc_code() {
        return loc_code;
    }

    public void setLoc_code(String loc_code) {
        this.loc_code = loc_code;
    }

    public String getZip_no() {
        return zip_no;
    }

    public void setZip_no(String zip_no) {
        this.zip_no = zip_no;
    }

    public String getRoadaddrpart1() {
        return roadaddrpart1;
    }

    public void setRoadaddrpart1(String roadaddrpart1) {
        this.roadaddrpart1 = roadaddrpart1;
    }

    public String getAddrdetail() {
        return addrdetail;
    }

    public void setAddrdetail(String addrdetail) {
        this.addrdetail = addrdetail;
    }

    public String getRoadaddrpart2() {
        return roadaddrpart2;
    }

    public void setRoadaddrpart2(String roadaddrpart2) {
        this.roadaddrpart2 = roadaddrpart2;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getStore_tel() {
        return store_tel;
    }

    public void setStore_tel(String store_tel) {
        this.store_tel = store_tel;
    }

    public String getStore_info() {
        return store_info;
    }

    public void setStore_info(String store_info) {
        this.store_info = store_info;
    }

    public String getStore_image() {
        return store_image;
    }

    public void setStore_image(String store_image) {
        this.store_image = store_image;
    }

    public String getStore_type() {
        return store_type;
    }

    public void setStore_type(String store_type) {
        this.store_type = store_type;
    }

    public String getStore_parking() {
        return store_parking;
    }

    public void setStore_parking(String store_parking) {
        this.store_parking = store_parking;
    }

    public String getStore_o_time() {
        return store_o_time;
    }

    public void setStore_o_time(String store_o_time) {
        this.store_o_time = store_o_time;
    }

    public String getStore_b_time() {
        return store_b_time;
    }

    public void setStore_b_time(String store_b_time) {
        this.store_b_time = store_b_time;
    }

    public String getStore_website() {
        return store_website;
    }

    public void setStore_website(String store_website) {
        this.store_website = store_website;
    }

    public String getStore_c_date() {
        return store_c_date;
    }

    public void setStore_c_date(String store_c_date) {
        this.store_c_date = store_c_date;
    }

    public String getStore_u_date() {
        return store_u_date;
    }

    public void setStore_u_date(String store_u_date) {
        this.store_u_date = store_u_date;
    }

    public String getStore_d_date() {
        return store_d_date;
    }

    public void setStore_d_date(String store_d_date) {
        this.store_d_date = store_d_date;
    }







}
