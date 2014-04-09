package br.usp.ime.escience.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import br.usp.ime.escience.gson.controller.StrokeJSONParser;
import br.usp.ime.escience.model.Stroke;

@Named
@Scope("request")
public class DrawablePanel implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DrawablePanel.class);

	@Inject
	private StrokeJSONParser strokeParser; 
	
	private String jsonString;
	private String jsonLoadString;
	private Stroke strokes[];

	public DrawablePanel() {
		super();
		this.jsonString = "";
	}
	
	public String saveStrokes() {
		try {
			logger.info("logandooo.");
			parseJsonString(jsonString);
			setJsonLoadString(jsonString);
			this.jsonString = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("entrou");
		return "";
	}
	
	private void parseJsonString(String jsonString)
    {
		setStrokes(this.getStrokeParser().arrayFromJSON(jsonString));
    }
	
	@PostConstruct
	public void init() {
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}

//	/**
//	 * @return the logger
//	 */
//	public Logger getLogger() {
//		return logger;
//	}
//
//	/**
//	 * @param logger the logger to set
//	 */
//	public void setLogger(Logger logger) {
//		this.logger = logger;
//	}

	/**
	 * @return the strokeParser
	 */
	public StrokeJSONParser getStrokeParser() {
		return strokeParser;
	}

	/**
	 * @param strokeParser the strokeParser to set
	 */
	public void setStrokeParser(StrokeJSONParser strokeParser) {
		this.strokeParser = strokeParser;
	}

	/**
	 * @return the jsonString
	 */
	public String getJsonString() {
		return jsonString;
	}

	/**
	 * @param jsonString the jsonString to set
	 */
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	/**
	 * @return the jsonLoadString
	 */
	public String getJsonLoadString() {
		return jsonLoadString;
	}

	/**
	 * @param jsonLoadString the jsonLoadString to set
	 */
	public void setJsonLoadString(String jsonLoadString) {
		this.jsonLoadString = jsonLoadString;
	}

	/**
	 * @return the strokes
	 */
	public Stroke[] getStrokes() {
		return strokes;
	}

	/**
	 * @param strokes the strokes to set
	 */
	public void setStrokes(Stroke[] strokes) {
		this.strokes = strokes;
	}
	
}
