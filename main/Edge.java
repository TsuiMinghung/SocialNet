package main;

public class Edge {
    private final int v1;
    private final int v2;
    private final int weight;

    public Edge(int v1,int v2,int weight) {
        this.v1 = v1;
        this.v2 = v2;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            Edge e = (Edge) obj;
            return (v1 == e.v1 && v2 == e.v2) || (v1 == e.v2 && v2 == e.v1);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return v1 * v2 + weight;
    }

    public int getV1() {
        return v1;
    }

    public int getV2() {
        return v2;
    }

    public int getWeight() {
        return weight;
    }
}
