package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import importDB.Data;
import importDB.Model;

public class GetComments {

	private Settings dbc = new Settings();
	private Connection cnlocal;

	public GetComments() {
	}

	public JSONArray getAll(JSONObject msg) throws JSONException {
		JSONArray result = new JSONArray();
		String[] pre_result = new String[50];
		String[] genders = Settings.genders.split(",,");
		JSONObject obj = new JSONObject();
		obj.put("Op", "comments");
		result.put(obj);
		String insert = new String();
		int[] topid = new int[50];
		PreparedStatement query1 = null;
		Model model = Data.modeldb.get(msg.getLong("Id"));
		int n_tops = 0;
		insert = "Select name,influence,location,gender,age,polarity,message from posts,authors where ( posts.id=? OR (age>=? AND age<=? ";
		ResultSet rs = null;
		if (model.getGender() == "All") {
			insert += "AND (";
			for (int i = 0; i < genders.length; i++) {
				insert += i == 0 ? "" : "OR";
				insert += "gender=? ";
			}
			insert += ") ";
		}else{
			insert += "AND gender=?) ";
		}
		insert += ") AND(opinions_id=? AND posts.authors_id=authors.id) ORDER BY posts.id ASC";
		try {
			dbconnect();
			query1 = cnlocal.prepareStatement(insert);
			int i = 0;
			query1.setString(1, msg.getString("Values"));
			query1.setString(2, model.getAge().split(",")[0]);
			query1.setString(3, model.getAge().split(",")[1]);
			if (model.getGender() == "All") {
				for (i = 0; i < genders.length; i++)
					query1.setString(3 + i + 1, genders[i]);
			}else{
				query1.setString(3+1+i, model.getGender());
				i++;
			}
			query1.setInt(3 + i + 1, msg.getInt("Values"));
			System.out.print(query1);

			rs = query1.executeQuery();

			for (i = 0; rs.next(); i++) {
				n_tops++;
				pre_result[i] = rs.getString("name") + ",," + rs.getDouble("influence") + ",,"
						+ rs.getString("location") + ",," + rs.getString("gender") + ",," + rs.getInt("age") + ",,";
				pre_result[i] += rs.getDouble("polarity") + ",,";
				pre_result[i] += rs.getString("message");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			;
			try {
				if (query1 != null)
					query1.close();
			} catch (Exception e) {
			}
			;
			try {
				if (cnlocal != null)
					cnlocal.close();
			} catch (Exception e) {
			}
			;
		}

		for (int i = 0; i < n_tops; i++) {
			obj = new JSONObject();
			String[] pre_results = pre_result[i].split(",,");
			obj.put("Name", pre_results[0]);
			obj.put("Influence", trunc(pre_results[1]));
			obj.put("Location", pre_results[2]);
			obj.put("Gender", pre_results[3]);
			obj.put("Age", pre_results[4]);
			obj.put("Polarity", trunc(pre_results[5]));
			obj.put("Message", pre_results[6]);
			result.put(obj);

		}
		System.out.print(result);

		return result;

	}

	private String trunc(String number) {
		double result = 0;
		try {

			result = Double.valueOf(number);
			number = String.format("%.2f", result);
			result = Double.parseDouble(number);

		} catch (Exception e) {
			number = number.replaceAll(",", ".");
			result = Double.parseDouble(number);

		}
		return Double.toString(result);

	}

	private void dbconnect() {
		try {
			cnlocal = Settings.connlocal();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
