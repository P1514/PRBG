package test;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import general.Backend;
import monitoring.Oversight;

public class BackendTest {

	JSONObject obj, obj1;
	Backend tester;
	String result;
	Oversight o = new Oversight(true);

	public BackendTest() {
		o.run();

	}

	// before running test, import test.db from the test directory
	@Test
	public void resolveRole() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Role", "DESIGNER");
		obj.put("Key", "10");
		tester = new Backend(22, obj);
		System.out.println("Role Test Output: " + tester.resolve().toString());


	}

	@Test
	public void resolveGetTree() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "gettree");
		obj.put("Key", "10");
		tester = new Backend(21, obj);
		System.out.println("Get Tree Test Output: " + tester.resolve().toString());


	}

	@Test
	public void resolveGetTreePss() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();

		obj = new JSONObject();
		obj.put("Op", "gettree");
		obj.put("Pss", "D231-2 PSS");
		obj.put("Key", "10");
		tester = new Backend(21, obj);
		System.out.println("Get Tree Pss Test Output: " + tester.resolve().toString());


	}

	@Test
	public void resolveTopreachglobalsentiment() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "Top5Reach");
		obj.put("Key", "10");
		tester = new Backend(20, obj);
		System.out.println("Top Reach Test Output: " + tester.resolve().toString());

	}

	@Test
	public void resolveLoad() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Key", "10");
		tester = new Backend(2, obj);
		System.out.println("Load Test Output: " + tester.resolve().toString());


	}

	@Test
	public void resolveGetposts() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Key", "10");
		obj.put("Op", "getposts");
		obj.put("Id", "838");
		obj.put("Product", "Morris Ground 1");
		tester = new Backend(4, obj);
		System.out.println("Get Posts Test Output: " + tester.resolve().toString());


	}

	@Test
	public void resolveGetmodels() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "getmodels");
		obj.put("Key", "10");

		tester = new Backend(5, obj);
		System.out.println("Get Models Test Output: " + tester.resolve().toString());


	}

	@Test
	public void resolveGetconfig() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "getconfig");
		obj.put("Id", "838");
		obj.put("Key", "10");
		tester = new Backend(12, obj);
		System.out.println("Get Config Test Output: " + tester.resolve().toString());

	}

	@Test
	public void resolveGetModel() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Key", "10");
		obj.put("Op", "get_model");
		obj.put("Id", "838");
		tester = new Backend(15, obj);
		System.out.println("Get Model Test Output: " + tester.resolve().toString());

	}

	@Test
	public void resolveGetPSS() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "getpss");
		obj.put("Key", "10");
		tester = new Backend(17, obj);
		System.out.println("Get Pss Test Output: " + tester.resolve().toString());

	}

	@Test
	public void resolveOpinionExtraction() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "opinion_extraction");
		obj.put("Id", "838");
		obj.put("Key", "10");
		tester = new Backend(18, obj);
		System.out.println("Opinio Extraction Test Output: " + tester.resolve().toString());

	}

	@Test
	public void resolveOeRefresh() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		// obj.put("Param", "Global");
		// obj.put("Values", "");
		obj.put("Filter", "");
		obj.put("Id", "838");
		obj.put("Key", "10");
		tester = new Backend(19, obj);
		System.out.println("Oe Refresh Test Output: " + tester.resolve().toString());

	}

	@Test
	public void resolveCreateModel() throws JSONException {

		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "create_model");
		obj.put("Key", "10");
		obj.put("PSS", "D522-1 PSS");
		obj.put("Products", "Morris Ground 1;");
		obj.put("Archive", "false");
		obj.put("User", "1");
		obj.put("Final_Products", "Morris Ground 1;");
		obj.put("Update", "1");
		obj.put("URI", "Facebook,abcd;");
		obj.put("Start_date", "1970-01-02");
		obj.put("Name", "guffiss");
		obj.put("mediawiki", "true");
		obj.put("Key", "10");
		tester = new Backend(14, obj);
		System.out.println("Create Model Test Output: " + tester.resolve().toString());

	}

	@Test
	public void resolveUpdateModel() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "update_model");
		obj.put("PSS", "D522-1 PSS");
		obj.put("Products", "Morris Ground 1;");
		obj.put("Archive", "false");
		obj.put("User", "1");
		obj.put("Final_Products", "Morris Ground 1;");
		obj.put("Update", "10");
		obj.put("URI", "Facebook,adidas;");
		obj.put("Start_date", "1970-01-02");
		obj.put("Name", "D522-1 PSS");
		obj.put("Id", "838");
		obj.put("mediawiki", "true");
		obj.put("Key", "10");
		tester = new Backend(16, obj);
		System.out.println("Update Model Test Output: " + tester.resolve().toString());

	}

	/*
	 * @Test public void resolveClean() throws JSONException {
	 * 
	 * obj = new JSONObject(); tester = new Backend(7, obj); result =
	 * "[{\"Op\":\"Error\",\"Message\":\"Cleaned Successfully\"}]";
	 * assertEquals("Should be equal to the string", result, tester.resolve());
	 * }
	 */

	@Test
	public void resolveOeRefreshExtrapolate() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "oe_refresh");
		obj.put("Param", "Age,Gender,Location,");
		obj.put("Values", "All,All,All,");
		obj.put("Filter", "Product");
		obj.put("Id", "856");
		obj.put("Extrapolate", 1);
		obj.put("Key", "10");
		tester = new Backend(19, obj);
		System.out.println("Extrapolate Test Output: " + tester.resolve().toString());

	}

	@Test
	public void resolvePrediction() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "Prediction");
		obj.put("Products", "14;15");
		obj.put("Services", "14;15");
		obj.put("Key", "10");
		tester = new Backend(23, obj);
		System.out.println("Prediction Test Output: " + tester.resolve().toString());

	}

	@Test
	public void resolveTopreachglobalsentiment5pss() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "Top5Reach");
		obj.put("PSS", "D522-1 PSS;D231-1 PSS;D522-1 PSS;D522-1 PSS;D522-1 PSS;D522-1 PSS");
		obj.put("Key", "10");
		tester = new Backend(20, obj);
		System.out.println("5pss Test Output: " + tester.resolve().toString());


	}

	@Test
	public void resolveSnapshotPrediction() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "Snapshot");
		obj.put("Products", "13;14;15");
		obj.put("name", "test01");
		obj.put("creation_date", "2017-01-28T16:37:01.466Z");
		obj.put("timespan", "6");
		obj.put("type", "Prediction");
		obj.put("user", "testee3e");
		obj.put("Key", "10");
		tester = new Backend(24, obj);
		System.out.println("Snapshot Prediction Test Output: " + tester.resolve().toString());


	}

	@Test
	public void resolveSnapshotLoadNames() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "load_snapshot");
		obj.put("Type", "Prediction");
		obj.put("Key", "10");
		tester = new Backend(25, obj);
		System.out.println("Snapshot Load Names Test Output: " + tester.resolve().toString());


	}

	@Test
	public void resolveSnapshotLoad() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "load_snapshot");
		obj.put("Name", "testing");
		obj.put("Key", "10");
		tester = new Backend(25, obj);
		System.out.println("Snapshot Load Test Output: " + tester.resolve().toString());


	}

	@Test
	public void resolveSnapshotExtraction() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "Snapshot");
		obj.put("name", "test123");
		obj.put("creation_date", "2017-01-28T16:37:01.466Z");
		obj.put("timespan", "6");
		obj.put("Id", "839");
		obj.put("type", "Extraction");
		obj.put("user", "test");
		obj.put("Key", "10");
		tester = new Backend(24, obj);
		System.out.println("Snapshot Extraction Test Output: " + tester.resolve().toString());
	}

	@Test
	public void resolveSnapshotLoadExtraction() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "load_snapshot");
		obj.put("Name", "test123");
		obj.put("Type", "all");
		obj.put("Key", "10");
		tester = new Backend(25, obj);
		System.out.println("Snapshot Extraction Load Test Output: " + tester.resolve().toString());


	}

	@Test
	public void resolveGetpostsWord() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "getposts");
		obj.put("Id", "838");
		obj.put("Product", "Morris Ground 1");
		obj.put("word", "average");
		obj.put("Key", "10");
		tester = new Backend(4, obj);
		System.out.println("Get Posts Word Test Output: " + tester.resolve().toString());

	}

	@Test
	public void resolveWiki() throws JSONException {

		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "get_mediawiki");
		obj.put("PSS", "pss1");
		obj.put("Key", "10");
		tester = new Backend(28, obj);
		System.out.println("Wiki Test Output: " + tester.resolve().toString());

	}

	@Test
	public void resolveIgnoreWord() throws JSONException {

		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		obj = new JSONObject();
		obj.put("Op", "set_ignore_word");
		obj.put("id", "838");
		obj.put("Word", "phenomenal");
		obj.put("Key", "10");
		tester = new Backend(27, obj);
		System.out.println("Ignore Word test Output: " + tester.resolve().toString());

	}

	@Test
	public void resolveTagcloud() throws JSONException {
		obj1 = new JSONObject("{\"Role\":\"DEVELOPER\",\"Op\":\"getrestrictions\",\"Key\":\"10\"}");
		new Backend(22, obj1).resolve();
		//System.out.println(obj1.toString());

		obj = new JSONObject();
		obj.put("Op", "tagcloud");
		obj.put("User", "4");
		obj.put("Id", "838");
		obj.put("Key", "10");

		tester = new Backend(26, obj);
		System.out.println("Tag Cloud Test Output: " + tester.resolve().toString());

	}
}
