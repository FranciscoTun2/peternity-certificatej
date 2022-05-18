package io.genobank.model;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.regex.Pattern;

import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * This is a specific laboratory result which can be notarized on the
 * GenoBank.io platform by a permitee.
 * 
 * @author William Entriken
 */

public class PermitteeRepresentations {

  private static final String versionCode = "V1";

  private static final String namespaceSuffix = ".certificates.v1.permittee-certification";
  
  public final Network network;

  public final String fatherName;

  public final String childName;

  public final LaboratoryProcedure procedure;
  
  public final LaboratoryProcedureResult result;
  
  public final String serial;

  public final java.time.Instant time;

  public final Integer permitteeId;

  public final String markers;

  public final String samples;

  public final String aditionalData;

  public PermitteeRepresentations(
    Network network,
    LaboratoryProcedure procedure,
    LaboratoryProcedureResult result,
    String serial,
    java.time.Instant time,
    Integer permitteeId,
    String jsonTest
  ) throws IllegalArgumentException {
    // Network
    java.util.Objects.requireNonNull(network);
    this.network = network;
    
    // Laboratory procedure
    java.util.Objects.requireNonNull(procedure);
    this.procedure = procedure;
    
    // Laboratory result
    java.util.Objects.requireNonNull(result);
    this.result = result;
    
    // Serial number
    if (!Pattern.matches("[A-Z0-9 -]*", serial)) {
      throw new IllegalArgumentException("Serial does not use required format");
    }
    this.serial = serial;
    
    // Time
    if (time.compareTo(Instant.parse("2021-01-01T00:00:00Z")) < 0) {
      throw new IllegalArgumentException("Time is too early, it is before 2021-01-01");
    }
    this.time = time;

    // Permittee ID
    this.permitteeId = permitteeId;

    java.util.Objects.requireNonNull(jsonTest);

    // parse String into JSONArray

    try{
        String markAux = "";
        String sampleAux = "";
        JSONObject jsonObject = new JSONObject(jsonTest);
        JSONArray jsonMarkers = jsonObject.getJSONArray("marcadores");
        JSONArray jsonSamples = jsonObject.getJSONArray("muestras");
        // String [] tipos = new String[jsonSamples.length()];
        String tipos = "";


        for (int i = 0; i < jsonMarkers.length(); i++) {
            markAux += jsonMarkers.getJSONObject(i).getString("marcador")+",";
        }

        for (int i = 0; i < jsonSamples.length(); i++) {
            JSONArray jsonGenotype = jsonSamples.getJSONObject(i).getJSONArray("genotipo");

            tipos += jsonSamples.getJSONObject(i).getString("tipo")+",";

            sampleAux +="[";
            for (int j = 0; j < jsonGenotype.length(); j++) {
                sampleAux += jsonGenotype.getJSONObject(j).getString("x")+","+jsonGenotype.getJSONObject(j).getString("y")+",";
            }
            sampleAux += "],";
        }
        System.out.println("markAux: "+markAux);
        System.out.println("sampleAux: "+sampleAux);

        this.markers = markAux.substring(0, markAux.length()-1);

        this.samples = sampleAux.substring(0, sampleAux.length()-1);
        // remove '\n' in the string
        // this.samples = this.samples.replaceAll("\n", "");
        String auxfatherName = jsonSamples.getJSONObject(0).getString("nombre");
        String auxchildName = jsonSamples.getJSONObject(1).getString("nombre");

        auxfatherName = auxfatherName.replaceAll("\n", "");
        auxchildName = auxchildName.replaceAll("\n", "");
        
        this.fatherName = auxfatherName;
        this.childName = auxchildName;
        
        this.aditionalData = tipos.substring(0, tipos.length()-1);

        System.out.println("aditionalData: "+this.aditionalData);

    
    } catch (Exception e) {
        throw new IllegalArgumentException("JSON is not valid");
    }

  }
 
  public String getFullSerialization() {
    DateTimeFormatter isoInstantWithMilliseconds = new DateTimeFormatterBuilder()
        .appendInstant(3)
        .toFormatter();

    return String.join("|", new String[]{
      network.namespacePrefix + namespaceSuffix,
      fatherName,
      childName,
      procedure.internationalName,
      result.internationalName,
      serial,
      isoInstantWithMilliseconds.format(time),
      permitteeId + "",
      markers,
      samples,
      aditionalData
    });
  }

  public String getTightSerialization() {
    return String.join("|", new String[]{
      fatherName,
      childName,
      procedure.code,
      result.code,
      serial,
      time.toEpochMilli() + "",
      permitteeId + "",
      markers,
      samples,
      aditionalData
    });  
  }  

  public byte[] getClaim() {
    return Sign.getEthereumMessageHash(getFullSerialization().getBytes(StandardCharsets.UTF_8));
//    byte[] message = getFullSerialization().getBytes(StandardCharsets.UTF_8);
//    return Hash.sha3(message);
  }
}
