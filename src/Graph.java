import java.util.*;


public class Graph {

    private List<Vertex> vertices;
    private ArrayList<String[]> isIndepenentqueries;
    private ArrayList<String[]> probs;



    Graph(){
        this.vertices = new ArrayList<>();
        this.isIndepenentqueries = new ArrayList<>();
        this.probs = new ArrayList<>();

    }
    Graph (Graph graph){
        this.vertices = graph.vertices;
        this.isIndepenentqueries = graph.isIndepenentqueries;
        this.probs = graph.probs;

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

    private boolean bayes_ball(String[] sourceAndTarget, String[] evidances){
        if (evidances != null){
            for (String label : evidances){
                Vertex evidance = findNodeByName(label);
                evidance.setEvidence(true);
            }
        }
        Queue<Vertex> queue = new LinkedList<>();
        String start = sourceAndTarget[0];
        String target = sourceAndTarget[1];
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

    public boolean isIndependent(String[] query)
    {
        for (Vertex vex: this.vertices) {
            vex.setEvidence(false);
        }

        String[] sourceAndTarget = query[0].split("-");
        if (query.length > 1){
            StringBuilder evidancess = new StringBuilder();
            for (int i = 0 ; i < query[1].length(); i ++){
                if (query[1].charAt(i) >= 60 || query[1].charAt(i) <= 60){
                    evidancess.append(query[1].charAt(i)).append(",");
                }
                String [] evidances = evidancess.toString().split(",");
                return bayes_ball(sourceAndTarget,evidances);
            }
        }
        String[] empty = null;
        return bayes_ball(sourceAndTarget,empty);
    }

    public List<Vertex> getVertices() {
        return vertices;
    }
    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }
    public ArrayList<String[]> getIsIndepenentqueries() { return isIndepenentqueries; }
    public void setIsIndepenentqueries(ArrayList<String[]> isIndepenentqueries) { this.isIndepenentqueries = isIndepenentqueries; }
    public ArrayList<String[]> getProbs() { return probs; }
    public void setProbs(ArrayList<String[]> probs) { this.probs = probs; }
}
