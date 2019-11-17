import java.util.*;


public class Graph {

    private List<Vertex> vertices;

    Graph(){
        this.vertices = new ArrayList<>();
    }
    Graph (Graph graph){
       this.vertices = graph.vertices;
    }
    void addVertex(String label) {
        Vertex vertex = new Vertex(label);
        vertices.add(vertex);

    }

    void removeVertex(String label) {
        Vertex v1 = findNodeByName(label);
        if (v1 != null) {
            vertices.stream().forEach(e -> e.getAdj().remove(v1));
            vertices.remove(v1);
        }
    }

    void addEdge(String label1, String label2) {

        Vertex v1 = findNodeByName(label1);
        Vertex v2 = findNodeByName(label2);

        if (v1!= null && v2!= null && !v1.hasEdge(v2)){
            v1.getAdj().add(v2);
        }
    }

    void removeEdge(String label1, String label2) {
        Vertex v1 = findNodeByName(label1);
        Vertex v2 = findNodeByName(label2);
        if (v1 != null && v2 != null) {
            v1.getAdj().remove(v2);
        }
    }


    public void setAdjVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    Vertex findNodeByName(String label){
        Iterator it = vertices.iterator();
        while (it.hasNext()){
                Vertex vertex = (Vertex) it.next();
                if (vertex.getLabel().equals(label)) return vertex;
        }
        return null;
    }

    void print() {

        for (Vertex v: vertices){
            System.out.print(v.getLabel() +" : [ ");
            for (Vertex vex: v.getAdj()){
                System.out.print(vex.getLabel()+" , ");
            }
            System.out.println("]");
        }
    }
}
