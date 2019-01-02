package com.javaking.ourmeal.model;


public class Food_menu {

	private String store_code;
	private String fm_code;
	private String fm_name;
	private String fm_image;
	private String fm_info;
	private String fm_price;
	private String fm_kcal;
	private String fm_allergy;
	
	public Food_menu() {}
	
	public Food_menu(String store_code, String fm_code, String fm_name, String fm_image, String fm_info,
			String fm_price, String fm_kcal, String fm_allergy) {
		
		this.store_code = store_code;
		this.fm_code = fm_code;
		this.fm_name = fm_name;
		this.fm_image = fm_image;
		this.fm_info = fm_info;
		this.fm_price = fm_price;
		this.fm_kcal = fm_kcal;
		this.fm_allergy = fm_allergy;
	}

	public String getStore_code() {
		return store_code;
	}

	public void setStore_code(String store_code) {
		this.store_code = store_code;
	}

	public String getFm_code() {
		return fm_code;
	}

	public void setFm_code(String fm_code) {
		this.fm_code = fm_code;
	}

	public String getFm_name() {
		return fm_name;
	}

	public void setFm_name(String fm_name) {
		this.fm_name = fm_name;
	}

	public String getFm_image() {
		return fm_image;
	}

	public void setFm_image(String fm_image) {
		this.fm_image = fm_image;
	}

	public String getFm_info() {
		return fm_info;
	}

	public void setFm_info(String fm_info) {
		this.fm_info = fm_info;
	}

	public String getFm_price() {
		return fm_price;
	}

	public void setFm_price(String fm_price) {
		this.fm_price = fm_price;
	}

	public String getFm_kcal() {
		return fm_kcal;
	}

	public void setFm_kcal(String fm_kcal) {
		this.fm_kcal = fm_kcal;
	}

	public String getFm_allergy() {
		return fm_allergy;
	}

	public void setFm_allergy(String fm_allergy) {
		this.fm_allergy = fm_allergy;
	}
	
	
	
	
}
