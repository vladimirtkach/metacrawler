package com.multicrawler;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.multicrawler.settings.Settings;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException{

    	int threadsCount = Settings.getInstance().threadsCount();
    	   	
        InputProducer inputProducer = new InputProducer();
        new Thread(inputProducer).start();
        
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
       
		for(int i=0; i < threadsCount; i++){
        	Runnable consumer = new Thread(new RunnableConsumer(inputProducer));
        	executor.execute(consumer);
        }
    	
       
    }
}



