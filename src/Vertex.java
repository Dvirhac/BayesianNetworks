import java.util.ArrayList;
import java.util.List;

public class Vertex {

    private String label;
    private boolean isVisited = false;
    private boolean isEvidence = false;
    private List<Vertex> parents;
    private List<Vertex> children;
    private MyCPT myCPT;

    Vertex(String label){
        this.label = label;
        parents = new ArrayList<>();
        children = new ArrayList<>();
        myCPT = new MyCPT();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    Vertex getVertexByName(String label){
        if (this.label.equals(label)) {
            return this;
        }
        else return null;
    }

    public void addParent(Vertex v){
        this.parents.add(v);
    }

    public void addChild(Vertex v){
        this.children.add(v);
    }

    public boolean hasEdge(Vertex v2, String type) {
        if (type.equals("parent")){
            for (Vertex vertex : parents){
                if (v2.label.equals(vertex.label)){
                    return true;
                }
            }
        }
        else {
            for (Vertex vertex : children){
                if (v2.label.equals(vertex.label)){
                    return true;
                }
            }
        }

        return false;
    }

    public List<Vertex> getChildren() {
        return children;
    }

    public void setChilds(List<Vertex> childs) {
        this.children = childs;
    }

    public List<Vertex> getParents() {
        return parents;
    }

    public void setParents(List<Vertex> parents) {
        this.parents = parents;
    }


    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public boolean isEvidence() {
        return isEvidence;
    }

    public void setEvidence(boolean evidence) {
        isEvidence = evidence;
    }

    public void setChildren(List<Vertex> children) {
        this.children = children;
    }


    public MyCPT getMyCPT() {
        return myCPT;
    }

    public void setMyCPT(MyCPT myCPT) {
        this.myCPT = myCPT;
    }

}
