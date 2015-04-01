package ch.uzh.ifi.seal.soprafs15.controller.beans.user;

import org.hibernate.validator.constraints.NotEmpty;

public class UserLoginLogoutRequestBean {

    @NotEmpty
	private String token;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
