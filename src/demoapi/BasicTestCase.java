package demoapi;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;
import org.testng.annotations.Test;

import file.ReusableMethods;

public class BasicTestCase {

	String api_key = "1ced4cf45ec39ed6ce5366814d1edc0a";
	String requestToken;
	String sessionID;
	String baseURI="https://api.themoviedb.org/3";
	String userName = "Shiwani";
	String password = "Password@1";
			
	int value = 240;
	

	@Test()
	public void getLatest() {

		// Get Latest

		System.out.println("----Here is the code of Get Latest----");

		RestAssured.baseURI = baseURI;
		given().log().all().queryParam("api_key", api_key).queryParam("language", "en-US").when().get("movie/latest")
				.then().log().all().assertThat().statusCode(200);
	}

	@Test()
	public void getPlaying() {
		// Get Now playing

		System.out.println("----Here is the code of Get Now Playing Request----");

		RestAssured.baseURI = baseURI;
		given().log().all().queryParam("api_key", api_key)
		.queryParam("page", "1").queryParam("language", "en-US")
		.when().get("movie/now_playing")
		.then().log().all().assertThat().statusCode(200);
	}

	@Test()
	public void getPopular() {
//Get Popular

		System.out.println("----Here is the code of Get Popular----");

		RestAssured.baseURI = baseURI;
		given().log().all().queryParam("api_key", api_key)
		.queryParam("page", "1").queryParam("language", "en-US")
		.when().get("movie/popular").then().log().all().assertThat().statusCode(200);
	}

	@Test(priority=1)
	public void newRequestToekn() {

		System.out.println("****Create a new request token*****");

		RestAssured.baseURI = baseURI;
		String response = given().log().all()
				.queryParam("api_key", api_key)
				.when().get("authentication/token/new")
				.then().log().all().assertThat().statusCode(200).extract().response().asString();

		JsonPath jP = ReusableMethods.rawToJson(response);
//			JsonPath jP = new JsonPath(response);

		requestToken = jP.getString("request_token");
		System.out.println("Request token is: " + requestToken);

	}

	@Test(priority=2)
	public void authorizeTheRequestToken()

	{
		System.out.println("****Get the user to authorize the request token*****");

		RestAssured.baseURI = baseURI;
		given().log().all()
		.header("Content-Type", "application/json;charset=utf-8")
		.queryParam("api_key", api_key)
		.body("{\r\n" + "  \"username\": \""+userName+"\",\r\n" + "  \"password\": \""+password+"\",\r\n"
						+ "  \"request_token\": \"" + requestToken + "\"\r\n" + "}")
		.when()
		.post("authentication/token/validate_with_login")
		.then().log().all().assertThat()
		.statusCode(200);

	}

	@Test(priority=3)
	public void newSessionIdForAuthorizedUser() {
		System.out.println("**Create a new Session Id with the athorized request token**");
		RestAssured.baseURI = "https://api.themoviedb.org/3";

		String responseSession = given().log().all().header("Content-Type", "application/json;charset=utf-8")
				.queryParam("api_key", api_key)
				.body("{\r\n" + "  \"request_token\": \"" + requestToken + "\"\r\n" + "}").when()
				.post("authentication/session/new").then().log().all().assertThat().statusCode(200).extract().response()
				.asString();

		JsonPath jR = ReusableMethods.rawToJson(responseSession);

//		JsonPath jR = new JsonPath(responseSession);

		sessionID = jR.getString("session_id");
		System.out.println("Session Id is: " + sessionID);

	}

	@Test(priority=4)
	public void getPostMovie() {

		// POST Movie

		System.out.println("----Here is the code of POST Rate Movie----");

		RestAssured.baseURI = "https://api.themoviedb.org/3";
		given().log().all()
		.header("Content-Type", "application/json;charset=utf-8")
		.queryParam("api_key", api_key)
		.queryParam("session_id", sessionID)
		.body("{\r\n" + "  \"value\": 8.5\r\n" + "}")
		.when()
		.post("movie/"+value+"/rating")
		.then().log().all().assertThat().statusCode(201);

	}
	
	@Test(priority=5)
	public void deleteRating() {

		// Delete Session

		System.out.println("----Here is the code of Delete Rating----");

		RestAssured.baseURI = "https://api.themoviedb.org/3";
		given().log().all()
		.queryParam("api_key", api_key)
		.queryParam("session_id", sessionID)
		.when()
		.delete("movie/"+value+"/rating")
		.then().log().all().assertThat().statusCode(200);

	}

}
