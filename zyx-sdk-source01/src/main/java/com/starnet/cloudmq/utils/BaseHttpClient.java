package com.starnet.cloudmq.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;










/**
 * @Description: 数据服务接口客户端封装
 * 信任自签名的https请求。需要将要访问的网站的ssl证书导出到服务器，然后才可访问。
 * 可以修改程序达到指定证书路径的功能。
 * 
 */
public final class BaseHttpClient {
	private static Logger log=Logger.getLogger(BaseHttpClient.class.getName());
	
	private static RequestConfig defaultRequestConfig;


	private static PoolingHttpClientConnectionManager poolingConnManagerSsl = new PoolingHttpClientConnectionManager();

	protected static String HTTP_REQUEST_ENCODING = "UTF-8";

	private static HttpClientBuilder httpClientBuilderSsl = null;

	static {
		int socketTimeout = 600000;
		int connectionRequestTimeout = 30000;
		int connectTimeout = 100000;
		int http_client_max_connections_per_route = 2000;
		int http_client_max_total_connections = 8000;
		defaultRequestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectionRequestTimeout).setStaleConnectionCheckEnabled(true).build();

		
		poolingConnManagerSsl.setMaxTotal(http_client_max_total_connections);
		poolingConnManagerSsl.setDefaultMaxPerRoute(http_client_max_connections_per_route);

		
		buildHttpClientBuilder();
	}

	private static void buildHttpClientBuilder() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}

				public void verify(String host, SSLSocket ssl) throws IOException {
				}

				public void verify(String host, X509Certificate cert) throws SSLException {
				}

				public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
				}

			});
			
			httpClientBuilderSsl = HttpClients.custom().setConnectionManager(poolingConnManagerSsl)
					.setDefaultRequestConfig(defaultRequestConfig).setSSLSocketFactory(sslsf);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Create httpClientBuilderSsl failed!",e);
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
			httppost.addHeader("Content-type","application/json; charset=utf-8");
			// 参数处理
			HttpEntity reqEntity = createRequestEntity( json);
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