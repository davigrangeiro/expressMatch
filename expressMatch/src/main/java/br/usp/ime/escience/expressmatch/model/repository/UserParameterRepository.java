package br.usp.ime.escience.expressmatch.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.usp.ime.escience.expressmatch.model.UserParameter;

public interface UserParameterRepository extends JpaRepository<UserParameter, Integer>{

	public UserParameter getUserParameterByRoot(boolean root);
	
}
