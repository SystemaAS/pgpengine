package no.systema.pgpengine;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import no.systema.pgpengine.sub.DecryptMain;

public class EngineDecrypt {
	
	// DRIVER ENGINE
	public static void main(String[] args) throws Exception {
		
		 String PUBLIC_KEY_RING = 	"/ownfiles/tullverketCert/TaricFildistribution.asc";
		 String PRIVATE_KEY_RING = 	"/ownfiles/tullverketCert/TrondHyttan.asc";
		 String SECRET_KEY_RING_PASS = 	"Hvem er Dennis";
		
		String _1Dir = "/ownfiles/tullverketCert/encrypted/";
		String _2Dir = "/ownfiles/tullverketCert/decrypted/";
		String _3Dir = "/ownfiles/tullverketCert/uncompressed/";
		
		DecryptMain engineDecrypt = new DecryptMain();
		PgpDecompressor decompressor = new PgpDecompressor();
		if(Files.exists(Paths.get(_1Dir)) && Files.exists(Paths.get(_2Dir)) && Files.exists(Paths.get(_3Dir))){
			//------
			//STEP 1 - Decrypt (we decrypt all first in order to have bulks in the pipe process)
			//------
			try (Stream<Path> paths = Files.walk(Paths.get(_1Dir))) {
				System.out.println("Starting decryption ...");
	    	    paths
	    	        .filter(Files::isRegularFile)
	    	        .forEach( e ->{
	    	        		if(!e.getFileName().toString().startsWith(".")){
	    	        			//System.out.println(e);
	    	        			engineDecrypt.runDecryptMain(e, _2Dir, PUBLIC_KEY_RING, PRIVATE_KEY_RING, SECRET_KEY_RING_PASS );
	    	        		}
	    	        	});
	    	 
	    	   //------- 
	    	   //STEP 2 - Decompress (decompress in a bulk: after all decrypted files have been processed)
	    	   //------- 
	    	   System.out.println();
	    	   System.out.println("Starting decompression ..."); 
	    	   decompressor.decompress(Paths.get(_2Dir), _3Dir);
	    	   
	    	   
			}catch (Exception e) { 
		       System.out.println("Exception: " + e); 
		    } 
		}else{
		   System.out.println("FATAL: All directories MUST exist.");	
		}
		
	}

}
