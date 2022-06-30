package com.bilgeadam.fileexample;

import java.io.Serializable;
import java.sql.Timestamp;

public class Person implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9051170631605991793L;
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String gender;
	private Timestamp birthday;
	
	public Person() {
		
	}
	public Person(int id, String firstName, String lastName, String email, String gender, Timestamp birthday) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
		this.birthday = birthday;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Timestamp getBirthday() {
		return birthday;
	}
	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", gender=" + gender + ", birthday=" + birthday + "]";
	}
	
	
	
	
	
	
	
}
