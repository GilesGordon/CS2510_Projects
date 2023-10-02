import java.util.ArrayList;
import java.util.Random;
import tester.Tester;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// representation of a cell
interface ICell {
  // floods this cell and its neighbors recursively
  void flood(Color c, ArrayList<ICell> visited);

  // helper method for flood
  void floodHelper(Color c, ArrayList<ICell> visited);
}

//Represents a single square of the game area
class Cell implements ICell {
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;
  ICell left;
  ICell top;
  ICell right;
  ICell bottom;

  Cell(int x, int y, Color color, boolean flooded) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = flooded;
  }

  // floods this cell and its neighbors recursively
  @Override
  public void flood(Color c, ArrayList<ICell> visited) {
    if (this.flooded) {
      this.color = c;
      floodHelper(c, visited);
    }
    else {
      if (this.color.equals(c)) {
        this.flooded = true;
        floodHelper(c, visited);
      }
    }
  }

  // helper method for flood
  @Override
  public void floodHelper(Color c, ArrayList<ICell> visited) {
    if (!visited.contains(this.left)) {
      visited.add(this.left);
    }
    if (!visited.contains(this.top)) {
      visited.add(this.top);
    }
    if (!visited.contains(this.right)) {
      visited.add(this.right);
    }
    if (!visited.contains(this.bottom)) {
      visited.add(this.bottom);
    }
  }

}

//representation of a border cell
class MtCell implements ICell {

  @Override
  public void flood(Color c, ArrayList<ICell> visited) {
    // TODO Auto-generated method stub

  }

  @Override
  public void floodHelper(Color c, ArrayList<ICell> visited) {
    // TODO Auto-generated method stub

  }
}

class FloodItWorld extends World {

  public static final int SCENE_SIZE = 1000;
  public ArrayList<ArrayList<Cell>> board;
  public ArrayList<ICell> visited;
  public int size;
  public int colors;
  public boolean flooding;
  Cell clickedCell;
  int allowedClicks;
  int clicksSoFar;
  boolean gameOver;
  boolean win;

  FloodItWorld(int size, int colors) {

    this.size = size;
    this.colors = colors;
    this.board = new ArrayList<ArrayList<Cell>>();
    this.flooding = true;
    this.allowedClicks = ((size * colors) / 3) + 1;
    this.clicksSoFar = 0;
    this.gameOver = false;
    this.win = false;
    if (colors > 8 || colors < 2) {
      throw new IllegalArgumentException("Unacceptable amount of colors");
    }
    if (size > 30 || size < 2) {
      throw new IllegalArgumentException("Unacceptable size");
    }
    // creates board
    Random r = new Random();
    for (int i = 0; i < size; i++) {
      board.add(new ArrayList<Cell>());
      for (int j = 0; j < size; j++) {
        Cell c = new Cell(SCENE_SIZE / 4 + j * (SCENE_SIZE / (2 * size)),
            SCENE_SIZE / 4 + i * (SCENE_SIZE / (2 * size)), this.getColor(colors, r), false);
        board.get(i).add(c);
      }
    }
    // assigns cell neighbors
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        Cell c = board.get(i).get(j);
        // top row
        if (i == 0) {
          c.top = new MtCell();
          c.bottom = board.get(1).get(j);
          if (j == 0) {
            c.left = new MtCell();
            c.right = board.get(0).get(1);
          }
          else if (j == size - 1) {
            c.left = board.get(0).get(size - 2);
            c.right = new MtCell();
          }
          else {
            c.left = board.get(i).get(j - 1);
            c.right = board.get(i).get(j + 1);
          }
        }
        // bottom row
        else if (i == size - 1) {
          c.bottom = new MtCell();
          c.top = board.get(size - 2).get(j);
          if (j == 0) {
            c.left = new MtCell();
            c.right = board.get(size - 1).get(1);
          }
          else if (j == size - 1) {
            c.left = board.get(size - 1).get(size - 2);
            c.right = new MtCell();
          }
          else {
            c.left = board.get(i).get(j - 1);
            c.right = board.get(i).get(j + 1);
          }
        }
        // middle rows
        else {
          c.top = board.get(i - 1).get(j);
          c.bottom = board.get(i + 1).get(j);
          if (j == 0) {
            c.left = new MtCell();
            c.right = board.get(i).get(j + 1);
          }
          else if (j == size - 1) {
            c.left = board.get(i).get(j - 1);
            c.right = new MtCell();
          }
          else {
            c.right = board.get(i).get(j + 1);
            c.left = board.get(i).get(j - 1);
          }
        }
      }
    }
    // make top left flooded
    board.get(0).get(0).flooded = true;
    visited = new ArrayList<ICell>();
    visited.add(board.get(0).get(0));
    clickedCell = (board.get(0).get(0));
  }

  @Override
  public WorldScene makeScene() {
    WorldScene background = new WorldScene(SCENE_SIZE, SCENE_SIZE);

    if (this.win && !this.gameOver) {
      background.placeImageXY(new TextImage("You Win!", 100, Color.BLACK), 500, 500);
    }
    else if (!this.gameOver) {
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          Cell c = board.get(i).get(j);
          background.placeImageXY(drawCell(c), c.x + (SCENE_SIZE / (2 * size)) / 2,
              c.y + (SCENE_SIZE / (2 * size)) / 2);
          background.placeImageXY(
              new TextImage(
                  this.clicksSoFar + "/" + this.allowedClicks , 50, Color.BLACK ), 500, 200);
        }
      }
    }
    else {
      background.placeImageXY(new TextImage("Game Over!", 100, Color.BLACK), 500, 500);
    }
    return background;
  }

  public Color getColor(int colors, Random r) {
    switch (r.nextInt(colors)) {
      case 1:
        return Color.BLACK;
      case 2:
        return Color.BLUE;
      case 3:
        return Color.RED;
      case 4:
        return Color.YELLOW;
      case 5:
        return Color.GREEN;
      case 6:
        return Color.ORANGE;
      case 7:
        return Color.MAGENTA;
      default:
        return Color.PINK;
    }
  }

  public WorldImage drawCell(Cell c) {
    return new RectangleImage(SCENE_SIZE / (2 * size), SCENE_SIZE / (2 * size), OutlineMode.SOLID,
        c.color);
  }

  public void onMouseClicked(Posn pos) {
    visited = new ArrayList<ICell>();
    if (SCENE_SIZE / 4 < pos.x && pos.x < SCENE_SIZE * 3 / 4 && SCENE_SIZE / 4 < pos.y
        && pos.y < SCENE_SIZE * 3 / 4) {
      clickedCell = board.get((pos.y - SCENE_SIZE / 4) / (SCENE_SIZE / (2 * size)))
          .get((pos.x - SCENE_SIZE / 4) / (SCENE_SIZE / (2 * size)));

      if ((!clickedCell.flooded) && (!board.get(0).get(0).color.equals(clickedCell.color))) {
        clicksSoFar += 1.0;

      }
      flooding = !clickedCell.flooded;
      visited.add(board.get(0).get(0));
      board.get(0).get(0).color = clickedCell.color;

    }
    //    System.out.println("posnx" + pos.x + "posny" + pos.y);
  }

  public void onTick() {
    if (flooding) {
      int j = visited.size();
      for (int i = 0; i < j; i++) {
        visited.get(i).flood(clickedCell.color, visited);
      }
      if (visited.size() == size * size) {
        flooding = false;
      }
    }
    if (this.clicksSoFar > this.allowedClicks) {
      this.gameOver = true;

    }
    if (checkWin()) {
      this.win = true;
    }

  }
  // when r is pressed the current game is ended
  // and a new game is started
  
  public void onKeyEvent(String key) {

    if (key.equals("r")) {
      this.gameOver = true;
      new FloodItWorld(this.size, this.colors).bigBang(1000, 1000, .0025);
      System.out.println("test");
    }
  }

  // checks whether all the cells are the same color
  // if so, you win the game

  public boolean checkWin() {
    boolean win = true;
    for (int i = 0; i < size; i++) {
      for (int j = 1; j < size; j++) {
        if (!board.get(i).get(j).color.equals(board.get(0).get(0).color)) {
          win = false;
        }
      }
    }
    return win;
  }
}

//Examples
class ExamplesFloodIt {
  void testFloodItWorld(Tester t) {
    // test Exceptions
    t.checkConstructorException(new IllegalArgumentException("Unacceptable amount of colors"),
        "FloodItWorld", 3, 9);
    t.checkConstructorException(new IllegalArgumentException("Unacceptable amount of colors"),
        "FloodItWorld", 3, 1);
    t.checkConstructorException(new IllegalArgumentException("Unacceptable size"), "FloodItWorld",
        1, 5);
    t.checkConstructorException(new IllegalArgumentException("Unacceptable size"), "FloodItWorld",
        32, 5);

    // test methods
    // test makeScene
    FloodItWorld testWorld = new FloodItWorld(2, 2);
    WorldScene testWorldScene = new WorldScene(FloodItWorld.SCENE_SIZE, FloodItWorld.SCENE_SIZE);
    testWorldScene.placeImageXY(
        new RectangleImage(FloodItWorld.SCENE_SIZE / 4, FloodItWorld.SCENE_SIZE / 4,
            OutlineMode.SOLID, testWorld.board.get(0).get(0).color),
        FloodItWorld.SCENE_SIZE / 4, FloodItWorld.SCENE_SIZE / 4);
    testWorldScene.placeImageXY(
        new RectangleImage(FloodItWorld.SCENE_SIZE / 4, FloodItWorld.SCENE_SIZE / 4,
            OutlineMode.SOLID, testWorld.board.get(0).get(1).color),
        FloodItWorld.SCENE_SIZE / 2, FloodItWorld.SCENE_SIZE / 4);
    testWorldScene.placeImageXY(
        new RectangleImage(FloodItWorld.SCENE_SIZE / 4, FloodItWorld.SCENE_SIZE / 4,
            OutlineMode.SOLID, testWorld.board.get(1).get(0).color),
        FloodItWorld.SCENE_SIZE / 4, FloodItWorld.SCENE_SIZE / 2);
    testWorldScene.placeImageXY(
        new RectangleImage(FloodItWorld.SCENE_SIZE / 4, FloodItWorld.SCENE_SIZE / 4,
            OutlineMode.SOLID, testWorld.board.get(1).get(1).color),
        FloodItWorld.SCENE_SIZE / 2, FloodItWorld.SCENE_SIZE / 2);
    testWorldScene.placeImageXY(new TextImage("0/1", 50, Color.BLACK), 500, 500);
    t.checkExpect(testWorld.makeScene(), testWorldScene);

    FloodItWorld testWorld2 = new FloodItWorld(5, 3);
    WorldScene testWorldScene2 = new WorldScene(FloodItWorld.SCENE_SIZE, FloodItWorld.SCENE_SIZE);
    testWorld2.win = true;
    testWorld2.gameOver = false;
    testWorldScene2.placeImageXY(new TextImage("You Win!", 100, Color.BLACK), 500, 500);
    t.checkExpect(testWorld2.makeScene(), testWorldScene2);

    // test checkWin
    FloodItWorld testWorld3 = new FloodItWorld(2, 2);
    testWorld3.board.get(0).set(0, new Cell(0,0, Color.black, true));
    testWorld3.board.get(0).set(1, new Cell(0,0, Color.black, true));
    testWorld3.board.get(1).set(0, new Cell(0,0, Color.black, true));
    testWorld3.board.get(1).set(1, new Cell(0,0, Color.black, true));
    t.checkExpect(testWorld3.checkWin(), true);

    // test onTick
    FloodItWorld testWorld4 = new FloodItWorld(2,2);
    WorldScene testWorldScene3 = new WorldScene(FloodItWorld.SCENE_SIZE, FloodItWorld.SCENE_SIZE);
    testWorldScene3.placeImageXY(new TextImage("Game Over!", 100, Color.BLACK), 500, 500);
    testWorld4.allowedClicks = 4;
    testWorld4.clicksSoFar = 5;
    testWorld4.onTick();
    t.checkExpect(testWorld4.makeScene(), testWorldScene3); 


    // test onKeyEvent
    //    FloodItWorld testWorld5 = new FloodItWorld(20,2);
    //    t.checkExpect(testWorld5.gameOver, false);
    //    testWorld5.onKeyEvent("r");
    //    t.checkExpect(testWorld5.gameOver, true);

    // test getColor
    Random r = new Random(6);
    t.checkExpect(testWorld.getColor(5, r), Color.BLACK);
    r.nextInt(5);
    r.nextInt(5);
    t.checkExpect(testWorld.getColor(5, r), Color.RED);

    // test flood
    Cell c = new Cell(0, 0, Color.RED, true);
    c.flood(Color.PINK, new ArrayList<ICell>());
    t.checkExpect(c.color, Color.PINK);

    // test onMouseClicked
    FloodItWorld testWorld6 = new FloodItWorld(2,2);
    testWorld6.onMouseClicked(new Posn(545, 386));
    t.checkExpect(testWorld6.clicksSoFar, 1);

    // test floodHelper

    ArrayList<ICell> visited2 = new ArrayList<ICell>();
    Cell c2 = new Cell(0, 0, Color.black, true);
    Cell c3 = new Cell(1, 0, Color.black, true);
    c2.left = c3;
    t.checkExpect(visited2.contains(c3), false);
    visited2.add(c2);
    c2.floodHelper(Color.BLACK, visited2);
    t.checkExpect(visited2.contains(c3), true);



    // test drawCell
    t.checkExpect(testWorld.drawCell(new Cell(0, 0, Color.PINK, true)), new RectangleImage(
        FloodItWorld.SCENE_SIZE / 4, FloodItWorld.SCENE_SIZE / 4, OutlineMode.SOLID, Color.PINK));

    // runs the game by creating a world and calling bigBang
    FloodItWorld starterWorld = new FloodItWorld(20, 6);
    int sceneSize = FloodItWorld.SCENE_SIZE;
    starterWorld.bigBang(sceneSize, sceneSize, .0025);
  }
}