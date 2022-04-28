package io.genobank;

import java.time.Instant;

import org.web3j.utils.Numeric;
import org.web3j.crypto.Keys;

/**
 * Implementation for permittee to certify laboratory results
 * 
 * @author William Entriken
 */
public class Main {
  public static void main(String[] args) throws IllegalArgumentException {

    if (args.length != 1) {
      showHelp();
      return;
    }

    System.err.println("Blockchain Lab Results Certification");
    System.err.println("Java Certification Example, version 1.1");
    System.err.println("(c) GenoBank.io 🧬");
    System.err.println();

    Network network;
    switch (Settings.NETWORK) {
      case "--test":
        System.err.println("Network:     " + ConsoleColors.GREEN + "TEST NETWORK" + ConsoleColors.RESET);
        network = Network.TEST;
        break;
      case "--local":
        System.err.println("Network:     " + ConsoleColors.BLUE_BRIGHT + "TEST LOCAL" + ConsoleColors.RESET);
        network = Network.LOCAL;
        break;
      case "--production":
        System.err.println("Network:     " + ConsoleColors.RED + "PRODUCTION NETWORK (BILLABLE)" + ConsoleColors.RESET);
        network = Network.PRODUCTION;
        break;
      default:
        throw new IllegalArgumentException("You must specify --test or --production network");
    }

    PermitteeSigner signer = new PermitteeSigner(Settings.TWELVE_WORD_PHRASE, Integer.parseInt(Settings.PERMITTEE_ID));
    System.err.println("Address:     " + ConsoleColors.YELLOW + Keys.toChecksumAddress(signer.credentials.getAddress()) + ConsoleColors.RESET);



    PermitteeRepresentations representations = new PermitteeRepresentations(
      network,
      LaboratoryProcedure.procedureWithCode(Settings.TEST), // Laboratory procedure (ALWAYS PATERNITY TEST)
      LaboratoryProcedure.procedureWithCode(Settings.TEST).resultWithCode(Settings.RESULT), // Laboratory result
      Settings.SERIAL, // Serial
      Instant.ofEpochMilli(Long.parseLong(Settings.TIMESTAMP)), // Time
      signer.permitteeId, // Permittee ID
      args[0] // JSON test
    );

    System.err.println("Father Name:     " + ConsoleColors.YELLOW + representations.fatherName + ConsoleColors.RESET);
    System.err.println("Child Name:    " + ConsoleColors.YELLOW + representations.childName + ConsoleColors.RESET);
    System.err.println("Procedure:   " + ConsoleColors.YELLOW + representations.procedure.code + ConsoleColors.RESET);
    System.err.println("Result:      " + ConsoleColors.YELLOW + representations.result.code + ConsoleColors.RESET);
    System.err.println("Serial:      " + ConsoleColors.YELLOW + representations.serial + ConsoleColors.RESET);
    System.err.println("Time:        " + ConsoleColors.YELLOW + representations.time.toEpochMilli() + "" + ConsoleColors.RESET);


    byte[] signature = signer.sign(representations);
    System.err.println("Signature:   " + ConsoleColors.YELLOW + Numeric.toHexString(signature) + ConsoleColors.RESET);
    System.err.println();
    
    System.err.println("Notarizing on blockchain...");
    Platform platform = new Platform(network, signer);
    NotarizedCertificate certificate = platform.notarize(representations, signature);
    System.err.println();

    System.err.println("Certificate URL");
    System.out.println(certificate.toURL());
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
