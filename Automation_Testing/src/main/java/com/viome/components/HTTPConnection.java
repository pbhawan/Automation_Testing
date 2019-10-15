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

	public HTTPConnection() {
	}

	public JSONObject PostCustomerJson(String Record) throws ParseException {

		//File file = new File("./src/test/resources/Customer.json");
		try {
			/*JSONParser parser = new JSONParser();
			data = (JSONObject) parser.parse(new FileReader(file.getAbsolutePath()));*/
			HttpPost request = SetHeader();
			StringEntity params = new StringEntity(Record);
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			System.out.println("Response is" + "=" + response);

			// handle response here...

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

	public HttpPost SetHeader() throws IOException {
		//HttpPost request = new HttpPost(PF.getURLPropertiesValue().getProperty("HttpshopifyURL"));
		HttpPost request = new HttpPost("https://shopify-services.viome.com/v1/viome/shopify/webhook");		
		request.addHeader("content-type", "application/json");
		request.addHeader("x-shopify-hmac-sha256", "Tdf9G6igMrjlYOkpEyx0F4u6KGKKCJCG5bgIaCVc7LU=");
		request.addHeader("x-shopify-shop-domain", "viome3-QA.Automation.com,PB");
		request.addHeader("x-shopify-topic", "customers/create");
		request.addHeader("Host", "shopify-services.viome.com");
		return request;

	}
}
