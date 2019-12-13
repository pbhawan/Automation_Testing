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

public class Theme {
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
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls",WH.Theme);

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
	public void VerifyThemeData() throws IOException, InterruptedException, SQLException, ParseException {
		System.out.println("<------------- Theme Verification Started ------------->");
		for (Object record : JsonRecords)
		{		
         	@SuppressWarnings("unchecked")
			Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			map.put("id", new Date().getTime());
			map.put("updated_at", new Date());
			map.put("created_at", new Date());
			JSONObject ThemeJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Theme);
			TimeUnit.SECONDS.sleep(20);
			_CP = DB.GetRecordFromDB(ThemeJsonData,"Theme");
			if (_CP.rs.next()) {
				try {
					Assert.assertEquals(ThemeJsonData.get("name").toString(),_CP.rs.getString("name").toString(), "Name not Match in Row" + Iteration);					
					Assert.assertEquals(ThemeJsonData.get("role").toString(),_CP.rs.getString("role").toString(), "role not Match in Row" + Iteration);
					Assert.assertEquals(ThemeJsonData.get("theme_store_id").toString(),_CP.rs.getString("theme_store_id").toString(),"theme_store_id not Match in Row" + Iteration);
					Assert.assertEquals(ThemeJsonData.get("previewable").toString(),_CP.rs.getString("previewable").toString(),"previewable not Match in Row" + Iteration);
					Assert.assertEquals(ThemeJsonData.get("processing").toString(),_CP.rs.getString("processing").toString(),"processing not Match in Row" + Iteration);
					Assert.assertEquals(ThemeJsonData.get("updated_by").toString(),_CP.rs.getString("updated_by").toString(),"created_at not Match in Row" + Iteration);
					Assert.assertEquals(ThemeJsonData.get("version").toString(),_CP.rs.getString("version").toString(),"version not Match in Row" + Iteration);
					Assert.assertEquals(ThemeJsonData.get("admin_graphql_api_id").toString(),_CP.rs.getString("admin_graphql_api_id").toString(),"admin_graphql_api_id not Match in Row" + Iteration);
					
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration,WH.Theme,ex.getMessage());
					continue;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration,WH.Theme);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration,WH.Theme,null);
			}

		}
		System.out.println("<------------- Theme Verification Started ------------->");
	}

}
