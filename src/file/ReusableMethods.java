package file;

import io.restassured.path.json.JsonPath;

public class ReusableMethods {
	
	
	public static JsonPath rawToJson(String response) {
		
		JsonPath jP = new JsonPath(response);
		return jP;
		
	}

}
