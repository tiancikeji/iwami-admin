package com.iwami.iwami.app.sal.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import com.iwami.iwami.app.sal.SMSSAL;

public class SMSSALImpl implements SMSSAL {
	
	private String url = "http://yunpian.com/v1/sms/send.json";
	
	private String apiKey = "da8ab8cb7815f4e379cd5a5a9956e7f1";
	
	private String encoding = "UTF-8";
	
	private boolean send = false;
	
	public static void main(String[] args) {
		SMSSALImpl sms = new SMSSALImpl();
//		sms.send = true;
//		System.out.println(sms.sendSMS("18611007601", "能不能提供正确的JAVA接入代码啊！【爱挖米】"));
		System.out.println(sms.addTemplate());
	}
	
	public boolean sendSMS(String cellPhone, String msg){
		if(!send)
			return true;
		
		boolean result = false;
		
		ByteArrayOutputStream bos = null;
		InputStream bis = null;
		byte[] buf = new byte[10240];
		String content = null;
		try{
			HttpClient client = new DefaultHttpClient();
			client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("apikey", apiKey));
			nameValuePairs.add(new BasicNameValuePair("text", msg));
			nameValuePairs.add(new BasicNameValuePair("mobile", cellPhone));
			
			HttpPost method = new HttpPost(url);
			method.setEntity(new UrlEncodedFormEntity(nameValuePairs, encoding));
			
			HttpResponse response = client.execute(method);
			
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				bis = response.getEntity().getContent();
				Header[] gzip = response.getHeaders("Content-Encoding");

				bos = new ByteArrayOutputStream();
				int count;
				while ((count = bis.read(buf)) != -1) {
					bos.write(buf, 0, count);
				}
				bis.close();

				if (gzip.length > 0
						&& gzip[0].getValue().equalsIgnoreCase("gzip")) {
					GZIPInputStream gzin = new GZIPInputStream(new ByteArrayInputStream(bos.toByteArray()));
					StringBuffer sb = new StringBuffer();
					int size;
					while ((size = gzin.read(buf)) != -1) {
						sb.append(new String(buf, 0, size, "utf-8"));
					}
					gzin.close();
					bos.close();

					content = sb.toString();
				} else {
					content = bos.toString();
				}

				System.out.println(content);
				result = true;
			} else {
				bis = response.getEntity().getContent();
				Header[] gzip = response.getHeaders("Content-Encoding");

				bos = new ByteArrayOutputStream();
				int count;
				while ((count = bis.read(buf)) != -1) {
					bos.write(buf, 0, count);
				}
				bis.close();

				if (gzip.length > 0
						&& gzip[0].getValue().equalsIgnoreCase("gzip")) {
					GZIPInputStream gzin = new GZIPInputStream(new ByteArrayInputStream(bos.toByteArray()));
					StringBuffer sb = new StringBuffer();
					int size;
					while ((size = gzin.read(buf)) != -1) {
						sb.append(new String(buf, 0, size, "utf-8"));
					}
					gzin.close();
					bos.close();

					content = sb.toString();
				} else {
					content = bos.toString();
				}

				System.out.println(content);
				System.out.println("error code is " + response.getStatusLine().getStatusCode());
			}
		} catch(Throwable t){
			t.printStackTrace();
		}
		return result;
	}
	
	public boolean addTemplate(){
		if(!send)
			return true;
		
		boolean result = false;
		
		ByteArrayOutputStream bos = null;
		InputStream bis = null;
		byte[] buf = new byte[10240];
		String content = null;
		try{
			HttpClient client = new DefaultHttpClient();
			client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("apikey", apiKey));
			nameValuePairs.add(new BasicNameValuePair("tpl_content", "#msg#【爱挖米】"));
			
			HttpPost method = new HttpPost("http://yunpian.com/v1/tpl/add.json");
			method.setEntity(new UrlEncodedFormEntity(nameValuePairs, encoding));
			
			HttpResponse response = client.execute(method);
			
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				bis = response.getEntity().getContent();
				Header[] gzip = response.getHeaders("Content-Encoding");

				bos = new ByteArrayOutputStream();
				int count;
				while ((count = bis.read(buf)) != -1) {
					bos.write(buf, 0, count);
				}
				bis.close();

				if (gzip.length > 0
						&& gzip[0].getValue().equalsIgnoreCase("gzip")) {
					GZIPInputStream gzin = new GZIPInputStream(new ByteArrayInputStream(bos.toByteArray()));
					StringBuffer sb = new StringBuffer();
					int size;
					while ((size = gzin.read(buf)) != -1) {
						sb.append(new String(buf, 0, size, "utf-8"));
					}
					gzin.close();
					bos.close();

					content = sb.toString();
				} else {
					content = bos.toString();
				}

				System.out.println(content);
				result = true;
			} else {
				bis = response.getEntity().getContent();
				Header[] gzip = response.getHeaders("Content-Encoding");

				bos = new ByteArrayOutputStream();
				int count;
				while ((count = bis.read(buf)) != -1) {
					bos.write(buf, 0, count);
				}
				bis.close();

				if (gzip.length > 0
						&& gzip[0].getValue().equalsIgnoreCase("gzip")) {
					GZIPInputStream gzin = new GZIPInputStream(new ByteArrayInputStream(bos.toByteArray()));
					StringBuffer sb = new StringBuffer();
					int size;
					while ((size = gzin.read(buf)) != -1) {
						sb.append(new String(buf, 0, size, "utf-8"));
					}
					gzin.close();
					bos.close();

					content = sb.toString();
				} else {
					content = bos.toString();
				}

				System.out.println(content);
				System.out.println("error code is " + response.getStatusLine().getStatusCode());
			}
		} catch(Throwable t){
			t.printStackTrace();
		}
		return result;
	}

	public boolean isSend() {
		return send;
	}

	public void setSend(boolean send) {
		this.send = send;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
