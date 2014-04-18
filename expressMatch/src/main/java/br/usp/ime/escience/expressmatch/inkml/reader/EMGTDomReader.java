package br.usp.ime.escience.expressmatch.inkml.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import br.usp.ime.escience.expressmatch.inkml.reader.dom.EMGTDomReaderUtil;
import br.usp.ime.escience.expressmatch.inkml.reader.generic.ExpressMatchGrountTruthReader;
import br.usp.ime.escience.expressmatch.model.Expression;


/**
 * The Class EMGTXStreamReader.
 */
@Service
public class EMGTDomReader implements ExpressMatchGrountTruthReader<File> {


	private static final Logger logger = LoggerFactory.getLogger(EMGTDomReader.class);
	
	/** The path. */
	private final String PATH = "/home/davi/git/expressMatch/expressMatch/database/expressions/inkml";
	
	/** The all. */
	private final String ALL = "ALL";
	
	/** The files prefix. */
	private final String FILES_PREFIX[] = {this.ALL};
	
	/** The suffix extension. */
	private final String SUFFIX_EXTENSION = ".inkml";
	
	/** The error. */
	private int error = 0;
	
	/** The read. */
	private int read = 0;
	/* (non-Javadoc)
	 * @see br.usp.ml.math.reader.generic.ExpressMatchGrountTruthReader#read(java.lang.Object)
	 */
	@Override
	public Expression read(File in) {
		Expression ret = null;
		try {
			//String fileContents = this.getFileContents(in);
			ret = EMGTDomReaderUtil.read(in);
			read++;
		} catch (FileNotFoundException e) {
			logger.info("Arquivo não encontrado.");
			e.printStackTrace();
			error++;
		} catch (IOException e) {
			logger.info("Ocorreram problemas ao ler o arquivo. (IO)");
			e.printStackTrace();
			error++;
		} catch (ParserConfigurationException e) {
			logger.info("Ocorreram problemas ao ler o arquivo. (PARSE)");
			e.printStackTrace();
			error++;
		} catch (SAXException e) {
			logger.info("Ocorreram problemas ao ler o arquivo. (SAX)");
			e.printStackTrace();
			error++;
		}
		logger.info("Files (error): "+error);
		logger.info("Files loaded:  "+read);
		return ret;
	}
	
	

	/**
	 * Gets the file contents.
	 *
	 * @param file the file
	 * @return the file contents
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unused")
	private String getFileContents(File file) throws FileNotFoundException, IOException{
		StringBuilder ret = new StringBuilder();
		FileInputStream input = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		if(reader != null){
			while(reader.ready()){
				ret.append(reader.readLine()).append("\n");
			}
		}
		return ret.toString();
	}
	
	
	/**
	 * Gets the data set.
	 *
	 * @return the data set
	 */
	public List<Expression> getDataSet(){
		List<Expression> ret = null;
		if(this.FILES_PREFIX.length == 1 && this.ALL.equalsIgnoreCase(this.FILES_PREFIX[0])){
			ret = this.getAllDataSet();
		}else{
			ret = this.getDataSetByPrefix();
		}
		return ret;
	}
	
//	public static void main(String[] args) {
//		EMGTDomReader reader = new EMGTDomReader();
//		List<Expression> a = reader.getAllDataSet();
//		System.out.println();
//	}
	
	/* (non-Javadoc)
	 * @see br.usp.ml.math.reader.generic.ExpressMatchGrountTruthReader#groupDataSet(java.util.List)
	 */
	public Map<Integer, Set<Expression>> groupDataSet(Set<Expression> in){
		Map<Integer, Set<Expression>> grouping = null;
		if(in.size() > 0){
			grouping = new HashMap<Integer, Set<Expression>>();
			for (Expression traceSet : in) {
				if(traceSet != null && 
				   traceSet.getExpressionType() != null && 
				   traceSet.getExpressionType().getId() != null){
					
					if(grouping.containsKey(Integer.valueOf(traceSet.getExpressionType().getId()))){
						grouping.get(Integer.valueOf(traceSet.getExpressionType().getId())).add(traceSet);
					}else{
						Set<Expression> l = new HashSet<Expression>();
						l.add(traceSet);
						grouping.put(Integer.valueOf(traceSet.getExpressionType().getId()), l);
					}
				}
			}
		}
		return grouping;
	}
	
	
	
	/**
	 * Gets the all data set.
	 *
	 * @return the all data set
	 */
	private List<Expression> getAllDataSet(){
		List<File> files = getAllFiles();
		return this.getTraceSetsFromFiles(files);
		
	}
	
	/**
	 * Gets the data set by prefix.
	 *
	 * @return the data set by prefix
	 */
	private List<Expression> getDataSetByPrefix(){
		List<File> files = getFilteredFiles();
		return this.getTraceSetsFromFiles(files);
		
	}
	
	/**
	 * Gets the trace sets from files.
	 *
	 * @param files the files
	 * @return the trace sets from files
	 */
	private List<Expression> getTraceSetsFromFiles(List<File> files){
		List<Expression> ret = new ArrayList<Expression>();
		for (File file : files) {
			logger.info(file.getName());
			ret.add(this.read(file));
		}
		return ret;
	}
	
	
	/**
	 * Gets the filtered files.
	 *
	 * @return the filtered files
	 */
	private List<File> getFilteredFiles(){
		List<File> ret = new ArrayList<File>();
		FilenameFilter filter = this.getFileNameFilter();
		File directory = new File(this.PATH);
		String[] fileNames = directory.list(filter);
		Arrays.sort(fileNames);
		File file = null;
		for (int i = 0; i < fileNames.length; i++) {
			file = new File(this.PATH + "/" + fileNames[i]);
			ret.add(file);
		}
		return ret;
	}
	
	/**
	 * Gets the all files.
	 *
	 * @return the all files
	 */
	private List<File> getAllFiles(){
		List<File> ret = new ArrayList<File>();
		FilenameFilter filter = this.getFileNameFilterForAll();
		File directory = new File(this.PATH);
		String[] fileNames = directory.list(filter);
		Arrays.sort(fileNames);
		File file = null;
		for (int i = 0; i < fileNames.length; i++) {
			file = new File(this.PATH + "/" + fileNames[i]);
			ret.add(file);
		}
		return ret;
	}
	
	/**
	 * Gets the file name filter.
	 *
	 * @return the file name filter
	 */
	private FilenameFilter getFileNameFilter(){
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
				boolean ret = false;
				for (int i = 0; i < FILES_PREFIX.length && !ret; i++) {
					if(fileName.endsWith(SUFFIX_EXTENSION)){
						if(fileName.contains(FILES_PREFIX[i])){
							ret = true;
						}
					}
				}
				return ret;
			}
		};
		return filter;
	}
	
	/**
	 * Gets the file name filter.
	 *
	 * @return the file name filter
	 */
	private FilenameFilter getFileNameFilterForAll(){
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
				boolean ret = false;
				for (int i = 0; i < FILES_PREFIX.length && !ret; i++) {
					if(fileName.endsWith(SUFFIX_EXTENSION)){
						ret = true;
					}
				}
				return ret;
			}
		};
		return filter;
	}
	
}
