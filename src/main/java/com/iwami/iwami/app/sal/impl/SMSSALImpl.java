package com.iwami.iwami.app.sal.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.iwami.iwami.app.util.IWamiUtils;

public class SMSSALImpl implements SMSSAL {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private String url = "http://yunpian.com/v1/sms/tpl_send.json";
	
	private String apiKey = "da8ab8cb7815f4e379cd5a5a9956e7f1";
	
	private String encoding = "UTF-8";
	
	private boolean send = false;
	
	public static void main(String[] args) {
		SMSSALImpl sms = new SMSSALImpl();
		sms.send = true;
//		sms.sendVerifyCodeSMS("18691841680", "7773");
//		System.out.println(sms.sendSMS("18611007601", "能不能提供正确的JAVA接入代码啊！【爱挖米】"));
		//314625
		//System.out.println(sms.addTemplate("米粒任务#appname#已经结束（#reason#），期限#startdate#-#enddate#，设定数量#total#，实际完成#count#【爱挖米】"));
		//sms.sendTaskSMS("18611007601", "task", "expire", new Date(), new Date(), 1000, 1002);
		//314626
		//System.out.println(sms.addTemplate("Push任务[#taskname#]已经完成（#startdatetime#~#enddatetime#），一共推送#count#人【爱挖米】"));
		//sms.sendJPushSMS("18611007601", 2l, new Date(), new Date(), 100);
		//314285
		//System.out.println(sms.addTemplate("温馨提示：您的朋友#name#（手机号：#phone#）向您赠送了#count#爱挖米米粒，可用来兑换小米手机/Q币等奖品。戳www.iwami.cn现在立即免费安装爱挖米，现在开始就用手机赚钱【爱挖米】"));
//		sms.sendInvitationSMS("18611007601", "test", 18611007601l, 2000);
	}
	
	public boolean sendJPushSMS(String cellPhone, long id, Date addTime, Date lastModTime, int count){
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
			nameValuePairs.add(new BasicNameValuePair("tpl_id", "314626"));
			nameValuePairs.add(new BasicNameValuePair("tpl_value", "#taskname#=" + id + "&#startdatetime#=" + IWamiUtils.getDateString(addTime) + "&#enddatetime#=" + IWamiUtils.getDateString(lastModTime) + "&#count#=" + count));
			nameValuePairs.add(new BasicNameValuePair("mobile", cellPhone));
			
			HttpPost method = new HttpPost(url);
			method.setEntity(new UrlEncodedFormEntity(nameValuePairs, encoding));
			
			HttpResponse response = client.execute(method);
			
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				bis = response.getEntity().getContent();
				Header[] gzip = response.getHeaders("Content-Encoding");

				bos = new ByteArrayOutputStream();
				int _count;
				while ((_count = bis.read(buf)) != -1) {
					bos.write(buf, 0, _count);
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

				logger.info(content);
				result = true;
			} else {
				bis = response.getEntity().getContent();
				Header[] gzip = response.getHeaders("Content-Encoding");

				bos = new ByteArrayOutputStream();
				int _count;
				while ((_count = bis.read(buf)) != -1) {
					bos.write(buf, 0, _count);
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

				logger.info(content);
				logger.info("error code is " + response.getStatusLine().getStatusCode());
			}
		} catch(Throwable t){
			t.printStackTrace();
		}
		return result;
	}
	
	public boolean sendTaskSMS(String cellPhone, String name, String reason, Date startdate, Date enddate, int total, int count){
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
			nameValuePairs.add(new BasicNameValuePair("tpl_id", "314625"));
			nameValuePairs.add(new BasicNameValuePair("tpl_value", "#appname#=" + name + "#reason#=" + reason + "&#startdate#=" + IWamiUtils.getDateString(startdate) + "&#enddate#=" + IWamiUtils.getDateString(enddate) + "&#total#=" + total + "&#count#=" + count));
			nameValuePairs.add(new BasicNameValuePair("mobile", cellPhone));
			
			HttpPost method = new HttpPost(url);
			method.setEntity(new UrlEncodedFormEntity(nameValuePairs, encoding));
			
			HttpResponse response = client.execute(method);
			
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				bis = response.getEntity().getContent();
				Header[] gzip = response.getHeaders("Content-Encoding");

				bos = new ByteArrayOutputStream();
				int _count;
				while ((_count = bis.read(buf)) != -1) {
					bos.write(buf, 0, _count);
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

				logger.info(content);
				result = true;
			} else {
				bis = response.getEntity().getContent();
				Header[] gzip = response.getHeaders("Content-Encoding");

				bos = new ByteArrayOutputStream();
				int _count;
				while ((_count = bis.read(buf)) != -1) {
					bos.write(buf, 0, _count);
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

				logger.info(content);
				logger.info("error code is " + response.getStatusLine().getStatusCode());
			}
		} catch(Throwable t){
			t.printStackTrace();
		}
		return result;
	}
	
	protected boolean addTemplate(String template){
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
			nameValuePairs.add(new BasicNameValuePair("tpl_content", template));
			
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

				logger.info(content);
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

				logger.info(content);
				logger.info("error code is " + response.getStatusLine().getStatusCode());
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
