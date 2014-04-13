package br.usp.ime.escience.controller;

import java.io.Serializable;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.usp.ime.escience.model.User;


@Named
@Scope("session")
public class UserController implements Serializable {
	 
    private User user;
 
    public UserController() {
        user = new User();
        SecurityContext context = SecurityContextHolder.getContext();
        if (context instanceof SecurityContext){
            Authentication authentication = context.getAuthentication();
            
            if (authentication instanceof Authentication){
                user.setUsername(((org.springframework.security.core.userdetails.User)authentication.getPrincipal()).getUsername());
            }
        }
    }
 
    public User getUser() {
        return user;
    }
 
    public void setUser(User usuario) {
        this.user = usuario;
    }
     
}
