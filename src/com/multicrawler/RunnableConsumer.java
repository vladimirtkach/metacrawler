package com.multicrawler;
import java.util.concurrent.BlockingQueue;

import com.multicrawler.db.DBHelper;
import com.multicrawler.scraper.Extractor;
import com.multicrawler.settings.Settings;
import com.multicrawler.scraper.*;

public class RunnableConsumer implements Runnable{
	    
	    private InputProducer producer;
	    private BlockingQueue<String> queue;
	     
	    
	    RunnableConsumer(InputProducer producer) throws InterruptedException{
	    	this.producer = producer;
	       	queue = producer.getQueue();
	    }

	    @Override
	    public void run() {
	        while (true) {
	        	if(producer.isFinished()) {
	        		if(queue.isEmpty()) break;
	        	}
	        	try {
					fetch(queue.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	    }
	    private void fetch(String url){
	    	Settings sett = Settings.getInstance();
	    	SiteScraper scraper = new SiteScraper(url);
	    	scraper.fetch();
	    	
	    	if(sett.hasExtractor()){
	    		Extractor extractor = new Extractor(sett.rulesMap());
		    	scraper.extract(extractor);
	    	}
	    	
	    	System.out.println(scraper.getData());	    	
	    	DBHelper.storeLinks(scraper.getData());
	    	 
	    }
}
