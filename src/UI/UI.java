package UI;

import Controller.ControladorGrafo;
import Controller.RW_File;
import Model.ExcepGrafo;

import java.io.IOException;
import java.util.Scanner;

public class UI {

    /**
     * Metodo main del programa.
     * @param args
     */
    public static void main(String[] args) throws IOException, ExcepGrafo {
        Scanner sc = new Scanner(System.in);
        boolean mainEnd = false;
        ControladorGrafo manager = new ControladorGrafo();

        System.out.println("\nWelcome to the new routing system for Agexport.\n Designers: Nahomy Castro & Xavier López");
        while(!mainEnd) {
            boolean found = false;
            boolean end = false;

            System.out.println("\nFile found (Remember to change the PATH in RW_File.java).");
            while(!end) {

                System.out.println("\nChoose weather:\n\t1. Normal.\n\t2. Rainy.\n\t3. Snowy.\n\t4. Stormy.\n\t5. IDK.");
                int op = 0;
                op = sc.nextInt();
                sc.nextLine();

                String[] fileContent = null;
                fileContent = RW_File.readFile(op);
                manager.fileToGraph(fileContent);


                String menu = "\n1. Shorter route between two cities.\n2. Graph Center.\n3. Modify Graph.\n4. Exit";
                int option = question(menu, 4, sc);
                String origin = "";
                String destination = "";
                int weight = 0;
                switch(option) {
                    case 1:

                        System.out.println("Starting city: ");
                        origin = sc.nextLine();
                        System.out.println("Destination city: ");
                        destination = sc.nextLine();
                        System.out.println(manager.shorterRoute(origin,destination));
                        break;
                    case 2:
                        System.out.println("The city at the center of the graph is: "+ manager.getGraphCenter());
                        break;
                    case 3:
                        String menuMod = "";
                        System.out.println("What kind of modification do you want to do?");
                        menuMod = "1. Interruption of traffic between two cities\n.\n2. New route between two cities";
                        option = question(menuMod,2,sc);
                        switch(option) {
                            case 1:
                                System.out.println("Starting city: ");
                                origin = sc.nextLine();
                                System.out.println("Destination city: ");
                                destination = sc.nextLine();
                                System.out.println(manager.breakRoute(origin,destination));
                                break;
                            case 2:
                                System.out.println("Starting city: ");
                                origin = sc.nextLine();
                                System.out.println("Destination city:");
                                destination = sc.nextLine();
                                weight = numeroEntero("Distance between them in KM:", sc);
                                try {
                                    System.out.println(manager.newRoute(origin,destination,weight));
                                }catch (ExcepGrafo e) {
                                    System.out.println("An error has occurred, exiting program...");
                                    end = true;
                                    mainEnd = true;
                                }
                                break;
                            default:
                                System.out.println("Invalid option.");
                                break;
                        }
                        break;
                    case 4:
                        System.out.println("Thank you for using our program.");
                        end = true;
                        mainEnd = true;
                        break;
                    default: //Opcion no valida
                        System.out.println("Invalid option.");
                        break;
                }
            }
        }
        RW_File.deleteFile();
        try {
            manager.rewriteFile();
        }catch(IOException e) {
            System.out.println("Ha ocurrido un error a reescribir el archivo.");
        }
    }

    /**
     * Se encarga de solicitar al usuario que ingrese un dato en especifico y validarlo.
     * @param question Mensaje a desplegar.
     * @param opciones Opciones consideradas como validas.
     * @param sc Objeto Scanner.
     * @return int.
     */
    public static int question(String question, int opciones, Scanner sc)
    {
        boolean bucle = true;
        int response = 0;
        try
        {
            while(bucle)
            {
                System.out.println(question);
                response = sc.nextInt();
                sc.nextLine();
                if(response > 0 && response <= opciones) bucle = false;
                else System.out.println("\nInvalid option.\n");
            }
        } catch (Exception e) {
            System.out.println("\nInvalid option, only numbers allowed.\n");
            sc.nextLine();
            response = question(question, opciones, sc);
        }
        return response;
    }

    /**
     * Se encarga de solicitar al usuario que ingrese un dato de tipo entero.
     * @param question Mensaje a desplegar al usuario.
     * @param sc Objeto Scanner
     * @return Int.
     */
    public static int numeroEntero(String question, Scanner sc) {
        boolean bucle = true;
        int num = 0;
        try
        {
            while(bucle)
            {
                System.out.println(question);
                num = sc.nextInt();
                sc.nextLine();
                if(num > 0) bucle = false;
                else System.out.println("\nInvalid option.\n");
            }
        } catch (Exception e) {
            sc.nextLine();
            System.out.println("\nInvalid option, only numbers allowed.\n");
            num = numeroEntero(question, sc);
        }
        return num;
    }

}

