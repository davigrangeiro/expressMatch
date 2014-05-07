package br.usp.ime.escience.expressmatch.controller;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.usp.ime.escience.expressmatch.model.Expression;
import br.usp.ime.escience.expressmatch.model.ExpressionType;
import br.usp.ime.escience.expressmatch.model.Point;
import br.usp.ime.escience.expressmatch.model.Stroke;
import br.usp.ime.escience.expressmatch.model.Symbol;
import br.usp.ime.escience.expressmatch.model.User;
import br.usp.ime.escience.expressmatch.service.expressions.ExpressionServiceProvider;
import br.usp.ime.escience.expressmatch.service.gson.generic.StrokeJSONParser;

@Component
@ManagedBean
@ViewScoped
public class ExpressionsCatalogController implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ExpressionsCatalogController.class);

	@Autowired
	private StrokeJSONParser strokeParser; 
	
	@Autowired
	private ExpressionServiceProvider expressionServiceProvider;
	
	private String jsonString;

    private TreeNode expressionTree;
    
    private Expression expression;
	
	private List<ExpressionType> types;
	
	private int eTypeIndex = 0;

	public ExpressionsCatalogController() {
		super();
		cleanTranscriptionData();
	}

	private void cleanTranscriptionData() {
		this.jsonString = "";
	}
	
	
	@PostConstruct
	public void init() {
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		types = this.expressionServiceProvider.loadExpressionTypes();
		
		eTypeIndex = 0;
		loadExpressionType(eTypeIndex);
	}

	private void loadExpressionType(int index) {
		ExpressionType type = this.types.get(index);
	
		this.expression = this.expressionServiceProvider.loadExpressionForExpressionType(type);
		
		Stroke[] modelStrokes = getStrokesForExpression(expression);
		
		setJsonString(this.getStrokeParser().toJSON(modelStrokes));
		
		assembleExpressionDataTree(this.expression);
	}

	private void assembleExpressionDataTree(Expression e) {
		
		this.expressionTree = new DefaultTreeNode(
				MessageFormat.format("Expression {0} <br> {1}", 
						 e.getId(), 
						 e.getLabel()),
						 null);
		
		for (Symbol symbol : e.getSymbols()) {
			
			TreeNode nodeSymbol = new DefaultTreeNode(
					MessageFormat.format("Symbol {0} : {1} <br> Ref: {2}", 
										 symbol.getId(), 
										 symbol.getLabel(),
										 symbol.getHref()), 
										 expressionTree);
			
			for (Stroke stroke : symbol.getStrokes()) {
				
				TreeNode nodeStroke = new DefaultTreeNode(
						MessageFormat.format("Stroke {0}", 
								 stroke.getStrokeId()), 
								 nodeSymbol);
				
				StringBuilder sb = new StringBuilder();

				for (int i = 0; i < stroke.getPoints().size()-1; i++) {
					sb.append(MessageFormat.format("X: {0}, Y: {1} <br>", 
								stroke.getPoints().get(i).getX(),
								stroke.getPoints().get(i).getY()));
				}
				sb.append(MessageFormat.format("X: {0}, Y: {1} <br>", 
						stroke.getPoints().get(stroke.getPoints().size()-1).getX(),
						stroke.getPoints().get(stroke.getPoints().size()-1).getY()));
				
				TreeNode nodePoint = new DefaultTreeNode(sb.toString(), nodeStroke);
						;
			}
		}
	}
	
	public String nextEM(){
		eTypeIndex = (eTypeIndex >= types.size()-1) ? 0: eTypeIndex + 1;
		loadExpressionType(eTypeIndex);
		
		return "";
	}
	
	public String previousEM(){
		eTypeIndex = (0 == eTypeIndex) ? types.size()-1: eTypeIndex - 1;
		loadExpressionType(eTypeIndex);
		
		return "";
		
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
	 * @return the expressionTree
	 */
	public TreeNode getExpressionTree() {
		return expressionTree;
	}

	/**
	 * @param expressionTree the expressionTree to set
	 */
	public void setExpressionTree(TreeNode expressionTree) {
		this.expressionTree = expressionTree;
	}
	
}
