package br.usp.ime.escience.expressmatch.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.usp.ime.escience.expressmatch.exception.ExpressMatchExpression;
import br.usp.ime.escience.expressmatch.model.Expression;
import br.usp.ime.escience.expressmatch.model.ExpressionType;
import br.usp.ime.escience.expressmatch.model.Stroke;
import br.usp.ime.escience.expressmatch.model.Symbol;
import br.usp.ime.escience.expressmatch.model.User;
import br.usp.ime.escience.expressmatch.service.expressions.ExpressionServiceProvider;
import br.usp.ime.escience.expressmatch.service.gson.generic.StrokeJSONParser;

@Component
@ManagedBean
@ViewScoped
public class EvaluateExpressionsPanelController implements Serializable{

	private static final int NOT_STARTED_YET = -1;

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(EvaluateExpressionsPanelController.class);

	@Autowired
	private StrokeJSONParser strokeParser; 
	
	@Autowired
	private UserController userController;
	
	@Autowired
	private ExpressionServiceProvider expressionServiceProvider;
	
	private String jsonString;
	
	private String jsonLoadString;
	
	private Stroke strokes[];
	
	private List<ExpressionType> types;
	
	private Expression userExpression;
	
	private int eTypeIndex = NOT_STARTED_YET;

	public EvaluateExpressionsPanelController() {
		super();
		cleanTranscriptionData();
	}

	private void cleanTranscriptionData() {
		this.jsonString = "";
	}
	
	public String saveStrokes() {
		try {
			parseJsonString(jsonString);
			
			if (null != this.strokes && this.strokes.length > 0) {
				this.expressionServiceProvider.saveTranscription(this.strokes, this.types.get(this.eTypeIndex), this.userController.getUser(), this.userExpression);
				addMessage("Success", "Your transcription was successfully saved", null);
			} else {
				addMessage("Warning", "You should transcribe the expression before save.", FacesMessage.SEVERITY_WARN);
			}
			
		} catch (ExpressMatchExpression e){
			logger.error(e.getMessage(), e);

			addMessage("Error", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);

			addMessage("Error", "There was an error while saving the transcription", FacesMessage.SEVERITY_ERROR);
		} finally {
			loadExpressionType(eTypeIndex, this.userController.getUser());
		}
		return "";
	}
	
	private void parseJsonString(String jsonString)
    {
		setStrokes(this.getStrokeParser().arrayFromJSON(jsonString));
    }
	
	public void init() {
		if(NOT_STARTED_YET == eTypeIndex){
			FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			types = this.expressionServiceProvider.loadExpressionTypes();
			
			eTypeIndex = 0;
			loadExpressionType(eTypeIndex, this.userController.getUser());
		}
	}

	private void loadExpressionType(int index, User user) {
		if(types != null && index < types.size()) {
			
			ExpressionType type = this.types.get(index);
		
			Stroke[] modelStrokes = getStrokesForExpression(this.expressionServiceProvider.loadExpressionForExpressionType(type));
			
			setJsonLoadString(this.getStrokeParser().toJSON(modelStrokes));
			
			cleanTranscriptionData();
			Stroke[] userEMStrokes;
			userExpression = getUserExpression(user, type);
			
			if(null != userExpression){
				userEMStrokes = getStrokesForExpression(userExpression);
				setJsonString(this.getStrokeParser().toJSON(userEMStrokes));
			}
		}
	}
	
	public String nextEM(){
		eTypeIndex = (eTypeIndex >= types.size()-1) ? 0: eTypeIndex + 1;
		loadExpressionType(eTypeIndex, this.userController.getUser());
		
		return "";
	}
	
	public String previousEM(){
		eTypeIndex = (0 >= eTypeIndex) ? types.size()-1: eTypeIndex - 1;
		loadExpressionType(eTypeIndex, this.userController.getUser());
		
		return "";
		
	}

	private Expression getUserExpression(User user, ExpressionType type) {
		return this.expressionServiceProvider.loadExpressionUserForExpressionType(type, user);
	}

	private Stroke[] getStrokesForExpression(Expression type) {
		Stroke[] modelStrokes = null;
		
		int strokeAmount = 0;
		for (Symbol symbol : type.getSymbols()) {
			strokeAmount += symbol.getStrokes().size();
		}
		
		modelStrokes = new Stroke[strokeAmount];
		int i = 0;
		
		for (Symbol symbol : type.getSymbols()) {
			for (Stroke stroke : symbol.getStrokes()) {
				modelStrokes[i++] = new Stroke(stroke);
			}
		}
		return modelStrokes;
	}
	
	public void addMessage(String header, String desc, Severity severity){
		FacesContext context = FacesContext.getCurrentInstance();
        
		if(null == severity){
			severity = FacesMessage.SEVERITY_INFO;
		}
		
		context.addMessage(null, new FacesMessage(severity, header, desc));
	}

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
