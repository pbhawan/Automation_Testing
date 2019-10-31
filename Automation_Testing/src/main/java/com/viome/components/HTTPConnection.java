package com.viome.components;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HTTPConnection {
	PageFactory PF = new PageFactory();
	JSONObject data;
	HttpClient httpClient = HttpClientBuilder.create().build();
	String webhook;

	public HTTPConnection() {
	}

	public JSONObject PostJson(String Record, String webhook_nm) throws ParseException {
		webhook=webhook_nm;
		
		try {
					
			StringEntity params = new StringEntity(Record);
			String params1=HmacUtil.calculateHMAC(Record, "07ac5dfed552350b42c51225ea89e87f17f5b91d069c1d8fe8a96b8124e6ecfb");
			HttpPost request = SetHeader(params1);
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			System.out.println("Response is" + "=" + response);

		

		} catch (Exception ex) {
			System.out.println(ex);

		} finally {
			// Deprecatedxx
			// httpClient.getConnectionManager().shutdown();
		}
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(Record);
		return json;
	}

	public HttpPost SetHeader(String params1 ) throws IOException {
		//HttpPost request = new HttpPost(PF.getURLPropertiesValue().getProperty("HttpshopifyURL"));
		HttpPost request = new HttpPost("https://shopify-services.viome.com/v1/viome/shopify/webhook");		
		request.addHeader("content-type", "application/json");
		request.addHeader("x-shopify-hmac-sha256", params1);
		request.addHeader("x-shopify-shop-domain", "viome3-QA.Automation.com,PB");
		if(webhook=="Customer")
		request.addHeader("x-shopify-topic", "customers/create");
		if(webhook=="Location")
			request.addHeader("x-shopify-topic", "customers/create");
		request.addHeader("Host", "shopify-services.viome.com");
		return request;

	}
}
