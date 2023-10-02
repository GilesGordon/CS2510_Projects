import java.awt.Color;
import java.util.Random;
import tester.Tester;
import javalib.funworld.*;
import javalib.worldimages.*;

// Represents a game of Simon Says
class SimonWorld extends World {

  public static final int SCENE_SIZE = 1000;

  public static final Button DARK_GREEN = new Button(Color.GREEN, 450, 450);

  public static final Button DARK_RED = new Button(Color.RED, 550, 450);

  public static final Button DARK_BLUE = new Button(Color.BLUE, 550, 550);

  public static final Button DARK_YELLOW = new Button(Color.YELLOW, 450, 550);

  public ILoButton actualSequence;

  public ILoButton givenSequence;

  public Button clicked;

  public int current;

  public int state;

  public Random rand;

  public boolean switcher;

  /*
   * TEMPLATE:
   * 
   * Fields: ... this.SCENE_SIZE ... -- int ... this.DARK_GREEN ... -- Button ...
   * this.DARK_RED ... -- Button ... this.DARK_BLUE ... -- Button ...
   * this.DARK_YELLOW ... -- Button ... this.actualSequence ... -- ILoButton ...
   * this.givenSequence ... -- ILoButton ... this.clicked ... -- Button ...
   * this.current ... -- int ... this.state ... -- int ... this.rand ... -- Random
   * ... this.switcher ... -- boolean
   * 
   * Methods: ... this.makeScene() ... -- WorldScene ... this.onTick() ... --
   * World ... this.addButton() ... -- void ... this.lastScene(String msg) ... --
   * WorldScene ... this.onMouseClicked() ... -- SimonWorld ...
   * this.distanceToCenter(Posn pos) ... -- double
   * 
   * Methods On Fields: ... this.DARK_GREEN.drawLit() ... -- WorldImage ...
   * this.DARK_GREEN.drawDark() ... -- WorldImage ... this.DARK_RED.drawLit() ...
   * -- WorldImage ... this.DARK_RED.drawDark() ... -- WorldImage ...
   * this.DARK_BLUE.drawLit() ... -- WorldImage ... this.DARK_BLUE.drawDark() ...
   * -- WorldImage ... this.DARK_YELLOW.drawLit() ... -- WorldImage ...
   * this.DARK_YELLOW.drawDark() ... -- WorldImage ...
   * this.DARK_GREEN.drawButton(Color darker) ... -- WorldImage ...
   * this.DARK_RED.drawButton(Color darker) ... -- WorldImage ...
   * this.DARK_BLUE.drawButton(Color darker) ... -- WorldImage ...
   * this.DARK_YELLOW.drawButton(Color darker) ... -- WorldImage ...
   * this.actualSequence.getButtonAt(int index) ... -- Button ...
   * this.actualSequence.getLength() ... -- int ...
   * this.actualSequence.append(Button button) ... -- ConsLoButton ...
   * this.actualSequence.compareSequence(ILoButton list) ... -- boolean ...
   * this.actualSequence.compareHelper(ConsLoButton list) ... -- boolean ...
   * this.givenSequence.getButtonAt(int index) ... -- Button ...
   * this.givenSequence.getLength() ... -- int ...
   * this.givenSequence.append(Button button) ... -- ConsLoButton ...
   * this.givenSequence.compareSequence(ILoButton list) ... -- boolean ...
   * this.givenSequence.compareHelper(ConsLoButton list) ... -- boolean ...
   * this.clicked.drawLit() ... -- WorldImage ... this.clicked.drawDark() ... --
   * WorldImage
   */

  // truly random constructor
  SimonWorld(ILoButton actualSequence, ILoButton givenSequence, int state) {
    this.actualSequence = actualSequence;
    this.givenSequence = givenSequence;
    this.state = state;
    this.rand = new Random();
    this.switcher = false;
    this.current = 0;
    this.addButton();
  }

  // seeded random for testing
  SimonWorld(ILoButton actualSequence, ILoButton givenSequence, int state, Random rand) {
    this.actualSequence = actualSequence;
    this.givenSequence = givenSequence;
    this.state = state;
    this.rand = rand;
    this.switcher = false;
    this.current = 0;
  }

  // Draw the current state of the world
  public WorldScene makeScene() {
    // WorldImage brightGreen = new Button(Color.GREEN, 450, 450).drawLit();
    // WorldImage brightRed = new Button(Color.RED, 550, 450).drawLit();
    // WorldImage brightBlue = new Button(Color.BLUE, 550, 550).drawLit();
    // WorldImage brightYellow = new Button(Color.YELLOW, 450, 550).drawLit();
    WorldScene background = new WorldScene(SCENE_SIZE, SCENE_SIZE);
    WorldScene simonBackground = background
        .placeImageXY(DARK_GREEN.drawDark(), DARK_GREEN.x, DARK_GREEN.y)
        .placeImageXY(DARK_RED.drawDark(), DARK_RED.x, DARK_RED.y)
        .placeImageXY(DARK_BLUE.drawDark(), DARK_BLUE.x, DARK_BLUE.y)
        .placeImageXY(DARK_YELLOW.drawDark(), DARK_YELLOW.x, DARK_YELLOW.y);
    if (state == 1) {
      if (switcher) {
        return simonBackground;
      }
      else {
        return simonBackground.placeImageXY(this.actualSequence.getButtonAt(this.current).drawLit(),
            this.actualSequence.getButtonAt(this.current).x,
            this.actualSequence.getButtonAt(this.current).y);
      }
    }
    else {
      if (switcher) {
        return simonBackground;
      }
      else {
        return simonBackground.placeImageXY(this.clicked.drawLit(), this.clicked.x, this.clicked.y);
      }
    }
  }

  // handles ticking of the clock and updating the world if needed
  public World onTick() {
    if (!this.givenSequence.compareSequence(this.actualSequence)) {
      state = 3;
      return this.endOfWorld("Incorrect pattern. You lose");
    }
    else if (this.state == 1) {
      if (this.current == this.actualSequence.getLength()) {
        this.switcher = false;
        this.givenSequence = new MtLoButton();
        this.state = 2;
      }
      if (this.switcher) {
        this.switcher = false;
      }
      else {
        this.current = this.current + 1;
        this.switcher = true;
      }
      return this;
    }
    else {
      if (!this.givenSequence.compareSequence(this.actualSequence)) {
        this.state = 3;
      }
      else if (this.givenSequence.getLength() == this.actualSequence.getLength()) {
        this.addButton();
        this.givenSequence = new MtLoButton();
        this.current = 0;
        this.state = 1;
        this.switcher = true;
        return this;
      }
      else {
        this.switcher = true;
        return this;
      }
    }
    return this;
  }

  // adds a random button to the actual sequence
  public void addButton() {
    int randomNum = this.rand.nextInt(4);
    if (randomNum == 0) {
      this.actualSequence = this.actualSequence.append(DARK_GREEN);
    }
    else if (randomNum == 1) {
      this.actualSequence = this.actualSequence.append(DARK_RED);
    }
    else if (randomNum == 2) {
      this.actualSequence = this.actualSequence.append(DARK_BLUE);
    }
    else {
      this.actualSequence = this.actualSequence.append(DARK_YELLOW);
    }
  }

  // Returns the final scene with the given message displayed
  public WorldScene lastScene(String msg) {
    // stub
    WorldScene background = new WorldScene(SCENE_SIZE, SCENE_SIZE);
    return background.placeImageXY(new TextImage(msg, Color.BLACK), SCENE_SIZE / 2, SCENE_SIZE / 2);
  }

  // handles mouse clicks and is given the mouse location
  public SimonWorld onMouseClicked(Posn pos) {
    this.switcher = false;
    if (this.state == 2) {
      if (pos.x < 500 && pos.y < 500 && this.distanceToCenter(pos) < 100) {
        this.clicked = DARK_GREEN;
        this.givenSequence = this.givenSequence.append(DARK_GREEN);
        return this;
      }
      else if (pos.x > 500 && pos.y < 500 && this.distanceToCenter(pos) < 100) {
        this.clicked = DARK_RED;
        this.givenSequence = this.givenSequence.append(DARK_RED);
        return this;
      }
      else if (pos.x > 500 && pos.y > 500 && this.distanceToCenter(pos) < 100) {
        this.clicked = DARK_BLUE;
        this.givenSequence = this.givenSequence.append(DARK_BLUE);
        return this;
      }
      else if (this.distanceToCenter(pos) < 100) {
        this.clicked = DARK_YELLOW;
        this.givenSequence = this.givenSequence.append(DARK_YELLOW);
        return this;
      }
      else {
        return this;
      }
    }
    else {
      return this;
    }
  }

  // finds the distance of a point to the center of the scene
  public double distanceToCenter(Posn pos) {
    return Math.sqrt(Math.pow(500 - pos.x, 2) + Math.pow(500 - pos.y, 2));
  }
}

// Represents a list of buttons
interface ILoButton {
  // gets the element at the given index that is less than the length of the list
  public Button getButtonAt(int index);

  // gets length of list
  public int getLength();

  // adds button to the end of the list
  public ConsLoButton append(Button button);

  // compares the actual sequence with the given sequence
  public boolean compareSequence(ILoButton list);

  // helper for CompareSequence
  public boolean compareHelper(ConsLoButton list);

}

// Represents an empty list of buttons
class MtLoButton implements ILoButton {

  /*
   * TEMPLATE:
   * 
   * Fields:
   * 
   * Methods: ... this.getButtonAt(int index) ... -- Button ... this.getLength()
   * ... -- int ... this.append(Button button) ... -- ConsLoButton ...
   * this.compareSequence(ILoButton list) ... -- boolean ...
   * this.compareHelper(ConsLoButton list) ... -- boolean
   * 
   * Methods On Fields:
   */

  // gets the element at the given index that is less than the length of the list
  @Override
  public Button getButtonAt(int index) {
    return null;
  }

  // gets length of list
  @Override
  public int getLength() {
    return 0;
  }

  // adds button to the end of the list
  @Override
  public ConsLoButton append(Button button) {
    return new ConsLoButton(button, this);
  }

  // compares the actual sequence with the given sequence
  @Override
  public boolean compareSequence(ILoButton list) {
    return true;
  }

  // helper for CompareSequence
  @Override
  public boolean compareHelper(ConsLoButton list) {
    return true;
  }
}

// Represents a non-empty list of buttons
class ConsLoButton implements ILoButton {
  Button first;
  ILoButton rest;

  /*
   * TEMPLATE:
   * 
   * Fields: ... this.first ... -- Button ... this.rest ... -- ILoButton
   * 
   * Methods: ... this.getButtonAt(int index) ... -- Button ... this.getLength()
   * ... -- int ... this.append(Button button) ... -- ConsLoButton ...
   * this.compareSequence(ILoButton list) ... -- boolean ...
   * this.compareHelper(ConsLoButton list) ... -- boolean
   * 
   * Methods On Fields:
   * 
   * ... this.first.drawLit() ... -- WorldImage ... this.first.drawDark() ... --
   * WorldImage ... this.first.drawButton(Color darker) ... -- WorldImage ...
   * this.rest.getButtonAt(int index) ... -- Button ... this.rest.getLength() ...
   * -- int ... this.rest.append(Button button) ... -- ConsLoButton ...
   * this.rest.compareSequence(ILoButton list) ... -- boolean ...
   * this.rest.compareHelper(ConsLoButton list) ... -- boolean
   */

  ConsLoButton(Button first, ILoButton rest) {
    this.first = first;
    this.rest = rest;
  }

  // gets the element at the given index that is less than the length of the list
  @Override
  public Button getButtonAt(int index) {
    if (index == 0) {
      return this.first;
    }
    else {
      return this.rest.getButtonAt(index - 1);
    }
  }

  // gets length of list
  @Override
  public int getLength() {
    return 1 + this.rest.getLength();
  }

  // adds button to the end of the list
  @Override
  public ConsLoButton append(Button button) {
    return new ConsLoButton(this.first, this.rest.append(button));
  }

  // compares the actual sequence with the given sequence
  @Override
  public boolean compareSequence(ILoButton list) {
    if (list.getLength() == 0) {
      return true;
    }
    else {
      return list.compareHelper(this);
    }
  }

  // helper for CompareSequence
  @Override
  public boolean compareHelper(ConsLoButton list) {
    if (this.first.equals(list.first)) {
      return this.rest.compareSequence(list.rest);
    }
    else {
      return false;
    }
  }
}

// Represents one of the four buttons you can click
class Button {
  Color color;
  int x;
  int y;

  /*
   * TEMPLATE:
   * 
   * Fields: ... this.color ... -- Color ... this.x ... -- int ... this.y ... --
   * int
   * 
   * Methods: ... this.drawLit() ... -- WorldImage ... this.drawDark() ... --
   * WorldImage ... this.drawButton(Color darker) ... -- WorldImage
   * 
   * Methods On Fields:
   */

  Button(Color color, int x, int y) {
    this.color = color;
    this.x = x;
    this.y = y;
  }

  public WorldImage drawButton(Color darker) {
    if (x < 500 && y < 500) {
      return new CropImage(0, 0, 100, 100, new CircleImage(100, OutlineMode.SOLID, darker));
    }
    else if (x > 500 && y < 500) {
      return new CropImage(100, 0, 100, 100, new CircleImage(100, OutlineMode.SOLID, darker));
    }
    else if (x < 500 && y > 500) {
      return new CropImage(0, 100, 100, 100, new CircleImage(100, OutlineMode.SOLID, darker));
    }
    else {
      return new CropImage(100, 100, 100, 100, new CircleImage(100, OutlineMode.SOLID, darker));
    }
  }

  // Draw this button dark
  WorldImage drawDark() {
    return this.drawButton(this.color.darker().darker());
  }

  // Draw this button lit
  WorldImage drawLit() {
    return this.drawButton(this.color.brighter().brighter());
  }

}

// Examples
class ExamplesSimon {

  SimonWorld starterWorldTest = new SimonWorld(new MtLoButton(), new MtLoButton(), 1,
      new Random(13));

  Button DARK_GREEN = new Button(Color.GREEN, 450, 450);
  Button DARK_RED = new Button(Color.RED, 550, 450);
  Button DARK_BLUE = new Button(Color.BLUE, 550, 550);
  Button DARK_YELLOW = new Button(Color.YELLOW, 450, 550);

  ILoButton example1 = new ConsLoButton(DARK_GREEN,
      new ConsLoButton(DARK_GREEN, new ConsLoButton(DARK_RED, new MtLoButton())));
  ILoButton example2 = new ConsLoButton(DARK_GREEN, new ConsLoButton(DARK_BLUE,
      new ConsLoButton(DARK_YELLOW, new ConsLoButton(DARK_RED, new MtLoButton()))));
  ILoButton example3 = new ConsLoButton(DARK_GREEN,
      new ConsLoButton(DARK_BLUE, new ConsLoButton(DARK_RED, new MtLoButton())));
  ILoButton example4 = new ConsLoButton(DARK_GREEN, new ConsLoButton(DARK_GREEN,
      new ConsLoButton(DARK_RED, new ConsLoButton(DARK_RED, new MtLoButton()))));

  // tests the makeScene method
  boolean testMakeScene(Tester t) {
    return t.checkExpect(
        new SimonWorld(new ConsLoButton(DARK_BLUE, new MtLoButton()), new MtLoButton(), 1,
            new Random(13)).makeScene(),
        new WorldScene(1000, 1000).placeImageXY(DARK_GREEN.drawDark(), DARK_GREEN.x, DARK_GREEN.y)
            .placeImageXY(DARK_RED.drawDark(), DARK_RED.x, DARK_RED.y)
            .placeImageXY(DARK_BLUE.drawDark(), DARK_BLUE.x, DARK_BLUE.y)
            .placeImageXY(DARK_YELLOW.drawDark(), DARK_YELLOW.x, DARK_YELLOW.y)
            .placeImageXY(DARK_BLUE.drawLit(), 550, 550));
  }

  // tests the onTick method
  boolean testOnTick(Tester t) {
    SimonWorld starterWorldChanged = new SimonWorld(new MtLoButton(), new MtLoButton(), 1,
        new Random(13));
    starterWorldChanged.state = 2;
    starterWorldChanged.switcher = true;
    starterWorldChanged.current = 1;
    return t.checkExpect(
        new SimonWorld(new MtLoButton(), new MtLoButton(), 1, new Random(13)).onTick(),
        starterWorldChanged);
  }

  // tests the addButton method
  boolean testAddButton(Tester t) {
    starterWorldTest.addButton();
    return t.checkExpect(starterWorldTest, new SimonWorld(
        new ConsLoButton(DARK_BLUE, new MtLoButton()), new MtLoButton(), 1, new Random(13)));
  }

  // tests the lastScene method
  boolean testLastScene(Tester t) {
    return t.checkExpect(starterWorldTest.lastScene("Incorrect pattern. You lose"),
        new WorldScene(1000, 1000)
            .placeImageXY(new TextImage("Incorrect pattern. You lose", Color.BLACK), 500, 500));
  }

  // tests the onMousClicked method
  boolean testOnMouseClicked(Tester t) {
    SimonWorld alteredWorld = new SimonWorld(new MtLoButton(),
        new ConsLoButton(DARK_GREEN, new MtLoButton()), 1, new Random(13));
    alteredWorld.clicked = DARK_GREEN;
    alteredWorld.state = 2;
    return t.checkExpect(
        new SimonWorld(new MtLoButton(), new MtLoButton(), 1, new Random(13))
            .onMouseClicked(new Posn(499, 499)),
        new SimonWorld(new MtLoButton(), new MtLoButton(), 1, new Random(13)))
        && t.checkExpect(new SimonWorld(new MtLoButton(), new MtLoButton(), 2, new Random(13))
            .onMouseClicked(new Posn(499, 499)), alteredWorld);
  }

  // tests the distanceToCenter method
  boolean testDistanceToCenter(Tester t) {
    return t.checkInexact(starterWorldTest.distanceToCenter(new Posn(400, 367)), 166.4001, 0.001);
  }

  // tests the getButtonAt method
  boolean testGetButtonAt(Tester t) {
    return t.checkExpect(this.example1.getButtonAt(1), DARK_GREEN)
        && t.checkExpect(this.example1.getButtonAt(5), null)
        && t.checkExpect(new MtLoButton().getButtonAt(3), null);
  }

  // tests the getLength method
  boolean testGetLength(Tester t) {
    return t.checkExpect(this.example1.getLength(), 3)
        && t.checkExpect(this.example2.getLength(), 4)
        && t.checkExpect(new MtLoButton().getLength(), 0);
  }

  // tests the append method
  boolean testAppend(Tester t) {
    return t.checkExpect(this.example2.append(DARK_BLUE),
        new ConsLoButton(DARK_GREEN, new ConsLoButton(DARK_BLUE, new ConsLoButton(DARK_YELLOW,
            new ConsLoButton(DARK_RED, new ConsLoButton(DARK_BLUE, new MtLoButton()))))));
  }

  // tests the compareSequence method
  boolean testCompareSequence(Tester t) {
    return t.checkExpect(this.example1.compareSequence(example4), true)
        && t.checkExpect(this.example4.compareSequence(example1), true)
        && t.checkExpect(this.example1.compareSequence(example3), false)
        && t.checkExpect(this.example3.compareSequence(example1), false)
        && t.checkExpect(this.example1.compareSequence(new MtLoButton()), true)
        && t.checkExpect(new MtLoButton().compareSequence(this.example1), true);
  }

  // tests the compareHelper method
  boolean testCompareHelper(Tester t) {
    return t.checkExpect(this.example1.compareHelper((ConsLoButton) example4), true)
        && t.checkExpect(this.example4.compareHelper((ConsLoButton) example1), true)
        && t.checkExpect(this.example1.compareHelper((ConsLoButton) example3), false)
        && t.checkExpect(this.example3.compareHelper((ConsLoButton) example1), false)
        && t.checkExpect(new MtLoButton().compareHelper((ConsLoButton) example1), true);
  }

  // tests the drawLit method
  boolean testDrawLit(Tester t) {
    return t.checkExpect(DARK_GREEN.drawLit(), new CropImage(0, 0, 100, 100,
        new CircleImage(100, OutlineMode.SOLID, Color.GREEN.brighter().brighter())));
  }

  // tests the drawDark method
  boolean testDrawDark(Tester t) {
    return t.checkExpect(DARK_GREEN.drawDark(), new CropImage(0, 0, 100, 100,
        new CircleImage(100, OutlineMode.SOLID, Color.GREEN.darker().darker())));
  }

  // tests the drawButton method
  boolean testDrawButton(Tester t) {
    return t.checkExpect(DARK_GREEN.drawButton(DARK_GREEN.color),
        new CropImage(0, 0, 100, 100, new CircleImage(100, OutlineMode.SOLID, Color.GREEN)));
  }

  // runs the game by creating a world and calling bigBang
  boolean testSimonSays(Tester t) {
    SimonWorld starterWorld = new SimonWorld(new MtLoButton(), new MtLoButton(), 1);
    int sceneSize = SimonWorld.SCENE_SIZE;
    return starterWorld.bigBang(sceneSize, sceneSize, .5);
  }
}