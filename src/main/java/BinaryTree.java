import java.util.Stack;

class BinaryTree {

    Node root;
    public Stack<String> stack = new Stack<>();

    public void add(Student value) {
        root = addRecursive(root, value);
    }

    private Node addRecursive(Node current, Student value) {
        if (current == null) {
            return new Node(value.getAverage(), value.getName());
        }
        if (value.getAverage() < current.value) {
            current.left = addRecursive(current.left, value);
        } else if (value.getAverage() > current.value) {
            current.right = addRecursive(current.right, value);
        }
        return current;
    }

    public void traverseInOrder(Node node) {
        if (node != null) {
            traverseInOrder(node.left);
            visit(node.value, node.name);
            traverseInOrder(node.right);
        }
    }
    private void visit(double value, String name) {
        stack.push(name + " : " + value + " \n");
    }

    public String beautify() {
        StringBuilder temp = new StringBuilder();
        for (int i = stack.size() - 1; i >= 0; i--) {
            temp.append(stack.get(i));
        }
        System.out.println(temp.toString());
        return temp.toString();
    }
    public void resetStack() {
        stack.clear();
    }
    static class Node {
        double value;
        String name;
        Node left;
        Node right;

        Node(Double value, String name) {
            this.value = value;
            this.name = name;
            right = null;
            left = null;
        }
    }
}