package extraction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import general.Backend;
import general.Data;
import general.Model;
import general.Settings;

public class Snapshot {
	private Connection cnlocal;
	private static final Logger LOGGER = Logger.getLogger(Data.class.getName());

	public Snapshot() {

	}

	public void create(String name, long date, int timespan, String user, String type, String result) {
		try {
			dbconnect();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "ERROR", e);
			return;
		}
		String insert = new String("Insert into " + "snapshots(name,creation_date,creation_user,result,type,timespan)"
				+ " values (?,?,?,?,?,?)");
		try (PreparedStatement query1 = cnlocal.prepareStatement(insert)) {
			query1.setString(1, name);
			query1.setLong(2, date);
			query1.setString(3, user);
			query1.setString(4, result);
			query1.setString(5, type);
			query1.setInt(6, timespan);
			query1.execute();

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "ERROR", e);
		}
		try {
			if (cnlocal != null)
				cnlocal.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void prediction(String name, String date, int timespan, String user, String products, String services) {
		String result;
		JSONObject obj = new JSONObject();
		long cdate;
		try {
			obj.put("Op", "Prediction");
			if(products!="")
			obj.put("Products", products);
			if(services!="")
			obj.put("Services", services);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dateaux = null;
		try {
			dateaux = df.parse(date);
		} catch (ParseException e) {
			LOGGER.log(Level.SEVERE, "ERROR Parsing Date from Browser", e);
		}
		cdate = dateaux.getTime();
		if (cdate < 0) {
			LOGGER.log(Level.SEVERE, "ERROR BAD DATE");
			return;
		}

		Backend b = new Backend(23, obj);
		result = b.resolve();

		create(name, cdate, timespan, user, "prediction", result);

	}

	private void dbconnect() throws ClassNotFoundException, SQLException {
		cnlocal = Settings.connlocal();
	}
	
	public JSONArray loadNames(String type) {
		JSONArray result = new JSONArray();
		JSONArray aux = new JSONArray();
		JSONObject obj = new JSONObject();
		ResultSet rs;
		try {
			dbconnect();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "ERROR", e);
			return null;
		}
		String insert = new String("SELECT name FROM sentimentanalysis.snapshots where type=?;");
		try (PreparedStatement query1 = cnlocal.prepareStatement(insert)) {
			query1.setString(1, type);
			rs=query1.executeQuery();
			//rs.next();//verify
			for (int i = 0; rs.next(); i++) {
				 obj.put("Name",  rs.getString("name"));
				 aux.put(obj);
			}
			result.put("Snapshots");
			result.put(aux);
			//System.out.println("****Names:"+result.toString());

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "ERROR", e);
		}
		try {
			if (cnlocal != null)
				cnlocal.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}
	
	public JSONArray load(String name) {
		JSONArray result = new JSONArray();
		ResultSet rs;
		try {
			dbconnect();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "ERROR", e);
			return null;
		}
		String insert = new String("SELECT result FROM sentimentanalysis.snapshots where name=?;");
		try (PreparedStatement query1 = cnlocal.prepareStatement(insert)) {
			query1.setString(1, name);
			rs=query1.executeQuery();
			rs.next();
			result.put(rs.getString("result"));
			System.out.println("****Names:"+result.toString());

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "ERROR", e);
		}
		try {
			if (cnlocal != null)
				cnlocal.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}



}