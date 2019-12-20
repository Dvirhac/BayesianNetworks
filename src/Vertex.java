import java.util.ArrayList;
import java.util.List;

public class Vertex {

    private String label;
    private boolean isVisited = false;
    private boolean isEvidence = false;
    private boolean cameFromParent = false;
    private boolean cameFromChild = false;
    private String parents_st;
    private String[] values;
    private List<Vertex> parents;
    private List<Vertex> children;
    private MyCpt myCPT;

    Vertex(String label){
        this.label = label;
        parents = new ArrayList<>();
        children = new ArrayList<>();
        myCPT = new MyCpt();
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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////Getters & Setters ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<Vertex> getChildren() {
        return children;
    }
    public List<Vertex> getParents() { return parents; }
    public boolean isEvidence() {
        return isEvidence;
    }
    public void setEvidence(boolean evidence) {
        isEvidence = evidence;
    }
    public void setMyCPT(MyCpt myCPT) { this.myCPT = myCPT; }
    public boolean isVisited() { return isVisited; }
    public void setVisited(boolean visited) { isVisited = visited; }
    public boolean isCameFromParent() { return cameFromParent; }
    public void setCameFromParent(boolean cameFromParent) { this.cameFromParent = cameFromParent; }
    public void setValues(String []values) { this.values = values; }
    public String[] getValues() { return values; }
    public boolean isCameFromChild() { return cameFromChild; }
    public void setCameFromChild(boolean cameFromChild) { this.cameFromChild = cameFromChild; }
    public String getLabel() { return label; }
    public MyCpt getMyCPT() { return myCPT; }
    public String getParents_st() { return parents_st; }
    public void setParents_st(String parents_st) { this.parents_st = parents_st; }
}
