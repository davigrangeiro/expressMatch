package br.usp.ime.escience.expressmatch.service.expressions.cost;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.usp.ime.escience.expressmatch.constants.SystemConstants;
import br.usp.ime.escience.expressmatch.model.Expression;
import br.usp.ime.escience.expressmatch.model.Point;
import br.usp.ime.escience.expressmatch.model.ShapeDescriptor;
import br.usp.ime.escience.expressmatch.model.Symbol;
import br.usp.ime.escience.expressmatch.model.UserParameter;
import br.usp.ime.escience.expressmatch.model.graph.Graph;
import br.usp.ime.escience.expressmatch.model.repository.ShapeDescriptorRepository;
import br.usp.ime.escience.expressmatch.model.repository.ShapeDescriptorTypeRepository;
import br.usp.ime.escience.expressmatch.service.expressions.preprocessing.PreprocessingAlgorithms;
import br.usp.ime.escience.expressmatch.service.user.UserServiceProvider;

import com.google.gson.Gson;


@Service
public class ShapeContextServiceProvider {


	private static final Logger logger = LoggerFactory.getLogger(ShapeContextServiceProvider.class);
	
	@Autowired
	private UserServiceProvider userServiceProvider;
	
	@Autowired
	private ShapeDescriptorTypeRepository shapeDescriptorTypeRepository;
	
	@Autowired
	private ShapeDescriptorRepository shapeDescriptorRepository;
	
	
	public double[][] evaluateSymbolShapeContext(Symbol s, UserParameter parameters){

		s = PreprocessingAlgorithms.preprocessSymbol(s);
		Point[] points = PreprocessingAlgorithms.getNPoints(s, parameters.getPointsPerSymbol());
		
		Graph g = new Graph();
        ShapeContext sc;
        
        for (int i = 0; i < points.length; i++) {
            g.addVertex(i, (float)points[i].getX(), (float)points[i].getY());
        }
            
        float diagonal = (float)Math.sqrt(Math.pow(g.getHeight(), 2) + Math.pow(g.getWidth(), 2));
        
        sc = new ShapeContext(diagonal, g, parameters.getPolarLocalRegions(), parameters.getAngularLocalRegions(), false);
        
        return sc.getSC();
	}
	
	public ShapeDescriptor generateShapeDescriptor(Symbol s, UserParameter parameters){
		ShapeDescriptor sDescriptor = new ShapeDescriptor();
		sDescriptor.setShapeDescriptorType(this.shapeDescriptorTypeRepository.findOne(SystemConstants.SYMBOL_DESCRIPTOR_TYPE));
		sDescriptor.setSymbol(s);
		
		Gson gson = new Gson();
		double[][] scontext = this.evaluateSymbolShapeContext(s, parameters);
		
		sDescriptor.setLenght(scontext.length);
		sDescriptor.setValues(gson.toJson(scontext));
		return sDescriptor;
	}
	
	
	public List<ShapeDescriptor> generateAndSaveShapeDescriptors(List<Symbol> symbols){
	    UserParameter parameters = this.userServiceProvider.getUserParameters();
	    List<ShapeDescriptor> res = new ArrayList<ShapeDescriptor>();
	    
	    for (Symbol symbol : symbols) {
			symbol.getStrokes();
			if(null == symbol.getStrokes() || symbol.getStrokes().isEmpty()){
				logger.info("symbol:" +symbol.getId());
				continue;
			}
	    	res.add(this.generateShapeDescriptor(symbol, parameters));
		}
	    
	    this.shapeDescriptorRepository.save(res);
	    
	    logger.info(MessageFormat.format("Saved {0} shape descriptors", res.size()));
	    return res;
	}
	
	public List<ShapeDescriptor> generateAndSaveShapeDescriptorsFromExpressions(List<Expression> expressions){
	    List<Symbol> symbols = new ArrayList<>();
	    List<ShapeDescriptor> res = null;
	    
	    for (Expression expression : expressions) {
	    	symbols.addAll(expression.getSymbols());
		}
	    
	    res = this.generateAndSaveShapeDescriptors(symbols);
	    return res;
	}
	
}
