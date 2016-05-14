package com.multicrawler.scraper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.multicrawler.settings.Rules;

public class Extractor {
	
	private HashMap<String, Rules> rules;
	
	public Extractor(HashMap<String, Rules> rules) {
		this.rules = rules;
	}

	public HashMap<String, Object> extract(Document doc){
		HashMap<String, Object> data = new HashMap<>();
		Iterator<String> it = rules.keySet().iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			Rules rule = rules.get(key);
			Elements els = doc.select(rule.getSelector());
			data.put(key, extractData(rule,els));
		}
		return data;
	}

	private Object extractData(Rules rule, Elements els) {
		if(!els.isEmpty()){

			switch(rule.getDataType()) {
				case Text:
					return extractNode(rule, els.first());
				case Array:
					return extractNodes(rule, els);
				}
		}
		return null;
	}
	
	private String extractNode(Rules rule, Element e) {
		
		switch(rule.getDataLocation()) {
			case insideNode:
				return e.text();
			case insideAttribute:
				return e.attr(rule.getAttrSelector());
		}
		return null;
	}
	
	private ArrayList<String> extractNodes(Rules rule, Elements els) {
		ArrayList<String> data = new ArrayList<>();
		for(Element e:els){
			data.add(extractNode(rule, e));
		}
		return data;
	}
}