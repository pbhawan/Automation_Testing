package com.viome.components;

//import java.io.File;
//import java.io.FileReader;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.viome.enums.webhooks;

public class HTTPConnection {
	PageFactory PF = new PageFactory();
	JSONObject data;
	HttpClient httpClient = HttpClientBuilder.create().build();
	@SuppressWarnings("rawtypes")
	Enum webhook;
	webhooks WH;

	public HTTPConnection() {
	}

	public JSONObject PostJson(String Record, @SuppressWarnings("rawtypes") Enum webhook_nm) throws ParseException {
		webhook=webhook_nm;
		
		try {
			System.out.println(webhook_nm + " " + "JSON---->" +Record);		
			StringEntity params = new StringEntity(Record);
			/*staging HMAC*/
//			String params1=HmacUtil.calculateHMAC(Record, "07ac5dfed552350b42c51225ea89e87f17f5b91d069c1d8fe8a96b8124e6ecfb");
			/*prod HMAC*/
			String params1=HmacUtil.calculateHMAC(Record, "8a6996dbb81013706671cbb514f21096e70b4ede62910a12170bd79fc48ec493");
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

	@SuppressWarnings("static-access")
	public HttpPost SetHeader(String params1 ) throws IOException {
		/* staging URL */
//		HttpPost request = new HttpPost("https://shopify-services.viome.com/v1/viome/shopify/webhook");
		/* production URL */
		HttpPost request = new HttpPost("https://shopify-int-services.viome.com/v1/viome/shopify/webhook");	
		request.addHeader("content-type", "application/json");
		request.addHeader("x-shopify-hmac-sha256", params1);
		request.addHeader("x-shopify-shop-domain", "viome3-QA.Automation.com,NJ");

		if(webhook==WH.Cart)
		request.addHeader("x-shopify-topic","carts/create");
		
		if(webhook==WH.Checkout)
		request.addHeader("x-shopify-topic","checkouts/create");
		
		if(webhook==WH.Customer)
		request.addHeader("x-shopify-topic","customers/create");
		
		if(webhook==WH.DraftOrder)
			request.addHeader("x-shopify-topic","draft_orders/create");
		
		if(webhook==WH.Fulfillment)
			request.addHeader("x-shopify-topic","fulfillments/create");
		
		if(webhook==WH.Location)
			request.addHeader("x-shopify-topic","locations/create");
		
		if(webhook==WH.Order)
			request.addHeader("x-shopify-topic","orders/create");
		
		if(webhook==WH.Order_Transaction)
			request.addHeader("x-shopify-topic","order_transactions/create");
		
		if(webhook==WH.Product)
		request.addHeader("x-shopify-topic","products/create");
		
		if(webhook==WH.Refund)
		request.addHeader("x-shopify-topic","refunds/create");
		
		if(webhook==WH.Shop)
		request.addHeader("x-shopify-topic","shop/update");

		if(webhook==WH.Tender_Transaction)
		request.addHeader("x-shopify-topic","tender_transactions/create");
		
		if(webhook==WH.Theme)
		request.addHeader("x-shopify-topic","themes/create");
		
		request.addHeader("Host", "shopify-int-services.viome.com");
		return request;

	}
}
