package com.sid.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity(name = "addresses")
public class AddressEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3280857106882001999L;
	@Id @GeneratedValue
	private long id;	
	@Column(nullable = false)
	private String addressId;
	@Column(nullable = false)
	private String country;
	@Column(nullable = false)
	private String city;
	@Column(nullable = false)
	private String street;
	@Column(nullable = false)
	private String type;
	@ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	

}
