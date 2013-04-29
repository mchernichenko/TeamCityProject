package ru.billing.currencyrates;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import android.os.AsyncTask;
import android.util.Log;

class WebServiceCaller extends AsyncTask<String, Void, Document> {

    private Exception exception;

    protected Document doInBackground(String... urls) {    	
    	//XmlPullParser scores;   	
        try {
        	URL url = new URL(urls[0]);   
        	HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        	int responseCode = httpConnection.getResponseCode();
        	
        	if (responseCode == HttpURLConnection.HTTP_OK) {
        		InputStream in = httpConnection.getInputStream();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document dom = db.parse(in);
				
				return dom;        		
        	}        	
        } catch (Exception e) {
            return null;
        }
		return null;
    }

    protected void onPostExecute(Document document) {
    	super.onPostExecute(document);
    }
}
