package no.systema.pgpengine;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import no.systema.pgpengine.sub.DecryptMain;

public class PgpDecompressor {
	/**
	 * 
	 * @param sourceDir
	 * @param targetDir
	 */
    public void decompress (Path sourceDir, String targetDir){
    	
    	Utility helper = new Utility();
    	
	    try (Stream<Path> paths = Files.walk(sourceDir)) {
		    paths
		        .filter(Files::isRegularFile)
		        .forEach( e ->{
		        		if(!e.getFileName().toString().startsWith(".")){
		        			System.out.println(targetDir + e.getFileName().toString().replace(".gz", ""));
		        			helper.gunzipIt(e.toString(), targetDir + e.getFileName().toString().replace(".gz", ""));
		        		}
		        	});
	    }catch (Exception e) { 
	        System.out.println("Exception: " + e); 
	    } 
    }
}
