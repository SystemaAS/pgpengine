package no.systema.pgpengine;

//Java Program to convert 
	// byte array to file 
	import java.io.File; 
	import java.io.FileOutputStream; 
	import java.io.OutputStream;
import java.nio.file.*;
import java.util.stream.Stream;
 
	 
public class TesterFileGrabber {

	    
	    // Method which write the bytes into a file 
	    static void walkTheDir(){ 
	        try (Stream<Path> paths = Files.walk(Paths.get("/ownfiles/tullverketCert/source"))) {
	        	    paths
	        	        .filter(Files::isRegularFile)
	        	        //.forEach(System.out::println);
	        	        .forEach( e ->{
	    	        		System.out.println(e);
	    	        		System.out.println(e.getParent());
	    	        		
	    	        	});
	        	        
        	}catch (Exception e) { 
	            System.out.println("Exception: " + e); 
	        } 
	    } 
	  
	    // Driver Code 
	    public static void main(String args[]) { 
	  
	        walkTheDir();
	    } 

	
}
