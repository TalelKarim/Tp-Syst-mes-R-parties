import org.omg.CORBA.*;

import java.io.*;

import java.lang.*;

import java.util.*;


public class Client {
    public static void main(String[] args) throws IOException {
        ////////////////////////////////////////////////////
        // On initialise l'ORB
        ////////////////////////////////////////////////////

        ORB orb = ORB.init(args, null);



        if (args.length != 0) {
            System.err.println("pas de parametre");
            System.exit(1);
        }

        ////////////////////////////////////////////////////
        // Recuperation de la reference d'objet du serveur
        // Dans cet exemple, la reference est stockee sous
        // la forme d'une chaine de caracteres (IOR) dans un
        // fichier. A ce stade, il est bien sur possible 
        // d'invoquer un service de nommage.
        ////////////////////////////////////////////////////
        String ior = null;

        try {
            String ref = "calcul.ref";
            FileInputStream file = new FileInputStream(ref);
            BufferedReader in = new BufferedReader(new InputStreamReader(file));
            ior = in.readLine();
            file.close();
        }
        catch (IOException ex) {
            System.err.println("Impossible de lire fichier : `" +
                    ex.getMessage() + "'");
            System.exit(1);
        }

        ////////////////////////////////////////////////////
        // Construction d'une reference d'objet, non typee d'abord,
        // puis "cast" en une reference sur l'interface 
        // "calcul"  avec narrow ("cast" dynamique)
        ////////////////////////////////////////////////////
        org.omg.CORBA.Object obj = orb.string_to_object(ior);

        if (obj == null) {
            System.err.println("Erreur sur string_to_object() ");
            throw new RuntimeException();
        }

        calcul calc = calculHelper.narrow(obj);

        if (calc == null) {
            System.err.println("Erreur sur narrow() ");
            throw new RuntimeException();
        }

        ////////////////////////////////////////////////////
        // Invocation du serveur
        ////////////////////////////////////////////////////
        try  {
            System.out.println("Valeur initiale de la memoire : " + calc.memoire());

            calc.ajouteMemoire(10);
            calc.multiplieMemoire(2);
            calc.soustraitMemoire(5);

            System.out.println("Valeur finale de la memoire : " + calc.memoire());

            calc.diviseMemoire(0);

            System.exit(0);

        }
        catch(divisionParZero ex)
        {
            System.err.println("Exception : division par zero" );
            System.exit(1);
        }

        System.exit(0);
    }
}