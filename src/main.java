import java.io.*;
import java.util.Arrays;

public class main {

    public static void main(String[] args) throws IOException {
        Graph graph = new Graph(GraphBuilder.createGraphFromText("C:\\Users\\Dvir\\IdeaProjects\\BayesianNetworks\\src\\input.txt"));
        for (String query : graph.getQueries()){
            if (query.charAt(0) != 'P'){
                String [] curr = query.split("\\|");
                graph.isIndependent(curr);
            }
            else {
                Probability probability = new Probability(graph, query);
                probability.start();
            }
        }
    }
}
