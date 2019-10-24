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

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DecryptMain.class);
	
	//public void runDecryptMain() {
	public void runDecryptMain(Path sourceFile, String targetDir, String publicKeyRing, String privateKeyRing, String secretKeyRingPass) {
		  
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
	        BouncyGPG.registerProvider();
	        long startTime = System.currentTimeMillis();
	
	        final int BUFFSIZE = 8 * 1024;
	        LOGGER.trace("Using a write buffer of {} bytes\n", BUFFSIZE);
	
	        final KeyringConfig keyringConfig = KeyringConfigs.withKeyRingsFromFiles(pubKeyRing,
	            secKeyRing, KeyringConfigCallbacks.withPassword(secKeyRingPassword));
	
	        try (
	            final InputStream cipherTextStream = Files.newInputStream(sourceFile);
	
	            final OutputStream fileOutput = Files.newOutputStream(destFile);
	            final BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOutput, BUFFSIZE);
	
	            final InputStream plaintextStream = BouncyGPG
	                .decryptAndVerifyStream()
	                .withConfig(keyringConfig)
	                .andValidateSomeoneSigned()
	                .fromEncryptedInputStream(cipherTextStream)
	
	        ) {
	          Streams.pipeAll(plaintextStream, bufferedOut);
	          
	        }
	        long endTime = System.currentTimeMillis();
	
	        LOGGER.info(String.format("DeEncryption took %.2f s",  ((double) endTime - startTime) / 1000));
	      } catch (Exception e) {
	        LOGGER.error("ERROR", e);
	      }
    	}
}
