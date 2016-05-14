package com.multicrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.multicrawler.settings.Settings;

public class InputProducer implements Runnable{

    
    private BlockingQueue<String> queue;
    
    public BlockingQueue<String> getQueue() {
		return queue;
	}
   
    public InputProducer() {
    	
        this.queue = new ArrayBlockingQueue<>(10000);
    }
    
    private boolean finished = false;

    @Override
    public void run() {
    
    	try (BufferedReader br = new BufferedReader(new FileReader(new File(Settings.getInstance().input())))) {
    	    String line;
    	    while ((line = br.readLine()) != null) {
    	    	try {
    				queue.put(line);
    			} catch (InterruptedException e1) {
    				e1.printStackTrace();
    			}
                
    	    }
    	} catch (IOException e) {
			e.printStackTrace();
		}
        finished = true;
        
    }

	public boolean isFinished() {
		return finished;
	}
	


	

}
