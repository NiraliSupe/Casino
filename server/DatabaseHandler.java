package server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseHandler {
	private static Connection con = null;
    private static Statement stmt = null;
    private static PreparedStatement preparedStatement = null;
    private static String userName = null;
    private static int availableAmt = 0;
    private static ArrayList<String> userNameList = new ArrayList<String>();
    private static ArrayList<String> blackJackList = new ArrayList<String>();
    private static ArrayList<String> slotMachineList = new ArrayList<String>();
    private static ArrayList<String> pokerList = new ArrayList<String>();
    
    public static ArrayList<String> getBlackJackList() {
		return blackJackList;
	}

	public static void setBlackJackList(ArrayList<String> blackJackList) {
		DatabaseHandler.blackJackList = blackJackList;
	}

	public static ArrayList<String> getSlotMachineList() {
		return slotMachineList;
	}

	public static void setSlotMachineList(ArrayList<String> slotMachineList) {
		DatabaseHandler.slotMachineList = slotMachineList;
	}

	public static ArrayList<String> getPokerList() {
		return pokerList;
	}

	public static void setPokerList(ArrayList<String> pokerList) {
		DatabaseHandler.pokerList = pokerList;
	}
    
    public static String getUserName() {
		return userName;
	}
    
	public static void setUserName(String userName) {
		DatabaseHandler.userName = userName;
	}

	public static int getAvailableAmt() {
		return availableAmt;
	}
	
	public static void setAvailableAmt(int availableAmt) {
		DatabaseHandler.availableAmt = availableAmt;
	}

	public static ArrayList<String> getUserNameList() {
		return userNameList;
	}
	
// Connect to database.	
	public static void initialize() throws ClassNotFoundException, SQLException{
    	Class.forName("org.sqlite.JDBC");
	    con = DriverManager.getConnection("jdbc:sqlite:Casino.db");
    }
// Check whether table exist or not. If not create a table.(Table name used is "USERS")
	public static void createTable(){
    	boolean tableFound = false;
    	try {
    		DatabaseMetaData dbm = con.getMetaData();
	    	ResultSet tables = dbm.getTables(null, null, "%", null);
	    	while(tables.next()){
	    		String tableName = tables.getString(3);
	    		if(tableName.equals("USERS")){
	    			tableFound  = true;
	    		}
	    	}
	    	if(!tableFound){
    			stmt = con.createStatement();
    			String sql = "CREATE TABLE USERS " +
    						"(EMAIL TEXT PRIMARY KEY NOT NULL," +
    						" FIRSTNAME  TEXT NOT NULL, " + 
    						" LASTNAME TEXT NOT NULL, " + 
    						" PASSWORD  TEXT NOT NULL, " +
    						" AMOUNT INT NOT NULL, "+
    						" SALT  TEXT NOT NULL )"; 
    			stmt.executeUpdate(sql);
    			stmt.close();
 	    	}
	    }catch ( Exception e ) {
	    	    System.exit(0);
	    }
    }
   
// Check whether user has registered or not.
// If registered, check for user's username and password. 
// If matches, return 200 (successful) else throw error message.
    public static String checkIn(String passwordToHash, String email){
    	 	if(userNameList.indexOf(email) >= 0){
	        	return "Already Signed In";	        	
	        }
    	    String selectSQL = "SELECT * FROM USERS WHERE EMAIL = ?";
			try {
				preparedStatement = con.prepareStatement(selectSQL);
				preparedStatement.setString(1, email);
				ResultSet rs = preparedStatement.executeQuery();
				if(rs.next()) {
					String securePassword = get_SHA_1_SecurePassword(passwordToHash, rs.getString("SALT"));
					if(!securePassword.equals(rs.getString("PASSWORD"))){
						return "Wrong UserName or Password";						
					}else{
						userName = rs.getString("FIRSTNAME") + " " + rs.getString("LASTNAME") ;
						availableAmt = rs.getInt("AMOUNT");
						userNameList.add(email);
					}
					return "200";
				}else{
					return "Wrong UserName or Password";
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return "Wrong UserName or Password";
			}
    }
 
// Create record for new user.
// Check if Email is already present or not.
// If present, throw message else create the record for new user.
    public static String registerUser(String passwordToHash, String email, String firstName, String lastName){
    	String salt = null;
		try {
			salt = getSalt();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String securePassword = get_SHA_1_SecurePassword(passwordToHash, salt);
        try {
        	preparedStatement = con.prepareStatement("SELECT EMAIL FROM USERS WHERE EMAIL = ?");
       		preparedStatement.setString(1, email);
       		ResultSet rs = preparedStatement.executeQuery();
       		if(rs.next()){
       				return "Email already present.";
       		}else{
       			preparedStatement = con.prepareStatement("insert into USERS values(?,?,?,?,?,?)");
       			preparedStatement.setString(1, email);
       			preparedStatement.setString(2, firstName);
       			preparedStatement.setString(3, lastName);
       			preparedStatement.setString(4, securePassword);
       			preparedStatement.setInt(5, 0);
       			preparedStatement.setString(6, salt);
       			preparedStatement.executeUpdate();
       			userNameList.add(email);
       		}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "temporarily unavailable. Please try again after sometime.";
		}
        return "200";
    }

// Update user's balance.    
    public static String updateAmount(String amount, String email) throws SQLException{
     	try {
        	preparedStatement = con.prepareStatement(" UPDATE USERS SET AMOUNT = ? WHERE EMAIL = ?");
       		preparedStatement.setInt(1, Integer.parseInt(amount));
       		preparedStatement.setString(2, email);
       		preparedStatement.executeUpdate();
       		availableAmt = Integer.parseInt(amount);
       	} catch (SQLException e1) {
			e1.printStackTrace();
			return "temporarily unavailable. Please try again after sometime.";
     	}	
     	return "200";
    }
 
// Get user's balance.    
    public static int getAmount(String email){
    	 String selectSQL = "SELECT * FROM USERS WHERE EMAIL = ?";
			try {
				preparedStatement = con.prepareStatement(selectSQL);
				preparedStatement.setString(1, email);
				ResultSet rs = preparedStatement.executeQuery();
				if(rs.next()) {
					availableAmt = rs.getInt("AMOUNT");
					return availableAmt;
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				return 404;
			}
			return 200;
    }
  
// Close the connection.
    public static void closeSQLCon() throws SQLException{
    	con.close();
    }

// Hash the password.    
    private static String get_SHA_1_SecurePassword(String passwordToHash, String salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    
//Add salt
    private static String getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }
}
