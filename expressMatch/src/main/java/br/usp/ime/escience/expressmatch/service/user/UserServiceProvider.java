package br.usp.ime.escience.expressmatch.service.user;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.usp.ime.escience.expressmatch.model.UserInfo;
import br.usp.ime.escience.expressmatch.model.dao.UserInfoDao;

@Service
public class UserServiceProvider {

	@Inject
	private UserInfoDao userInfoDao;
	
	public UserInfo loadUserInformation(String nick){
		return this.userInfoDao.getUserInformationByNickName(nick);
	}
	
}
