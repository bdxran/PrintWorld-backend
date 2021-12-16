package com.rbl.printworld.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class User {
	private String pseudo;
	private String mail;
	private String password;
	private Access access;

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Access getAccess() {
		return access;
	}

	public void setAccess(Access access) {
		this.access = access;
	}

	@Override
	public String toString() {
		return "User{" +
				"pseudo='" + pseudo + '\'' +
				", mail='" + mail + '\'' +
				", password='" + password + '\'' +
				", access=" + access +
				'}';
	}
}
