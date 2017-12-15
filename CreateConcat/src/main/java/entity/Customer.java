package entity;

import java.util.Date;

public class Customer {
	private String firstName;
	private String surname;
	private Date dateBirth;
	private String nationality;
	private String nationalIdentifier;
	
	public Customer() {};
	
	public Customer(String firstName, String surname, Date dateBirth, String nationality, String nationalIdentifier) {
		super();
		this.firstName = firstName;
		this.surname = surname;
		this.dateBirth = dateBirth;
		this.nationality = nationality;
		this.nationalIdentifier = nationalIdentifier;
	}
	
	public synchronized String getFirstName() {
		return firstName;
	}
	public synchronized void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public synchronized String getSurname() {
		return surname;
	}
	public synchronized void setSurname(String surname) {
		this.surname = surname;
	}
	public synchronized Date getDateBirth() {
		return dateBirth;
	}
	public synchronized void setDateBirth(Date dateBirth) {
		this.dateBirth = dateBirth;
	}
	public synchronized String getNationality() {
		return nationality;
	}
	public synchronized void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public synchronized String getNationalIdentifier() {
		return nationalIdentifier;
	}
	public synchronized void setNationalIdentifier(String nationalIdentifier) {
		this.nationalIdentifier = nationalIdentifier;
	}
	
	

	
	
	
}
