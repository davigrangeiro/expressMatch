package br.usp.ime.escience.expressmatch.controller.config;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import br.usp.ime.escience.expressmatch.service.expressions.importable.InkmlImportServiceProvider;


@Named
@Scope("request")
public class ConfigController implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);
	
	@Inject
	private InkmlImportServiceProvider importServiceProvider;
	
	public String importDataBase(){
		logger.debug("Starting dataset import");
		importServiceProvider.importDataSet();
		return "";
	}

}
