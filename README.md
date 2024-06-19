# RedBlackTree Quiz

## Overview

RedBlackTree Quiz is an interactive text-based application where players engage in a quiz format to perform Red-Black Tree insertions and removals. This project aims to enhance understanding of Red-Black Tree concepts through an engaging gaming experience.

## Skills Utilized

- Java
- Command Prompt
- Data Structures
- Quiz
- File Management
- Game Development

## Features

- **Interactive Quiz Format**: Engage in a series of Red-Black Tree insertion and removal questions.
- **Text-Based Interaction**: Answer questions by editing a text file (`results.txt`), simulating real Red-Black Tree manipulations.
- **Visualization Aid**: The Red-Black Tree is displayed in a clear, hierarchical format with color indications.
- **Feedback and Evaluation**: Receive immediate feedback on your answers after submission.

## Requirements

- Java Development Kit (JDK)
- JUnit 5 (for testing)

## Setup and Usage

### Compiling and Building the JAR

To compile the Java files and build the JAR file, run:

```sh
make all
```

### Running the Application

To run the application, use:

```sh
make run
```

Or directly using:

```sh
java -jar out/RBTreeQuiz.jar
```

### Running the Tests

To run the tests, use:

```sh
make runTests
```

### Cleaning Up

To clean up the compiled files and the generated JAR file, use:

```sh
make clean
```

## How to Use the Application

1. Run the application using the provided commands.
2. You will be prompted with a series of questions about Red-Black Tree insertions and removals.
3. Answer the questions by editing the `results.txt` file that will be created in the application's directory.
4. The Red-Black Tree in the file is denoted in the following format:
   ```
   \-- parent (0/1)
        |-- lChild (0/1)
        |   |-- llChild (0/1)
        |   \-- lrChild (0/1)
        \-- rChild (0/1)
   ```
   Where `0/1` indicate the colors of the nodes (`0` is red, `1` is black).
5. It is recommended to use `vim`, `neovim`, or any other text editor of your liking. Be sure to reload the file if prompted to.
6. Press submit only after saving your result in the file.
7. Follow the on-screen instructions to continue or quit the quiz.

## Contact

For any queries or support, feel free to reach out to the project maintainers.

Enjoy enhancing your understanding of Red-Black Trees through the RedBlackTree Quiz!

## License

This project is licensed under the MIT License - see the LICENSE file for details.
