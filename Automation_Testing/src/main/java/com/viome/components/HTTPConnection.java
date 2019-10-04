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
import org.openqa.selenium.WebDriver;

public class HTTPConnection {
	PageFactory PF = new PageFactory();
	JSONObject data;
	HttpClient httpClient = HttpClientBuilder.create().build();

	public HTTPConnection() {
	}

	public JSONObject GetHttpConnectionForCustomer() {

		File file = new File("./src/test/resources/Customer.json");
		try {
			JSONParser parser = new JSONParser();
			data = (JSONObject) parser.parse(new FileReader(file.getAbsolutePath()));
			HttpPost request = SetHeader();
			System.out.println(data.get("id"));
			StringEntity params = new StringEntity(data.toJSONString());
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
		return data;
	}

	public HttpPost SetHeader() throws IOException {
		HttpPost request = new HttpPost(PF.getURLPropertiesValue().getProperty("HttpshopifyURL"));
		request.addHeader("content-type", "application/json");
		request.addHeader("x-shopify-hmac-sha256", "Tdf9G6igMrjlYOkpEyx0F4u6KGKKCJCG5bgIaCVc7LU=");
		request.addHeader("x-shopify-shop-domain", "viome3-dev.myshopify.com,Vijay");
		request.addHeader("x-shopify-topic", "customers/create");
		request.addHeader("Host", "shopify-services.viome.com");
		return request;

	}
}
