package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

import org.hibernate.validator.constraints.NotEmpty;

public class GamePlayerRequestBean {

    @NotEmpty
	private String token;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}