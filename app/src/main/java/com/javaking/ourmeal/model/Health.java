package com.javaking.ourmeal.model;

public class Health {
    private String health_no;
    private String member_id;
    private String health_height;
    private String health_weight;
    private String health_basal;

    public Health() {
    }

    public String getHealth_no() {
        return health_no;
    }

    public String getMember_id() {
        return member_id;
    }

    public String getHealth_height() {
        return health_height;
    }

    public String getHealth_weight() {
        return health_weight;
    }

    public String getHealth_basal() {
        return health_basal;
    }

    public void setHealth_no(String health_no) {
        this.health_no = health_no;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public void setHealth_height(String health_height) {
        this.health_height = health_height;
    }

    public void setHealth_weight(String health_weight) {
        this.health_weight = health_weight;
    }

    public void setHealth_basal(String health_basal) {
        this.health_basal = health_basal;
    }
}
