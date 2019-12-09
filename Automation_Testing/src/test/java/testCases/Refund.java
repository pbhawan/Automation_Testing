package testCases;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
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

public class Refund {
	HTTPConnection HC;
	DBConnection DB;
	ResultSet rs;
	ConnectionProperties _CP;
	ExcelToJSONConvertor EJ;
	webhooks WH;

	private static ObjectMapper mapper = new ObjectMapper();
	List<Object> JsonRecords;
	int Iteration = 0;

	@SuppressWarnings("static-access")
	@BeforeTest
	public void Setup() throws Exception {
			
		HC = new HTTPConnection();
		DB = new DBConnection();
		EJ = new ExcelToJSONConvertor();
		//CJ = new CSVFileHandling();
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls",WH.Refund);

		@SuppressWarnings("unused")
		ConnectionProperties _CP = DB.DBConnection();
	}

	@AfterTest
	public void Teardown() throws SQLException {

		_CP.rs.close();
		_CP.conn.close();
		_CP.stmt.close();

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	@Test
	public void VerifyRefundData() throws IOException, InterruptedException, SQLException, ParseException {
		System.out.println("<------------- Refund Verification Started ------------->");
		for (Object record : JsonRecords)
		{		
         	Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			JSONObject JsonArrayObject;
			JSONArray JsonArray;
			
			map.put("id", new Date().getTime());
			//map.put("line_item_id", new Date().getTime());
			map.put("processed_at", new Date());
			map.put("created_at", new Date());
			if (null != map.get("refund_line_items")) {
			List<Map> refund_line_items = (List<Map>) map.get("refund_line_items");
		for (Map m : refund_line_items) {
			long id = new Date().getTime();
			m.put("id", new Date().getTime());
			m.put("line_item_id", id);
			if (null != m.get("line_item")) {
				Map line_item = (Map) m.get("line_item");
				line_item.put("id", id);
			m.put("line_item", line_item);
				}
		}

			}
			
			JSONObject RefundJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Refund);
			TimeUnit.SECONDS.sleep(30);
			_CP = DB.GetRecordFromDB(RefundJsonData,"Refund");
			if (_CP.rs.next()) {
				try {
//					Assert.assertEquals(RefundJsonData.get("id").toString(),_CP.rs.getString("id").toString(), "id not Match in Row" + Iteration);
					Assert.assertEquals(RefundJsonData.get("note").toString(),_CP.rs.getString("note").toString(), "note not Match in Row" + Iteration);
					Assert.assertEquals(RefundJsonData.get("order_adjustments").toString(),_CP.rs.getString("order_adjustments").toString(),"order_adjustments not Match in Row" + Iteration);
//					Assert.assertEquals(RefundJsonData.get("order_id").toString(),_CP.rs.getString("order_id").toString(), "order_adjustments not Match in Row" + Iteration);
//					Assert.assertEquals(RefundJsonData.get("user_id").toString(),_CP.rs.getString("user_id").toString(), "order_adjustments not Match in Row" + Iteration);
					Assert.assertEquals(RefundJsonData.get("restock").toString(),_CP.rs.getString("restock").toString(),"restock not Match in Row" + Iteration);
//					Assert.assertEquals(RefundJsonData.get("admin_graphql_api_id").toString(),_CP.rs.getString("admin_graphql_api_id").toString(),"admin_graphql_api_id not Match in Row" + Iteration);			

					//Object under customer	
					 JsonArray = (JSONArray) RefundJsonData.get("refund_line_items");
						for (int i = 0; i < JsonArray.size(); i++) {
						JsonArrayObject = (JSONObject) JsonArray.get(i);	
//						Assert.assertEquals(JsonArrayObject.get("id").toString(),_CP.rs.getString("id").toString(), "id not Match in Row" + Iteration);

						JSONObject line_item = (JSONObject) JsonArrayObject.get("line_item");
						Assert.assertEquals(line_item.get("discount_allocations").toString(),_CP.rs.getString("discount_allocations"),"discount_allocations not Match in Row"+Iteration);
//						Assert.assertEquals(line_item.get("fulfillable_quantity").toString(),_CP.rs.getString("fulfillable_quantity"),"fulfillable_quantity not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("fulfillment_service"),_CP.rs.getString("fulfillment_service"),"fulfillment_service not Match in Row"+Iteration);
//null					Assert.assertEquals(line_item.get("fulfillment_status"),_CP.rs.getString("fulfillment_status"),"fulfillment_status not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("gift_card").toString(),_CP.rs.getString("gift_card"),"gift_card not Match in Row"+Iteration);
//						Assert.assertEquals(line_item.get("id"),_CP.rs.getString("id"),"id not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("name").toString(),_CP.rs.getString("name"),"name not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("price").toString(),_CP.rs.getString("price"),"price not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("price_set").toString(),_CP.rs.getString("price_set"),"price_set not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("product_exists").toString(),_CP.rs.getString("product_exists"),"product_exists not Match in Row"+Iteration);
//						Assert.assertEquals(line_item.get("product_id"),_CP.rs.getString("product_id"),"product_id not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("properties").toString(),_CP.rs.getString("properties"),"properties not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("quantity").toString(),_CP.rs.getString("quantity"),"quantity not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("requires_shipping").toString(),_CP.rs.getString("requires_shipping"),"requires_shipping not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("sku").toString(),_CP.rs.getString("sku"),"sku not Match in Row"+Iteration);
//						Assert.assertEquals(line_item.get("tax_lines"),_CP.rs.getString("tax_lines"),"tax_lines not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("taxable").toString(),_CP.rs.getString("taxable"),"taxable not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("title").toString(),_CP.rs.getString("title"),"title not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("total_discount").toString(),_CP.rs.getString("total_discount"),"total_discount not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("total_discount_set").toString(),_CP.rs.getString("total_discount_set"),"total_discount_set not Match in Row"+Iteration);
//						Assert.assertEquals(line_item.get("variant_id"),_CP.rs.getString("variant_id"),"variant_id not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("variant_title").toString(),_CP.rs.getString("variant_title"),"variant_title not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("vendor").toString(),_CP.rs.getString("vendor"),"vendor not Match in Row"+Iteration);
						Assert.assertEquals(line_item.get("variant_inventory_management").toString(),_CP.rs.getString("variant_inventory_management"),"variant_inventory_management not Match in Row"+Iteration);

						Assert.assertEquals(JsonArrayObject.get("quantity").toString(),_CP.rs.getString("quantity").toString(), "quantity not Match in Row" + Iteration);
//						Assert.assertEquals(JsonArrayObject.get("line_item_id").toString(),_CP.rs.getString("line_item_id").toString(), "quantity not Match in Row" + Iteration);
//						Assert.assertEquals(JsonArrayObject.get("location_id").toString(),_CP.rs.getString("location_id").toString(), "location_id not Match in Row" + Iteration);
						Assert.assertEquals(JsonArrayObject.get("restock_type").toString(),_CP.rs.getString("restock_type").toString(), "restock_type not Match in Row" + Iteration);
//int					Assert.assertEquals(JsonArrayObject.get("subtotal").toString(),_CP.rs.getString("subtotal").toString(), "subtotal not Match in Row" + Iteration);
//int					Assert.assertEquals(JsonArrayObject.get("total_tax").toString(),_CP.rs.getString("total_tax").toString(), "total_tax not Match in Row" + Iteration);
						Assert.assertEquals(JsonArrayObject.get("subtotal_set").toString(),_CP.rs.getString("subtotal_set").toString(), "subtotal_set not Match in Row" + Iteration);
						Assert.assertEquals(JsonArrayObject.get("total_tax_set").toString(),_CP.rs.getString("total_tax_set").toString(), "total_tax_set not Match in Row" + Iteration);
						
//						Assert.assertEquals(JsonArrayObject.get("line_item_id").toString(),_CP.rs.getString("line_item_id").toString(), "line_item_id not Match in Row" + Iteration);
//						Assert.assertEquals(JsonArrayObject.get("location_id").toString(),_CP.rs.getString("location_id").toString(), "location_id not Match in Row" + Iteration);
//						Assert.assertEquals(JsonArrayObject.get("quantity").toString(),_CP.rs.getString("quantity").toString(), "quantity not Match in Row" + Iteration);
						Assert.assertEquals(JsonArrayObject.get("restock_type").toString(),_CP.rs.getString("restock_type").toString(),"restock_type not Match in Row" + Iteration);
						Assert.assertEquals(JsonArrayObject.get("subtotal_set").toString(),_CP.rs.getString("subtotal_set").toString(),"subtotal_set not Match in Row" + Iteration);
						}
						Assert.assertEquals(RefundJsonData.get("restock").toString(),_CP.rs.getString("restock").toString(), "restock not Match in Row" + Iteration);
						Assert.assertEquals(RefundJsonData.get("transactions").toString(),_CP.rs.getString("order_transactions").toString(), "transactions not Match in Row" + Iteration);
//						Assert.assertEquals(RefundJsonData.get("user_id").toString(),_CP.rs.getString("user_id").toString(), "user_id not Match in Row" + Iteration);
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration, WH.Refund);
					break;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration,  WH.Refund);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration,  WH.Refund);
			}

		}
		System.out.println("<------------- Refund Verification Ended ------------->");

	}

}
