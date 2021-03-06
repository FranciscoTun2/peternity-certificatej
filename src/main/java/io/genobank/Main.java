package io.genobank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;


import org.web3j.utils.Numeric;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.crypto.Keys;

import io.genobank.certificate.Certificate;
/**
 * Implementation for permittee to certify laboratory results
 * 
 * @author William Entriken
 */
     
public class Main {  

	  public static void main(String[] args) throws IllegalArgumentException, JSONException {
		String network = "--production";
		String idPermitee = "8";
		String twelve_words = "wrong outside clever wagon father insane boy junk punch duck drift cupboard";
		String collection_date = "1656692640000";
		String jsonString = readMyFile("json 2 muestras.txt");
		String [] myArguments = {network, idPermitee, twelve_words, collection_date, jsonString};
		Certificate certificado = new Certificate(myArguments);
		System.out.print("This is my certificate URI: "+certificado.getUri());
		System.out.println();
  }
  
  public static String readMyFile(String path) {
	  String content = "";
	  try {
		  String line = null;
		  FileReader fileReader = new FileReader(path);
		  // Always wrap FileReader in BufferedReader.
		  BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		  while((line = bufferedReader.readLine()) != null) {
			  content += line;
		  }   bufferedReader.close();  
	  }
	  catch(FileNotFoundException ex) {
		System.out.print(ex);
        System.out.println(
            "Unable to open file '" + path + "'");                
        }
	  catch(IOException ex) {
        System.out.println(
            "Error reading file '" 
            + path + "'");                  
      }
	  return content;
  }
	  
}
