package main;

public class Node implements Comparable<Node> {
    private final int node;
    private final int cost;

    public Node(int node,int cost) {
        this.node = node;
        this.cost = cost;
    }

    public int getNode() {
        return node;
    }

    @Override
    public int compareTo(Node node) {
        return Integer.compare(this.cost,node.cost);
    }
}