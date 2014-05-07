package br.usp.ime.escience.expressmatch.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.usp.ime.escience.expressmatch.model.User;

@Component
@ManagedBean
@SessionScoped
public class UserController implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	
    public UserController() {
    	super();
    }
 
    public User getUser() {
        User user = new User();
        SecurityContext context = SecurityContextHolder.getContext();
        
        if (context instanceof SecurityContext){
            Authentication authentication = context.getAuthentication();
            
            if (authentication instanceof Authentication){
                user.setNick(((org.springframework.security.core.userdetails.User)authentication.getPrincipal()).getUsername());
            }
        }
            
        return user;
    }
     
}
