
import java.io.IOException;
import java.util.ArrayList;

public class GraphBuilder {
    static Graph graph = new Graph();
    static ArrayList<String> queries = new ArrayList<>();

    /**
     *
     * @param path
     * @return graph with all the Vertices, edges, cpt's and queries that were given in the text.
     * @throws IOException
     */
    //////////////////////////////////////////////////////////////////////////////
    /////////////////////////Adding the vertices into the graph///////////////////
    //////////////////////////////////////////////////////////////////////////////

    static Graph createGraphFromText(String path)   {
        String [] dataSet = new String[0];
        try {
            dataSet = MyFile.getData(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] vars = dataSet[0].split("\\r?\\n");
        String[] vertices = vars[1].split(",");
        vertices[0] = vertices[0].substring(11);
        for ( int i = 0 ; i < vertices.length; i++){
            graph.addVertex(vertices[i]);
        }

            ///////////////////////////////////////////////////////////////////////////
            //////////////////////Adding the edges between the vertices////////////////
            ///////////////////////////////////////////////////////////////////////////

        for (int i = 1; i < dataSet.length -1 ; i++){
            MyCpt myCpt = new MyCpt();
            vars = dataSet[i].split("\\r?\\n");
            String values = vars[1].substring(8);
            String [] vexValues = values.split(",");
            String var = vars[0].substring(4);
            Vertex currVex = graph.findNodeByName(var);
            String parents = vars[2].substring(9);
            if (parents.equals("none")) {
                parents = "";
            }
                currVex.setParents_st(parents);
            if (!parents.equals("none")){
                String [] parents1 = parents.split(",");
                for (String parent : parents1){
                    graph.addParentEdge(var, parent);
                    graph.addChildrenEdge(parent, var);
                }
            }
            //////////////////////////////////////////////////////////////////////////
            //////////////////////////////////Init MyCpt////////////////////////////////
            //////////////////////////////////////////////////////////////////////////

            myCpt.build(vars);
            currVex.setMyCPT(myCpt);
            currVex.setValues(vexValues);
        }

            //////////////////////////////////////////////////////////////////////////
            //////////////////////////////////Init Queries////////////////////////////
            //////////////////////////////////////////////////////////////////////////

        String blockQuery = dataSet[dataSet.length-1];
        String[] queries_array = blockQuery.split("\n");
        for (int i = 1 ; i < queries_array.length ; i ++){
           queries.add(queries_array[i]);
        }
        graph.setQueries(queries);
        return graph;
    }
}
