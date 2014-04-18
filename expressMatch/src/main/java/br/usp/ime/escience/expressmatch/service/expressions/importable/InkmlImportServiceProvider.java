package br.usp.ime.escience.expressmatch.service.expressions.importable;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.usp.ime.escience.expressmatch.inkml.reader.EMGTDomReader;
import br.usp.ime.escience.expressmatch.model.Expression;
import br.usp.ime.escience.expressmatch.service.expressions.ExpressionServiceProvider;
import br.usp.ime.escience.expressmatch.service.user.UserServiceProvider;

@Service
public class InkmlImportServiceProvider {

	@Inject
	private ExpressionServiceProvider expressionProvider;

	@Inject
	private EMGTDomReader inkmlDomReader;
	
	@Inject
	private UserServiceProvider userServiceProvider;
	
	public void importDataSet(){
		List<Expression> expressions = inkmlDomReader.getDataSet();
		for (Expression expression : expressions) {
			if("user0".equals(expression.getUserInfo().getName())){
				expression.getUserInfo().setName("root");
			}
			expression.setUserInfo(this.userServiceProvider.loadUserInformation(expression.getUserInfo().getName()));
		}
		expressionProvider.saveExpressions(expressions);
	}
	
}
