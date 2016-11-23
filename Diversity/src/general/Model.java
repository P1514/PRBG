package general;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import monitoring.Monitor;;

// TODO: Auto-generated Javadoc
/**
 * The Class Model.
 */
public final class Model {
	
	private Connection cnlocal;
	private long id = 0, pss;
	private long frequency, user;
	private String name, uri, age, gender, products;
	private boolean archived;

	/**
	 * Instantiates a new model.
	 *
	 * @param _id the id of the model
	 * @param _frequency the frequency of update
	 * @param _user the user id
	 * @param _name the name of the model
	 * @param _uri the uri of the source and account list Example:"facebook,shop;twitter,run"
	 * @param _pss the pss id
	 * @param _age the age range wanted
	 * @param _gender the gender wanted
	 * @param _products the products wanted
	 * @param _archived the archived if deleted or not
	 */
	public Model(long _id, long _frequency, long _user, String _name, String _uri, Long _pss, String _age,
			String _gender, String _products, Boolean _archived) {
		this.id = _id;
		this.frequency = _frequency;
		this.user = _user;
		this.name = _name;
		this.uri = _uri;
		this.pss = _pss;
		this.age = _age;
		this.gender = _gender;
		this.products = _products;
		this.archived = _archived;
	}

	/**
	 * Instantiates a new model.
	 */
	public Model() {
	}

	/**
	 * Adds the model.
	 *
	 * @param msg the msg
	 * @return the JSON array with information if successful or not
	 * @throws JSONException the JSON exception
	 */
	public JSONArray add_model(JSONObject msg) throws JSONException {
		// TODO Verify data that exists in sources to be updated
		JSONArray result = new JSONArray();
		JSONObject obj = new JSONObject();
		name = msg.getString("Name");
		uri = msg.getString("URI");
		pss = Data.identifyPSSbyname(msg.getString("PSS"));
		frequency = msg.getInt("Update");
		archived = msg.getBoolean("Archive");
		String[] productsbyname = msg.has("Final_Products") ? msg.getString("Final_Products").split(";") : null;
		products = "";
		Product product=null;
		PSS pss1=null;
		for (String a : productsbyname) {
			product = null;
			pss1=null;
			for (Product product2 : Data.productdb.values())
				if (product2.get_Name().equals(a)) {
					product = product2;
					break;
				}
			for(PSS pss2 : Data.pssdb.values()){
				if(pss2.getID() == pss){
					pss1=pss2;
					break;
				}
			}
			if (product == null || pss1==null)
				continue;
			if (product.get_PSS() != pss1.getID())
				continue;
			products += product.get_Id() + ",";
		}
		// products = msg.getString("Final_Product");
		user = msg.getInt("User");
		// age = msg.getString("Age");
		// gender = msg.getString("Gender");
		dbconnect();

		String insert = "Insert into " + Settings.lmtable + "(" + Settings.lmtable_name + "," + Settings.lmtable_uri
				+ "," + Settings.lmtable_pss + "," + Settings.lmtable_update + "," + Settings.lmtable_archived + ","
				+ Settings.lmtable_monitorfinal + ","
				+ Settings.lmtable_creator /*
											 * + "," + Settings.lmtable_age +
											 * "," + Settings.lmtable_gender
											 */ + ") values (?,?,?,?,?,?,?"/* ,?,? */ + ")";
		PreparedStatement query1 = null;
		try {
			query1 = cnlocal.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);
			query1.setString(1, name);
			query1.setString(2, uri);
			query1.setLong(3, pss);
			query1.setLong(4, frequency);
			query1.setBoolean(5, archived);
			query1.setString(6, products);
			query1.setLong(7, user);
			// query1.setString(8, age);
			// query1.setString(9, gender);
			query1.executeUpdate();
			ResultSet generatedKeys = query1.getGeneratedKeys();
			if (generatedKeys.next())
				id = generatedKeys.getLong(1);
			obj.put("id", id);
		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			obj.put("Op", "Error");
			obj.put("Message", "Model name Already Exists");
			result.put(obj);
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			obj.put("Op", "Error");
			obj.put("Message", "Error adding model to DB");
			result.put(obj);
			return result;
		} finally {
			try {
				if (query1 != null)
					query1.close();
				if (cnlocal != null)
					cnlocal.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		obj.put("Op", "Error2");
		obj.put("Message", "Successfully added model " + name + " to monitor module");
		result.put(obj);

		Monitor.update(msg.getString("URI"));
		return result;

	}

	/**
	 * Update model.
	 *
	 * @param msg the msg query from the front_end
	 * @return the JSON array
	 * @throws JSONException the JSON exception
	 */
	public JSONArray update_model(JSONObject msg) throws JSONException {
		JSONArray result = new JSONArray();
		JSONObject obj = new JSONObject();

		if (!msg.get("Name").equals(this.name) || Data.pssdb.get(Data.identifyPSSbyname(msg.getString("PSS"))).getID() != this.pss) {
			obj.put("id", msg.getInt("Id"));
			obj.put("Op", "Error");
			obj.put("Message", "Error updating model " + msg.getString("Name") + "updated attempt not allowed");
			result.put(obj);
			return result;
		}
		String product=new String();

		String insert = "Update " + Settings.lmtable
				+ " Set "/*
							 * + Settings.lmtable_age + "=?, " +
							 * Settings.lmtable_gender + "=?, "
							 */ + Settings.lmtable_archived + "=?, " + Settings.lmtable_monitorfinal + "=?, "
				+ Settings.lmtable_uri + "=?, " + Settings.lmtable_update + "=? Where " + Settings.lmtable_id + "=?";
		PreparedStatement query1 = null;
		try {
			dbconnect();
			query1 = cnlocal.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);
			query1.setString(3, msg.getString("URI"));
			query1.setInt(4, msg.getInt("Update"));
			query1.setBoolean(1, msg.getBoolean("Archive"));
			
			if(msg.has("Final_Products")){
				for (String a : msg.getString("Final_Products").split(";")){
					product+=Data.identifyProduct(a)+",";
				}
			}
			query1.setString(2, products);
			query1.setInt(5, msg.getInt("Id"));
			// query1.setString(1, msg.getString("Age"));
			// query1.setString(2, msg.getString("Gender"));
			System.out.println(query1);
			query1.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			obj.put("Op", "Error");
			obj.put("Message", "Error adding model to DB");
			result.put(obj);
			return result;
		} finally {
			try {
				if (query1 != null)
					query1.close();
				if (cnlocal != null)

					cnlocal.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		this.uri = msg.getString("URI");
		this.frequency = msg.getInt("Update");
		this.archived = msg.getBoolean("Archive");
		this.products = product.equals("") ? "," : product;

		obj.put("id", msg.getInt("Id"));
		obj.put("Op", "Error");
		obj.put("Message", "Successfully updated model " + msg.getString("Name"));
		result.put(obj);
		Monitor.update(msg.getString("URI"));
		return result;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the pss.
	 *
	 * @return the pss
	 */
	public Long getPSS() {
		return this.pss;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Gets the archived.
	 *
	 * @return the archived
	 */
	public boolean getArchived() {
		return this.archived;
	}

	/**
	 * Gets the age.
	 *
	 * @return the age
	 */
	public String getAge() {
		return this.age;
	}

	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	public String getURI() {
		return this.uri;
	}

	/**
	 * Gets the frequency.
	 *
	 * @return the frequency
	 */
	public long getFrequency() {
		return this.frequency;
	}

	/**
	 * Gets the gender.
	 *
	 * @return the gender
	 */
	public String getGender() {
		return this.gender;
	}

	/**
	 * Gets the products.
	 *
	 * @return the products
	 */
	public String getProducts() {
		if(this.products.isEmpty()) return null;
		return this.products.substring(0, this.products.length()-1);
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
