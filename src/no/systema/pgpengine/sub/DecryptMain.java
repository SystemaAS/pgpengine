package no.systema.pgpengine.sub;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bouncycastle.util.io.Streams;

import name.neuhalfen.projects.crypto.bouncycastle.openpgp.BouncyGPG;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.callbacks.KeyringConfigCallbacks;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.keyrings.KeyringConfig;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.keyrings.KeyringConfigs;

public class DecryptMain {

	
	//public void runDecryptMain() {
	public int runDecryptMain(Path sourceFile, String targetDir, String publicKeyRing, String privateKeyRing, String secretKeyRingPass) {
		  int retval = 0;
		  final File pubKeyRing = new File(publicKeyRing);
	      final File secKeyRing = new File(privateKeyRing);
	      final String secKeyRingPassword = secretKeyRingPass;
	      
	      String tmp = sourceFile.getFileName().toString();
	      tmp = tmp.replace(".pgp", "");
	      final Path destFile = Paths.get(targetDir + tmp);
	     
	      System.out.println("input:" + sourceFile + " output:" + destFile);
	      
		  /*	
		  final File pubKeyRing = new File(PUBLIC_KEY_RING);
	      final File secKeyRing = new File(PRIVATE_KEY_RING);
	      final String secKeyRingPassword = SECRET_KEY_RING_PASS;
	      final Path sourceFile = Paths.get("/ownfiles/tullverketCert/source/Certificate_example.xml.gz.pgp");
	      final Path destFile = Paths.get("/ownfiles/tullverketCert/target/Certificate_example.xml.gz");
	      */
	      
	      try {
	    	System.out.println("input2: BEFORE BouncyGPG.registerProvider()" );
	        BouncyGPG.registerProvider();
	        long startTime = System.currentTimeMillis();
	
	        final int BUFFSIZE = 8 * 1024;
	        System.out.println("input2: AFTER BouncyGPG.registerProvider()" );
	
	        final KeyringConfig keyringConfig = KeyringConfigs.withKeyRingsFromFiles(pubKeyRing,
	            secKeyRing, KeyringConfigCallbacks.withPassword(secKeyRingPassword));
	        System.out.println("AA");
	        try (
	        		 
	            final InputStream cipherTextStream = Files.newInputStream(sourceFile);
	        	final OutputStream fileOutput = Files.newOutputStream(destFile);
	            final BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOutput, BUFFSIZE);
	
	            final InputStream plaintextStream = BouncyGPG
	                .decryptAndVerifyStream()
	                .withConfig(keyringConfig)
	                .andValidateSomeoneSigned()
	                .fromEncryptedInputStream(cipherTextStream)
	                 
	
	        )  
	        {
	          Streams.pipeAll(plaintextStream, bufferedOut);
	          
	        }
	        System.out.println("BB");  
	        long endTime = System.currentTimeMillis();
	
	        System.out.println(String.format("DeEncryption took %.2f s",  ((double) endTime - startTime) / 1000));
	      } catch (Exception e) {
	    	  System.out.println("ERROR " + e);
	    	  return -1;
	      }
	      
	      return retval;
    	}
}
