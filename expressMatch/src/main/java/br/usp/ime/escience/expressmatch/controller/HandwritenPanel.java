package br.usp.ime.escience.expressmatch.controller;
//package br.usp.ime.escience.controller;
//
//import br.usp.escience.view.domain.Stroke;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import java.io.PrintStream;
//import java.util.Date;
//import java.util.logging.Logger;
//import javax.faces.context.ExternalContext;
//import javax.faces.context.FacesContext;
//
//@Model
//public class HandwritenPanel {
//
//	public String saveStrokes() {
//		try {
//			parseJsonString(jsonString);
//			setJsonLoadString(jsonString);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("entrou");
//		return "";
//	}
//
//	public HandwritenPanel() {
//	}
//
//	@PostConstruct
//	public void init() {
//		FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//	}
//
//	public FacesContext getFacesContext() {
//		return facesContext;
//	}
//
//	public void setFacesContext(FacesContext facesContext) {
//		this.facesContext = facesContext;
//	}
//
//	public Logger getLogger() {
//		return logger;
//	}
//
//	public void setLogger(Logger logger) {
//		this.logger = logger;
//	}
//
//	public Stroke[] getStrokes() {
//		return strokes;
//	}
//
//	public void setStrokes(Stroke strokes[]) {
//		this.strokes = strokes;
//	}
//
//	public String getJsonString() {
//		return jsonString;
//	}
//
//	public void setJsonString(String jsonString) {
//		this.jsonString = jsonString;
//	}
//
//	private void parseJsonString(String jsonString)
//    {
//        GsonBuilder builder = new GsonBuilder();
//        builder.registerTypeAdapter(java/util/Date, new  Object()     /* anonymous class not found */
//    class _anm1 {}
//
//);
//        Gson gson = builder.create();
//        setStrokes((Stroke[])gson.fromJson(jsonString, [Lbr/usp/escience/view/domain/Stroke;));
//    }
//
//	public String getJsonLoadString() {
//		return jsonLoadString;
//	}
//
//	public void setJsonLoadString(String jsonLoadString) {
//		this.jsonLoadString = jsonLoadString;
//	}
//
//	@Inject
//	private FacesContext facesContext;
//	@Inject
//	private transient Logger logger;
//	private String jsonString;
//	private String jsonLoadString;
//	private Stroke strokes[];
//}
