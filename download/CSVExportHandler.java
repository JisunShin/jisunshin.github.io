package com.k4m.rtas.util;

import static com.opencsv.CSVWriter.DEFAULT_QUOTE_CHARACTER;
import static com.opencsv.CSVWriter.DEFAULT_SEPARATOR;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.ResultContext;

import com.opencsv.CSVWriter;

public class CSVExportHandler  implements org.apache.ibatis.session.ResultHandler {
    	private static final char bom = '\ufeff'; 
	private String[] header = null;
	private String[] nextLine = null;
	
	private CSVWriter writer;
	
	public CSVExportHandler(HttpServletResponse response, String[] header) throws Exception {
	    Writer outWriter= response.getWriter();
	    response.setContentType("text/csv; charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    outWriter.write(bom);
	    
		this.writer = new CSVWriter(outWriter, DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER);
		this.writer.writeNext(header);
		this.header = header;
		this.nextLine = new String[this.header.length];
	}
	
	@Override
	public void handleResult(ResultContext context) {

		@SuppressWarnings("unchecked")
		Map<String,Object> row = (Map<String,Object>)context.getResultObject();
		
		Object value = null;
		for(int i=0;i<nextLine.length;i++) {
			value = row.get(header[i]);
			if(value == null) {
				nextLine[i] = null;
			} else if(value instanceof String) {
				nextLine[i] = (String) value;
			} else {
				nextLine[i] = String.valueOf(value);
			}
		}
		this.writer.writeNext(nextLine);
	}
	
	public void close() {
		if(this.writer != null) {
			try {
				this.writer.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
