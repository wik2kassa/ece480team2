package com.iDocent;

import org.json.*;
import org.apache.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/*
 * This class is going to be the interface to use 
 * in order to query the SQL database backend. It
 * will be making HTTP Queries to an apache server
 * running php which in turn will be returning its
 * results in a JSON format for the phone to parse
 */
public class DBManager {

	private HttpClient client;	
	private final String server = "http://480team2.dyndns-server.com";
	
	public DBManager(){
		client = new DefaultHttpClient();
	}
	
	public void Query(String MAC){
		HttpPost post = new HttpPost(server);
		//post.setEntity(new UrlEncodedFormEntity());
	}

}
