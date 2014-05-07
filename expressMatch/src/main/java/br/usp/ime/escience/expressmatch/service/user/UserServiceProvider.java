package br.usp.ime.escience.expressmatch.service.user;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.usp.ime.escience.expressmatch.model.UserInfo;
import br.usp.ime.escience.expressmatch.model.UserParameter;
import br.usp.ime.escience.expressmatch.model.repository.UserInfoRepository;
import br.usp.ime.escience.expressmatch.model.repository.UserParameterRepository;

@Service
public class UserServiceProvider {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceProvider.class);
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private UserParameterRepository userParameterRepository;
	
	public UserInfo loadUserInformation(String nick){
		nick = nick.toLowerCase();
		
		UserInfo res = this.userInfoRepository.getUserInfoByUserNick(nick);
		if(null == res){
			logger.warn(MessageFormat.format("User ({0}) not found.", nick));
		}
		return res;
	}
	
	public UserInfo getCurrentUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String name = null;
		
		if (null != auth) {
			name = auth.getName();
		}

		if(null == name){
			logger.warn("There is no logged user.");
		}
		logger.info(MessageFormat.format("Found logged user ({0})", name));
		
		UserInfo res = null;
		res = this.userInfoRepository.getUserInfoByUserNick(name.toLowerCase());
		
		if(null == res){
			logger.warn(MessageFormat.format("User ({0}) not found.", name));
		}
		
		return res;
	}
	
	public UserParameter getUserParameters(UserInfo uInfo){
		UserParameter res = uInfo.getUserParameter();
		
		if(null == res){
			res = this.userParameterRepository.getUserParameterByRoot(true);
		}
		return res;
	}
	
	public UserParameter getUserParameters(){
		return getUserParameters(this.getCurrentUser());
	}
	

}
