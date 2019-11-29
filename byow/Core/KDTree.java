package byow.Core;

import java.util.List;

public class KDTree {
    private Node root;
    private static final boolean HORIZONTAL = false;
    private static final boolean VERTICAL = true;

    private class Node {
        private Point point;
        private boolean orientation;
        private Node leftChild = null;
        private Node rightChild = null;
        Node(Point p, boolean orientation) {
            point = p;
            this.orientation = orientation;
        }
    }

    private int comparePoints(Point a, Point b, boolean orientation) {
        if (orientation == HORIZONTAL) {
            return Double.compare(a.getX(), b.getX());
        }
        return Double.compare(a.getY(), b.getY());
    }

    private void add(Point p, Node n) {
        if (n.point.equals(p)) {
            return;
        }
        int x = comparePoints(p, n.point, n.orientation);
        if (x < 0) {
            if (n.leftChild != null) {
                add(p, n.leftChild);
            } else {
                n.leftChild = new Node(p, !n.orientation);
            }
        } else {
            if (n.rightChild != null) {
                add(p, n.rightChild);
            } else {
                n.rightChild = new Node(p, !n.orientation);
            }
        }
    }

    public KDTree(List<Point> points) {
        root = new Node(points.get(0), HORIZONTAL);
        for (Point p : points.subList(1, points.size())) {
            add(p, root);
        }
    }

    public Point nearest(int x, int y) {
        Point goal = new Point(x, y);
        return nearest(root, goal, root).point;
    }

    private Node nearest(Node n, Point goal, Node best) {
        if (n == null) {
            return best;
        }
        if (Point.distance(n.point, goal) < Point.distance(best.point, goal)) {
            best = n;
        }
        int x = comparePoints(goal, n.point, n.orientation);
        Node good;
        Node bad;
        if (x < 0) {
            good = n.leftChild;
            bad = n.rightChild;
        } else {
            good = n.rightChild;
            bad = n.leftChild;
        }
        best = nearest(good, goal, best);
        if (n.orientation == HORIZONTAL) {
            if (Point.distance(n.point, goal) != Math.pow(n.point.getX() - goal.getX(), 2)
                    && Math.pow(n.point.getX() - goal.getX(), 2)
                    < Point.distance(best.point, goal)) {
                best = nearest(bad, goal, best);
            }
        } else {
            if (Point.distance(n.point, goal) != Math.pow(n.point.getY() - goal.getY(), 2)
                    && Math.pow(n.point.getY() - goal.getY(), 2)
                    < Point.distance(best.point, goal)) {
                best = nearest(bad, goal, best);
            }
        }
        return best;
    }
}
