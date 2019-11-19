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
            vertices.stream().forEach(e -> e.getChildren().remove(v1));
            vertices.stream().forEach(e -> e.getParents().remove(v1));

            vertices.remove(v1);
        }
    }

    void addChildrenEdge(String label1, String label2) {

        Vertex v1 = findNodeByName(label1);
        Vertex v2 = findNodeByName(label2);

        if (v1!= null && v2!= null && !v1.hasEdge(v2,"")){
            v1.addChild(v2);
        }
    }

    void addParentEdge(String label1, String label2) {

        Vertex v1 = findNodeByName(label1);
        Vertex v2 = findNodeByName(label2);

        if (v1!= null && v2!= null && !v1.hasEdge(v2,"parent")){
            v1.addParent(v2);
        }
    }

    void removeEdge(String label1, String label2, String type) {
        Vertex v1 = findNodeByName(label1);
        Vertex v2 = findNodeByName(label2);
        if (v1 != null && v2 != null) {
            if (type.equals("parent"))
                v1.getParents().remove(v2);
            else {
                v1.getChildren().remove(v2);
            }
        }
    }


    public void setAdjVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public void setEvidence(String [] evidences){
        for (String evidence : evidences){
            for (Vertex vertex : vertices){
                if (vertex.getLabel().equals(evidence)){
                    vertex.setEvidence(true);
                }
            }
        }
    }

    Vertex findNodeByName(String label){
        Iterator it = vertices.iterator();
        while (it.hasNext()){
            Vertex vertex = (Vertex) it.next();
            if (vertex.getLabel().equals(label)) return vertex;
        }
        return null;
    }

    private boolean bayes_ball(String start, String target){

        Queue<Vertex> queue = new LinkedList<>();
        Vertex startVex = findNodeByName(start);
        startVex.setVisited(true);
        queue.add(startVex);
        while (!queue.isEmpty()){
            Vertex vertex = queue.poll();
            if (vertex.getLabel().equals(target)) {
                return false;
            }

            for (Vertex child : vertex.getChildren()) {
                if (child.getLabel().equals(target)) return false;
                if (!child.isVisited()) {
                    child.setVisited(true);
                    if (child.isEvidence()) {
                        queue.addAll(child.getParents());
                    } else {
                        queue.addAll(child.getChildren());
                    }
                }
            }

            for (Vertex parent : vertex.getParents()) {
                if (parent.getLabel().equals(target)) return false;
                if (!parent.isVisited()) {
                    parent.setVisited(true);
                    if (!parent.isEvidence()) {
                        queue.add(parent);
                    }
                }
            }

        }
        return true;
    }

    public boolean isIndependent(String source, String target) {
        return bayes_ball(source, target);
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

}
