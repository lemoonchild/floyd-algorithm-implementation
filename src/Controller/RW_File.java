package Controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class RW_File {

    public static final String PATH = "C:\\Users\\n8nug\\IdeaProjects\\floyd-algorithm-implementation\\logistica.txt";

    public static String[] readFile(int opcion) throws IOException {
        File doc = new File("C:\\Users\\n8nug\\IdeaProjects\\floyd-algorithm-implementation\\logistica.txt");
        BufferedReader obj = new BufferedReader(new FileReader(doc));
        ArrayList<String> linesList = new ArrayList<String>();

        String line;
        while ((line = obj.readLine()) != null) {
            String[] tokens = line.split(" "); // Dividir la línea en tokens
            if (tokens.length >= 3) { // Asegurarse de que haya al menos 3 elementos en la línea
                String ciudad1 = tokens[0];
                String ciudad2 = tokens[1];
                int numero;

                if (tokens.length >= 6) {
                    switch (opcion) {
                        case 1:
                            numero = Integer.valueOf(tokens[2]);
                            break;
                        case 2:
                            numero = Integer.parseInt(tokens[3]);
                            break;
                        case 3:
                            numero = Integer.parseInt(tokens[4]);
                            break;
                        case 4:
                            numero = Integer.parseInt(tokens[5]);
                            break;
                        case 5:
                            Random random = new Random();
                            int randomIndex = random.nextInt(4) + 2; // Generar un índice aleatorio entre 2 y 5
                            numero = Integer.parseInt(tokens[randomIndex]);
                            break;
                        default:
                            // Opción inválida, no hacer nada
                            continue;
                    }
                } else {
                    numero = Integer.parseInt(tokens[2]);
                }

                linesList.add(ciudad1 + " " + ciudad2 + " " + numero);
            }
        }

        obj.close();

        return linesList.toArray(new String[linesList.size()]);
    }

    /**
     * Permite crear(si no existe) el archivo de almacenamiento y sobreescribir su contenido.
     * @param text Contenido del archivo
     * @throws IOException
     */
    public static void writeFile(String text) throws IOException {
        File file = new File("C:\\Users\\n8nug\\IdeaProjects\\floyd-algorithm-implementation\\logistica.txt");
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file, true);

        fw.write(text);
        fw.close();
    }

    public static void deleteFile() {
        File file = new File("C:\\Users\\n8nug\\IdeaProjects\\floyd-algorithm-implementation\\logistica.txt");
        file.delete();
    }
}
