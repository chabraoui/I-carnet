package com.sid.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ForgetPasswordRequest {
	@NotBlank(message="Ce champ ne doit etre null !")
	@Email(message="ce champ doit respecter le format email !")
	private String email;
	@NotBlank(message="Ce champ ne doit etre null !")
	@Size(min=8, message="mot de passe doit avoir au moins 8 caracteres !")
	@Size(max=12, message="mot de passe doit avoir au max 12 caracteres !")
	@Pattern(regexp="(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message="ce mot de passe doit avoir des lettres en Maj et Minsc et numero et plus que 8 char")
	private String Password;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String Password) {
		this.Password = Password;
	}


}
