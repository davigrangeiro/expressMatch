package br.usp.ime.escience.expressmatch.inkml.reader.dom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.usp.ime.escience.expressmatch.model.Expression;
import br.usp.ime.escience.expressmatch.model.ExpressionType;
import br.usp.ime.escience.expressmatch.model.Point;
import br.usp.ime.escience.expressmatch.model.Stroke;
import br.usp.ime.escience.expressmatch.model.Symbol;
import br.usp.ime.escience.expressmatch.model.UserInfo;


// TODO: Auto-generated Javadoc
/**
 * The Class EMDTDomReaderUtil.
 */
public class EMGTDomReaderUtil {


	/** The white space. */
	private static final String WHITE_SPACE = " ";
	
	/** The comma. */
	private static final String COMMA = ",";
	
	/** The Constant ANNOTATION. */
	private static final String ANNOTATION = "annotation";
	
	/** The Constant TRACE_GROUP. */
	private static final String TRACE_GROUP = "traceGroup";
	
	/** The Constant TRACE. */
	private static final String TRACE = "trace";
	
	/** The Constant EXPRESSIION. */
	private static final String EXPRESSIION = "expression";
	
	/** The Constant WRITER. */
	private static final String WRITER = "writer";
	
	/** The Constant CATEGORY. */
	private static final String CATEGORY = "category";
	
	/** The Constant TRUTH. */
	private static final String TRUTH = "truth";
	
	/** The Constant ID. */
	private static final String ID = "id";
	

	/** The Constant ID_XML. */
	private static final String ID_XML = "xml:id";
	
	/** The Constant TYPE. */
	private static final String TYPE = "type";
	
	/** The Constant TRACE_VIEW. */
	private static final String TRACE_VIEW = "traceView";
	
	/** The Constant TRACE_DATA_REF. */
	private static final String TRACE_DATA_REF = "traceDataRef";
	
	/** The Constant ANNOTATION_XML. */
	private static final String ANNOTATION_XML = "annotationXML";
	
	/** The Constant HREF. */
	private static final String HREF = "HREF";
	
	/**
	 * Read.
	 *
	 * @param in the in
	 * @return the trace set
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the sAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Expression read(File in) throws ParserConfigurationException, SAXException, IOException{
		Expression ret = new Expression();
		ExpressionType eType = new ExpressionType();
		UserInfo user = new UserInfo();
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(in);
		doc.getDocumentElement().normalize();
		Element parent = doc.getDocumentElement();
		
		NodeList annotations = parent.getElementsByTagName(EMGTDomReaderUtil.ANNOTATION);
		if(annotations != null && annotations.getLength() != 0){
			for (int i = 0; i < annotations.getLength(); i++) {
				Element e = (Element) annotations.item(i);
				String type = e.getAttribute(EMGTDomReaderUtil.TYPE);
				if(EMGTDomReaderUtil.EXPRESSIION.equalsIgnoreCase(type) &&
						  e.getFirstChild() != null && e.getFirstChild().getNodeValue() != null){
					eType.setId(Integer.valueOf(e.getFirstChild().getNodeValue()));
				}else if(EMGTDomReaderUtil.WRITER.equalsIgnoreCase(type) &&
						  e.getFirstChild() != null && e.getFirstChild().getNodeValue() != null){
					user.setName(e.getFirstChild().getNodeValue());
				}else if(EMGTDomReaderUtil.CATEGORY.equalsIgnoreCase(type) &&
						  e.getFirstChild() != null && e.getFirstChild().getNodeValue() != null){
					eType.setName(e.getFirstChild().getNodeValue());
				}else if(EMGTDomReaderUtil.TRUTH.equalsIgnoreCase(type) && parent == e.getParentNode() &&
						  e.getFirstChild() != null && e.getFirstChild().getNodeValue() != null){
					ret.setLabel(e.getFirstChild().getNodeValue());
				} 
			}
		}
		ret.setUserInfo(user);
		ret.setExpressionType(eType);
		
		List<Stroke> strokes = new ArrayList<>();
		NodeList traces = parent.getElementsByTagName(EMGTDomReaderUtil.TRACE);
		if(traces != null && traces.getLength() != 0){
			Stroke t = null;
			for (int i = 0; i < traces.getLength(); i++) {
				t = new Stroke();
				Element e = (Element) traces.item(i);
				t.setStrokeId((Integer.valueOf(e.getAttribute(EMGTDomReaderUtil.ID))));
				t.setPoints(new HashSet<Point>());
				String points = e.getFirstChild().getNodeValue();

				String[] instances = points.split(COMMA),
						 tPoints = null; 
				for (int j = 0; j < instances.length; j++) {
					tPoints = instances[j].trim().split(WHITE_SPACE);
					Point p = new Point();
					p.setX(Float.valueOf(tPoints[0]));
					p.setY(Float.valueOf(tPoints[1]));
					p.setTime(new Date(Long.valueOf(tPoints[2])));
					t.getPoints().add(p);
				}
				
				strokes.add(t);
			}
		}
		
		NodeList traceGroup = parent.getElementsByTagName(EMGTDomReaderUtil.TRACE_GROUP);
		if(traceGroup != null && traceGroup.getLength() != 0){
			Symbol symbol = null;
			
			for (int i = 1; i < traceGroup.getLength(); i++) {
				symbol = new Symbol();
				symbol.setStrokes(new HashSet<Stroke>());

				Element e = (Element) traceGroup.item(i);
				
				symbol.setId(Integer.valueOf(e.getAttribute(EMGTDomReaderUtil.ID_XML)));
				symbol.setLabel(getTextValue(e, EMGTDomReaderUtil.ANNOTATION));
				symbol.setHref(getAtributeValueForNode(e, EMGTDomReaderUtil.ANNOTATION_XML, EMGTDomReaderUtil.HREF));
				
				List<Integer> strokeIds = getAtributeValueForNodes(e, EMGTDomReaderUtil.TRACE_VIEW, EMGTDomReaderUtil.TRACE_DATA_REF);
				for (Integer id : strokeIds) {
					for (Stroke stroke : strokes) {
						if(id == stroke.getStrokeId()){
							symbol.getStrokes().add(stroke);
							break;
						}
					}
				}
				
				ret.getSymbols().add(symbol);
			}
		}
		return ret;
	}
	
	
	private static List<Integer> getAtributeValueForNodes(Element ele, String tagName, String attribute){
		List<Integer> ret = new ArrayList<Integer>();
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element)nl.item(i);
				String textVal = el.getAttribute(attribute);
				ret.add(Integer.valueOf(textVal));
			}
		}
		return ret;
	}
	
	
	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is 'name' I will return John
	 */
	private static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}
	
	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is 'name' I will return John
	 */
	private static String getAtributeValueForNode(Element ele, String tagName, String attribute) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getAttribute(attribute);
		}

		return textVal;
	}
	
}
