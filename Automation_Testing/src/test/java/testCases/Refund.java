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
			map.put("id", new Date().getTime());
			map.put("processed_at", new Date());
			map.put("created_at", new Date());
			@SuppressWarnings("static-access")
			JSONObject RefundJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Refund);
			TimeUnit.SECONDS.sleep(10);
			_CP = DB.GetRecordFromDB(RefundJsonData,"Refund");
			if (_CP.rs.next()) {
				try {
					Assert.assertEquals(RefundJsonData.get("note").toString(),
							_CP.rs.getString("note").toString(), "note not Match in Row" + Iteration);
					
					Assert.assertEquals(RefundJsonData.get("order_adjustments").toString(),
							_CP.rs.getString("order_adjustments").toString(), "order_adjustments not Match in Row" + Iteration);
					
					Assert.assertEquals(RefundJsonData.get("order_id").toString(),
							_CP.rs.getString("order_id").toString(),"order_id not Match in Row" + Iteration);
					
//					Assert.assertEquals(RefundJsonData.get("refund_line_items").toString(),
//jsonarray							_CP.rs.getString("refund_line_items").toString(),"refund_line_items not Match in Row" + Iteration);
					
					Assert.assertEquals(RefundJsonData.get("restock ").toString(),
							_CP.rs.getString("restock ").toString(),"restock  not Match in Row" + Iteration);
					
//					Assert.assertEquals(RefundJsonData.get("transactions ").toString(),
//							_CP.rs.getString("transactions ").toString(),"transactions  not Match in Row" + Iteration);
					
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
