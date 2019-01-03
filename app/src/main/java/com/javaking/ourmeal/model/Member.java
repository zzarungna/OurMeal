package com.javaking.ourmeal.model;

import java.util.Date;

public class Member {

    private String member_id;
    private String member_pw;
    private String member_name;
    private String member_email;
    private String loc_code;
    private String member_address;
    private String member_phone;
    private String member_birth;
    private String member_sex;
    private String member_date;
    private String member_image;
    private int member_type;
    private String member_grade;

    public Member() {}

    public Member(String member_id, String member_pw, String member_name, String member_email, String member_phone,
                  String member_birth, String member_sex, String member_date, String member_image, String member_grade,
                  String member_address) {

        this.member_id = member_id;
        this.member_pw = member_pw;
        this.member_name = member_name;
        this.member_email = member_email;
        this.member_phone = member_phone;
        this.member_birth = member_birth;
        this.member_sex = member_sex;
        this.member_date = member_date;
        this.member_image = member_image;
        this.member_grade = member_grade;
        this.member_address = member_address;
    }
    public Member(String member_id, String member_pw, String member_name, String member_email, String loc_code,
                  String member_phone, String member_birth, String member_sex, String member_date, String member_image,
                  String member_grade, String member_address) {

        this.member_id = member_id;
        this.member_pw = member_pw;
        this.member_name = member_name;
        this.member_email = member_email;
        this.loc_code = loc_code;
        this.member_phone = member_phone;
        this.member_birth = member_birth;
        this.member_sex = member_sex;
        this.member_date = member_date;
        this.member_image = member_image;
        this.member_grade = member_grade;
        this.member_address = member_address;
    }

    public Member(String member_id, String member_pw, String member_name, String member_email, String loc_code,
                  String member_phone, String member_birth, String member_sex, String member_date, String member_image,
                  String member_grade, String member_address, int member_type) {
        super();
        this.member_id = member_id;
        this.member_pw = member_pw;
        this.member_name = member_name;
        this.member_email = member_email;
        this.loc_code = loc_code;
        this.member_phone = member_phone;
        this.member_birth = member_birth;
        this.member_sex = member_sex;
        this.member_date = member_date;
        this.member_image = member_image;
        this.member_grade = member_grade;
        this.member_address = member_address;
        this.member_type = member_type;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_pw() {
        return member_pw;
    }

    public void setMember_pw(String member_pw) {
        this.member_pw = member_pw;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_email() {
        return member_email;
    }

    public void setMember_email(String member_email) {
        this.member_email = member_email;
    }

    public String getLoc_code() {
        return loc_code;
    }

    public void setLoc_code(String loc_code) {
        this.loc_code = loc_code;
    }

    public String getMember_address() {
        return member_address;
    }

    public void setMember_address(String member_address) {
        this.member_address = member_address;
    }

    public String getMember_phone() {
        return member_phone;
    }

    public void setMember_phone(String member_phone) {
        this.member_phone = member_phone;
    }

    public String getMember_birth() {
        return member_birth;
    }

    public void setMember_birth(String member_birth) {
        this.member_birth = member_birth;
    }

    public String getMember_sex() {
        return member_sex;
    }

    public void setMember_sex(String member_sex) {
        this.member_sex = member_sex;
    }

    public String getMember_date() {
        return member_date;
    }

    public void setMember_date(String member_date) {
        this.member_date = member_date;
    }

    public String getMember_image() {
        return member_image;
    }

    public void setMember_image(String member_image) {
        this.member_image = member_image;
    }

    public int getMember_type() {
        return member_type;
    }

    public void setMember_type(int member_type) {
        this.member_type = member_type;
    }

    public String getMember_grade() {
        return member_grade;
    }

    public void setMember_grade(String member_grade) {
        this.member_grade = member_grade;
    }

}
