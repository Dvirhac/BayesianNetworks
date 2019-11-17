import java.io.*;


public class main {

        public static void main(String[] args) throws IOException {


                Graph graph = new Graph(GraphBuilder.createGraphFromText("C:\\Users\\Dvir\\IdeaProjects\\BayesianNetworks\\src\\input.txt"));
                graph.print();


        }
}
