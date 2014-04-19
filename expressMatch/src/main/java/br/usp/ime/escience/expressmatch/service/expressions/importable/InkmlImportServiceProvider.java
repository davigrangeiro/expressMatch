package br.usp.ime.escience.expressmatch.service.expressions.importable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.usp.ime.escience.expressmatch.inkml.reader.EMGTDomReader;
import br.usp.ime.escience.expressmatch.model.Expression;
import br.usp.ime.escience.expressmatch.model.ExpressionType;
import br.usp.ime.escience.expressmatch.service.expressions.ExpressionServiceProvider;
import br.usp.ime.escience.expressmatch.service.user.UserServiceProvider;

@Service
public class InkmlImportServiceProvider {

	private static final Logger logger = LoggerFactory.getLogger(InkmlImportServiceProvider.class);

	private static final String ROOT_NICK = "root";

	private static final String ROOT_USER = "user0";

	@Inject
	private ExpressionServiceProvider expressionProvider;

	@Inject
	private EMGTDomReader inkmlDomReader;
	
	@Inject
	private UserServiceProvider userServiceProvider;
	
	public void importDataSet(){
		logger.info("Importing dataset");
		
		List<Expression> rootExpressions = new ArrayList<Expression>();
		List<Expression> expressions = inkmlDomReader.getDataSet();
		
		for (Expression expression : expressions) {
				
			if(null != expression){
		
				if(ROOT_USER.equals(expression.getUserInfo().getName())){
					
					logger.info(MessageFormat.format("Found a model Expression: {0}", expression.getExpressionType().getId()));
					
					expression.getUserInfo().setName(ROOT_NICK);
					rootExpressions.add(expression);
				}
				
				expression.setUserInfo(this.userServiceProvider.loadUserInformation(expression.getUserInfo().getName()));
			}		
		}
		
		List<ExpressionType> models = expressionProvider.generateExpressionTypesByExpression(rootExpressions);
		
		LIST_OF_EXPRESSIONS: for (Expression expression : expressions) {
			for (ExpressionType rootExpression : models) {
				if(expression.getExpressionId().intValue() == rootExpression.getId()){
					expression.setExpressionType(rootExpression);
					
					if(ROOT_NICK.equalsIgnoreCase(expression.getUserInfo().getUser().getNick())){
						rootExpression.setExpression(expression);
					}
						
					continue LIST_OF_EXPRESSIONS;
				}
			}
		}
		
		expressionProvider.saveExpressions(expressions);
		expressionProvider.saveExpressionTypes(models);
	}
	
}
