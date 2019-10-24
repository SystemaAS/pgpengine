package no.systema.pgpengine;

import javax.crypto.Cipher;

import com.google.gdata.util.common.util.Base64;
import com.google.gdata.util.common.util.Base64DecoderException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.zip.Inflater;

 

public class PgpDecrypter {

	
	/**
	 * Starting point
	 * 
	 * @param payload
	 * @param privateKey
	 * @throws Exception
	 */
	public void doRun(byte[] payload, PrivateKey privateKey) throws Exception{
			
			byte[] content = decrypt(payload, privateKey);
			//now decompress
			byte[] decom = this.decompressByteArray(content);
			System.out.println("Decomplressed Data: " + new String(decom));
			
			//serialize
			String FILEPATH = "/ownfiles/a_tull_test.xml";
			Utility helper = new Utility();
			helper.writeByteArrayToFile(decom, new File(FILEPATH));
	       
	}
	
	 /**
     * The message is decrypted like so:
     *    - Read the encrypted public key
     *    - Decrypt the public key with the private key
     *    - Read the encrypted message
     *    - Use the decrypted public key to decrypt the encrypted message
     *    
     * @param message The encrypted message
     * @param key The private key
     * @return The decrypted message
     * @throws GeneralSecurityException
     */
	private byte[] decrypt(byte[] payload, PrivateKey key) throws GeneralSecurityException {
        ByteBuffer buffer = ByteBuffer.wrap(payload);
        int keyLength = buffer.getInt();
        byte[] encyptedPublicKey = new byte[keyLength];
        buffer.get(encyptedPublicKey);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] encodedPublicKey = cipher.doFinal(encyptedPublicKey);

        PublicKey publicKey = getPublicKey(encodedPublicKey);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] encryptedMessage = new byte[buffer.remaining()];
        buffer.get(encryptedMessage);

        return cipher.doFinal(encryptedMessage);
    }

	
	protected static PublicKey getPublicKey(byte[] encodedKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(encodedKey);
        return factory.generatePublic(encodedKeySpec);
    }
	
	
	/**
	   * Retrieves a {@link java.security.PrivateKey} from a file.
	   *
	   * @param filename The filename from which to load the private key.
	   * @return A {@link java.security.PrivateKey} object.
	   * @throws Base64DecoderException
	   * @throws InvalidKeySpecException
	   * @throws IOException
	   * @throws NoSuchAlgorithmException
	   */
	  public PrivateKey getPrivateKeyFromFilename(String filename)
	      throws Base64DecoderException, InvalidKeySpecException, IOException,
	      NoSuchAlgorithmException {
	    return getPrivateKey(new File(filename));
	  }

	  /**
	   * Retrieves a {@link java.security.PrivateKey} from a file.
	   *
	   * @param file The {@link java.io.File} object from which to load the private
	   *     key.
	   * @return A {@link java.security.PrivateKey} object.
	   * @throws Base64DecoderException
	   * @throws InvalidKeySpecException
	   * @throws IOException
	   * @throws NoSuchAlgorithmException
	   */
	  public PrivateKey getPrivateKey(File file)
	      throws Base64DecoderException, InvalidKeySpecException, IOException,
	      NoSuchAlgorithmException {
	    return getPrivateKey(new BufferedReader(new FileReader(file)));
	  }

	  /**
	   * Retrieves a {@link java.security.PrivateKey} from a reader.
	   *
	   * @param privateKeyReader The {@link java.io.Reader} object from which to
	   *     load the private key.
	   * @return A {@link java.security.PrivateKey} object.
	   * @throws Base64DecoderException
	   * @throws InvalidKeySpecException
	   * @throws IOException
	   * @throws NoSuchAlgorithmException
	   */
	  public PrivateKey getPrivateKey(Reader privateKeyReader)
	      throws Base64DecoderException, InvalidKeySpecException, IOException,
	      NoSuchAlgorithmException {
	    return getPrivateKey(readToString(privateKeyReader));
	  }

	  /**
	   * Retrieves a {@link java.security.PrivateKey} from a string.
	   *
	   * @param privateKeyString The string from which to load the private key.
	   * @return A {@link java.security.PrivateKey} object.
	   * @throws Base64DecoderException
	   * @throws InvalidKeySpecException
	   * @throws NoSuchAlgorithmException
	   */
	  public PrivateKey getPrivateKey(String privateKeyString)
	      throws Base64DecoderException, InvalidKeySpecException,
	      NoSuchAlgorithmException {
		// Strip off delimiters, if they exist.
	    String begin = "-----BEGIN PRIVATE KEY-----";
	    String end = "-----END PRIVATE KEY-----";
	    if (privateKeyString.contains(begin) && privateKeyString.contains(end)) {
	      privateKeyString = privateKeyString.substring(begin.length(),
	          privateKeyString.lastIndexOf(end));
	    }
	    System.out.println(privateKeyString);
	    return getPrivateKey(Base64.decode(privateKeyString));
	  }

	  /**
	   * Retrieves a {@link java.security.PrivateKey} from an array of bytes.
	   *
	   * @param privateKeyBytes The array of bytes from which to load the private
	   *     key.
	   * @return A {@link java.security.PrivateKey} object.
	   * @throws InvalidKeySpecException
	   * @throws NoSuchAlgorithmException
	   */
	  public PrivateKey getPrivateKey(byte[] privateKeyBytes)
	      throws InvalidKeySpecException, NoSuchAlgorithmException {
	    KeyFactory fac = KeyFactory.getInstance("RSA");
	    EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
	    return fac.generatePrivate(privKeySpec);
	  }

	  /** Converts the contents of a {@link java.io.Reader} object to a string. */
	  private String readToString(Reader in) throws IOException {
	    StringBuffer buf = new StringBuffer();
	    try {
	      for (int c = in.read(); c != -1; c = in.read()) {
	        buf.append((char) c);
	      }
	      return buf.toString();
	    } catch (IOException e) {
	      throw e;
	    } finally {
	      try {
	        in.close();
	      } catch (Exception e) {
	        // ignored
	      }
	    }
	  }
	  
	  /**
	   * 
	   * @param bytes
	   * @return
	   */
	  public byte[] decompressByteArray(byte[] bytes){
	         
	        ByteArrayOutputStream baos = null;
	        Inflater iflr = new Inflater();
	        iflr.setInput(bytes);
	        baos = new ByteArrayOutputStream();
	        byte[] tmp = new byte[4*1024];
	        try{
	            while(!iflr.finished()){
	                int size = iflr.inflate(tmp);
	                baos.write(tmp, 0, size);
	            }
	        } catch (Exception ex){
	             
	        } finally {
	            try{
	                if(baos != null) baos.close();
	            } catch(Exception ex){}
	        }
	         
	        return baos.toByteArray();
	    }
	
}
