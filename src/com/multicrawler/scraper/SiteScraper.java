package com.multicrawler.scraper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLHandshakeException;
import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.multicrawler.settings.Settings;

public class SiteScraper{
	private HashMap<String, Object> extractedData;
	private HashMap<String, Object> fetchedData;
	private HashMap<String, Object> allData;
	private Response response;
	private String ip;
	private final String agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";
	private int timeout = 1000;
	private String url;
	
	public SiteScraper(String url){
		this.url = url;
	}
	
	public HashMap<String, Object> getData() {
		fetchedData = new HashMap<>();
		allData = new HashMap<>();
		fetchedData.put("url", url);
		HashMap<String,Boolean> meta = Settings.getInstance().metaSettings();
		if(meta.get("ip")) {
			fetchIp();
			fetchedData.put("ip", ip);
		}
		if(meta.get("statusCode")) fetchedData.put("statusCode", getStatusCode());
		if(meta.get("headers")) fetchedData.put("headers", headers());
		if(meta.get("html")) fetchedData.put("html", html());
				
		allData.putAll(fetchedData);
		if(extractedData!=null)
		allData.putAll(extractedData);
		return allData;
	}
	
	public void extract(Extractor extractor){
		extractedData = extractor.extract(getDocument());
	}
		
	public void fetch(){
		try {
	         response = Jsoup.connect("http://" + url)
	            .ignoreHttpErrors(true)
	            .userAgent(agent)
	            .timeout(timeout)
	            .execute();
			} catch (SocketTimeoutException e){
				System.out.println(e.getMessage());
			} catch (HttpStatusException e){
				System.out.println(e.getMessage());
			} catch (SSLHandshakeException e) {
				System.out.println(e.getMessage());
			} catch (UnknownHostException e) {
				System.out.println(e.getMessage());
			}  catch (SocketException e) {
				System.out.println(e.getMessage());
		    } catch (IOException e) {
		         System.out.println(e.getMessage());
		    }
	}
		
	public int getStatusCode() {
		  return (response != null) ? response.statusCode() : 0;
	   }
	
	public Document getDocument(){
		Document doc = new Document("");
		try {
			if(response!=null) 
			doc = response.parse();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return doc;
	}
	
	public String getIp(){
		return ip;
	}
	
	public Map<String, String> headers(){
		if(response != null)
			return response.headers();
		return null;
	}
	
	public String html(){
		if(response != null)
			return response.body();
		return null;
	}
	
	private void fetchIp(){
		InetAddress address = null;
		try {
			address = InetAddress.getByName(url);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if(address!=null){
			ip = address.getHostAddress(); 
		}
	}


}