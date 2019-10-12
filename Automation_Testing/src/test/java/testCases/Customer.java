package testCases;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.viome.components.ConnectionProperties;
import com.viome.components.DBConnection;
import com.viome.components.ExcelToJSONConvertor;
import com.viome.components.HTTPConnection;
import com.viome.utilites.FileConversionXLSToXLXS;

import junit.framework.Assert;

public class Customer {
	HTTPConnection HC;
	DBConnection DB;
	ResultSet rs;
	ConnectionProperties _CP;
	ExcelToJSONConvertor EJ;
	FileConversionXLSToXLXS FC;

	@BeforeTest
	public void Setup() throws IOException {

		HC = new HTTPConnection();
		DB = new DBConnection();
		EJ = new ExcelToJSONConvertor();
		FC=new FileConversionXLSToXLXS();
	}

	@AfterTest
	public void Teardown() throws SQLException {
		_CP.rs.close();
		_CP.conn.close();
		_CP.stmt.close();
	}

	@Test
	public void VerifyCustomerData() throws IOException, InterruptedException, SQLException, ParseException {
		
		//String XLXSFilePath =FC.convertXLS2XLSX("./src/test/resources/DemoSheet.xls");

		List<String> PostJson = EJ.CreteJSONAndTextFileFromExcel("./src/test/resources/DemoSheet.xls");
		for (String s : PostJson) {
			JSONObject CustomerJsonData = HC.PostCustomer(s);
			TimeUnit.SECONDS.sleep(30);
			_CP = DB.GetCustomerFromDB(CustomerJsonData);
			while (_CP.rs.next()) {
				Assert.assertEquals(CustomerJsonData.get("first_name").toString(),
						_CP.rs.getString("first_name").toString());
				Assert.assertEquals(CustomerJsonData.get("last_name").toString(),
						_CP.rs.getString("last_name").toString());
				Assert.assertEquals(CustomerJsonData.get("email").toString(), _CP.rs.getString("email").toString());
				Assert.assertEquals(CustomerJsonData.get("accepts_marketing").toString(),
						_CP.rs.getString("accepts_marketing").toString());
				Assert.assertEquals(CustomerJsonData.get("orders_count").toString(),
						_CP.rs.getString("orders_count").toString());
				Assert.assertEquals(CustomerJsonData.get("total_spent").toString(),
						_CP.rs.getString("total_spent").toString());
//            Assert.assertEquals(CustomerJsonData.get("last_order_id").toString(),_CP.rs.getString("last_order_id").toString());
				// Assert.assertEquals(CustomerJsonData.get("note").toString(),_CP.rs.getString("note").toString(),
				// "Note issue");
				Assert.assertEquals(CustomerJsonData.get("verified_email").toString(),
						_CP.rs.getString("verified_email").toString());
				// Assert.assertEquals(CustomerJsonData.get("multipass_identifier").toString(),_CP.rs.getString("multipass_identifier").toString());
				Assert.assertEquals(CustomerJsonData.get("tax_exempt").toString(),
						_CP.rs.getString("tax_exempt").toString());
				// Assert.assertEquals(CustomerJsonData.get("phone").toString(),_CP.rs.getString("phone").toString());
				Assert.assertEquals(CustomerJsonData.get("tags").toString(), _CP.rs.getString("tags").toString());
				// Assert.assertEquals(CustomerJsonData.get("last_order_name").toString(),_CP.rs.getString("last_order_name").toString());
				Assert.assertEquals(CustomerJsonData.get("currency").toString(),
						_CP.rs.getString("currency").toString());
				// Assert.assertEquals(CustomerJsonData.get("accepts_marketing_updated_at").toString(),_CP.rs.getString("accepts_marketing_updated_at").toString());
				// Assert.assertEquals(CustomerJsonData.get("marketing_opt_in_level").toString(),_CP.rs.getString("marketing_opt_in_level").toString());

			}

		}
	}

}