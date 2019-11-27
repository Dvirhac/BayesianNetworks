import java.io.*;

public class main {

        public static void main(String[] args) throws IOException {


                Graph graph = new Graph(GraphBuilder.createGraphFromText("C:\\Users\\Dvir\\IdeaProjects\\BayesianNetworks\\src\\input.txt"));
                System.out.println(graph.isIndependent(graph.getIsIndepenentqueries().get(0)));
                System.out.println();

                /*for (Vertex vertex : graph.getVertices()){
                        System.out.println(vertex.getMyCPT().toString());
                }*/

        }
}
