# Directories
SRC_DIR := src
OUT_DIR := out
BIN_DIR := bin

# Main class
MAIN_CLASS := RBTreeQuiz

# JAR file name
JAR_NAME := RBTreeQuiz.jar

# Java compiler
JAVAC := javac

# Java compiler flags
JAVAC_FLAGS := -cp .:$(BIN_DIR):junit5.jar -d $(BIN_DIR)

# Java archive tool
JAR := jar

# Source files
SRC_FILES := $(wildcard $(SRC_DIR)/*.java)

# Class files
CLASS_FILES := $(SRC_FILES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# Dependency order
RBTreeQuiz_CLASS := $(BIN_DIR)/RBTreeQuiz.class
RBTreeTests_CLASS := $(BIN_DIR)/RBTreeTests.class
RBTrees_CLASS := $(BIN_DIR)/RBTrees.class
SortedCollectionInterface_CLASS := $(BIN_DIR)/SortedCollectionInterface.class

# Make all
all: $(OUT_DIR)/$(JAR_NAME)

# Compile Java files in order
$(RBTreeQuiz_CLASS): $(SRC_DIR)/RBTreeQuiz.java $(RBTrees_CLASS) $(SortedCollectionInterface_CLASS)
	@mkdir -p $(BIN_DIR)
	$(JAVAC) $(JAVAC_FLAGS) $(SRC_DIR)/RBTreeQuiz.java

$(RBTreeTests_CLASS): $(SRC_DIR)/RBTreeTests.java $(RBTrees_CLASS) $(SortedCollectionInterface_CLASS)
	@mkdir -p $(BIN_DIR)
	$(JAVAC) $(JAVAC_FLAGS) $(SRC_DIR)/RBTreeTests.java

$(RBTrees_CLASS): $(SRC_DIR)/RBTrees.java $(SortedCollectionInterface_CLASS)
	@mkdir -p $(BIN_DIR)
	$(JAVAC) $(JAVAC_FLAGS) $(SRC_DIR)/RBTrees.java

$(SortedCollectionInterface_CLASS): $(SRC_DIR)/SortedCollectionInterface.java
	@mkdir -p $(BIN_DIR)
	$(JAVAC) $(JAVAC_FLAGS) $(SRC_DIR)/SortedCollectionInterface.java

# Build JAR file
$(OUT_DIR)/$(JAR_NAME): $(CLASS_FILES)
	@mkdir -p $(OUT_DIR)
	$(JAR) cfe $(OUT_DIR)/$(JAR_NAME) $(MAIN_CLASS) -C $(BIN_DIR) .

# Clean up
clean:
	rm -rf $(BIN_DIR) $(OUT_DIR)

run:
	java -jar $(OUT_DIR)/$(JAR_NAME)

runTests:
	java -jar junit5.jar -cp $(BIN_DIR):junit5.jar -c RBTreeTests

.PHONY: all clean
