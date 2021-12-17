
import org.omg.CORBA.*;

import org.omg.PortableServer.*;

import java.io.*;

import java.lang.*;

import java.util.*;


public class Serveur {
    public static void main(String[] args) throws IOException {
        ////////////////////////////////////////
        // Initialisation de l'ORB et de la POA
        ////////////////////////////////////////
        try {
            //init ORB
            ORB orb = ORB.init(args, null);

            //init POA
            POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            poa.the_POAManager().activate();

            ////////////////////////////////////////////////////////////////
            // Instantiation de l'objet : creation de
            //	l'implantation de l'objet
            ////////////////////////////////////////////////////////////////
            calculImpl calcImpl = new calculImpl();

            // create the object reference
            org.omg.CORBA.Object calc = poa.servant_to_reference(calcImpl);

            try {
                String calc_ref = orb.object_to_string(calc);
                String refFile = "calcul.ref";
                PrintWriter out = new PrintWriter(new FileOutputStream(refFile));
                out.println(calc_ref);
                out.close();
            }
            catch (IOException ex) {
                System.err.println(
                        "Impossible d'ecrire la reference dans calcul.ref");
                System.exit(1);
            }

            System.out.println("Le serveur est pret ");

            // wait for requests
            orb.run();

            System.exit(0);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}