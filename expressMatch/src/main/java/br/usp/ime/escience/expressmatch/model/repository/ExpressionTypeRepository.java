package br.usp.ime.escience.expressmatch.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.usp.ime.escience.expressmatch.model.ExpressionType;

public interface ExpressionTypeRepository extends JpaRepository<ExpressionType, Integer> {

}
