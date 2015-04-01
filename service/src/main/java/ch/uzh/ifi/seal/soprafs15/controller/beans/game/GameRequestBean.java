package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

import org.hibernate.validator.constraints.NotEmpty;

public class GameRequestBean {

    @NotEmpty
	private String name;

    @NotEmpty
	private String token;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}