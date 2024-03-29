import java.util.*;


public class Graph {

    private ArrayList<Vertex> vertices;
    private ArrayList<String> queries = new ArrayList<>();

    Graph(){
        this.vertices = new ArrayList<>();
    }

    Graph (Graph graph){
        this.vertices = graph.vertices;
        this.queries = graph.getQueries();
    }

    /**
     *
     * @param label
     * add new vertex to the graph with the label
     */
    void addVertex(String label) {
        Vertex vertex = new Vertex(label);
        vertices.add(vertex);

    }

    /**
     *
     * @param label1
     * @param label2
     * add parent -> children edge
     */
    void addChildrenEdge(String label1, String label2) {

        Vertex v1 = findNodeByName(label1);
        Vertex v2 = findNodeByName(label2);

        if (v1!= null && v2!= null && !v1.hasEdge(v2,"")){
            v1.addChild(v2);
        }
    }

    /**
     *
     * @param label1
     * @param label2
     * add children -> parent edge
     */
    void addParentEdge(String label1, String label2) {

        Vertex v1 = findNodeByName(label1);
        Vertex v2 = findNodeByName(label2);

        if (v1!= null && v2!= null && !v1.hasEdge(v2,"parent")){
            v1.addParent(v2);
        }
    }

    /**
     *
     * @param label
     * @return the vertex with the same label
     */
    Vertex findNodeByName(String label){
        Iterator it = vertices.iterator();
        while (it.hasNext()){
            Vertex vertex = (Vertex) it.next();
            if (vertex.getLabel().equals(label)) return vertex;
        }
        return null;
    }

    /**
     *
     * @param query
     * @return if the variables are independent given evidences
     */
    public String isIndependent(String[] query)
    {
        resetVertices();
        String[] sourceAndTarget = query[0].split("-");
        if (query.length > 1){
            StringBuilder evidancess = new StringBuilder();
            for (int i = 0 ; i < query[1].length(); i ++) {
                if (query[1].charAt(i) >= 65 && query[1].charAt(i) <= 90) {
                    evidancess.append(query[1].charAt(i)).append(",");
                }
            }
            String [] evidances = evidancess.toString().split(",");
            return bayes_ball(sourceAndTarget,evidances);
        }

        else {
            String[] empty = null;
            return bayes_ball(sourceAndTarget, empty);
        }
    }

    private String bayes_ball(String[] sourceAndTarget, String[] evidances){
        setEvidences(evidances);
        Queue<Vertex> queue = new LinkedList<>();
        String start = sourceAndTarget[0];
        String target = sourceAndTarget[1];
        Vertex startVex = findNodeByName(start);
        for (Vertex child : startVex.getChildren()){
            child.setCameFromParent(true);
            queue.add(child);
        }
        for (Vertex parent: startVex.getParents()){
            parent.setCameFromChild(true);
            queue.add(parent);
        }
        while (!queue.isEmpty()){
            Vertex vertex = queue.poll();
            if (checkTarget(vertex.getLabel(), target)){
                System.out.println("no");
                return "no";
            }
            if (!vertex.isVisited()) {
                if (vertex.isEvidence() && vertex.isCameFromParent()) {
                    for (Vertex parent : vertex.getParents()) {
                        parent.setCameFromChild(true);
                        parent.setCameFromParent(false);
                        queue.add(parent);
                        parent.setVisited(false);
                    }
                } else if (!vertex.isEvidence() && vertex.isCameFromParent()) {
                    for (Vertex child : vertex.getChildren()) {
                        child.setCameFromParent(true);
                        child.setCameFromChild(false);
                        child.setVisited(false);
                        queue.add(child);
                    }
                } else if (!vertex.isEvidence() && vertex.isCameFromChild()) {

                    for (Vertex child : vertex.getChildren()) {
                        child.setCameFromParent(true);
                        child.setCameFromChild(false);
                        queue.add(child);
                    }
                    for (Vertex parent : vertex.getParents()) {
                        parent.setCameFromChild(true);
                        parent.setCameFromParent(false);
                        queue.add(parent);
                    }
                }
            }
            vertex.setVisited(true);
        }
        System.out.println("yes");
        return "yes";

    }

    /**
     * reset the mode of the vertices
     */
    public void resetVertices() {
        for (Vertex vertex : vertices){
            vertex.setVisited(false);
            vertex.setCameFromChild(false);
            vertex.setCameFromChild(false);
            vertex.setEvidence(false);
        }
    }

    /**
     *
     * @param label
     * @param label2
     * @return return if the labels are equal
     */
    boolean checkTarget(String label, String label2){
        return label.equals(label2);
    }

    /**
     * set the evidences vertices
     * @param evidences
     */
    private void setEvidences(String[] evidences) {
        if (evidences != null){
            for (String label : evidences){
                Vertex evidance = findNodeByName(label);
                evidance.setEvidence(true);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////Getters and Setters/////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    public ArrayList<Vertex> getVertices() { return vertices; }
    public ArrayList<String> getQueries() { return queries; }
    public void setQueries(ArrayList<String> queries) { this.queries = queries; }

}
