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
	
	@Test
	public void VerifyThemeData() throws IOException, InterruptedException, SQLException, ParseException {

		for (Object record : JsonRecords)
		{		
         	@SuppressWarnings("unchecked")
			Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			JSONObject JsonArrayObject;
			JSONArray JsonArray;
			map.put("id", new Date().getTime());
			map.put("processed_at", new Date());
			map.put("created_at", new Date());
			@SuppressWarnings("static-access")
			JSONObject RefundJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Refund);
			TimeUnit.SECONDS.sleep(10);
			_CP = DB.GetRecordFromDB(RefundJsonData,"Refund");
			if (_CP.rs.next()) {
				try {
					Assert.assertEquals(RefundJsonData.get("note").toString(),_CP.rs.getString("note").toString(), "note not Match in Row" + Iteration);
					Assert.assertEquals(RefundJsonData.get("order_adjustments").toString(),_CP.rs.getString("order_adjustments").toString(), "order_adjustments not Match in Row" + Iteration);
//					Assert.assertEquals(RefundJsonData.get("order_id").toString(),_CP.rs.getString("order_id").toString(),"order_id not Match in Row" + Iteration);
					
//					Assert.assertEquals(RefundJsonData.get("refund_line_items").toString(),
//jsonarray							_CP.rs.getString("refund_line_items").toString(),"refund_line_items not Match in Row" + Iteration);

					//Object under customer	  
					JSONObject refund_line_items = (JSONObject) RefundJsonData.get("refund_line_items");
//					Assert.assertEquals(refund_line_items.get("id").toString(),_CP.rs.getString("line_items_id"),"customer_accepts_marketing not Match in Row"+Iteration);

					//Object inside refind_line_items line_items object handling
					JSONObject line_item = (JSONObject) refund_line_items.get("line_item");
	    			Assert.assertEquals(line_item.get("discount_allocations").toString(),_CP.rs.getString("discount_allocations").toString(), "discount_allocations not Match in Row" + Iteration);
    				Assert.assertEquals(line_item.get("fulfillable_quantity").toString(),_CP.rs.getString("fulfillable_quantity").toString(),	"fulfillable_quantity not Match in Row" + Iteration);
    				Assert.assertEquals(line_item.get("fulfillment_service").toString(),_CP.rs.getString("fulfillment_service").toString(), "fulfillment_service not Match in Row" + Iteration);
//null    			Assert.assertEquals(line_item.get("fulfillment_status").toString(),_CP.rs.getString("fulfillment_status").toString(), "fulfillment_status not Match in Row" + Iteration);
   				    Assert.assertEquals(line_item.get("gift_card").toString(),_CP.rs.getString("gift_card").toString(), "gift_card not Match in Row" + Iteration);
    				Assert.assertEquals(line_item.get("grams").toString(),_CP.rs.getString("grams").toString(),"grams" + Iteration);
//	    			Assert.assertEquals(JsonArrayObject.get("name").toString(),_CP.rs.getString("name").toString(),"name not Match in Row" + Iteration);
    				Assert.assertEquals(line_item.get("price_set").toString(),_CP.rs.getString("price_set").toString(),"price_set not Match in Row" + Iteration);
    				Assert.assertEquals(line_item.get("price").toString(),_CP.rs.getString("price").toString(),"price not Match in Row" + Iteration);
//	    			Assert.assertEquals(line_item.get("product_id").toString(),_CP.rs.getString("product_id").toString(),"product_id not Match in Row" + Iteration);
//	    			Assert.assertEquals(line_item.get("product_exists").toString(),_CP.rs.getString("product_exists").toString(),"product_exists not Match in Row" + Iteration);
//	    			Assert.assertEquals(line_item.get("properties").toString(),_CP.rs.getString("properties").toString(),"properties not Match in Row" + Iteration);
    				Assert.assertEquals(line_item.get("quantity").toString(),_CP.rs.getString("quantity").toString(),"quantity not Match in Row" + Iteration);
    				Assert.assertEquals(line_item.get("requires_shipping").toString(),_CP.rs.getString("requires_shipping").toString(),"quantity not Match in Row" + Iteration);
//    				Assert.assertEquals(line_item.get("sku").toString(),_CP.rs.getString("sku").toString(),"sku not Match in Row" + Iteration);
//	    			Assert.assertEquals(line_item.get("tax_lines").toString(),_CP.rs.getString("tax_lines").toString(),"tax_lines not Match in Row" + Iteration);
    				Assert.assertEquals(line_item.get("taxable").toString(),_CP.rs.getString("taxable").toString(),"taxable not Match in Row" + Iteration);
    				Assert.assertEquals(line_item.get("title").toString(),_CP.rs.getString("title").toString(),"title not Match in Row" + Iteration);
    				Assert.assertEquals(line_item.get("total_discount").toString(),_CP.rs.getString("total_discounts").toString(),"total_discount not Match in Row" + Iteration);
//    				Assert.assertEquals(line_item.get("total_discount_set").toString(),_CP.rs.getString("total_discount_set").toString(),"total_discount_set not Match in Row" + Iteration);
//	    			Assert.assertEquals(line_item.get("variant_id").toString(),_CP.rs.getString("variant_id").toString(),"variant_id not Match in Row" + Iteration);
//null    			Assert.assertEquals(line_item.get("variant_title").toString(),_CP.rs.getString("variant_title").toString(),"variant_title not Match in Row" + Iteration);
    				Assert.assertEquals(line_item.get("vendor").toString(),_CP.rs.getString("vendor").toString(),"vendor not Match in Row" + Iteration);

//					Assert.assertEquals(refund_line_items.get("id").toString(),_CP.rs.getString("line_items_id"),"customer_accepts_marketing not Match in Row"+Iteration);
//					Assert.assertEquals(refund_line_items.get("location_id").toString(),_CP.rs.getString("location_id"),"customer_accepts_marketing not Match in Row"+Iteration);
//    				Assert.assertEquals(refund_line_items.get("line_item_id").toString(),_CP.rs.getString("line_item_id"),"line_item_id not Match in Row"+Iteration);
    				Assert.assertEquals(refund_line_items.get("restock_type").toString(),_CP.rs.getString("restock_type"),"restock_type not Match in Row"+Iteration);
//int				Assert.assertEquals(refund_line_items.get("subtotal").toString(),_CP.rs.getString("subtotal"),"subtotal not Match in Row"+Iteration);
					Assert.assertEquals(refund_line_items.get("subtotal_set").toString(),_CP.rs.getString("subtotal_set"),"subtotal_set not Match in Row"+Iteration);
					Assert.assertEquals(refund_line_items.get("total_tax").toString(),_CP.rs.getString("total_tax"),"total_tax not Match in Row"+Iteration);
					Assert.assertEquals(refund_line_items.get("total_tax_set").toString(),_CP.rs.getString("total_tax_set"),"total_tax_set not Match in Row"+Iteration);

					Assert.assertEquals(RefundJsonData.get("restock ").toString(),_CP.rs.getString("restock ").toString(),"restock  not Match in Row" + Iteration);
					
					Assert.assertEquals(RefundJsonData.get("transactions").toString(),_CP.rs.getString("order_transactions").toString(),"transactions  not Match in Row" + Iteration);
					
// 				    Assert.assertEquals(ThemeJsonData.get("user_id ").toString(),
//							_CP.rs.getString("user_id ").toString(),"user_id  not Match in Row" + Iteration);
					
					
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
	}

}
