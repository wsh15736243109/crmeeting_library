package com.cr.pn.Utils.xmlhandle;

import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XmlManager {
	
	public static final SAXParserFactory factory = SAXParserFactory.newInstance();
	
	/**
	 * xml字符串转XmlCommand.
	 * @param str
	 * @param handler
	 * @return
	 */
	public static XmlCommand xmlStringToCommand(String str,YiLuoteXmlHandler handler){
		try {
			SAXParser parser = factory.newSAXParser();
			InputStream inputStream = new ByteArrayInputStream(str.getBytes());
			parser.parse(inputStream,handler);
			return handler.getXmlCommand();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * xml文件地址转Command.
	 * @param str
	 * @param handler
	 * @return
	 */
	public static XmlCommand xmlFileToCommand(String str,YiLuoteXmlHandler handler){
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(str,handler);
			return handler.getXmlCommand();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * xml文件转Command.
	 * @param str
	 * @param handler
	 * @return
	 */
	public static XmlCommand xmlFileToCommand(File file,YiLuoteXmlHandler handler){
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(file,handler);
			return handler.getXmlCommand();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
