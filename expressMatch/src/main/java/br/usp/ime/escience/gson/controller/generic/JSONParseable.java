package br.usp.ime.escience.gson.controller.generic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The Class JSONParseable.
 *
 * @param <T> the generic type
 */
public abstract class JSONParseable<T> {


    /** The type parameter class. */
    private final Class<T> TYPE_PARAMETER_CLASS;

    /** The type parameter array class. */
    private final Class<T[]> TYPE_PARAMETER_ARRAY_CLASS;
	
	/** The gson. */
	protected Gson gson;
	
	/**
	 * Instantiates a new jSON parseable.
	 */
	public JSONParseable() {
		super();

		this.TYPE_PARAMETER_CLASS = this.getTypeClass();
		this.TYPE_PARAMETER_ARRAY_CLASS = this.getArrayTypeClass();
		this.gson = this.getGson();
	}
	
	/**
	 * Gets the specific gson builder.
	 *
	 * @return the specific gson builder
	 */
	public abstract GsonBuilder getSpecificGsonBuilder(); 
	
	/**
	 * Gets the type class.
	 *
	 * @return the type class
	 */
	public abstract Class<T> getTypeClass();
	
	/**
	 * Gets the array type class.
	 *
	 * @return the array type class
	 */
	public abstract Class<T[]> getArrayTypeClass();

	/**
	 * Gets the gson.
	 *
	 * @return the gson
	 */
	private Gson getGson(){
		Gson res = null;
		GsonBuilder gsonBuilder = this.getSpecificGsonBuilder();
		if(gsonBuilder != null){
			res = gsonBuilder.create();
		}else{
			res = new Gson();
		}
		return res;
	}
	
	/**
	 * To json.
	 *
	 * @param object the object
	 * @return the string
	 */
	public String toJSON(T object) {
		return this.gson.toJson(object);
	}

	/**
	 * To json.
	 *
	 * @param object the object
	 * @return the string
	 */
	public String toJSON(T[] object) {
		return this.gson.toJson(object);
	}

	/**
	 * From json.
	 *
	 * @param jsonStringRepresentation the json string representation
	 * @return the t
	 */
	public T fromJSON(String jsonStringRepresentation) {
		return this.gson.fromJson(jsonStringRepresentation, this.TYPE_PARAMETER_CLASS);
	}
	
	/**
	 * Array from json.
	 *
	 * @param jsonStringRepresentation the json string representation
	 * @return the t[]
	 */
	public T[] arrayFromJSON(String jsonStringRepresentation) {
		return this.gson.fromJson(jsonStringRepresentation, this.TYPE_PARAMETER_ARRAY_CLASS);
	}
	
}
