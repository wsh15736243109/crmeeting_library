package com.cr.pn.Utils.xmlhandle;


import androidx.collection.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XmlCommand {
	
	private String pNameStart="";
	
	private String qNameEnd="";
	
	private Map<String,String> qNames = new ArrayMap<String, String>();
	
	private List<XmlCommand> xmlCommands = new ArrayList<>();
	
	private XmlCommand parentXml;
	
	private String content;
	
	public XmlCommand getParentXml() {
		return parentXml;
	}

	public void setParentXml(XmlCommand parentXml) {
		this.parentXml = parentXml;
		if(parentXml!=null){
			this.parentXml.addChild(this);
		}
		
	}
	
	public void addChild(XmlCommand value){
		xmlCommands.add(value);
	}

	public void setQVName(String key,String value){
		qNames.put(key, value);
	}
	
	public String getpNameStart() {
		return pNameStart;
	}

	public void setpNameStart(String pNameStart) {
		this.pNameStart = pNameStart;
	}

	public String getqNameEnd() {
		return qNameEnd;
	}

	public void setqNameEnd(String qNameEnd) {
		this.qNameEnd = qNameEnd;
	}

	public Map<String, String> getqNames() {
		return qNames;
	}

	public void setqNames(Map<String, String> qNames) {
		this.qNames = qNames;
	}

	public List<XmlCommand> getXmlCommands() {
		return xmlCommands;
	}

	public void setXmlCommands(List<XmlCommand> xmlCommands) {
		this.xmlCommands = xmlCommands;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
