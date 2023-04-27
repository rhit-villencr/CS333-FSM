package fsm.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Base64;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;

public class UserService {
	private static final Random RANDOM = new SecureRandom();
	private static final Base64.Encoder enc = Base64.getEncoder();
	private static final Base64.Decoder dec = Base64.getDecoder();
	private DatabaseConnectionService dbService = null;

	public UserService(DatabaseConnectionService dbService) {
		this.dbService = dbService;

	}

	public boolean useApplicationLogins() {
		return true;
	}

	public boolean login(String username, String password) {
		// TODO: Complete this method.
		Connection con = this.dbService.getConnection();

	    try {
	        String query = "SELECT PasswordSalt, PasswordHash\nFROM [User]\nWHERE Username = ?";
	        PreparedStatement checkPass = con.prepareStatement(query);
	        checkPass.setString(1, username);
	        ResultSet rs = checkPass.executeQuery(); 
	        
	        if (rs.next()) { 
	            byte[] testSalt = dec.decode(rs.getString("PasswordSalt"));
	            String testHash = rs.getString("PasswordHash");
	            String hashedSalt = hashPassword(testSalt, password);
	            if (!testHash.equals(hashedSalt)) {
	                JOptionPane.showMessageDialog(null, "Login Failed");
	                return false;
	            }
	            return true;
	        } else { 
	            JOptionPane.showMessageDialog(null, "Login Failed");
	            return false;
	        }
	    } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Login Error");
	        e.printStackTrace();
	        return false;
	    }

	}

	public boolean register(String username, String password) {
		// TODO: Task 6
		byte[] newSalt = getNewSalt();
		String passHash = hashPassword(newSalt, password);
		try {
			CallableStatement cs = this.dbService.getConnection().prepareCall("{? = call Register(?, ?, ?)}");
			cs.registerOutParameter(1, Types.INTEGER);

			cs.setString(2, username);
			cs.setString(3, getStringFromBytes(newSalt));
			cs.setString(4, passHash);

			cs.execute();
			int returnValue = cs.getInt(1);

			if (returnValue == 4) {
				JOptionPane.showMessageDialog(null, "ERROR: Username already exists.");
				return false;
			} else if (returnValue == 3) {
				JOptionPane.showMessageDialog(null, "PasswordHash cannot be null or empty.");
				return false;
			} else if (returnValue == 2) {
				JOptionPane.showMessageDialog(null, "PasswordSalt cannot be null or empty.");
				return false;
			} else if (returnValue == 1) {
				JOptionPane.showMessageDialog(null, "Username cannot be null or empty.");
				return false;
			}
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Add User not working.");
			return false;

		}
	}

	public byte[] getNewSalt() {
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return salt;
	}

	public String getStringFromBytes(byte[] data) {
		return enc.encodeToString(data);
	}

	public String hashPassword(byte[] salt, String password) {

		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory f;
		byte[] hash = null;
		try {
			f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			hash = f.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			JOptionPane.showMessageDialog(null, "An error occurred during password hashing. See stack trace.");
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			JOptionPane.showMessageDialog(null, "An error occurred during password hashing. See stack trace.");
			e.printStackTrace();
		}
		return getStringFromBytes(hash);
	}

}
