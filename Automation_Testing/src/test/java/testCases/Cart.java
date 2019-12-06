package testCases;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viome.components.ConnectionProperties;
import com.viome.components.DBConnection;

import com.viome.components.ExcelToJSONConvertor;
import com.viome.components.HTTPConnection;
import com.viome.enums.webhooks;

public class Cart {
	HTTPConnection HC;
	DBConnection DB;
	ResultSet rs;
	ConnectionProperties _CP;
	ExcelToJSONConvertor EJ;
	webhooks WH;

	private static ObjectMapper mapper = new ObjectMapper();
	ArrayList<Object> JsonRecords;
	int Iteration = 0;

	@SuppressWarnings("static-access")
	@BeforeTest
	public void Setup() throws Exception {

		HC = new HTTPConnection();
		DB = new DBConnection();
		EJ = new ExcelToJSONConvertor();
		// CJ = new CSVFileHandling();
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls", WH.Cart);
		@SuppressWarnings("unused")
		ConnectionProperties _CP = DB.DBConnection();
	}

	@AfterTest
	public void Teardown() throws SQLException {

		_CP.rs.close();
		_CP.conn.close();
		_CP.stmt.close();

	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@Test
	public void VerifyCartData() throws IOException, InterruptedException, SQLException, ParseException {

		for (Object record : JsonRecords) {
			JSONObject JsonArrayObject;
			JSONArray JsonArray;
			Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			map.put("id", new Date().getTime());
			map.put("updated_at", new Date());
			map.put("created_at", new Date());
			JSONObject CartJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Cart);
			TimeUnit.SECONDS.sleep(10);
			_CP = DB.GetRecordFromDB(CartJsonData, "Cart");
			if (_CP.rs.next()) {
				try {
		    Assert.assertEquals(CartJsonData.get("token").toString(), _CP.rs.getString("token").toString(),	"token not Match in Row" + Iteration);
		     Assert.assertEquals(CartJsonData.get("note").toString(), _CP.rs.getString("note").toString(), 	"note not Match in Row" + Iteration);
		    JsonArray = (JSONArray) CartJsonData.get("line_items");
			for (int i = 0; i < JsonArray.size(); i++) {
			JsonArrayObject = (JSONObject) JsonArray.get(i);
			Assert.assertEquals(JsonArrayObject.get("quantity").toString(),_CP.rs.getString("quantity").toString(), "quantity not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("discounted_price").toString(),_CP.rs.getString("discounted_price").toString(), "discounted_price not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("line_price").toString(),_CP.rs.getString("line_price").toString(),"line_price not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("original_price").toString(),_CP.rs.getString("original_price").toString(),"original_price not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("original_line_price").toString(),_CP.rs.getString("original_line_price").toString(),"original_line_price not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("total_discount").toString(),_CP.rs.getString("total_discount").toString(),	"total_discount not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("gift_card").toString(),_CP.rs.getString("gift_card").toString(), "gift_card not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("taxable").toString(),_CP.rs.getString("taxable").toString(), "taxable not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("title").toString(),_CP.rs.getString("title").toString(), "title not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("total_discount_set").toString(),_CP.rs.getString("total_discount_set").toString(),	"total_discount_set not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("discounted_price_set").toString(),_CP.rs.getString("discounted_price_set").toString(),	"discounted_price_set not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("line_price_set").toString(),_CP.rs.getString("line_price_set").toString(),	"line_price_set not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("original_line_price_set").toString(),_CP.rs.getString("original_line_price_set").toString(),	"original_line_price_set not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("price_set").toString(),_CP.rs.getString("price_set").toString(),	"price_set not Match in Row" + Iteration);		
			Assert.assertEquals(CartJsonData.get("note").toString(), _CP.rs.getString("note").toString(),"note not Match in Row" + Iteration);	
			Assert.assertEquals(JsonArrayObject.get("variant_id").toString(),_CP.rs.getString("variant_id").toString(), "variant_id not Match in Row" + Iteration);						
			Assert.assertEquals(JsonArrayObject.get("price").toString(),_CP.rs.getString("price").toString(), "price not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("vendor").toString(),_CP.rs.getString("vendor").toString(), "vendor not Match in Row" + Iteration);
		    Assert.assertEquals(JsonArrayObject.get("product_id").toString(),_CP.rs.getString("product_id").toString(), "product_id not Match in Row" + Iteration);					
			Assert.assertEquals(JsonArrayObject.get("id").toString(), _CP.rs.getString("id").toString(),"id not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("grams").toString(),_CP.rs.getString("grams").toString(), "grams not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("sku").toString(), _CP.rs.getString("sku").toString(),"sku not Match in Row" + Iteration);
			Assert.assertEquals(JsonArrayObject.get("price_set").toString(),_CP.rs.getString("price_set").toString(), "price_set not Match in Row" + Iteration);						
			Assert.assertEquals(JsonArrayObject.get("properties").toString(),_CP.rs.getString("properties").toString(), "properties not Match in Row" + Iteration);
					}

				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration, WH.Cart);
					break;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration, WH.Cart);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration, WH.Cart);
			}

		}
	}

}
