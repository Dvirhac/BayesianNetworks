
import java.io.IOException;

public class GraphBuilder {
    static Graph graph = new Graph();

    /**
     *
     * @param path
     * @return graph with all the Vertices and edges that were given in the text.
     * @throws IOException
     */
    //----------------Adding the vertices into the graph--------------
    static Graph createGraphFromText(String path) throws IOException {
        String [] dataSet = readFile.getData(path);
        String[] vars = dataSet[0].split("\\r?\\n");
        String[] varss = vars[1].split(",");
        varss[0] = varss[0].substring(11);
        for ( int i = 0 ; i < varss.length; i++){
            graph.addVertex(varss[i]);
        }

        //---------Adding the edges between the vertices-----------------------

        for (int i = 1; i < dataSet.length -1 ; i++){
            vars = dataSet[i].split("\\r?\\n");
            String var = vars[0].substring(4);
            String parents = vars[2].substring(9);
            if (!parents.equals("none")){
                String [] parents1 = parents.split(",");
                for (String parent : parents1){
                    graph.addEdge(var, parent);
                    graph.addEdge(parent, var);
                }
            }
        }

        return graph;
    }
}
