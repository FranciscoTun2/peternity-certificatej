package io.genobank.certificate;

import java.time.Instant;

import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import io.genobank.network.*;
import io.genobank.model.*;
import io.genobank.cryptography.*;
import io.genobank.Settings;

public class Certificate {
	
	String uri = "";
	
	public Certificate(String args[]) {
		Settings settings = new Settings();

        // if args.length is different from 3 then throw an exception and no continue
        if (args.length != 3) {
            showHelp();
            throw new IllegalArgumentException("Invalid number of arguments");
        }

	    Network network;
	    
	    switch (settings.NETWORK) {
	      case "--test":
	        network = Network.TEST;
	        break;
	      case "--local":
	        network = Network.LOCAL;
	        break;
	      case "--production":
	        network = Network.PRODUCTION;
	        break;
	      default:
	        throw new IllegalArgumentException("You must specify --test or --production network");
	    }
	    PermitteeSigner signer = new PermitteeSigner(args[1], Integer.parseInt(args[0]));
	    

	    PermitteeRepresentations representations = new PermitteeRepresentations(
	      network,
	      LaboratoryProcedure.procedureWithCode(Settings.TEST), // Laboratory procedure (ALWAYS PATERNITY TEST)
	      LaboratoryProcedure.procedureWithCode(Settings.TEST).resultWithCode(Settings.RESULT), // Laboratory result
	      Settings.SERIAL, // Serial
	      Instant.ofEpochMilli(Long.parseLong(Settings.TIMESTAMP)), // Time
	      signer.permitteeId, // Permittee ID
	      args[2] // JSON test
	    );

	    byte[] signature = signer.sign(representations);

	    Platform platform = new Platform(network, signer);
	    NotarizedCertificate certificate = platform.notarize(representations, signature);
	    
	    this.uri = certificate.toURL();
	}
	
	public String getUri() {
		return this.uri;
	}
	
	public String createCertificate(String args[]) {
		
		Settings settings = new Settings();

        // if args.length is different from 3 then throw an exception and no continue
        if (args.length != 3) {
            showHelp();
            throw new IllegalArgumentException("Invalid number of arguments");
        }

	    System.err.println("Blockchain Lab Results Certification");
	    System.err.println("Java Certification Example, version 1.1");
	    System.err.println("(c) GenoBank.io 🧬");
	    System.err.println();

	    Network network;
	    
	    System.out.print(Settings.NETWORK);
	    switch (settings.NETWORK) {
	      case "--test":
	        System.err.println("Network:     " + "TEST NETWORK");
	        network = Network.TEST;
	        break;
	      case "--local":
	        System.err.println("Network:     " +  "TEST LOCAL");
	        network = Network.LOCAL;
	        break;
	      case "--production":
	        System.err.println("Network:     " + "PRODUCTION NETWORK (BILLABLE)");
	        network = Network.PRODUCTION;
	        break;
	      default:
	        throw new IllegalArgumentException("You must specify --test or --production network");
	    }
	    PermitteeSigner signer = new PermitteeSigner(args[1], Integer.parseInt(args[0]));
	    
	    System.out.print(network);

	    PermitteeRepresentations representations = new PermitteeRepresentations(
	      network,
	      LaboratoryProcedure.procedureWithCode(Settings.TEST), // Laboratory procedure (ALWAYS PATERNITY TEST)
	      LaboratoryProcedure.procedureWithCode(Settings.TEST).resultWithCode(Settings.RESULT), // Laboratory result
	      Settings.SERIAL, // Serial
	      Instant.ofEpochMilli(Long.parseLong(Settings.TIMESTAMP)), // Time
	      signer.permitteeId, // Permittee ID
	      args[2] // JSON test
	    );

	    System.err.println("Father Name:     " + representations.fatherName);
	    System.err.println("Child Name:    " + representations.childName);
	    System.err.println("Procedure:   " + representations.procedure.code);
	    System.err.println("Result:      " + representations.result.code);
	    System.err.println("Serial:      " + representations.serial);
	    System.err.println("Time:        " + representations.time.toEpochMilli());

	    byte[] signature = signer.sign(representations);
	    System.err.println("Signature:   " + Numeric.toHexString(signature));
	    System.err.println();
	    
	    System.err.println("Notarizing on blockchain...");
	    Platform platform = new Platform(network, signer);
	    NotarizedCertificate certificate = platform.notarize(representations, signature);
	    System.err.println();

	    System.err.println("Certificate URL");
	    System.out.println(certificate.toURL());
	    
	    return certificate.toURL();
	}
		      
	public static void showHelp() {
	    System.err.println("Blockchain Lab Results Certification");
	    System.err.println("Java Certification Example, version 1.1");
	    System.err.println("(c) GenoBank.io 🧬");
	    System.err.println();
	    System.err.println("SYNOPSIS");
	    System.err.println("    certificates --test TWELVE_WORD_PHRASE PERMITTEE_ID PATIENT_NAME PATIENT_PASSPORT PROCEDURE_CODE RESULT_CODE SERIAL TIMESTAMP");
	    System.err.println("    certificates --production TWELVE_WORD_PHRASE PERMITTEE_ID PATIENT_NAME PATIENT_PASSPORT PROCEDURE_CODE RESULT_CODE SERIAL TIMESTAMP");
	    System.err.println();
	    System.err.println("DESCRIPTION");
	    System.err.println("    This notarizes a laboratory result using the GenoBank.io platform.");
	    System.err.println("    Running on the production network is billable per your laboratory agreement.");
	    System.err.println();
	    System.err.println("    TWELVE_WORD_PHRASE a space-separated string of your twelve word phrase");
	    System.err.println("    PERMITTEE_ID       your GenoBank.io permittee identifier");
	    System.err.println("    PATIENT_NAME       must match [A-Za-z0-9 .-]+");
	    System.err.println("    PATIENT_PASSPORT   must match [A-Z0-9 -]+");
	    System.err.println("    PROCEDURE_CODE     must be a procedure key in the Laboratory Procedure Taxonomy");
	    System.err.println("    RESULT_CODE        must be a result key in the Laboratory Procedure Taxonomy");
	    System.err.println("    SERIAL             must match [A-Z0-9 -]*");
	    System.err.println("    TIMESTAMP          procedure/sample collection time as number of milliseconds since UNIX epoch");
	    System.err.println();
	    System.err.println("OUTPUT");
	    System.err.println("    A complete URL for the certificate is printed to standard output.");
	    System.err.println("    Please note: you should keep a copy of this output because you paid for it");
	    System.err.println("    and nobody else has a copy or can recreate it for you.");
	    System.err.println();
	    System.err.println("REFERENCES");
	    System.err.println("    Laboratory Procedure Taxonomy (test):");
	    System.err.println("    https://genobank.io/certificates/laboratoryProcedureTaxonomy.json");
	    System.err.println();
	    System.err.println("    Laboratory Procedure Taxonomy (production):");
	    System.err.println("    https://genobank.io/test/certificates/laboratoryProcedureTaxonomy.json");
	}
	
}
