package com.iteye.weimingtom.hct;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class HttpClientTemplate {
	private final static boolean USE_POST = false;
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static void main(String[] args) throws ClientProtocolException,
			IOException {
		
		final String CALL_URL = "http://www.baidu.com/baidu";
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("wd", "template");
		map.put("tn", "cnopera");
		map.put("ie", "utf-8");
//		String text = readWordToFile("content.txt", "UTF-8");
//		map.put("content", text);
		
		HttpClient httpclient = new DefaultHttpClient();
		if (USE_POST) {
			HttpPost httpPost = new HttpPost(
				CALL_URL
			);
	        List<NameValuePair> list = new ArrayList<NameValuePair>();  
	        Iterator<Entry<String, String>> iterator = map.entrySet().iterator();  
	        while(iterator.hasNext()){  
	            Entry<String,String> elem = (Entry<String, String>) iterator.next();  
	            list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
	        }  
	        if(list.size() > 0){
	            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "utf-8");  
	            httpPost.setEntity(entity);  
	        }  
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instreams = entity.getContent();
				String str = convertStreamToString(instreams, "UTF-8");
				System.out.println("Post Do something");
				System.out.println(str);
				// Do not need the rest
				httpPost.abort();
			}
		} else {
			String url = "";
			List<NameValuePair> list = new ArrayList<NameValuePair>();  
	        Iterator<Entry<String, String>> iterator = map.entrySet().iterator();  
	        while(iterator.hasNext()){  
	            Entry<String,String> elem = (Entry<String, String>) iterator.next();  
	            list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
	        } 
	        url = URLEncodedUtils.format(list, HTTP.UTF_8);
			HttpGet httpGet = new HttpGet(
					CALL_URL + "?" + url
			);
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instreams = entity.getContent();
				String str = convertStreamToString(instreams, "UTF-8");
				System.out.println("Get: Do something");
				System.out.println(str);
				// Do not need the rest
				httpGet.abort();
			}
		}
	}

	public static String convertStreamToString(InputStream is, String charset) throws UnsupportedEncodingException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	
    private static final String readWordToFile(String filename, String charset) throws IOException {
    	FileInputStream fin = new FileInputStream(filename);
    	InputStreamReader isr = new InputStreamReader(fin, charset);
    	BufferedReader reader = new BufferedReader(isr);
    	StringBuffer sb = new StringBuffer();
    	char[] cbuf = new char[1];
    	while (true) {
    		int num = reader.read(cbuf);
    		if (num == 1) {
    			sb.append(cbuf[0]);
    		} else {
    			break;
    		}
    	}
    	reader.close();
    	isr.close();
    	fin.close();
    	return sb.toString();
    }
}

