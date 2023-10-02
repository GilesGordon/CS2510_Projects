import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// represents an edge
class Edge implements Comparable<Edge> {
  int c1;
  int c2;
  int weight;

  // constructors for edge class
  Edge(int c1, int c2) {
    this.c1 = c1;
    this.c2 = c2;
    this.weight = 0;
  }

  Edge(int c1, int c2, int weight) {
    this.c1 = c1;
    this.c2 = c2;
    this.weight = weight;
  }

  // compares the weights of the edges
  @Override
  public int compareTo(Edge o) {
    return this.weight - o.weight;
  }
}

// represents a Queue
class Queue {
  ArrayList<Integer> queue;

  // constructor for Queue
  Queue() {
    this.queue = new ArrayList<Integer>();
  }

  // adds to the back of the queue
  public void add(int c) {
    this.queue.add(c);
  }

  // gets from the front of the queue and removes that int.
  public int get() {
    if (queue.size() > 0) {
      int temp = queue.get(0);
      queue.remove(0);
      return temp;
    }
    else {
      throw new IndexOutOfBoundsException();
    }
  }

  // gets the size of the queue
  public int size() {
    return queue.size();
  }

  // checks the front of the queue without removing
  public int check() {
    if (queue.size() > 0) {
      return queue.get(0);
    }
    else {
      throw new IndexOutOfBoundsException();
    }
  }
}

// class to represent a stack
class Stack {
  ArrayList<Integer> stack;

  // constructor for the stack class
  Stack() {
    this.stack = new ArrayList<Integer>();
  }

  // adds to the top of the stack
  public void add(int c) {
    this.stack.add(c);
  }

  // gets the value from the top of the stack then removes it from the stack
  public int get() {
    if (stack.size() > 0) {
      int temp = stack.get(stack.size() - 1);
      stack.remove(stack.size() - 1);
      return temp;
    }
    else {
      throw new IndexOutOfBoundsException();
    }
  }

  // finds the size of the stack
  public int size() {
    return stack.size();
  }

  // chacks the value of the top of the stack without removing
  public int check() {
    if (stack.size() > 0) {
      return stack.get(stack.size() - 1);
    }
    else {
      throw new IndexOutOfBoundsException();
    }
  }
}

// class to represent a maze creator and searcher
class MazeWorld extends World {
  public static final int FULL_SCENE_SIZE = 900;
  public static final int SCENE_SIZE = FULL_SCENE_SIZE - 100;
  ArrayList<Edge> edgesInTree;
  ArrayList<Edge> worklist; // all edges in graph, sorted by edge weights;
  ArrayList<Edge> toDraw;

  // queue
  Queue worklistBFS;
  ArrayList<Integer> visitedBFS;
  HashMap<Integer, Integer> pathBFS;
  ArrayList<Integer> drawPathBFS;

  // stack
  Stack worklistDFS;
  ArrayList<Integer> visitedDFS;
  HashMap<Integer, Integer> pathDFS;
  ArrayList<Integer> drawPathDFS;

  HashMap<Integer, Integer> representatives;
  Random r;

  int state;
  int sizeX;
  int sizeY;

  // constructs the world with a width and height
  MazeWorld(int sizeX, int sizeY) {
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    this.r = new Random();
    this.representatives = new HashMap<Integer, Integer>();
    this.worklist = new ArrayList<Edge>();
    this.toDraw = new ArrayList<Edge>();
    this.edgesInTree = new ArrayList<Edge>();
    this.state = 0;
    // BFS init
    this.worklistBFS = new Queue();
    this.visitedBFS = new ArrayList<Integer>();
    this.pathBFS = new HashMap<Integer, Integer>();
    this.drawPathBFS = new ArrayList<Integer>();
    // DFS init
    this.worklistDFS = new Stack();
    this.visitedDFS = new ArrayList<Integer>();
    this.pathDFS = new HashMap<Integer, Integer>();
    this.drawPathDFS = new ArrayList<Integer>();

    // set all representatives as themselves
    for (int i = 0; i < sizeX * sizeY; i++) {
      representatives.put(i, i);
    }
    for (int i = 0; i < sizeX * sizeY; i++) {
      pathBFS.put(i, i);
    }
    for (int i = 0; i < sizeX * sizeY; i++) {
      pathDFS.put(i, i);
    }
    // adds all possible edges to the worklist
    for (int i = 0; i < sizeY; i++) {
      for (int j = 0; j < sizeX; j++) {

        if (j < sizeX - 1) {
          Edge right = new Edge(i * sizeX + j, i * sizeX + j + 1, r.nextInt(sizeX * sizeY));
          worklist.add(right);
        }
        if (i < sizeY - 1) {
          Edge bottom = new Edge(i * sizeX + j, (i + 1) * sizeX + j, r.nextInt(sizeX * sizeY));
          worklist.add(bottom);
        }
      }
    }
    // sorts the worklist
    Collections.sort(worklist);
    // copies worklist to the toDraw list
    for (int i = 0; i < worklist.size(); i++) {
      toDraw.add(worklist.get(i));
    }
    // adds edges in tree using Kruskals algorithm

    for (int i = 0; i < worklist.size(); i++) {
      int c1 = findRep(representatives, worklist.get(i).c1);
      int c2 = findRep(representatives, worklist.get(i).c2);
      if (c1 != c2) {
        edgesInTree.add(worklist.get(i));
        representatives.replace(c1, c2);
      }
    }

    // removes the walls of the maze in EdgesInTree
    for (int i = toDraw.size() - 1; i > -1; i--) {
      if (edgesInTree.contains(toDraw.get(i))) {
        toDraw.remove(i);
      }
    }

    // adds nodes into BFS and DFS worklists
    this.worklistBFS.add(0);
    this.visitedBFS.add(0);
    this.worklistDFS.add(0);
    this.visitedDFS.add(0);
  }

  // constructs the world with a seeded random as well as width and height
  MazeWorld(int sizeX, int sizeY, int seed) {
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    this.r = new Random(seed);
    this.representatives = new HashMap<Integer, Integer>();
    this.worklist = new ArrayList<Edge>();
    this.toDraw = new ArrayList<Edge>();
    this.edgesInTree = new ArrayList<Edge>();
    this.state = 0;
    // BFS init
    this.worklistBFS = new Queue();
    this.visitedBFS = new ArrayList<Integer>();
    this.pathBFS = new HashMap<Integer, Integer>();
    this.drawPathBFS = new ArrayList<Integer>();
    // DFS init
    this.worklistDFS = new Stack();
    this.visitedDFS = new ArrayList<Integer>();
    this.pathDFS = new HashMap<Integer, Integer>();
    this.drawPathDFS = new ArrayList<Integer>();

    // set all representatives as themselves
    for (int i = 0; i < sizeX * sizeY; i++) {
      representatives.put(i, i);
    }
    for (int i = 0; i < sizeX * sizeY; i++) {
      pathBFS.put(i, i);
    }
    for (int i = 0; i < sizeX * sizeY; i++) {
      pathDFS.put(i, i);
    }

    // adds all possible edges to the worklist
    for (int i = 0; i < sizeY; i++) {
      for (int j = 0; j < sizeX; j++) {

        if (j < sizeX - 1) {
          Edge right = new Edge(i * sizeX + j, i * sizeX + j + 1, r.nextInt(sizeX * sizeY));
          worklist.add(right);
        }
        if (i < sizeY - 1) {
          Edge bottom = new Edge(i * sizeX + j, (i + 1) * sizeX + j, r.nextInt(sizeX * sizeY));
          worklist.add(bottom);
        }
      }
    }
    // sorts the worklist
    Collections.sort(worklist);
    // copies worklist to the toDraw list
    for (int i = 0; i < worklist.size(); i++) {
      toDraw.add(worklist.get(i));
    }
    // adds edges in tree using Kruskals algorithm

    for (int i = 0; i < worklist.size(); i++) {
      int c1 = findRep(representatives, worklist.get(i).c1);
      int c2 = findRep(representatives, worklist.get(i).c2);
      if (c1 != c2) {
        edgesInTree.add(worklist.get(i));
        representatives.replace(c1, c2);
      }
    }

    // removes the walls of the maze in EdgesInTree
    for (int i = toDraw.size() - 1; i > -1; i--) {
      if (edgesInTree.contains(toDraw.get(i))) {
        toDraw.remove(i);
      }
    }

    // adds nodes into BFS and DFS worklists
    this.worklistBFS.add(0);
    this.visitedBFS.add(0);
    this.worklistDFS.add(0);
    this.visitedDFS.add(0);
  }

  // finds the representative of the an int in the hashmap
  int findRep(HashMap<Integer, Integer> map, int cell) {
    if (map.get(cell) == cell) {
      return cell;
    }
    else {
      return findRep(map, map.get(cell));
    }
  }

  // draws the worldscene
  @Override
  public WorldScene makeScene() {
    WorldScene background = new WorldScene(FULL_SCENE_SIZE, FULL_SCENE_SIZE);
    // draws the border and text
    background.placeImageXY(new LineImage(new Posn(0, SCENE_SIZE), Color.BLACK), 50,
        SCENE_SIZE / 2 + 50);
    background.placeImageXY(new LineImage(new Posn(0, SCENE_SIZE), Color.BLACK), SCENE_SIZE + 50,
        SCENE_SIZE / 2 + 50);
    background.placeImageXY(new LineImage(new Posn(SCENE_SIZE, 0), Color.BLACK),
        SCENE_SIZE / 2 + 50, 50);
    background.placeImageXY(new LineImage(new Posn(SCENE_SIZE, 0), Color.BLACK),
        SCENE_SIZE / 2 + 50, SCENE_SIZE + 50);
    background.placeImageXY(new TextImage("hold 'b' for breadth first search", Color.BLUE), 150,
        20);
    background.placeImageXY(new TextImage("hold 'd' for depth first search", Color.RED), 450, 20);
    // places the walls
    for (int i = 0; i < toDraw.size(); i++) {
      if (toDraw.get(i).c2 % sizeX - toDraw.get(i).c1 % sizeX == 0) {
        background.placeImageXY(new LineImage(new Posn(SCENE_SIZE / sizeX, 0), Color.BLACK),
            50 + (toDraw.get(i).c1 % sizeX) * SCENE_SIZE / sizeX + SCENE_SIZE / (sizeX * 2),
            50 + (toDraw.get(i).c1 / sizeX) * SCENE_SIZE / sizeY + SCENE_SIZE / sizeY);
      }
      else {
        background.placeImageXY(new LineImage(new Posn(0, SCENE_SIZE / sizeY), Color.BLACK),
            50 + (toDraw.get(i).c1 % sizeX) * SCENE_SIZE / sizeX + SCENE_SIZE / sizeX,
            50 + (toDraw.get(i).c1 / sizeX) * SCENE_SIZE / sizeY + SCENE_SIZE / (sizeY * 2));
      }
    }
    // draws the BFS algorithm at work
    if (state == 1) {
      for (int i = 0; i < visitedBFS.size(); i++) {
        background.placeImageXY(
            (new RectangleImage(SCENE_SIZE / (sizeX + 2), SCENE_SIZE / (sizeY + 2),
                OutlineMode.SOLID, Color.blue)),
            50 + (visitedBFS.get(i) % sizeX) * SCENE_SIZE / sizeX + SCENE_SIZE / (sizeX * 2),
            50 + (visitedBFS.get(i) / sizeX) * SCENE_SIZE / sizeY + SCENE_SIZE / (sizeY * 2));
      }
      // draws the finished BFS path
      for (int i = 0; i < drawPathBFS.size(); i++) {
        background.placeImageXY(
            (new RectangleImage(SCENE_SIZE / (sizeX + 4), SCENE_SIZE / (sizeY + 4),
                OutlineMode.SOLID, Color.GREEN)),
            50 + (drawPathBFS.get(i) % sizeX) * SCENE_SIZE / sizeX + SCENE_SIZE / (sizeX * 2),
            50 + (drawPathBFS.get(i) / sizeX) * SCENE_SIZE / sizeY + SCENE_SIZE / (sizeY * 2));
      }
    }
    // draws the DFS algorithm at work
    if (state == 2) {
      for (int i = 0; i < visitedDFS.size(); i++) {
        background.placeImageXY(
            (new RectangleImage(SCENE_SIZE / (sizeX + 2), SCENE_SIZE / (sizeY + 2),
                OutlineMode.SOLID, Color.RED)),
            50 + (visitedDFS.get(i) % sizeX) * SCENE_SIZE / sizeX + SCENE_SIZE / (sizeX * 2),
            50 + (visitedDFS.get(i) / sizeX) * SCENE_SIZE / sizeY + SCENE_SIZE / (sizeY * 2));
      }
      // draws the DFS finished path
      for (int i = 0; i < drawPathDFS.size(); i++) {
        background.placeImageXY(
            (new RectangleImage(SCENE_SIZE / (sizeX + 4), SCENE_SIZE / (sizeY + 4),
                OutlineMode.SOLID, Color.YELLOW)),
            50 + (drawPathDFS.get(i) % sizeX) * SCENE_SIZE / sizeX + SCENE_SIZE / (sizeX * 2),
            50 + (drawPathDFS.get(i) / sizeX) * SCENE_SIZE / sizeY + SCENE_SIZE / (sizeY * 2));
      }
    }
    return background;
  }

  // alters the world on tick
  @Override
  public void onTick() {
    if (state == 1) {
      this.breadthFirstSearch();
    }
    if (state == 2) {
      this.depthFirstSearch();
    }
  }

  // responds to key events
  @Override
  public void onKeyEvent(String key) {
    if (key.equals("b")) {
      state = 1;
    }
    else if (key.equals("d")) {
      state = 2;
    }
  }

  // responds to key released
  @Override
  public void onKeyReleased(String key) {
    state = 0;
  }

  // implements one tick of the BFS algorithm
  public void breadthFirstSearch() {
    if (this.worklistBFS.size() != 0) {
      int next = 0;
      if (this.worklistBFS.check() != this.sizeX * this.sizeY - 1) {
        next = this.worklistBFS.get();
      }
      else {
        next = this.worklistBFS.check();
      }
      if (!visitedBFS.contains(next) && next == this.sizeX * this.sizeY - 1) {
        drawPathBFS.add(reconstructBFS(pathBFS, next));
        return;
      }
      else {
        visitedBFS.add(next);
        // check top
        if (next >= this.sizeX && !containsEdge(toDraw, next - sizeX, next)
            && !visitedBFS.contains(next - sizeX)) {
          worklistBFS.add(next - sizeX);
          pathBFS.put(next - sizeX, next);
        }
        // check right
        if ((next + 1) % sizeX != 0 && !containsEdge(toDraw, next, next + 1)
            && !visitedBFS.contains(next + 1)) {
          worklistBFS.add(next + 1);
          pathBFS.put(next + 1, next);
        }
        // check bottom
        if (next <= this.sizeX * (this.sizeY - 1) - 1 && !containsEdge(toDraw, next, next + sizeX)
            && !visitedBFS.contains(next + sizeX)) {
          worklistBFS.add(next + sizeX);
          pathBFS.put(next + sizeX, next);
        }
        // check left
        if ((next) % sizeX != 0 && !containsEdge(toDraw, next - 1, next)
            && !visitedBFS.contains(next - 1)) {
          worklistBFS.add(next - 1);
          pathBFS.put(next - 1, next);
        }
      }
    }
  }

  // calculates one node of the completed path of BFS
  public int reconstructBFS(HashMap<Integer, Integer> pathBFS, int next) {
    int rep = this.findRep(pathBFS, next);
    int temp = next;
    while (pathBFS.get(temp) != rep) {
      temp = pathBFS.get(temp);
    }
    pathBFS.put(temp, temp);
    return rep;
  }

  // implements one tick of the DFS algorithm
  public void depthFirstSearch() {
    if (this.worklistDFS.size() != 0) {
      int next = 0;
      if (this.worklistDFS.check() != this.sizeX * this.sizeY - 1) {
        next = this.worklistDFS.get();
      }
      else {
        next = this.worklistDFS.check();
      }
      if (!visitedDFS.contains(next) && next == this.sizeX * this.sizeY - 1) {
        drawPathDFS.add(reconstructDFS(pathDFS, next));
        return;
      }
      else {
        visitedDFS.add(next);

        // check top
        if (next >= this.sizeX && !containsEdge(toDraw, next - sizeX, next)
            && !visitedDFS.contains(next - sizeX)) {
          worklistDFS.add(next - sizeX);
          pathDFS.put(next - sizeX, next);
        }

        // check right
        if ((next + 1) % sizeX != 0 && !containsEdge(toDraw, next, next + 1)
            && !visitedDFS.contains(next + 1)) {
          worklistDFS.add(next + 1);
          pathDFS.put(next + 1, next);
        }
        // check bottom
        if (next <= this.sizeX * (this.sizeY - 1) - 1 && !containsEdge(toDraw, next, next + sizeX)
            && !visitedDFS.contains(next + sizeX)) {
          worklistDFS.add(next + sizeX);
          pathDFS.put(next + sizeX, next);
        }
        // check left
        if ((next) % sizeX != 0 && !containsEdge(toDraw, next - 1, next)
            && !visitedDFS.contains(next - 1)) {
          worklistDFS.add(next - 1);
          pathDFS.put(next - 1, next);
        }
      }
    }
  }

  // calculates one node of the completed path of DFS
  public int reconstructDFS(HashMap<Integer, Integer> pathDFS, int next) {
    int rep = this.findRep(pathDFS, next);
    int temp = next;
    while (pathDFS.get(temp) != rep) {
      temp = pathDFS.get(temp);
    }
    pathDFS.put(temp, temp);
    return rep;
  }

  // checks if an array list of edges contains a given edge
  boolean containsEdge(ArrayList<Edge> list, int c1, int c2) {
    for (int i = 0; i < list.size(); i++) {
      Edge temp = list.get(i);
      if (temp.c1 == c1 && temp.c2 == c2 || temp.c1 == c2 && temp.c2 == c1) {
        return true;
      }
    }
    return false;
  }
}

class ExamplesMaze {
  void testFloodItWorld(Tester t) {

    MazeWorld testWorld = new MazeWorld(2, 2, 3);
    MazeWorld testWorld2 = new MazeWorld(3, 3, 4);
    MazeWorld starterWorld = new MazeWorld(15, 15);
    int sceneSize = MazeWorld.FULL_SCENE_SIZE;

    // test compareTo method
    t.checkExpect(new Edge(3, 4, 50).compareTo(new Edge(5, 6, 20)), 30);

    // test findRep
    testWorld.representatives.put(6, 6);
    testWorld.representatives.put(5, 6);
    testWorld.representatives.put(4, 5);
    t.checkExpect(testWorld.findRep(testWorld.representatives, 4), 6);

    // test makeScene
    WorldScene background = new WorldScene(MazeWorld.FULL_SCENE_SIZE, MazeWorld.FULL_SCENE_SIZE);
    background.placeImageXY(new LineImage(new Posn(0, MazeWorld.SCENE_SIZE), Color.BLACK), 50,
        MazeWorld.SCENE_SIZE / 2 + 50);
    background.placeImageXY(new LineImage(new Posn(0, MazeWorld.SCENE_SIZE), Color.BLACK),
        MazeWorld.SCENE_SIZE + 50, MazeWorld.SCENE_SIZE / 2 + 50);
    background.placeImageXY(new LineImage(new Posn(MazeWorld.SCENE_SIZE, 0), Color.BLACK),
        MazeWorld.SCENE_SIZE / 2 + 50, 50);
    background.placeImageXY(new LineImage(new Posn(MazeWorld.SCENE_SIZE, 0), Color.BLACK),
        MazeWorld.SCENE_SIZE / 2 + 50, MazeWorld.SCENE_SIZE + 50);
    background.placeImageXY(new LineImage(new Posn(0, 400), Color.BLACK), 450, 650);
    background.placeImageXY(new TextImage("hold 'b' for breadth first search", Color.BLUE), 150,
        20);
    background.placeImageXY(new TextImage("hold 'd' for depth first search", Color.RED), 450, 20);

    WorldScene background2 = new WorldScene(MazeWorld.FULL_SCENE_SIZE, MazeWorld.FULL_SCENE_SIZE);
    background2.placeImageXY(new LineImage(new Posn(0, MazeWorld.SCENE_SIZE), Color.BLACK), 50,
        MazeWorld.SCENE_SIZE / 2 + 50);
    background2.placeImageXY(new LineImage(new Posn(0, MazeWorld.SCENE_SIZE), Color.BLACK),
        MazeWorld.SCENE_SIZE + 50, MazeWorld.SCENE_SIZE / 2 + 50);
    background2.placeImageXY(new LineImage(new Posn(MazeWorld.SCENE_SIZE, 0), Color.BLACK),
        MazeWorld.SCENE_SIZE / 2 + 50, 50);
    background2.placeImageXY(new LineImage(new Posn(MazeWorld.SCENE_SIZE, 0), Color.BLACK),
        MazeWorld.SCENE_SIZE / 2 + 50, MazeWorld.SCENE_SIZE + 50);
    background2.placeImageXY(new LineImage(new Posn(0, 800), Color.BLACK), 50, 450);
    background2.placeImageXY(new LineImage(new Posn(0, 800), Color.BLACK), 850, 450);
    background2.placeImageXY(new LineImage(new Posn(800, 0), Color.BLACK), 450, 50);
    background2.placeImageXY(new LineImage(new Posn(800, 0), Color.BLACK), 450, 850);
    background2.placeImageXY(new LineImage(new Posn(266, 0), Color.BLACK), 716, 316);
    background2.placeImageXY(new LineImage(new Posn(0, 266), Color.BLACK), 582, 716);
    background2.placeImageXY(new LineImage(new Posn(266, 0), Color.BLACK), 183, 582);
    background2.placeImageXY(new LineImage(new Posn(0, 266), Color.BLACK), 316, 183);
    background2.placeImageXY(new TextImage("hold 'b' for breadth first search", Color.BLUE), 150,
        20);
    background2.placeImageXY(new TextImage("hold 'd' for depth first search", Color.RED), 450, 20);

    t.checkExpect(testWorld.makeScene(), background);
    t.checkExpect(testWorld2.makeScene(), background2);

    // test queue methods
    Queue testQueue = new Queue();
    t.checkExpect(testQueue.queue.size(), 0);
    t.checkExpect(testQueue.size(), 0);
    testQueue.add(0);
    testQueue.add(3);
    t.checkExpect(testQueue.queue.size(), 2);
    t.checkExpect(testQueue.size(), 2);
    t.checkExpect(testQueue.queue.get(1), 3);
    t.checkExpect(testQueue.check(), 0);
    t.checkExpect(testQueue.get(), 0);
    t.checkExpect(testQueue.size(), 1);
    t.checkExpect(testQueue.get(), 3);
    t.checkExpect(testQueue.size(), 0);
    // exceptions
    t.checkException(new IndexOutOfBoundsException(), testQueue, "get");
    t.checkException(new IndexOutOfBoundsException(), testQueue, "get");

    // test stack methods
    Stack testStack = new Stack();
    t.checkExpect(testStack.stack.size(), 0);
    t.checkExpect(testStack.size(), 0);
    testStack.add(0);
    testStack.add(3);
    t.checkExpect(testStack.stack.size(), 2);
    t.checkExpect(testStack.size(), 2);
    t.checkExpect(testStack.stack.get(1), 3);
    t.checkExpect(testStack.check(), 3);
    t.checkExpect(testStack.get(), 3);
    t.checkExpect(testStack.size(), 1);
    t.checkExpect(testStack.get(), 0);
    t.checkExpect(testStack.size(), 0);
    // exceptions
    t.checkException(new IndexOutOfBoundsException(), testStack, "get");
    t.checkException(new IndexOutOfBoundsException(), testStack, "get");

    // key event
    MazeWorld testWorld3 = new MazeWorld(3, 3, 4);
    t.checkExpect(testWorld3.state, 0);
    testWorld3.onKeyEvent("b");
    t.checkExpect(testWorld3.state, 1);
    testWorld3.state = 0;
    testWorld3.onKeyEvent("d");
    t.checkExpect(testWorld3.state, 2);
    testWorld3.onKeyReleased("d");
    t.checkExpect(testWorld3.state, 0);

    // BFS
    testWorld3 = new MazeWorld(3, 3, 4);
    t.checkExpect(testWorld3.worklistBFS.check(), 0);
    t.checkExpect(testWorld3.visitedBFS.size(), 1);
    testWorld3.breadthFirstSearch();
    t.checkExpect(testWorld3.worklistBFS.check(), 3);
    testWorld3.breadthFirstSearch();
    t.checkExpect(testWorld3.visitedBFS.size(), 3);
    t.checkExpect(testWorld3.visitedBFS.get(1), 0);

    // DFS
    testWorld3 = new MazeWorld(3, 3, 4);
    t.checkExpect(testWorld3.worklistDFS.check(), 0);
    t.checkExpect(testWorld3.visitedDFS.size(), 1);
    testWorld3.depthFirstSearch();
    t.checkExpect(testWorld3.worklistDFS.check(), 3);
    testWorld3.depthFirstSearch();
    t.checkExpect(testWorld3.visitedDFS.size(), 3);
    t.checkExpect(testWorld3.visitedDFS.get(1), 0);

    // on tick
    testWorld3 = new MazeWorld(3, 3, 4);
    testWorld3.state = 1;
    t.checkExpect(testWorld3.worklistBFS.check(), 0);
    t.checkExpect(testWorld3.visitedBFS.size(), 1);
    testWorld3.onTick();
    t.checkExpect(testWorld3.worklistBFS.check(), 3);
    testWorld3.onTick();
    t.checkExpect(testWorld3.visitedBFS.size(), 3);
    t.checkExpect(testWorld3.visitedBFS.get(1), 0);

    testWorld3 = new MazeWorld(3, 3, 4);
    testWorld3.state = 2;
    t.checkExpect(testWorld3.worklistDFS.check(), 0);
    t.checkExpect(testWorld3.visitedDFS.size(), 1);
    testWorld3.onTick();
    t.checkExpect(testWorld3.worklistDFS.check(), 3);
    testWorld3.onTick();
    t.checkExpect(testWorld3.visitedDFS.size(), 3);
    t.checkExpect(testWorld3.visitedDFS.get(1), 0);
    testWorld3 = new MazeWorld(3, 3, 4);

    // test reconstructBFS
    HashMap<Integer, Integer> testHash = new HashMap<Integer, Integer>();
    for (int i = 0; i < 3; i++) {
      testHash.put(i, i + 1);
      testHash.put(3, 3);
    }
    t.checkExpect(testWorld3.findRep(testHash, 0), 3);
    t.checkExpect(testWorld3.reconstructBFS(testHash, 0), 3);
    t.checkExpect(testWorld3.findRep(testHash, 0), 2);

    // test reconstructDFS
    testHash = new HashMap<Integer, Integer>();
    for (int i = 0; i < 3; i++) {
      testHash.put(i, i + 1);
      testHash.put(3, 3);
    }
    t.checkExpect(testWorld3.findRep(testHash, 0), 3);
    t.checkExpect(testWorld3.reconstructDFS(testHash, 0), 3);
    t.checkExpect(testWorld3.findRep(testHash, 0), 2);

    // test containsEdge
    ArrayList<Edge> testEdges = new ArrayList<Edge>();
    testEdges.add(new Edge(2, 3, 40));
    testEdges.add(new Edge(5, 6, 40));
    testEdges.add(new Edge(7, 8, 40));
    t.checkExpect(testWorld3.containsEdge(testEdges, 3, 2), true);
    t.checkExpect(testWorld3.containsEdge(testEdges, 7, 8), true);
    t.checkExpect(testWorld3.containsEdge(testEdges, 5, 2), false);

    // run world
    starterWorld.bigBang(sceneSize, sceneSize, .04);
  }
}
