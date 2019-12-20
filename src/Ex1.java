import java.io.*;

    public class Ex1 {

        public static void main(String[] args)  {
            Graph graph = new Graph(GraphBuilder.createGraphFromText("input5.txt"));
            PrintWriter out = null;
            try {
                out = new PrintWriter("output.txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (String query : graph.getQueries()){

                if (query.charAt(0) != 'P'){
                    String [] curr = query.split("\\|");
                    out.println(graph.isIndependent(curr));
                }
                else {
                    Probability probability = new Probability(graph, query);
                    out.println(probability.start());
                }
            }
            out.close();
        }
}
