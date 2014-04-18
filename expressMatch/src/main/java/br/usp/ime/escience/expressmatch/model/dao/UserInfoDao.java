package br.usp.ime.escience.expressmatch.model.dao;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.usp.ime.escience.expressmatch.model.User;
import br.usp.ime.escience.expressmatch.model.UserInfo;
import br.usp.ime.escience.expressmatch.model.dao.generic.GenericDao;

@Repository
public class UserInfoDao extends GenericDao<UserInfo, Integer> {

	public UserInfo getUserInformationByNickName(String nick){
		UserInfo res = null;
		
		TypedQuery<User> query = super.em.createQuery("select u from User u where u.nick = LOWER(:nick)", User.class);
		query.setParameter("nick", nick);
		
		try{
			User u = query.getSingleResult();
			res = u.getUserInfo();
		} catch (NoResultException e){
			res = null;
		}
		return res;
	}
	
}
