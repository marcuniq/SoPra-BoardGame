package ch.uzh.ifi.seal.soprafs15.controller.beans.user;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class UserRequestBean {

    @NotNull
	private Integer age;

    @NotEmpty
	private String username;
	
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
