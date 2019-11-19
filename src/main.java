import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class main {

        public static void main(String[] args) throws IOException {


                Graph graph = new Graph(GraphBuilder.createGraphFromText("C:\\Users\\Dvir\\IdeaProjects\\BayesianNetworks\\src\\input.txt"));
               // String [] evidances = {"A"};
                //graph.setEvidence(evidances);
                //System.out.println(graph.isIndependent("B", "E"));


                for (Vertex vertex : graph.getVertices()){
                        System.out.println(vertex.getMyCPT().toString());
                }

        }
}
