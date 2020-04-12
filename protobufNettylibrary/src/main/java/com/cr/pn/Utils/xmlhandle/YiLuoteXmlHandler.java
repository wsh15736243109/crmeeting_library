package com.cr.pn.Utils.xmlhandle;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class YiLuoteXmlHandler extends DefaultHandler{
	
	private XmlCommand xmlCommand;
	
	private String nowContent="";

	private boolean isFirst = true;
	
	private XmlCommand parentXml;
	
	private XmlCommand nowXml;
	
	public XmlCommand getXmlCommand() {
		return xmlCommand;
	}

	public YiLuoteXmlHandler(){
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(isFirst){
			isFirst = false;
			xmlCommand = new XmlCommand();
			xmlCommand.setParentXml(null);
			xmlCommand.setpNameStart(qName);
			parentXml = xmlCommand;
			nowXml = xmlCommand;
			int num = attributes.getLength();
            for(int i = 0; i < num; i++){
            	String key = attributes.getQName(i);
            	String value = attributes.getValue(key);
            	xmlCommand.setQVName(key, value);
            }
		}else {
			XmlCommand command = new XmlCommand();
			command.setParentXml(nowXml);
			nowXml = command;
			command.setpNameStart(qName);
			int num = attributes.getLength();
			for(int i = 0; i < num; i++){
				String key = attributes.getQName(i);
				String value = attributes.getValue(key);
				command.setQVName(key, value);
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		nowXml.setqNameEnd(qName);
		nowXml = nowXml.getParentXml();
		if(nowXml!=null){
			parentXml = nowXml.getParentXml();
		}
	}

	@Override
	public void startDocument() throws SAXException {
	}


	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		nowContent = new String(ch,start,length);
		nowXml.setContent(nowContent);
	}
}
