package general;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.fabric.xmlrpc.base.Array;
import com.sun.media.jfxmedia.logging.Logger;
import monitoring.Monitor;;

// TODO: Auto-generated Javadoc
/**
 * The Class Model.
 */
public final class Model {

	private long id, pss, design_project;
	private long frequency, user;
	private String name, uri, age, gender, products;
	private boolean archived;
	private long nextupdate, cdate;
	private Boolean add_mediawiki = false;
	private static final java.util.logging.Logger LOGGER = new Logging().create(Model.class.getName());

	/**
	 * Instantiates a new model.
	 *
	 * @param _id
	 *            the id of the model
	 * @param _frequency
	 *            the frequency of update
	 * @param _user
	 *            the user id
	 * @param _name
	 *            the name of the model
	 * @param _uri
	 *            the uri of the source and account list
	 *            Example:"facebook,shop;twitter,run"
	 * @param _pss
	 *            the pss id
	 * @param _age
	 *            the age range wanted
	 * @param _gender
	 *            the gender wanted
	 * @param _products
	 *            the products wanted
	 * @param _archived
	 *            the archived if deleted or not
	 */
	public Model(long _id, long _frequency, long _user, String _name, String _uri, Long _pss, String _age,
			String _gender, String _products, Boolean _archived, long _created_date, long _nextupdate,
			long design_project, Boolean _add_mediawiki) {
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
		this.cdate = _created_date;
		this.nextupdate = _nextupdate;
		this.design_project = design_project;
		this.add_mediawiki = _add_mediawiki;
	}

	/**
	 * Instantiates a new model.
	 */
	public Model() {
	}

	/**
	 * Adds the model.
	 *
	 * @param msg
	 *            the msg
	 * @return the JSON array with information if successful or not
	 * @throws JSONException
	 *             the JSON exception
	 * @throws UnsupportedEncodingException 
	 */
	public JSONArray add_model(JSONObject msg) throws JSONException {
		// TODO Verify data that exists in sources to be updated
		JSONArray result = new JSONArray();
		JSONObject obj = new JSONObject();
		name = StringEscapeUtils.escapeHtml4(msg.getString("Name"));
		uri = msg.has("URI") ? StringEscapeUtils.unescapeHtml4(msg.getString("URI")) : "";
		//System.out.println(StringEscapeUtils.unescapeHtml4(msg.getString("PSS")));
		pss = Data.identifyPSSbyname(StringEscapeUtils.unescapeHtml4(msg.getString("PSS")));
		frequency = msg.getInt("Update");
		archived = msg.getBoolean("Archive");
		if (msg.has("mediawiki") && msg.getBoolean("mediawiki")) {
			add_mediawiki = true;
		}
		if (msg.has("design_project"))
			design_project = msg.getLong("design_project");
		else
			design_project = 0;
		if (msg.has("Start_date")) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			try {
				date = df.parse(msg.getString("Start_date"));
			} catch (ParseException e) {
				LOGGER.log(Level.SEVERE, "Error Parsing Date from Browser", e);
			}
			cdate = date.getTime();
			if (cdate < 0) {
				obj.put("Op", "Error");
				obj.put("Message", "Bad Date");
				result.put(obj);
				return result;

			}
		} else {
			cdate = System.currentTimeMillis();
		}
		String[] productsbyname = msg.has("Final_Products") ? msg.getString("Final_Products").split(";") : null;
		products = "";
		Product product = null;
		PSS pss1 = null;
		for (String a : productsbyname) {
			product = null;
			pss1 = null;
			for (Product product2 : Data.dbproductall())
				if (product2.get_Name().equals(a)) {
					product = product2;
					break;
				}
			for (PSS pss2 : Data.dbpssall()) {
				if (pss2.getID() == pss) {
					pss1 = pss2;
					break;
				}
			}
			if (product == null || pss1 == null)
				continue;
			if (Data.identifyPSSbyproduct(product.get_Id()) != pss1.getID())
				continue;
			products += product.get_Id() + ",";
		}
		// products = msg.getString("Final_Product");
		user = msg.has("User") ? msg.getInt("User") : 0;
		nextupdate = cdate;
		// age = msg.getString("Age");
		// gender = msg.getString("Gender");

		String insert = "Insert into " + Settings.lmtable + "(" + Settings.lmtable_name + "," + Settings.lmtable_uri
				+ "," + Settings.lmtable_pss + "," + Settings.lmtable_update + "," + Settings.lmtable_archived + ","
				+ Settings.lmtable_monitorfinal + "," + Settings.lmtable_creator + "," + Settings.lmtable_cdate + ","
				+ Settings.lmtable_udate + "," + Settings.lmtable_add_mediawiki + ","
				+ Settings.lmtable_designproject /*
													 * + "," + Settings.lmtable_age + "," + Settings.lmtable_gender
													 */
				+ ") values (?,?,?,?,?,?,?,?,?,?,?"/* ,?,? */ + ")";
		try (Connection cnlocal = Settings.connlocal();
				PreparedStatement query1 = cnlocal.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)) {
			query1.setString(1, name);
			query1.setString(2, uri);
			query1.setLong(3, pss);
			query1.setLong(4, frequency);
			query1.setBoolean(5, archived);
			query1.setString(6, products);
			query1.setLong(7, user);
			query1.setLong(8, cdate);
			query1.setLong(9, nextupdate);
			if (msg.has("mediawiki"))
				query1.setBoolean(10, msg.getBoolean("mediawiki"));
			else
				query1.setBoolean(10, false);
			query1.setLong(11, design_project);

			// query1.setString(8, age);
			// query1.setString(9, gender);
			query1.executeUpdate();
			try(ResultSet generatedKeys = query1.getGeneratedKeys()){
			if (generatedKeys.next())
				id = generatedKeys.getLong(1);
			obj.put("id", id);
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			LOGGER.log(Level.INFO,"Model name Already Exists");
			obj.put("Op", "Error");
			obj.put("Message", "Model name Already Exists");
			result.put(obj);
			return result;

		} catch (SQLException e) {
			LOGGER.log(Level.INFO, "Error adding model to DB");
			obj.put("Op", "Error");
			obj.put("Message", "Error adding model to DB");
			result.put(obj);
			return result;
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, "No Class Found");
		}

		obj.put("Op", "Error2");
		obj.put("Message", "Successfully added model " + name + " to monitor module");
		result.put(obj);
		if (!"".equals(uri))
			Monitor.update(uri, pss);
		return result;

	}

	/**
	 * Update model.
	 *
	 * @param msg
	 *            the msg query from the front_end
	 * @return the JSON array
	 * @throws JSONException
	 *             the JSON exception
	 */
	public JSONArray update_model(JSONObject msg) throws JSONException {
		JSONArray result = new JSONArray();
		JSONObject obj = new JSONObject();
		String uri = msg.has("URI") ? msg.getString("URI") : "";
		Boolean delete = msg.has("Archive") ? msg.getBoolean("Archive") : false;
		int rangeindex = 1;
		if (!msg.get("Name").equals(this.name)
				|| Data.getpss(Data.identifyPSSbyname(msg.getString("PSS"))).getID() != this.pss) {
			obj.put("id", msg.getInt("Id"));
			obj.put("Op", "Error");
			obj.put("Message", "Error updating model " + msg.getString("Name") + "updated attempt not allowed");
			result.put(obj);
			return result;
		}
		String product = "";

		String insert = "Update " + Settings.lmtable
				+ " Set "/*
							 * + Settings.lmtable_age + "=?, " + Settings.lmtable_gender + "=?, "
							 */ + Settings.lmtable_archived + "=? "
				+ (delete ? ""
						: ", " + Settings.lmtable_monitorfinal + "=?, " + Settings.lmtable_uri + "=?, "
								+ Settings.lmtable_update + "=?, " + Settings.lmtable_add_mediawiki + "=? ")
				+ "Where " + Settings.lmtable_id + "=? ";
		try (Connection cnlocal = Settings.connlocal();
			PreparedStatement query1 = cnlocal.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)){
			query1.setBoolean(rangeindex++, msg.getBoolean("Archive"));

			if (msg.has("Final_Products")) {
				if ("".equals(msg.getString("Final_Products"))) {
					product = "";
				} else {
					for (String a : msg.getString("Final_Products").split(";")) {
						product += Data.identifyProduct(a) + ",";
					}
				}
			}

			if (!delete) {
				query1.setString(rangeindex++, products);
				query1.setString(rangeindex++, uri);
				query1.setInt(rangeindex++, msg.getInt("Update"));
				if (msg.has("mediawiki")) {
					query1.setBoolean(rangeindex++, true);
				} else
					query1.setBoolean(rangeindex++, false);
			}

			query1.setInt(rangeindex++, msg.getInt("Id"));
			// query1.setString(1, msg.getString("Age"));
			// query1.setString(2, msg.getString("Gender"));
			// System.out.println(query1);
			query1.execute();
		} catch (SQLException | ClassNotFoundException e) {
			LOGGER.log(Level.INFO,"Error adding model to DB");
			obj.put("Op", "Error");
			obj.put("Message", "Error adding model to DB");
			result.put(obj);
			return result;
		}

		this.uri = uri;
		this.frequency = msg.getInt("Update");
		this.archived = msg.getBoolean("Archive");
		this.products = "".equals(product) ? "" : product;

		obj.put("id", msg.getInt("Id"));
		obj.put("Op", "Error");
		obj.put("Message", "Successfully updated model " + msg.getString("Name"));
		result.put(obj);
		if (!"".equals(uri))
			Monitor.update(uri, pss);
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

	public Long getDate() {
		return this.cdate;
	}

	public Long getLastUpdate() {
		return this.nextupdate - (this.frequency * 86400000);
	}

	public Long getUpdate() {
		return this.nextupdate;
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
		if (this.products.isEmpty())
			return "";
		return this.products.substring(0, this.products.length() - 1);
	}

	public long getProject() {
		return this.design_project;
	}

	public Boolean getMediawiki() {
		return this.add_mediawiki;
	}

	public ArrayList<String> getSources(boolean all) {
		String[] sources = this.uri.split(";");
		ArrayList<String> result = new ArrayList<>();
		if("".equals(sources[0])) return result;
		for (String s : sources) {
			result.add(s.split(",")[0]);
		}
		if (this.add_mediawiki && all) {
			result.add("mediawiki");
		} 
		return result;
	}

	public ArrayList<String> getAccounts(boolean all) {
		String[] accounts = this.uri.split(";");
		ArrayList<String> result = new ArrayList<>();
		if("".equals(accounts[0])) return result;
		for (String s : accounts) {
			String[] temp = s.split(",");
			if (temp.length > 1)
				result.add(s.split(",")[1]);
		}
		if (this.add_mediawiki && all) {
			result.add("mediawiki");
		}
		return result;
	}
}
