package Controller;

import Model.ExcepGrafo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class ControladorGrafo {
    private ArrayList<String[]> aristas = new ArrayList<String[]>();
    private HashMap<String, String[]> rutas;
    private ArrayList<String> vertices = new ArrayList<String>();
    private String graphCenter = "";

    /**
     * Se encarga de extraer los datos del archivo y los almacena en un formato determinado.
     * @param lines Lineas del archivo de texto.
     * @throws ExcepGrafo
     */
    public void fileToGraph(String[] lines) throws ExcepGrafo {
        for(String l : lines) {
            String[] line = l.split(" ");
            String[] inverted = l.split(" ");
            String origen = inverted[0];
            inverted[0] = inverted[1];
            inverted[1] = origen;
            if(!aristas.contains(line)) {
                aristas.add(line);
                aristas.add(inverted);
            }
            if(!vertices.contains(line[1]))
                vertices.add(line[1]);
            if(!vertices.contains(line[0]))
                vertices.add(line[0]);
        }
        matrizAdyacencias();
    }

    /**
     * Se encarga de crear la matriz de adyacencia a partir de los vertices y aristas del grafo.
     * @throws ExcepGrafo
     */
    private void matrizAdyacencias() throws ExcepGrafo {
        Double[][] pesos = new Double[vertices.size()][vertices.size()];
        for(int i =0; i<vertices.size();i++) {
            int adyacencias = 0;
            for(int j=0;j<vertices.size();j++) {
                if(i==j)
                    pesos[i][j] = 0.00;
                else {
                    boolean foundAdy = false;
                    for(String[] a : aristas) {
                        if(a[0].equals(vertices.get(i))&&a[1].equals(vertices.get(j))) {
                            pesos[i][j] = Double.parseDouble(a[2]);
                            foundAdy = true;
                            adyacencias++;
                        }
                    }
                    if(!foundAdy)
                        pesos[i][j] = Double.POSITIVE_INFINITY;
                }
            }
            if(adyacencias<1)
                throw new ExcepGrafo();
        }
        floyd(pesos);
    }

    /**
     * Se encarga de ejecutar el algoritmo de floyd para calcular la distancia más corta entre dos nodos.
     * @param pesos Matriz con los pesos de cada una de las aristas.
     * @throws ExcepGrafo
     */
    private void floyd(Double[][] pesos) throws ExcepGrafo{
        rutas=new HashMap<String, String[]>();
        ArrayList<String> ruta = new ArrayList<String>();
        for(int i=0;i<vertices.size();i++) {
            for(int j=0;j<vertices.size();j++) {
                if(i==j) {
                    ruta = new ArrayList<String>();
                    ruta.add("0");
                    rutas.put(vertices.get(j)+", "+vertices.get(i), ruta.toArray(new String[ruta.size()]));
                }
                else {
                    for(int k=0;k<vertices.size();k++) {
                        if(k!=i && k!=j) {
                            ruta = new ArrayList<String>();
                            String viaje = vertices.get(j) + ", "+vertices.get(k);
                            double newRoute = pesos[j][i]+pesos[i][k];
                            if(newRoute<pesos[j][k]) {
                                pesos[j][k]=newRoute;
                                ruta.add(((Double)newRoute).toString());
                                getIntermediateCities(ruta, vertices.get(j)+", "+vertices.get(i));
                                ruta.add(vertices.get(i));
                            }else {
                                if(!rutas.containsKey(viaje))
                                    ruta.add(((Double)pesos[j][k]).toString());
                            }
                            if(ruta.size()>0)
                                rutas.put(viaje, ruta.toArray(new String[ruta.size()]));
                        }
                    }
                }
            }
        }
        if(!validGraph(pesos))
            throw new ExcepGrafo();
        graphCenter(pesos);
    }

    /**
     * Se encarga de determinar si los datos almacenados corresponden al formato de un grafo valido.
     * @param matriz
     * @return boolean
     */
    private boolean validGraph(Double[][] matriz) {
        for(Double[] d : matriz) {
            if(Arrays.asList(d).contains(Double.POSITIVE_INFINITY))
                return false;
        }
        return true;
    }

    /**
     * Permite determinar las ciudades intermedias de una ruta.
     * @param ruta Almacena las diferentes rutas entre ciudades.
     * @param key Identificador de la ruta.
     */
    private void getIntermediateCities(ArrayList<String> ruta, String key) {
        if (rutas.containsKey(key)) {
            String[] info = rutas.get(key);
            for(int i =1;i<info.length;i++) {
                ruta.add(info[i]);
            }
        }
    }

    /**
     * Permite ejecutar el algoritmo y seleccionar la ruta mas corta entre dos ciudades.
     * @param origen Nombre de la ciudad de origen.
     * @param destino Nombre de la ciudad de destino.
     * @return String. Ruta de llegada mas corta.
     */
    public String shorterRoute(String origen, String destino) {
        String viaje = origen+", "+destino;
        if(origen.equals(destino))
            return "You are heading to the same city, the route is 0km\n.";
        if(rutas.containsKey(viaje)) {
            String ruta = "";
            ruta = "Route: "+rutas.get(viaje)[0];
            ruta += rutas.get(viaje).length>1 ? " km\n"+"Intermediate Cities: "+intermediateCities(rutas.get(viaje)) : " km";
            return ruta;
        }else
            return "Route not found.";
    }

    /**
     * Se encarga de generar un String con cada una de las ciudades intermedias de una ruta.
     * @param cities Arreglo que contiene diferentes nombres de ciudades.
     * @return String. Ciudades intermedias.
     */
    private String intermediateCities(String[] cities) {
        String iCities = "";
        for(int i = 1;i<cities.length;i++)
            iCities += cities[i] + ", ";
        return iCities.substring(0, iCities.length()-2);
    }

    /**
     * Se encarga de calcular el centro del grafo.
     * @param pesos Matriz con los pesos de cada arista.
     */
    public void graphCenter(Double[][] pesos) {
        Double[] eccentricities = new Double[vertices.size()];
        for(int i=0;i<vertices.size();i++) {
            for(int j=0;j<vertices.size();j++) {
                if(eccentricities[j]==null)
                    eccentricities[j]=pesos[i][j];
                else if (pesos[i][j]>eccentricities[j])
                    eccentricities[j]=pesos[i][j];
            }
        }
        int min = eccentricities[0].intValue();
        graphCenter = vertices.get(0);
        for(int i=0;i<vertices.size();i++) {
            if(eccentricities[i]<min) {
                min = eccentricities[i].intValue();
                graphCenter = vertices.get(i);
            }
        }
    }

    /**
     * Permite la eliminación de una arista entre dos nodos (ruta entre dos ciudades).
     * @param origen Ciudad de origen.
     * @param destino Ciudad de destino.
     * @return String. Mensaje de respuesta.
     */
    public String breakRoute(String origen, String destino) {
        String[] ruta = null;
        String[] inverted = null;
        for(String[] a : aristas) {
            if(a[0].equals(origen) && a[1].equals(destino))
                ruta = a;
            if(a[1].equals(origen) && a[0].equals(destino))
                inverted = a;
        }
        if(ruta != null) {
            aristas.remove(ruta);
            aristas.remove(inverted);
            try {
                matrizAdyacencias();
                return "Route successfully deleted, the shortest routes have been recalculated.\n";
            } catch (ExcepGrafo e) {
                return "An error occurred while trying to delete this route.\n";
            }
        }else
            return "The specified path was not found.\n";
    }

    /**
     * Permite crear nodos (si las ciudades de origen o destino no existen) y establecer relaciones(rutas) entre ellos.
     * @param origen Nombre de la ciudad de origen.
     * @param destino Nombre de la ciudad de destino.
     * @param peso Peso (Km) de la arista.
     * @return String. Mensaje de respuesta.
     * @throws ExcepGrafo
     */
    public String newRoute(String origen, String destino, int peso) throws ExcepGrafo {
        String[] ruta = null;
        String[] inverted = null;
        int indexA = -1;
        int indexB = -1;
        for(int i=0;i<aristas.size();i++) {
            String[] arista = aristas.get(i);
            if(arista[0].equals(origen) && arista[1].equals(destino)) {
                ruta = arista;
                indexA = i;
            }
            if(arista[1].equals(origen) && arista[0].equals(destino)) {
                inverted = arista;
                indexB = i;
            }
        }
        if(ruta != null) {
            if(Integer.parseInt(ruta[2])<peso)
                return "There is already a route between these cities, with a shorter distance.";
            else {
                aristas.get(indexA)[2] = String.valueOf(peso);
                aristas.get(indexB)[2] = String.valueOf(peso);
                try {
                    matrizAdyacencias();
                    return "There is already a route between these cities, the distance has been changed.\n";
                } catch (ExcepGrafo e) {
                    return "An error occurred while updating the graph.\n";
                }
            }
        }else {
            String[] newRoute = {origen,destino,String.valueOf(peso)};
            String[] invertedNew = {destino,origen,String.valueOf(peso)};
            aristas.add(newRoute);
            aristas.add(invertedNew);
            vertices.add(origen);
            vertices.add(destino);
            try {
                matrizAdyacencias();
                return "Route added. The shortest routes have been recalculated.\n";
            }catch(ExcepGrafo e) {
                aristas.remove(newRoute);
                aristas.remove(invertedNew);
                vertices.remove(origen);
                vertices.remove(destino);
                matrizAdyacencias();
                return "Including this path would make the graph non-convex, the action has been omitted.";
            }
        }
    }

    /**
     * Reescribe el archivo guategrafo.txt con la informacion generada en la ejecucion.
     * @throws IOException
     */
    public void rewriteFile() throws IOException {
        ArrayList<String> parejas = new ArrayList<String>();
        for(String[] a : aristas) {
            String viaje = a[0] + ", "+a[1];
            String invertedViaje = a[1]+", "+a[0];
            if(!parejas.contains(viaje) && !parejas.contains(invertedViaje))
                RW_File.writeFile(a[0] + " " + a[1] + " "+a[2]+"\n");
            parejas.add(viaje);
            parejas.add(invertedViaje);
        }
    }

    /**
     * Metodo getter del centro del grafo.
     * @return String. Centro del grafo.
     */
    public String getGraphCenter() {
        return this.graphCenter;
    }
}