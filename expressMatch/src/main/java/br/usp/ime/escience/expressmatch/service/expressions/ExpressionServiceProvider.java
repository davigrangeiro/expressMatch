package br.usp.ime.escience.expressmatch.service.expressions;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.usp.ime.escience.expressmatch.model.Expression;
import br.usp.ime.escience.expressmatch.model.dao.ExpressionDao;

@Service
public class ExpressionServiceProvider {

	@Inject
	private ExpressionDao expressionDao;
	
	public void saveExpressions(List<Expression> expressions){
		for (Expression expression : expressions) {
			expression.setExpressionType(null);
			this.expressionDao.create(expression);
			break;
		}
	}
	
}
