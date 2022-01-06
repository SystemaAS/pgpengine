package no.systema.pgpengine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


import no.systema.pgpengine.sub.DecryptMain;


public class ManualEngine {

	public static void main(String[] args) {
		ManualEngine engine = new ManualEngine();
		engine.run();
				
	}
	
	
	
	
	public void run(){
		
			
		System.out.println("Inside method: run");

			
			//final String PUBLIC_KEY_RING = getPgpKey(ApplicationPropertiesUtil.getProperty("pgp.tulltaxa.cert.pubkey"));
			//final String PUBLIC_KEY_RING = "/Users/oscardelatorre/ttax/certs/TaricFildistribution.asc"; //must be in the classpath
			final String PUBLIC_KEY_RING = "/Library/Tomcat/apache-tomcat-9.0.53/certstulltaxa/TaricFildistribution.asc";
			
			//final String PRIVATE_KEY_RING = getPgpKey(ApplicationPropertiesUtil.getProperty("pgp.tulltaxa.cert.privkey"));
			//final String PRIVATE_KEY_RING = "/Users/oscardelatorre/ttax/certs/TrondHyttan.asc"; //must be in the classpath
			final String PRIVATE_KEY_RING = "/Library/Tomcat/apache-tomcat-9.0.53/certstulltaxa/TrondHyttan.asc"; //must be in the classpath
			
			//final String SECRET_KEY_RING_PASS = ApplicationPropertiesUtil.getProperty("pgp.tulltaxa.cert.seckey");
			final String SECRET_KEY_RING_PASS = "Hvem er Dennis";
			/*
			final String _1Dir = request.getParameter("lnkpgp");
			final String _2Dir = request.getParameter("lnkzip");
			final String _3Dir = request.getParameter("lnkunzip");
			*/
			final String _1Dir = "/Users/oscardelatorre/ttax/";
			final String _2Dir = "/Users/oscardelatorre/ttax/gz/";
			final String _3Dir = "/Users/oscardelatorre/ttax/xml/";
			
			System.out.println("lnkpgp:" + _1Dir);
			System.out.println("lnkpgp:" + _2Dir);
			System.out.println("lnkpgp:" + _3Dir);
			
			
			DecryptMain engineDecrypt = new DecryptMain();
			PgpDecompressor decompressor = new PgpDecompressor();
			if(Files.exists(Paths.get(_1Dir)) && Files.exists(Paths.get(_2Dir)) && Files.exists(Paths.get(_3Dir))){
				//------
				//STEP 1 - Decrypt (we decrypt all first in order to have bulks in the pipe process)
				//------
				try (Stream<Path> paths = Files.list(Paths.get(_1Dir))) {
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
		    	  
		    	    System.out.println("Start decompression ..."); 
		    	   decompressor.decompress(Paths.get(_2Dir), _3Dir);
		    	   System.out.println("End decompression ...");
		    	   
				}catch (Exception e) { 
					System.out.println("Exception: " + e); 
				   
			    } 
			}else{
			   System.out.println("Exception: All directories MUST exist.");	
			   
			}
	    	
	}

}
