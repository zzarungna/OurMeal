package com.javaking.ourmeal.model;

public class Star_bulletin {
	
	
	private String sb_no;
	private String sb_title;
	private String store_code;
	private String member_id;
	private String sb_score;
	private String sb_content;
	private String sb_image;
	private String sb_c_date;
	private String sb_u_date;
	private String sb_d_date;
	private String member_image;
	
	public Star_bulletin() {}
	
	
	public Star_bulletin(String sb_no, String sb_title, String store_code, String member_id, String sb_score,
			String sb_content, String sb_image, String sb_c_date, String sb_u_date, String sb_d_date, String member_image) {
		
		this.sb_no = sb_no;
		this.sb_title = sb_title;
		this.store_code = store_code;
		this.member_id = member_id;
		this.sb_score = sb_score;
		this.sb_content = sb_content;
		this.sb_image = sb_image;
		this.sb_c_date = sb_c_date;
		this.sb_u_date = sb_u_date;
		this.sb_d_date = sb_d_date;
		this.member_image = member_image;
	}
	

	public String getMember_image() {
		return member_image;
	}


	public void setMember_image(String member_image) {
		this.member_image = member_image;
	}


	public String getSb_no() {
		return sb_no;
	}

	public void setSb_no(String sb_no) {
		this.sb_no = sb_no;
	}

	public String getSb_title() {
		return sb_title;
	}

	public void setSb_title(String sb_title) {
		this.sb_title = sb_title;
	}

	public String getStore_code() {
		return store_code;
	}

	public void setStore_code(String store_code) {
		this.store_code = store_code;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getSb_score() {
		return sb_score;
	}

	public void setSb_score(String sb_score) {
		this.sb_score = sb_score;
	}

	public String getSb_content() {
		return sb_content;
	}

	public void setSb_content(String sb_content) {
		this.sb_content = sb_content;
	}

	public String getSb_image() {
		return sb_image;
	}

	public void setSb_image(String sb_image) {
		this.sb_image = sb_image;
	}

	public String getSb_c_date() {
		return sb_c_date;
	}

	public void setSb_c_date(String sb_c_date) {
		this.sb_c_date = sb_c_date;
	}

	public String getSb_u_date() {
		return sb_u_date;
	}

	public void setSb_u_date(String sb_u_date) {
		this.sb_u_date = sb_u_date;
	}

	public String getSb_d_date() {
		return sb_d_date;
	}

	public void setSb_d_date(String sb_d_date) {
		this.sb_d_date = sb_d_date;
	}
	
	
	
}
