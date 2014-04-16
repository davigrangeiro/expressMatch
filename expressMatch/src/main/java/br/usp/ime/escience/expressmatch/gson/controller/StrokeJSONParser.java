package br.usp.ime.escience.expressmatch.gson.controller;

import java.lang.reflect.Type;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import br.usp.ime.escience.expressmatch.gson.controller.generic.JSONParseable;
import br.usp.ime.escience.expressmatch.model.Stroke;

/**
 * The Class StrokeJSONParser.
 */
@Service
public class StrokeJSONParser extends JSONParseable<Stroke> {

	/**
	 * Instantiates a new stroke json parser.
	 */
	public StrokeJSONParser() {
		super();
	}

	/* (non-Javadoc)
	 * @see br.usp.ime.escience.gson.controller.generic.JSONParseable#getSpecificGsonBuilder()
	 */
	@Override
	public GsonBuilder getSpecificGsonBuilder() {
		GsonBuilder builder = new GsonBuilder();

		// Register an adapter to manage the date types as long values
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

			@Override
			public Date deserialize(JsonElement json, Type typeOfT,
					JsonDeserializationContext context)
					throws JsonParseException {
				return new Date(json.getAsJsonPrimitive().getAsLong());
			}
		});
		
		return builder;
	}

	/* (non-Javadoc)
	 * @see br.usp.ime.escience.gson.controller.generic.JSONParseable#getTypeClass()
	 */
	@Override
	public Class<Stroke> getTypeClass() {
		return Stroke.class;
	}

	/* (non-Javadoc)
	 * @see br.usp.ime.escience.gson.controller.generic.JSONParseable#getArrayTypeClass()
	 */
	@Override
	public Class<Stroke[]> getArrayTypeClass() {
		return Stroke[].class;
	}

}
