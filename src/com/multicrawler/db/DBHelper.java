package com.multicrawler.db;
import java.util.HashMap;
import java.util.Iterator;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.multicrawler.settings.Settings;

public class DBHelper {
	static final Settings sett = Settings.getInstance();
	static final MongoClient mongoClient = new MongoClient(sett.getDBHost(), sett.getDBPort());
	static final MongoDatabase database = mongoClient.getDatabase(sett.getDBName());
	static final MongoCollection<Document> collection = database.getCollection(sett.getDBCollection());
	
	public static void storeLinks(HashMap<String, Object> list){
		  Document doc = new Document();
	      Iterator<String> it = list.keySet().iterator();
		  while(it.hasNext()){
	 		   String key = it.next();
	 		   doc.append(key, list.get(key));
		 }
		 collection.insertOne(doc);
 	}

	public static void close(){
		mongoClient.close();
	}
}



