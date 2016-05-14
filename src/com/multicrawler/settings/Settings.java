package com.multicrawler.settings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Settings{
	
    private  HashMap<String,Rules> rulesMap = new HashMap<>();
    private  HashMap<String,Boolean> metaSettings = new HashMap<>();
	private Document settingsDoc;
    private static  Settings instance;

    
	private Settings(){
		File in = new File("settings.xml");
		try {
			Document settingsDoc = Jsoup.parse(in, "UTF-8", "");
			this.settingsDoc = settingsDoc;
			
			parseRules();
			getMetaSettings();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDBHost() {
		Elements e = settingsDoc.select("storage host");
		if(!e.isEmpty()) {
			return e.first().text();
		}
		return null;
	}
	
	public int getDBPort() {
		Elements e = settingsDoc.select("storage port");
		if(!e.isEmpty()) {
			return Integer.parseInt(e.first().text());
		}
		return -1;
	}
	
	public String getDBName() {
		Elements e = settingsDoc.select("storage dbName");
		if(!e.isEmpty()) {
			return e.first().text();
		}
		return null;
	}
	
	public String getDBCollection() {
		Elements e = settingsDoc.select("storage collectionName");
		if(!e.isEmpty()) {
			return e.first().text();
		}
		return null;
	}
	
	public int threadsCount() {
		Elements e = settingsDoc.select("threadsCount");
		if(!e.isEmpty()) {
			return Integer.parseInt(e.first().text());
		}
		return 1;
	}
	
	public boolean hasExtractor(){
		Elements e = settingsDoc.select("rules");
		if(!e.isEmpty()) {
			return Boolean.parseBoolean(e.attr("active"));
		}
		return false;
	}

	private void getMetaSettings() {
		metaSettings.put("html", getMetaNode("metaData rawHtml"));
		metaSettings.put("ip", getMetaNode("metaData hostIp"));
		metaSettings.put("headers", getMetaNode("metaData headers"));
		metaSettings.put("statusCode", getMetaNode("metaData statusCode"));
	}
	
	private boolean getMetaNode(String selector) {
		Elements e = settingsDoc.select(selector);
		if (!e.isEmpty()) return Boolean.parseBoolean(e.first().text());
		return false;
	}

	private void parseRules() {
		for(Element e:settingsDoc.select("settings rules rule")){
			String key = e.attr("key");
			String selector = e.attr("selector");
			DataType dataType = e.attr("dataType").equals("array") ? DataType.Array : DataType.Text;
			Rules rules = null;
			if(e.hasAttr("attrSelector")){
				rules = new Rules(selector,dataType,e.attr("attrSelector"));
			} else {
				rules = new Rules(selector,dataType);
			}
			
			rulesMap.put(key, rules);
		}
	}
	
	public HashMap<String,Rules> rulesMap(){
		return rulesMap;
	}
	
	public HashMap<String,Boolean> metaSettings(){
		return metaSettings;
	}
	
	public static Settings getInstance() {
		if(instance == null){
	        synchronized (Settings.class) {
	            if(instance == null){
	                instance = new Settings();
	            }
	        }
	    }
	    return instance;
	}

	public String input() {
		Elements e = settingsDoc.select("inputFile");
		if(!e.isEmpty()) {
			return e.first().text();
		}
		
		return null;
		
	}
	
}