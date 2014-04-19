package br.usp.ime.escience.expressmatch.service.expressions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.usp.ime.escience.expressmatch.model.Expression;
import br.usp.ime.escience.expressmatch.model.ExpressionType;
import br.usp.ime.escience.expressmatch.model.repository.ExpressionRepository;
import br.usp.ime.escience.expressmatch.model.repository.ExpressionTypeRepository;

@Service
public class ExpressionServiceProvider {

	private static final Logger logger = LoggerFactory.getLogger(ExpressionServiceProvider.class);
	
	@Inject
	private ExpressionRepository expressionRepository;
	
	@Inject
	private ExpressionTypeRepository expressionTypeRepository;
	
	public void saveExpressions(List<Expression> expressions){
		logger.info("Saving expressions.");
		this.expressionRepository.save(expressions);
		logger.info(MessageFormat.format("Saved {0} expressions.", expressions.size()));
	}
	
	public List<ExpressionType> generateExpressionTypesByExpression(List<Expression> expressions){
		logger.info("Generating expression Types");
		List<ExpressionType> types = new ArrayList<ExpressionType>(); 
		for (Expression expression : expressions) {
			ExpressionType type = expression.getExpressionType();
			type.setLabel(expression.getLabel());
			expression.setExpressionType(type);
			types.add(type);
		}
		this.expressionTypeRepository.save(types);
		logger.info(MessageFormat.format("Flushing expression Types, Inserted {0} records.", types.size()));
		this.expressionRepository.flush();
		return types;
	}
	
	public void saveExpressionTypes(List<ExpressionType> expressions){
		logger.info("Saving expression types.");
		this.expressionTypeRepository.save(expressions);
		logger.info(MessageFormat.format("Saved {0} expression types.", expressions.size()));
	}
	
	
}
