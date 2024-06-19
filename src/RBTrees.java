import java.util.LinkedList;
import java.util.List;

/**
 * This class models a Red Black Tree
 *
 * @param <T> A comparable generic data type
 */
public class RBTrees<T extends Comparable<T>> implements SortedCollectionInterface<T> {
  /**
   * A class modelling a red black tree node
   *
   * @param <T> a generic comparable parameter
   */
  protected static class Node<T extends Comparable<T>> {

    /**
     * the data stored in the node
     */
    T data;
    /**
     * the array of nodes which determine the parent (context 0), left child (context 1), and right
     * child (context 2) for a red black tree
     */
    Node<T>[] context;
    static boolean color;
    /**
     * the blackHeight value of the current node
     */
    int blackHeight;

    /**
     * The constructor of this class
     *
     * @param data        the data
     * @param context     the context
     * @param blackHeight the black height
     */
    public Node(T data, Node<T>[] context, int blackHeight) {
      this.data = data;
      this.context = context;
      this.blackHeight = blackHeight;
    }

    /**
     * Returns true if this is the right child
     *
     * @return true if this is the right child
     */
    public boolean isRightChild() {
      if (context[0] == null) {
        return false;
      }
      return context[0].context[2] == this;
    }

    /**
     * Returns true if this node is the root
     *
     * @return true if this node is
     */
    public boolean isRoot() {
      return context[0] == null;
    }

    /**
     * To string method to print this nodes data with color
     *
     * @return a string representation of this node
     */
    @Override
    public String toString() {
      String color;
      if (blackHeight == 0) {
        color = "\u001B[31m";
      } else if (blackHeight == 1) {
        color = "\u001B[37m";
      } else {
        color = "\u001B[36m";
      }
      if(Node.color)
        return color + data.toString() + "\u001B[0m";
       return data.toString()+" ("+blackHeight+")";
    }
  }


  private Node<T> root; // The node storing the root of the red black tree
  private int size; // The size of the RB tree

  /**
   * Constructor for the red black tree
   */
  public RBTrees() {
    // Instantiates the size and root
    root = null;
    size = 0;
  }

  /**
   * Returns the root
   * @return the root
   */
  protected Node<T> getRoot(){
    return root;
  }

  /**
   * Insert method for the red black tree
   *
   * @param newData The data to add to the Red black tree
   * @return true if the insert operation was successful
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean insert(T newData) {
    // If the root is null, then add the data to the root
    if (root == null) {
      root = new Node<>(newData, (Node<T>[]) new Node[3], 1); // Instantiates the root to be a
      // black node
    } else {
      // Otherwise find the place to insert the root
      Node<T> currNode = root;
      // Instantiate the new node to enter the tree to be a new red node
      Node<T> newNode = new Node<>(newData, (Node<T>[]) new Node[3], 0);

      // While an appropriate location is not found,
      while (true) {
        // If the newData is more than the current nodes data
        if (currNode.data.compareTo(newData) < 0) {
          // check if right node is null
          if (currNode.context[2] == null) {
            // if null, assign the right child to be the new node
            currNode.context[2] = newNode;
            // break the while loop
            break;
          }
          // otherwise, change the current node to be the right node and continue to next iteration
          currNode = currNode.context[2];
          // similar process with the left node...
        } else if (currNode.data.compareTo(newData) > 0) {
          if (currNode.context[1] == null) {
            currNode.context[1] = newNode;
            break;
          }
          currNode = currNode.context[1];
        } else { // return false if the value is same ...
          return false;
        }
      }
      // assign the new node's parent to be the currNode
      newNode.context[0] = currNode;

      // call the red black tree insertBalancer on the inserted node to balance the tree
      insertBalancer(newNode);
    }

    size++;
    return true;
  }


  /**
   * Returns the sibling of the current node
   *
   * @param node The node whose sibling we are looking for
   * @return The sibling of the current node
   * @throws IllegalStateException if the node is root
   */
  private Node<T> getSibling(Node<T> node) {
    if (node.isRoot()) {
      throw new IllegalStateException("The node is parent");
    }

    // if the node is right child, instantiate the sibling context to be the other nodes context
    int siblingContext = (node.isRightChild()) ? 1 : 2;
    // return the node's sibling
    return node.context[0].context[siblingContext];
  }

  /**
   * The insert balancer function which balances the tree on the inserted node
   *
   * @param insertedNode the node which was inserted in the tree
   */
  private void insertBalancer(Node<T> insertedNode) {
    // If the node is root set the black height 1
    if (insertedNode.isRoot()) {
      insertedNode.blackHeight = 1;
      return;
    } else if (insertedNode.context[0].blackHeight == 1) { // otherwise if it is already valid,
      // do nothing
      return;
    }

    int childContext = (insertedNode.isRightChild()) ? 2 : 1;
    int parentContext = (insertedNode.context[0].isRightChild()) ? 2 : 1;
    Node<T> aunt = getSibling(insertedNode.context[0]);

    // If the aunt is null or is a black node
    if (aunt == null || aunt.blackHeight == 1) {
      // Case 1: If the red parent's aunt is a black, and inserted node and the aunt are on
      // different sides, ie if the child and parent have the same context
      if (childContext == parentContext) {
        rotateNodes(insertedNode.context[0].context[0], insertedNode.context[0]);
      } else { // Case 2: If the parent and child have different contexts, ie they are not on the
        // same side:
        rotateNodes(insertedNode.context[0], insertedNode); // Rotate the parent and child, so
        // they are on same side now
        // Perform same rotation as case 1
        rotateNodes(insertedNode.context[0], insertedNode);
      }
    } else { // Case 3: If the aunt is a red node
      // Switch the aunt and parent to be black
      aunt.blackHeight = 1;
      insertedNode.context[0].blackHeight = 1;
      // Switch the grand Parent to be red to maintain balance
      insertedNode.context[0].context[0].blackHeight = 0;
      // Call insertBalancer on the grand parent
      insertBalancer(insertedNode.context[0].context[0]);
    }

  }

  /**
   * Performs a valid RB Tree rotation so that the black height is not affected
   *
   * @param parent the parent node
   * @param child  the child node
   * @throws IllegalArgumentException if the parent and child do not have a relationship
   */
  private void rotateNodes(Node<T> parent, Node<T> child) throws IllegalArgumentException {

    if (child.context[0] != parent || (parent.context[1] != child && parent.context[2] != child)) {
      throw new IllegalArgumentException(
          "The nodes do not have a doubly linked parent child " + "relationship");
    }

    // Rotates the child and parent
    int childContext = (child.isRightChild()) ? 2 : 1;
    int hoppingChild = (child.isRightChild()) ? 1 : 2;
    parent.context[childContext] = child.context[hoppingChild];
    if (child.context[hoppingChild] != null) {
      child.context[hoppingChild].context[0] = parent;
    }
    child.context[hoppingChild] = parent;
    replaceNodes(parent, child);
    parent.context[0] = child;


    // Switches their black heights
    int parentHeight = parent.blackHeight;
    parent.blackHeight = child.blackHeight;
    child.blackHeight = parentHeight;


  }

  /**
   * A helper method to replace a node with another node in the given tree
   *
   * @param nodeToReplace     the node to replace in the given rb tree
   * @param nodeToReplaceWith the node to replace a given node with in the rb tree (null to remove)
   */
  private void replaceNodes(Node<T> nodeToReplace, Node<T> nodeToReplaceWith) {
    if (root == nodeToReplace) {
      root = nodeToReplaceWith;
      if (nodeToReplaceWith != null) {
        nodeToReplaceWith.context[0] = null;
      }
    } else {
      int replaceContext = (nodeToReplace.isRightChild()) ? 2 : 1;
      nodeToReplace.context[0].context[replaceContext] = nodeToReplaceWith;
      if (nodeToReplaceWith != null) {
        nodeToReplaceWith.context[0] = nodeToReplace.context[0];
      }
    }
  }

  /**
   * Remove method which removes the data
   *
   * @param data the data to remove
   * @return true if the data was successfully removed
   */
  @Override
  public boolean remove(T data) {

    if(root == null){
      return false;
    }
    // Code to find the data
    Node<T> currNode = root;
    while (currNode.data.compareTo(data) != 0) {
      if (currNode.data.compareTo(data) < 0) {
        currNode = currNode.context[2];
      } else {
        currNode = currNode.context[1];
      }
      if (currNode == null) {
        return false;
      }
    }

    // If the node to remove is a leaf node
    if (currNode.context[1] == null && currNode.context[2] == null) {
      // If the node is red
      if (currNode.blackHeight == 0) {
        // replace it with null
        replaceNodes(currNode, null);
      } else {
        // If the node is a black root
        if (currNode == root) {
          // set root to null
          root = null;
        } else {
          // otherwise make the current node double black
          currNode.blackHeight++;
          // remove the double black
          removeDoubleBlack(currNode);
          // replace the removed double black node with null
          replaceNodes(currNode, null);
        }
      }
    } else if (currNode.context[1] != null &&
        currNode.context[2] != null) { // If the node has 2 children
      Node<T> successor = getSuccessor(currNode); // gets the successor of the current node
      remove(successor.data); // Removes the successor from the tree
      size++; // in order to fix the offset caused by above remove operation

      // Shifts the successor and currNode's children to the successor
      successor.context[1] = currNode.context[1];
      successor.context[2] = currNode.context[2];

      replaceNodes(currNode, successor); // replaces the current node with the successor in effect
      // removing the currNode

      // Assigns the successors new children's parent to successor if they exist
      if(successor.context[1] != null)
        successor.context[1].context[0] = successor;
      if(successor.context[2]!=null)
        successor.context[2].context[0] = successor;

      successor.blackHeight = currNode.blackHeight; // Replaces the black heights to not cause any
      // problems
    } else { // If the node has 1 child
      int childIndex = (currNode.context[1] != null) ? 1 : 2;
      currNode.context[childIndex].blackHeight++; // Convert child into a black node
      replaceNodes(currNode, currNode.context[childIndex]); // replace the current node with child
      // node
    }
    size--;
    return true;
  }

  /**
   * Gets the successor of a node <b>WITH A CHILD</b>
   *
   * @param node the node whose successor is to be found
   * @return the successor of the node
   */
  private Node<T> getSuccessor(Node<T> node) {
    Node<T> currNode = node.context[2]; // Successor is the leftmost child of the right child
    while (currNode.context[1] != null) {
      currNode = currNode.context[1]; // keep changing to the left node until a leaf is found
    }
    return currNode;
  }

  /**
   * A private helper method to remove double blacks
   *
   * @param doubleBlackNode removes the double black node
   */
  private void removeDoubleBlack(Node<T> doubleBlackNode) {
    if (doubleBlackNode == root) {
      doubleBlackNode.blackHeight--;
      return;
    }
    Node<T> sibling = getSibling(doubleBlackNode);
    Node<T> parent = doubleBlackNode.context[0];

    // Case 3: If the sibling is black and both  children are not red nodes
    if (sibling.blackHeight == 1 &&
        (sibling.context[1] == null || sibling.context[1].blackHeight != 0) &&
        (sibling.context[2] == null || sibling.context[2].blackHeight != 0)) {
      sibling.blackHeight--; // Change sibling to be a red node
      doubleBlackNode.blackHeight--; // change the double blacks black-height
      parent.blackHeight++; // Increment the parent to account for the deficit black height
      if (parent.blackHeight == 2) { // call remove double black on parent
        removeDoubleBlack(parent);
      }
    } else if (sibling.blackHeight == 0) { // Case 2: If the siblings is red
      // rotate the parent and sibling
      rotateNodes(parent, sibling);
      // call this method again, as we have one of the solved cases now
      removeDoubleBlack(doubleBlackNode);
    } else { // Case 3: If sibling is black and one of its child is red
      int doubleBlackContext = (doubleBlackNode.isRightChild()) ? 2 : 1;
      int oppositeContext = (doubleBlackContext == 2) ? 1 : 2;
      // if the siblings opposite context child is not null, and is red, then
      if (sibling.context[oppositeContext] != null &&
          sibling.context[oppositeContext].blackHeight == 0) {
        rotateNodes(parent, sibling); // rotate parent and sibling
        sibling.context[oppositeContext].blackHeight++;// Convert that sibling node to a black node
        doubleBlackNode.blackHeight--; // remove the double black height

      } else { // otherwise rotate the siblings child once and then perform the same operation
        rotateNodes(sibling, sibling.context[doubleBlackContext]);
        removeDoubleBlack(doubleBlackNode); // call double black again now.
      }
    }

  }

  /**
   * True if the tree contains the given data
   *
   * @param data the data
   * @return true if it contains the data
   */
  @Override
  public boolean contains(T data) {
    Node<T> currNode = root;
    while (currNode != null) {
      if (currNode.data.compareTo(data) == 0) {
        return true;
      } else if (currNode.data.compareTo(data) < 0) {
        currNode = currNode.context[2];
      } else {
        currNode = currNode.context[1];
      }
    }
    return false;
  }

  /**
   * Returns the size of the tree
   *
   * @return the size
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Returns true if the tree is empty
   *
   * @return true if the tree is empty
   */
  @Override
  public boolean isEmpty() {
    return size == 0;
  }


  /**
   * Converts the tree into a string
   *
   * @return a string representation of this tree
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    // toStringHelper1(0, sb, root);
    toStringHelper("", root, sb, false);
    return sb.toString();
  }


  /**
   * Helper function to convert in string
   *
   * @param prefix  the prefix to build
   * @param node    the node
   * @param builder the string builder
   * @param isLeft  checks if the current node is left
   */
  private void toStringHelper(String prefix, Node<T> node, StringBuilder builder, boolean isLeft) {
    if (node == null) {
      return;
    }
    builder.append(prefix).append(isLeft ? "|-- " : "\\-- ").append(node).append("\n");
    toStringHelper(prefix + (isLeft ? "|   " : "    "), node.context[1], builder, true);
    toStringHelper(prefix + (isLeft ? "|   " : "    "), node.context[2], builder, false);
  }

  /**
   * The main method
   *
   * @param args Unused Parameters
   */
  public static void main(String[] args) {
    RBTrees<Integer> tree = new RBTrees<>();
    List<Integer> nums = new LinkedList<>();
    int index = 0;
    for (Integer random : new Integer[] {8, 25, 31, 49, 78, 35, 85, 63, 55, 87}) {
      //      int random = (int) (Math.random() * 100);
      nums.add(random);
      tree.insert(random);
    }
    System.out.println(tree + "\n" + nums + "\n" + "=".repeat(50));
    while (!tree.isEmpty()) {
      int num = nums.get(index++);
      tree.remove(num);
      System.out.println("Tree after removing " + num + "\n" + tree + "\n" + "=".repeat(50));
    }
  }
}

