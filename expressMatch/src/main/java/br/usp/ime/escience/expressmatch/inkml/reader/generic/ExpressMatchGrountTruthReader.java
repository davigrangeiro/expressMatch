package br.usp.ime.escience.expressmatch.inkml.reader.generic;

import java.util.List;
import java.util.Map;
import java.util.Set;

import br.usp.ime.escience.expressmatch.model.Expression;

/**
 * The Interface ExpressMatchGrountTruthReader.
 *
 * @param <Type> the generic type
 */
public interface ExpressMatchGrountTruthReader<Type> {

	/**
	 * Delegate the read method for express math.
	 * Read.
	 *
	 * @param in the in
	 * @return the trace set
	 */
	public Expression read(Type in);
	
	
	/**
	 * Gets the data set.
	 *
	 * @return the data set
	 */
	public List<Expression> getDataSet(final String[] filesPrefix);
	
	/**
	 * Group data set.
	 *
	 * @param in the in
	 * @return the map
	 */
	public Map<Integer, Set<Expression>> groupDataSet(Set<Expression> in);
}
