package testCases;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

public class Shop {
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
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls",WH.Shop);

		@SuppressWarnings("unused")
		ConnectionProperties _CP = DB.DBConnection();
	}

	@AfterTest
	public void Teardown() throws SQLException {

		_CP.rs.close();
		_CP.conn.close();
		_CP.stmt.close();

	}
	
	@SuppressWarnings("static-access")
	@Test
	public void VerifyShopData() throws IOException, InterruptedException, SQLException, ParseException {

		for (Object record : JsonRecords)
		{		
         	@SuppressWarnings("unchecked")
			Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			map.put("id", new Date().getTime());
			map.put("updated_at", new Date());
			map.put("created_at", new Date());
			@SuppressWarnings("static-access")
			JSONObject ShopJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Shop);
			TimeUnit.SECONDS.sleep(10);
			_CP = DB.GetRecordFromDB(ShopJsonData,"Shop");
			if (_CP.rs.next()) {
				try {
					Assert.assertEquals(ShopJsonData.get("address1").toString(),
							_CP.rs.getString("address1").toString(), "address1 not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("address2").toString(),
							_CP.rs.getString("address2").toString(), "address2 not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("checkout_api_supported").toString(),
							_CP.rs.getString("checkout_api_supported").toString(),"checkout_api_supported not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("city").toString(),
							_CP.rs.getString("city").toString(),"city not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("country").toString(),
							_CP.rs.getString("country").toString(),"country not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("country_code").toString(),
							_CP.rs.getString("country_code").toString(),"country_code not Match in Row" + Iteration);
					
 				    Assert.assertEquals(ShopJsonData.get("country_name").toString(),
							_CP.rs.getString("country_name").toString(),"country_name not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("county_taxes").toString(),
							_CP.rs.getString("county_taxes").toString(),"county_taxes not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("currency").toString(),_CP.rs.getString("currency").toString(),
							"currency not Match in Row" + Iteration);
										
					Assert.assertEquals(ShopJsonData.get("customer_email").toString(),_CP.rs.getString("customer_email").toString(),
							"customer_email not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("domain").toString(),_CP.rs.getString("domain").toString(),
							"domain not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("eligible_for_card_reader_giveaway").toString(),
							_CP.rs.getString("eligible_for_card_reader_giveaway").toString(),"eligible_for_card_reader_giveaway not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("eligible_for_payments").toString(),
							_CP.rs.getString("eligible_for_payments").toString(),"eligible_for_payments not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("email").toString(),
							_CP.rs.getString("email").toString(),"email not Match in Row" + Iteration);
					
//json array					Assert.assertEquals(ShopJsonData.get("enabled_presentment_currencies").toString(),
//							_CP.rs.getString("enabled_presentment_currencies").toString(),"enabled_presentment_currencies not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("finances").toString(),
							_CP.rs.getString("finances").toString(),"finances not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("force_ssl").toString(),
							_CP.rs.getString("force_ssl").toString(),"force_ssl not Match in Row" + Iteration);
					
//					Assert.assertEquals(ShopJsonData.get("google_apps_domain").toString(),
//null							_CP.rs.getString("google_apps_domain").toString(),"google_apps_domain not Match in Row" + Iteration);
					
//					Assert.assertEquals(ShopJsonData.get("google_apps_login_enabled").toString(),
//null							_CP.rs.getString("google_apps_login_enabled").toString(),"google_apps_login_enabled not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("has_discounts").toString(),
							_CP.rs.getString("has_discounts").toString(),"has_discounts not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("has_gift_cards").toString(),
							_CP.rs.getString("has_gift_cards").toString(),"has_gift_cards not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("has_storefront").toString(),
							_CP.rs.getString("has_storefront").toString(),"has_storefront not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("iana_timezone").toString(),
							_CP.rs.getString("iana_timezone").toString(),"iana_timezone not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("latitude").toString(),
							_CP.rs.getString("latitude").toString(),"latitude not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("longitude").toString(),
							_CP.rs.getString("longitude").toString(),"longitude not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("money_format").toString(),
							_CP.rs.getString("money_format").toString(),"money_format not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("money_in_emails_format").toString(),
							_CP.rs.getString("money_in_emails_format").toString(),"money_in_emails_format not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("money_with_currency_format").toString(),
							_CP.rs.getString("money_with_currency_format").toString(),"money_with_currency_format not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("money_with_currency_in_emails_format").toString(),
							_CP.rs.getString("money_with_currency_in_emails_format").toString(),"money_with_currency_in_emails_format not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("multi_location_enabled").toString(),
							_CP.rs.getString("multi_location_enabled").toString(),"multi_location_enabled not Match in Row" + Iteration);

					Assert.assertEquals(ShopJsonData.get("myshopify_domain").toString(),
							_CP.rs.getString("myshopify_domain").toString(),"myshopify_domain not Match in Row" + Iteration);

					Assert.assertEquals(ShopJsonData.get("name").toString(),
							_CP.rs.getString("name").toString(),"name not Match in Row" + Iteration);

					Assert.assertEquals(ShopJsonData.get("password_enabled").toString(),
							_CP.rs.getString("password_enabled").toString(),"password_enabled not Match in Row" + Iteration);

					Assert.assertEquals(ShopJsonData.get("phone").toString(),
							_CP.rs.getString("phone").toString(),"phone not Match in Row" + Iteration);

					Assert.assertEquals(ShopJsonData.get("plan_display_name").toString(),
							_CP.rs.getString("plan_display_name").toString(),"plan_display_name not Match in Row" + Iteration);

					Assert.assertEquals(ShopJsonData.get("plan_name").toString(),
							_CP.rs.getString("plan_name").toString(),"plan_name not Match in Row" + Iteration);

					Assert.assertEquals(ShopJsonData.get("pre_launch_enabled").toString(),
							_CP.rs.getString("pre_launch_enabled").toString(),"pre_launch_enabled not Match in Row" + Iteration);

					Assert.assertEquals(ShopJsonData.get("primary_locale").toString(),
							_CP.rs.getString("primary_locale").toString(),"primary_locale not Match in Row" + Iteration);

//					Assert.assertEquals(ShopJsonData.get("primary_location_id").toString(),
//							_CP.rs.getString("primary_location_id").toString(),"primary_location_id not Match in Row" + Iteration);

					Assert.assertEquals(ShopJsonData.get("province").toString(),
							_CP.rs.getString("province").toString(),"province not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("province_code").toString(),
							_CP.rs.getString("province_code").toString(),"province_code not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("requires_extra_payments_agreement").toString(),
							_CP.rs.getString("requires_extra_payments_agreement").toString(),"requires_extra_payments_agreement not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("setup_required").toString(),
							_CP.rs.getString("setup_required").toString(),"setup_required not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("shop_owner").toString(),
							_CP.rs.getString("shop_owner").toString(),"shop_owner not Match in Row" + Iteration);
					
//null					Assert.assertEquals(ShopJsonData.get("source").toString(),
//							_CP.rs.getString("source").toString(),"source not Match in Row" + Iteration);
					
//null					Assert.assertEquals(ShopJsonData.get("tax_shipping").toString(),
//							_CP.rs.getString("tax_shipping").toString(),"tax_shipping not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("taxes_included").toString(),
							_CP.rs.getString("taxes_included").toString(),"taxes_included not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("timezone").toString(),
							_CP.rs.getString("timezone").toString(),"timezone not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("weight_unit").toString(),
							_CP.rs.getString("weight_unit").toString(),"weight_unit not Match in Row" + Iteration);
					
					Assert.assertEquals(ShopJsonData.get("zip").toString(),
							_CP.rs.getString("zip").toString(),"zip not Match in Row" + Iteration);

					
					
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration, WH.Shop);
					break;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration,  WH.Shop);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration,  WH.Shop);
			}

		}
	}

}
