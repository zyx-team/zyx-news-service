package com.starnet.cloudmq.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;


import com.starnet.cloudmq.utils.JsonUtil;

import javax.net.ssl.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 绕过签名的https请求方式，无需证书。
 * 
 * @author Administrator
 *
 */
public class HttpUtil {

	private static Logger log = Logger
			.getLogger(BaseHttpClient.class.getName());

	private static RequestConfig defaultRequestConfig;

	private static PoolingHttpClientConnectionManager poolingConnManagerSsl = null;

	private static SSLConnectionSocketFactory sslsf=null;
	
	protected static String HTTP_REQUEST_ENCODING = "UTF-8";

	private static HttpClientBuilder httpClientBuilderSsl = null;
	
	private static X509HostnameVerifier x509HostnameVerifier= new X509HostnameVerifier() {

		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}

		public void verify(String host, SSLSocket ssl) throws IOException {
		}

		public void verify(String host, X509Certificate cert) throws SSLException {
		}

		public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
		}

	};

	static {
		int socketTimeout = 600000;
		int connectionRequestTimeout = 30000;
		int connectTimeout = 100000;
		int http_client_max_connections_per_route = 2000;
		int http_client_max_total_connections = 8000;
		defaultRequestConfig = RequestConfig.custom()
				.setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectionRequestTimeout)
				.setStaleConnectionCheckEnabled(true).build();
		SSLContext sslContext=null;
		try {
			sslContext = SSLContext.getInstance("SSL");
			// 设置为不需要获取证书
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(
						X509Certificate[] x509Certificates, String s)
						throws CertificateException {
				}

				@Override
				public void checkServerTrusted(
						X509Certificate[] x509Certificates, String s)
						throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					// 设置为不需要获取证书
					return null;
				}
			} }, null);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Create httpClientBuilderSsl failed!", e);
		}
		
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(sslContext,x509HostnameVerifier))
				.build();
		poolingConnManagerSsl = new PoolingHttpClientConnectionManager(
				socketFactoryRegistry);
		poolingConnManagerSsl.setMaxTotal(http_client_max_total_connections);
		poolingConnManagerSsl
				.setDefaultMaxPerRoute(http_client_max_connections_per_route);
		buildHttpClientBuilder();

	}

	private static void buildHttpClientBuilder() {
		try {

			httpClientBuilderSsl = HttpClients.custom()
					.setConnectionManager(poolingConnManagerSsl)
					.setDefaultRequestConfig(defaultRequestConfig);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Create httpClientBuilderSsl failed!", e);
		}
	}

	private static CloseableHttpClient getHttpClient(String url) {
		CloseableHttpClient httpClient = null;
		if (null != httpClientBuilderSsl) {
			httpClient = httpClientBuilderSsl.build();
		} else {
			synchronized (BaseHttpClient.class) {
				if (null != httpClientBuilderSsl) {
					httpClient = httpClientBuilderSsl.build();
				} else {
					buildHttpClientBuilder();
				}
			}
		}
		return httpClient;
	}
	public static String postJson(String url, Object object) throws Exception {
        return httpPostForStr(url, (JSONObject)object);
    }
	/**
	 * @Description: post数据
	 * 
	 * @param url
	 *            :请求地址
	 * @param json
	 *            ：JSONObject
	 * @return String：json格式字符串
	 * 
	 */
	public static String httpPostForStr(String url, JSONObject json) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		String returnStr = null;
		HttpPost httppost = null;
		try {
			 httpClient = getHttpClient(url);
			
			// 创建httppost
			httppost = new HttpPost(url);
			httppost.addHeader("Content-type",
					"application/json; charset=utf-8");
			// 参数处理
			HttpEntity reqEntity = createRequestEntity(json);
			httppost.setEntity(reqEntity);

			response = httpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
			returnStr = EntityUtils.toString(entity);
		} catch (UnsupportedEncodingException e) {
			log.log(Level.SEVERE, "Failure! UnsupportedEncodingException!", e);
		} catch (ClientProtocolException e) {
			log.log(Level.SEVERE, "Failure! ClientProtocolException!", e);
		} catch (ParseException e) {
			log.log(Level.SEVERE, "Failure! ParseException!", e);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Failure! IOException!", e);
		} catch (Throwable e) {
			log.log(Level.SEVERE, "Failure! Throwable!", e);
		} finally {
			if (null != response) {
				try {
					response.close();
				} catch (Throwable e) {
					log.log(Level.SEVERE, "Failure! response.close()!", e);
				}
			}

			try {
				httppost.abort();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return returnStr;
	}

	private static HttpEntity createRequestEntity(JSONObject json) {
		return new StringEntity(json.toString(), HTTP_REQUEST_ENCODING);
	}
}