import java.util.ArrayList;
import java.util.List;

public class Vertex {

    private String label;
    private List<Vertex> adj;

    public List<Vertex> getParents() {
        return parents;
    }

    public void setParents(List<Vertex> parents) {
        this.parents = parents;
    }

    private List<Vertex> parents;

    public List<Vertex> getAdj() {
        return adj;
    }

    public void setAdj(List<Vertex> adj) {
        this.adj = adj;
    }

    Vertex(String label){
        this.label = label;
        adj = new ArrayList<>();
        parents = new ArrayList<>();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    Vertex getVertexByName(String label){
        if (this.label == label)
            return this;
        else return null;
    }

    public boolean hasEdge(Vertex v2) {
        for (Vertex vertex : adj){
            if (v2.label.equals(vertex.label)){
                return true;
            }
        }
        return false;
    }
}
