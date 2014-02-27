package com.iwami.iwami.app.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HttpContext;

import sun.misc.BASE64Encoder;

public class SMSUtils {

	private static boolean isSend = false;

	private static final String STATUS_OK = "0";

	private static Log logger = LogFactory.getLog(SMSUtils.class);

	// TODO change iwami user&password
	private static String smsbaoUrl = "http://www.smsbao.com/sms?u=*****&p=**********";

	private static String luosimaoUrl = "https://sms-api.luosimao.com/v1/send.json";

	// sms bao
	public static boolean sendSMSBao(String data, String phone) {
		if (!isSend)
			return true;
		boolean result = false;
		long start = System.currentTimeMillis();
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpGet httpget = new HttpGet(smsbaoUrl + "&m=" + phone + "&c=" + data);
			logger.info("request uri: " + httpget.getURI());

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpClient.execute(httpget, responseHandler);
			logger.info("request response: " + response);

			if (StringUtils.equals(response, STATUS_OK)) {
				result = true;
			}
		} catch (Throwable t) {
			logger.error("Exception when sending code <" + data + "> <" + phone
					+ ">", t);
		} finally {
			httpClient.getConnectionManager().shutdown();
			logger.info("send1 sms <" + result + "> <" + data + "> <" + phone
					+ "> used <" + (System.currentTimeMillis() - start) + "ms>");
		}
		return result;
	}
	// end of sms bao

	// luosimao
	public static boolean sendLuosiMao(String data, String phone) {
		if (!isSend)
			return true;
		boolean result = false;
		long start = System.currentTimeMillis();
		DefaultHttpClient client = wrapClient(new DefaultHttpClient());
		client.addRequestInterceptor(new HttpRequestInterceptor() {
			@Override
			public void process(HttpRequest request, HttpContext context)
					throws HttpException, IOException {
				request.addHeader("Accept-Encoding", "gzip");
				// TODO change iwami user&password md5
				request.addHeader("Authorization", "Basic " + new BASE64Encoder().encode("api:**************".getBytes("utf-8")));
			}
		});

		client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);

		HttpPost request = new HttpPost(luosimaoUrl);

		ByteArrayOutputStream bos = null;
		InputStream bis = null;
		byte[] buf = new byte[10240];

		String content = null;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("mobile", phone));
			params.add(new BasicNameValuePair("message", data));
			request.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

			HttpResponse response = client.execute(request);

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

		} catch (Throwable t) {
			logger.error("Exception when sending code <" + data + "> <" + phone
					+ ">", t);
		} finally {
			if (bis != null) {
				try {
					bis.close();// 最后要关闭BufferedReader
				} catch (Exception e) {
				}
			}
			logger.info("send sms <" + result + "> <" + data + "> <" + phone + "> used <" + (System.currentTimeMillis() - start) + "ms>");
		}

		return result;
	}
	
	public static DefaultHttpClient wrapClient(HttpClient base) 
	{
	    try {
	        SSLContext ctx = SSLContext.getInstance("TLS");
	        X509TrustManager tm = new X509TrustManager() {

	            public void checkClientTrusted(X509Certificate[] chain,
	                    String authType) throws CertificateException
	            {
	                // TODO Auto-generated method stub

	            }

	            public void checkServerTrusted(X509Certificate[] chain,
	                    String authType) throws CertificateException
	            {
	                // TODO Auto-generated method stub

	            }

	            public X509Certificate[] getAcceptedIssuers()
	            {
	                // TODO Auto-generated method stub
	                return null;
	            }

	        };
	        ctx.init(null, new TrustManager[] { tm }, null);
	        SSLSocketFactory ssf = new SSLSocketFactory(ctx);
	        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	        ClientConnectionManager ccm = base.getConnectionManager();
	        SchemeRegistry sr = ccm.getSchemeRegistry();
	        //设置要使用的端口，默认是443
	        sr.register(new Scheme("https", ssf, 443));
	        return new DefaultHttpClient(ccm, base.getParams());
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return null;
	    }
	}
	// end of luosimao
}
