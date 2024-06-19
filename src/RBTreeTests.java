import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 */
public class RBTreeTests {

  private RBTrees<Integer> tree;
  List<Integer> orderToAdd;
  List<Integer> orderToRemove;
  int expectedSize;


  /**
   * Initializes a tree, an order to add in the tree, an order to remove from the tree, and shuffles
   * a random order
   */
  @BeforeEach
  public void init() {
    tree = new RBTrees<>();
    orderToAdd = new ArrayList<>();
    orderToRemove = new ArrayList<>();
    expectedSize = (int) (Math.random() * 3000) + 1;
    HashSet<Integer> seen = new HashSet<>();
    for (int i = 0; i < expectedSize; i++) {
      int randomNum = (int) (Math.random() * 100000);
      if(seen.contains(randomNum)){
        i--;
        continue;
      }
      orderToAdd.add(randomNum);
      orderToRemove.add(randomNum);
      seen.add(randomNum);
    }
    Collections.shuffle(orderToRemove);
  }


  /**
   * Test to check if insert works properly and after each insert the tree is still connected and is
   * still having same black heights
   */
  @Test
  public void insertCheck() {
    for (Integer i : orderToAdd) {
      tree.insert(i);
      assertTrue(blackHeightCheck(tree.getRoot()), "The black heights were not the same\n" + tree);
      assertTrue(tree.getRoot().context[0] == null && checkIfAllNodesConnected(tree.getRoot()),
          "All nodes were not connected for tree\n" + tree);
    }
    assertEquals(tree.size(), expectedSize, "The size was not same");
  }

  /**
   * Test to check if remove works properly and after each remove the tree is still connected and is
   * still having same black heights
   */
  @Test
  public void removeCheck() {
    for (Integer i : orderToAdd) {
      tree.insert(i);
    }

    for (Integer i : orderToRemove) {
      tree.remove(i);
      assertTrue(blackHeightCheck(tree.getRoot()), "The black heights were not the same\n" + tree);
      assertTrue(tree.getRoot() == null ||
              tree.getRoot().context[0] == null && checkIfAllNodesConnected(tree.getRoot()),
          "All nodes were not connected for tree\n" + tree);
    }
    assertEquals(tree.size(), 0, "The size was not 0");
    assertTrue(tree.isEmpty(), "The size was not empty");
  }

  /**
   * Private helper method which checks if all nodes are connected and no red nodes are connected
   * together
   *
   * @param node the node from where we need to start checking
   * @return true if all nodes in the tree are property linked
   */
  private boolean checkIfAllNodesConnected(RBTrees.Node<Integer> node) {
    if (node == null) {
      return true;
    }
    if (node.context[1] != null && node.context[1].context[0] != node) {
      return node.blackHeight == 1 || node.context[1].blackHeight == 1;
    }
    if (node.context[2] != null && node.context[2].context[0] != node) {
      return node.blackHeight == 1 || node.context[2].blackHeight == 1;
    }

    return checkIfAllNodesConnected(node.context[1]) && checkIfAllNodesConnected(node.context[2]);
  }


  /**
   * Checks if the black height for both sides of the tree is the same
   *
   * @param root the root of the tree
   * @return true if black heights of left and right subtrees are same
   */
  private boolean blackHeightCheck(RBTrees.Node<Integer> root) {
    if (root == null) {
      return true;
    }
    return getBlackHeight(root.context[1]) == getBlackHeight(root.context[2]) &&
        blackHeightCheck(root.context[1]) && blackHeightCheck(root.context[2]);
  }

  /**
   * Returns the black-height under a particular node
   *
   * @param node the node under whose black-height should be returned
   * @return the number of black-nodes under the node including it
   */
  private int getBlackHeight(RBTrees.Node<Integer> node) {
    if (node == null) {
      return 0;
    }
    return Math.max(getBlackHeight(node.context[1]), getBlackHeight(node.context[2])) +
        node.blackHeight;
  }



}
