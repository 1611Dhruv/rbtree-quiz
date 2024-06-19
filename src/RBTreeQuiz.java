import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * This class models a quiz
 */
public class RBTreeQuiz {

  private List<Integer> values;
  private Random random;
  private RBTrees<Integer> rbTree;
  private static int RANGE_VAL = 100;
  private static int LOWER_BOUND = 1;
  private static int AVERAGE_SIZE = 7;

  public static void saveToFile(File f, String str) {
    try (FileWriter fw = new FileWriter(f)) {
      fw.write(str);
    } catch (IOException e) {
      // nothing
    }
  }

  public RBTreeQuiz() {
    random = new Random();
    rbTree = new RBTrees<>();
    values = new LinkedList<>();

    RBTrees.Node.color = false;
    int initialAmount =
        random.nextInt(AVERAGE_SIZE) + 1; // initial amount of the tree will be from 1 to
    // Average_size
    while (rbTree.size() < initialAmount) {
      if (isRemove()) {
        Integer randomElementToRemove = values.remove(random.nextInt(values.size()));
        rbTree.remove(randomElementToRemove);
      } else {
        Integer randomInt = random.nextInt(RANGE_VAL) + LOWER_BOUND;
        rbTree.insert(randomInt);
        values.add(randomInt);
      }
    }
  }

  public void askQuestion() {
    if (isRemove()) {
      Integer randomElementToRemove = values.remove(random.nextInt(values.size()));
      rbTree.remove(randomElementToRemove);
      System.out.println("How will the tree look when (" + randomElementToRemove + ") is removed?");
    } else {
      Integer randomInt = random.nextInt(RANGE_VAL) + LOWER_BOUND;
      while (values.contains(randomInt)) {
        randomInt = random.nextInt(RANGE_VAL) + LOWER_BOUND;
      }
      rbTree.insert(randomInt);
      values.add(randomInt);
      System.out.println("How will the tree look when (" + randomInt + ") is inserted?");
    }
  }


  public boolean isRemove() {
    int currEvent = random.nextInt(100); // 0 to 99
    int probabilityRemove = 50 * rbTree.size() / AVERAGE_SIZE;
    return currEvent < probabilityRemove;
  }

  public void runQuiz() {
    File file = new File("result.txt");
    saveToFile(file, rbTree.toString());
    System.out.println("Welcome to RBT Quiz.");
    System.out.println("You will be asked a series of remove or insert questions and you will " +
        "have to answer them in the result.txt file by editing the file.");
    System.out.println("The Red Black tree in the file is denoted in the following format: ");
    System.out.println("""
                       \\-- parent (0/1)
                            |-- lChild (0/1)
                            |   |-- llChild (0/1)
                            |   \\-- lrChild (0/1)
                            \\-- rChild (0/1)
                       """);
    System.out.println("Where 0/1 indicate the colours of the nodes (0 is red, 1 is black)");
    System.out.println("It is recommended that you use vim or neovim for this quiz or any other " +
        "text editor of your liking. \nBe sure to reload the file if promoted to.");
    System.out.println("Press submit only after saving your result in the file.");
    Scanner sc = new Scanner(System.in);
    int questionCounter = 1;
    while (true) {
      System.out.println("\n\n\n");
      System.out.println("Q" + questionCounter++ + ")");
      askQuestion();
      System.out.print("(Press enter if you are done editing your answer or press any other " +
          "key and enter to quit the quiz)\n>");
      String input = sc.nextLine();
      if (!input.isBlank()) {
        break;
      }
      boolean isCorrect = checkAnswer(file);
      if (isCorrect) {
        System.out.println("\n\u001B[32mCorrect Answer\u001B[0m 🥳🥳🥳");
      } else {
        RBTrees.Node.color = true;
        System.out.println("\n\u001B[31mIncorrect Answer...\u001B[0m\n  The correct answer has " +
            "been written in your " + "result file.\n it was:\n" + rbTree.toString());
        RBTrees.Node.color = false;
        saveToFile(file, rbTree.toString());
      }
    }
    System.out.println("Do you wish to keep results.txt? [Y/N]");
    if (sc.next().startsWith("Y")) {
      System.out.println("Kept results.txt");
    } else {
      file.delete();
      System.out.println("Deleted results.txt");
    }
    System.out.println("\nThank you for taking the RBT quiz");
  }


  private boolean checkAnswer(File file) {
    String rbString = rbTree.toString();
    Scanner rbScanner = new Scanner(rbString);
    try (Scanner fileScanner = new Scanner(file)) {
      while (rbScanner.hasNextLine()) {
        if (!fileScanner.hasNextLine()) {
          return rbScanner.nextLine().isBlank();
        }
        String rLine = rbScanner.nextLine();
        rLine = rLine.replaceAll("[^(0-9)]*", "");
        String fLine = fileScanner.nextLine();
        fLine = fLine.replaceAll("[^(0-9)]*", "");
        if (!fLine.equals(rLine)) {
          return false;
        }
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    return true;
  }

  /**
   * The main method
   *
   * @param args Unused Parameters
   */
  public static void main(String[] args) {
    if (args.length != 0) {
      try {
        RBTreeQuiz.AVERAGE_SIZE = Integer.parseInt(args[0]);
        RBTreeQuiz.LOWER_BOUND = Integer.parseInt(args[1]);
        RBTreeQuiz.RANGE_VAL = AVERAGE_SIZE * 4;
      } catch (Exception e) {
        System.out.println(
            "Average size or Lower bound could not be could not be successfully " + "imported");
      }
    }
    new RBTreeQuiz().runQuiz();
  }

}
