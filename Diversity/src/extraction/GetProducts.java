package extraction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import general.Data;
import general.PSS;
import general.Product;

public class GetProducts {
	
	public GetProducts(){
		
		
	}

	
	public static final JSONArray getTree() throws JSONException{
		
		JSONArray result = new JSONArray();
		JSONObject obj = new JSONObject();
		result.put(new JSONObject().put("Op", "Tree"));
		for (PSS pss : Data.pssdb.values()) {
			obj = new JSONObject();
			obj.put("PSS", pss.getName());
			JSONArray sub_products = new JSONArray();
			for (Product product : Data.productdb.values()) {
				if (pss.getID() == product.get_PSS())
					sub_products.put(new JSONObject().put("Name", product.get_Name()));
					// TODO Replace this with recursive mode that can be done until the ammount of products 
					// reaches the end of infinity, and that also doesn't need to iterate over everything
			}
			obj.put("Products", sub_products);
			result.put(obj);
		}
		return result;
		
	}
}