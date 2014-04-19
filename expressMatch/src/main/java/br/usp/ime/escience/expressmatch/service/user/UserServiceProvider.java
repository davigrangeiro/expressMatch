package br.usp.ime.escience.expressmatch.service.user;

import java.text.MessageFormat;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.usp.ime.escience.expressmatch.model.UserInfo;
import br.usp.ime.escience.expressmatch.model.repository.UserInfoRepository;

@Service
public class UserServiceProvider {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceProvider.class);
	
	@Inject
	private UserInfoRepository userInfoRepository;
	
	public UserInfo loadUserInformation(String nick){
		nick = nick.toLowerCase();
		
		UserInfo res = this.userInfoRepository.getUserInfoByUserNick(nick);
		if(null == res){
			logger.warn(MessageFormat.format("User ({0}) not found.", nick));
		}
		return res;
	}

}
