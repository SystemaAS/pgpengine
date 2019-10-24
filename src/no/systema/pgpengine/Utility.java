package no.systema.pgpengine;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureList;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentVerifierBuilderProvider;

public class Utility {
	
	final int BUFFSIZE = 8 * 1024;
	
	
	// Method which write the bytes into a file 
    public void writeByteArrayToFile(byte[] bytes, File file){ 
        try { 
  
            // Initialize a pointer 
            // in file using OutputStream 
            OutputStream os = new FileOutputStream(file); 
  
            // Starts writing the bytes in it 
            os.write(bytes); 
            System.out.println("Successfully"+ " byte array inserted"); 
            // Close the file 
            os.close(); 
        } 
  
        catch (Exception e) { 
            System.out.println("Exception: " + e); 
        } 
    } 
  
    /**
     * 
     * @param file
     * @return
     */
    public byte[] readFileToByteArray(File file){
        FileInputStream fis = null;
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
        byte[] bArray = new byte[(int) file.length()];
        try{
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();        
            
        }catch(IOException ioExp){
            ioExp.printStackTrace();
        }
        return bArray;
    }
    /**
     * Decompresses a zip file
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
    
    
   /**
    * 
    * @param gzipFile
    * @param outputFile
    */
    public void gunzipIt(String gzipFile, String outputFile){
    	byte[] buffer = new byte[1024];
    
        try{
    
       	 	GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(gzipFile));
       	 	FileOutputStream out = new FileOutputStream(outputFile);
    
       	 	int len;
       	 	while ((len = gzis.read(buffer)) > 0) {
       	 		out.write(buffer, 0, len);
       	 	}
    
       	 	gzis.close();
       	 	out.close();
    
       	 	System.out.println("Done");
       	
       }catch(IOException ex){
            ex.printStackTrace();   
       }
    } 
    
    
    
}
