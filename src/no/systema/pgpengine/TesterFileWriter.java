package no.systema.pgpengine;

//Java Program to convert 
	// byte array to file 
	import java.io.File; 
	import java.io.FileOutputStream; 
	import java.io.OutputStream; 
	 
public class TesterFileWriter {

	    // Path of a file 
	    static String FILEPATH = "/ownfiles/baby.txt"; 
	    static File file = new File(FILEPATH); 
	  
	    // Method which write the bytes into a file 
	    static void writeByte(byte[] bytes){ 
	        try { 
	  
	            // Initialize a pointer 
	            // in file using OutputStream 
	            OutputStream os = new FileOutputStream(file); 
	  
	            // Starts writing the bytes in it 
	            os.write(bytes); 
	            System.out.println("Successfully"+ " byte inserted"); 
	            // Close the file 
	            os.close(); 
	        } 
	  
	        catch (Exception e) { 
	            System.out.println("Exception: " + e); 
	        } 
	    } 
	  
	    // Driver Code 
	    public static void main(String args[]) { 
	  
	        String string = "GeeksForGeeks" + " - A Computer Science" + " Portal for geeks"; 
	        // Get byte array from string 
	        byte[] bytes = string.getBytes(); 
	  
	        // Convert byte array to file 
	        writeByte(bytes); 
	    } 

	
}
