import tester.*;

// represents a Person with a user name and a list of buddies
class Person {

  String username;
  ILoBuddy buddies;

  /*
   * TEMPLATE FIELDS: ... this.username ... -- String ... this.buddies ... --
   * ILoBuddy
   * 
   * METHODS: ... this.hasDirectBuddy(Person that) ... -- boolean ...
   * this.partyCount() ... -- int ... this.countBuddiesExtended(VisitedList
   * visited) ... -- int ... this.countCommonBuddies(Person that) ... -- int ...
   * this.hasExtendedBuddy(Person that) ... -- boolean ...
   * this.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean ...
   * this.addBuddy(Person buddy) ... -- void
   * 
   * METHODS FOR FIELDS: ... this.buddies.addBuddy(Person buddy) ... -- ILoBuddy
   * ... this.buddies.checkFor(Person that) ... -- boolean ...
   * this.buddies.compareList(Person that) ... -- int ...
   * this.buddies.searchFor(Person that, VisitedList visitedList) ... -- boolean
   * ... this.buddies.countExtended(VisitedList visitedList) ... -- int
   */

  Person(String username) {
    this.username = username;
    this.buddies = new MTLoBuddy();
  }

  // returns true if this Person has that as a direct buddy
  boolean hasDirectBuddy(Person that) {
    /*
     * FIELDS ON PARAMETERS: ... that.username ... -- String ... that.buddies ... --
     * ILoBuddy
     * 
     * METHODS ON PARAMETERS ... that.hasDirectBuddy(Person that) ... -- boolean ...
     * that.partyCount() ... -- int ... that.countBuddiesExtended(VisitedList
     * visited) ... -- int ... that.countCommonBuddies(Person that) ... -- int ...
     * that.hasExtendedBuddy(Person that) ... -- boolean ...
     * that.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean ...
     * that.addBuddy(Person buddy) ... -- void
     */

    return this.buddies.checkFor(that);
  }

  // returns the number of people who will show up at the party
  // given by this person
  int partyCount() {
    VisitedList visitedList = new VisitedList(new ConsLoBuddy(this, new MTLoBuddy()));
    return 1 + this.buddies.countExtended(visitedList);
  }

  // calls the searchFor method on the buddy field
  int countBuddiesExtended(VisitedList visited) {
    /*
     * FIELDS ON PARAMETERS: ... visited.visited ... -- ILoBuddy
     * 
     * METHODS ON PARAMETERS:
     * 
     */

    return this.buddies.countExtended(visited);
  }

  // returns the number of people that are direct buddies
  // of both this and that person
  int countCommonBuddies(Person that) {
    /*
     * FIELDS ON PARAMETERS: ... that.username ... -- String ... that.buddies ... --
     * ILoBuddy
     * 
     * METHODS ON PARAMETERS ... that.hasDirectBuddy(Person that) ... -- boolean ...
     * that.partyCount() ... -- int ... that.countBuddiesExtended(VisitedList
     * visited) ... -- int ... that.countCommonBuddies(Person that) ... -- int ...
     * that.hasExtendedBuddy(Person that) ... -- boolean ...
     * that.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean ...
     * that.addBuddy(Person buddy) ... -- void
     */
    return this.buddies.compareList(that);
  }

  // will the given person be invited to a party
  // organized by this person?
  boolean hasExtendedBuddy(Person that) {
    /*
     * FIELDS ON PARAMETERS: ... that.username ... -- String ... that.buddies ... --
     * ILoBuddy
     * 
     * METHODS ON PARAMETERS ... that.hasDirectBuddy(Person that) ... -- boolean ...
     * that.partyCount() ... -- int ... that.countBuddiesExtended(VisitedList
     * visited) ... -- int ... that.countCommonBuddies(Person that) ... -- int ...
     * that.hasExtendedBuddy(Person that) ... -- boolean ...
     * that.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean ...
     * that.addBuddy(Person buddy) ... -- void
     */
    VisitedList visitedList = new VisitedList(new ConsLoBuddy(this, new MTLoBuddy()));
    return this.buddies.searchFor(that, visitedList);
  }

  // calls the searchFor method on the buddy field
  boolean searchBuddiesFor(Person that, VisitedList visited) {
    /*
     * FIELDS ON PARAMETERS: ... that.username ... -- String ... that.buddies ... --
     * ILoBuddy ... visited.visited ... -- ILoBuddy
     * 
     * METHODS ON PARAMETERS ... that.hasDirectBuddy(Person that) ... -- boolean ...
     * that.partyCount() ... -- int ... that.countBuddiesExtended(VisitedList
     * visited) ... -- int ... that.countCommonBuddies(Person that) ... -- int ...
     * that.hasExtendedBuddy(Person that) ... -- boolean ...
     * that.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean ...
     * that.addBuddy(Person buddy) ... -- void
     */
    return this.buddies.searchFor(that, visited);
  }

  // Change this person's buddy list so that it includes the given person
  void addBuddy(Person buddy) {
    /*
     * FIELDS ON PARAMETERS: ... buddy.username ... -- String ... buddy.buddies ...
     * -- ILoBuddy
     * 
     * METHODS ON PARAMETERS ... buddy.hasDirectBuddy(Person that) ... -- boolean
     * ... buddy.partyCount() ... -- int ... buddy.countBuddiesExtended(VisitedList
     * visited) ... -- int ... that.countCommonBuddies(Person that) ... -- int ...
     * buddy.hasExtendedBuddy(Person that) ... -- boolean ...
     * buddy.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean ...
     * buddy.addBuddy(Person buddy) ... -- void
     */
    this.buddies = this.buddies.addBuddy(buddy);
  }
}

//represents a list of Person's buddies
interface ILoBuddy {

  // adds a buddy to the buddy list
  public ILoBuddy addBuddy(Person buddy);

  // checks if buddy is on list
  public boolean checkFor(Person that);

  // checks similarities between this list and given persons buddy list
  public int compareList(Person that);

  // checks if a buddy is connected to anyone in the list
  public boolean searchFor(Person that, VisitedList visitedList);

  // counts every buddy and connected buddy
  public int countExtended(VisitedList visitedList);
}

//represents a list of Person's buddies
class ConsLoBuddy implements ILoBuddy {

  Person first;
  ILoBuddy rest;

  /*
   * TEMPLATE FIELDS: ... this.first ... -- Person ... this.rest ... -- ILoBuddy
   * 
   * METHODS: ... this.addBuddy(Person buddy) ... -- ILoBuddy ...
   * this.checkFor(Person that) ... -- boolean ... this.compareList(Person that)
   * ... -- int ... this.searchFor(Person that, VisitedList visitedList) ... --
   * boolean ... this.countExtended(VisitedList visitedList) ... -- int
   * 
   * METHODS FOR FIELDS: ... this.first.hasDirectBuddy(Person that) ... -- boolean
   * ... this.first.partyCount() ... -- int ...
   * this.first.countBuddiesExtended(VisitedList visited) ... -- int ...
   * this.first.countCommonBuddies(Person that) ... -- int ...
   * this.first.hasExtendedBuddy(Person that) ... -- boolean ...
   * this.first.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean
   * ... this.first.addBuddy(Person buddy) ... -- void ...
   * this.rest.addBuddy(Person buddy) ... -- ILoBuddy ...
   * this.rest.checkFor(Person that) ... -- boolean ...
   * this.rest.compareList(Person that) ... -- int ... this.rest.searchFor(Person
   * that, VisitedList visitedList) ... -- boolean ...
   * this.rest.countExtended(VisitedList visitedList) ... -- int
   */

  ConsLoBuddy(Person first, ILoBuddy rest) {
    this.first = first;
    this.rest = rest;
  }

  // adds a buddy to the buddy list
  @Override
  public ILoBuddy addBuddy(Person buddy) {
    /*
     * FIELDS ON PARAMETERS: ... buddy.username ... -- String ... buddy.buddies ...
     * -- ILoBuddy
     * 
     * METHODS ON PARAMETERS ... buddy.hasDirectBuddy(Person that) ... -- boolean
     * ... buddy.partyCount() ... -- int ... buddy.countBuddiesExtended(VisitedList
     * visited) ... -- int ... that.countCommonBuddies(Person that) ... -- int ...
     * buddy.hasExtendedBuddy(Person that) ... -- boolean ...
     * buddy.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean ...
     * buddy.addBuddy(Person buddy) ... -- void
     */
    return new ConsLoBuddy(this.first, this.rest.addBuddy(buddy));
  }

  // checks if buddy is on list
  @Override
  public boolean checkFor(Person that) {
    /*
     * FIELDS ON PARAMETERS: ... that.username ... -- String ... that.buddies ... --
     * ILoBuddy
     * 
     * METHODS ON PARAMETERS ... that.hasDirectBuddy(Person that) ... -- boolean ...
     * that.partyCount() ... -- int ... that.countBuddiesExtended(VisitedList
     * visited) ... -- int ... that.countCommonBuddies(Person that) ... -- int ...
     * that.hasExtendedBuddy(Person that) ... -- boolean ...
     * that.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean ...
     * that.addBuddy(Person buddy) ... -- void
     */
    return this.first.equals(that) || this.rest.checkFor(that);
  }

  // checks similarities between this list and given persons buddy list
  @Override
  public int compareList(Person that) {
    /*
     * FIELDS ON PARAMETERS: ... that.username ... -- String ... that.buddies ... --
     * ILoBuddy
     * 
     * METHODS ON PARAMETERS ... that.hasDirectBuddy(Person that) ... -- boolean ...
     * that.partyCount() ... -- int ... that.countBuddiesExtended(VisitedList
     * visited) ... -- int ... that.countCommonBuddies(Person that) ... -- int ...
     * that.hasExtendedBuddy(Person that) ... -- boolean ...
     * that.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean ...
     * that.addBuddy(Person buddy) ... -- void
     */

    if (that.buddies.checkFor(this.first)) {
      return 1 + this.rest.compareList(that);
    }
    else {
      return this.rest.compareList(that);
    }
  }

  // checks if a buddy is connected to anyone in the list
  @Override
  public boolean searchFor(Person that, VisitedList visited) {
    /*
     * FIELDS ON PARAMETERS: ... that.username ... -- String ... that.buddies ... --
     * ILoBuddy ... visited.visited ... -- ILoBuddy
     * 
     * METHODS ON PARAMETERS ... that.hasDirectBuddy(Person that) ... -- boolean ...
     * that.partyCount() ... -- int ... that.countBuddiesExtended(VisitedList
     * visited) ... -- int ... that.countCommonBuddies(Person that) ... -- int ...
     * that.hasExtendedBuddy(Person that) ... -- boolean ...
     * that.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean ...
     * that.addBuddy(Person buddy) ... -- void
     */

    if (visited.visited.checkFor(this.first)) {
      return this.rest.searchFor(that, visited);
    }
    else if (this.first.equals(that)) {
      return true;
    }
    else {
      visited.visited = visited.visited.addBuddy(this.first);
      return this.rest.searchFor(that, visited) || this.first.searchBuddiesFor(that, visited);
    }
  }

  // counts every buddy and connected buddy
  @Override
  public int countExtended(VisitedList visited) {
    /*
     * FIELDS ON PARAMETERS: ... visited.visited ... -- ILoBuddy
     * 
     * METHODS ON PARAMETERS:
     * 
     */

    if (visited.visited.checkFor(this.first)) {
      return this.rest.countExtended(visited);
    }
    else {
      visited.visited = visited.visited.addBuddy(this.first);
      return 1 + this.rest.countExtended(visited) + this.first.countBuddiesExtended(visited);
    }
  }
}

//represents an empty list of Person's buddies
class MTLoBuddy implements ILoBuddy {

  /*
   * TEMPLATE FIELDS:
   * 
   * METHODS: ... this.addBuddy(Person buddy) ... -- ILoBuddy ...
   * this.checkFor(Person that) ... -- boolean ... this.compareList(Person that)
   * ... -- int ... this.searchFor(Person that, VisitedList visitedList) ... --
   * boolean ... this.countExtended(VisitedList visitedList) ... -- int
   * 
   * METHODS FOR FIELDS:
   */

  MTLoBuddy() {
  }

  // adds a buddy to the buddy list
  @Override
  public ILoBuddy addBuddy(Person buddy) {
    /*
     * FIELDS ON PARAMETERS: ... buddy.username ... -- String ... buddy.buddies ...
     * -- ILoBuddy
     * 
     * METHODS ON PARAMETERS ... buddy.hasDirectBuddy(Person that) ... -- boolean
     * ... buddy.partyCount() ... -- int ... buddy.countBuddiesExtended(VisitedList
     * visited) ... -- int ... that.countCommonBuddies(Person that) ... -- int ...
     * buddy.hasExtendedBuddy(Person that) ... -- boolean ...
     * buddy.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean ...
     * buddy.addBuddy(Person buddy) ... -- void
     */

    return new ConsLoBuddy(buddy, this);
  }

  // checks if buddy is on list
  @Override
  public boolean checkFor(Person that) {
    /*
     * FIELDS ON PARAMETERS: ... that.username ... -- String ... that.buddies ... --
     * ILoBuddy
     * 
     * METHODS ON PARAMETERS ... that.hasDirectBuddy(Person that) ... -- boolean ...
     * that.partyCount() ... -- int ... that.countBuddiesExtended(VisitedList
     * visited) ... -- int ... that.countCommonBuddies(Person that) ... -- int ...
     * that.hasExtendedBuddy(Person that) ... -- boolean ...
     * that.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean ...
     * that.addBuddy(Person buddy) ... -- void
     */

    return false;
  }

  // checks similarities between this list and given persons buddy list
  @Override
  public int compareList(Person that) {
    /*
     * FIELDS ON PARAMETERS: ... that.username ... -- String ... that.buddies ... --
     * ILoBuddy
     * 
     * METHODS ON PARAMETERS ... that.hasDirectBuddy(Person that) ... -- boolean ...
     * that.partyCount() ... -- int ... that.countBuddiesExtended(VisitedList
     * visited) ... -- int ... that.countCommonBuddies(Person that) ... -- int ...
     * that.hasExtendedBuddy(Person that) ... -- boolean ...
     * that.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean ...
     * that.addBuddy(Person buddy) ... -- void
     */

    return 0;
  }

  // checks if a buddy is connected to anyone in the list
  @Override
  public boolean searchFor(Person that, VisitedList visited) {
    /*
     * FIELDS ON PARAMETERS: ... that.username ... -- String ... that.buddies ... --
     * ILoBuddy ... visited.visited ... -- ILoBuddy
     * 
     * METHODS ON PARAMETERS ... that.hasDirectBuddy(Person that) ... -- boolean ...
     * that.partyCount() ... -- int ... that.countBuddiesExtended(VisitedList
     * visited) ... -- int ... that.countCommonBuddies(Person that) ... -- int ...
     * that.hasExtendedBuddy(Person that) ... -- boolean ...
     * that.searchBuddiesFor(Person that, VisitedList visited) ... -- boolean ...
     * that.addBuddy(Person buddy) ... -- void
     */
    return false;
  }

  // counts every buddy and connected buddy
  @Override
  public int countExtended(VisitedList visited) {
    /*
     * FIELDS ON PARAMETERS: ... visited.visited ... -- ILoBuddy
     * 
     * METHODS ON PARAMETERS:
     * 
     */
    return 0;
  }
}

class VisitedList {

  ILoBuddy visited;

  /*
   * TEMPLATE FIELDS: ... this.visited ... -- ILoBuddy
   * 
   * METHODS:
   * 
   * METHODS FOR FIELDS: ... this.visited.addBuddy(Person buddy) ... -- ILoBuddy
   * ... this.visited.checkFor(Person that) ... -- boolean ...
   * this.visited.compareList(Person that) ... -- int ...
   * this.visited.searchFor(Person that, VisitedList visitedList) ... -- boolean
   * ... this.visited.countExtended(VisitedList visitedList) ... -- int
   */

  VisitedList(ILoBuddy visited) {
    this.visited = visited;
  }
}

//runs tests for the buddies problem
class ExamplesBuddies {

  Person ann;
  Person bob;
  Person cole;
  Person dan;
  Person ed;
  Person fay;
  Person gabi;
  Person hank;
  Person jan;
  Person kim;
  Person len;
  VisitedList visitedList;

  void initBuddies() {
    ann = new Person("ann");
    bob = new Person("bob");
    cole = new Person("cole");
    dan = new Person("dan");
    ed = new Person("ed");
    fay = new Person("Fay");
    gabi = new Person("gabi");
    hank = new Person("hank");
    jan = new Person("jan");
    kim = new Person("kim");
    len = new Person("len");

    ann.addBuddy(bob);
    ann.addBuddy(cole);
    bob.addBuddy(ann);
    bob.addBuddy(ed);
    bob.addBuddy(hank);
    cole.addBuddy(dan);
    dan.addBuddy(cole);
    ed.addBuddy(fay);
    fay.addBuddy(ed);
    fay.addBuddy(gabi);
    gabi.addBuddy(ed);
    gabi.addBuddy(fay);
    jan.addBuddy(kim);
    jan.addBuddy(len);
    kim.addBuddy(jan);
    kim.addBuddy(len);
    len.addBuddy(jan);
    len.addBuddy(kim);
  }

  void testMethods(Tester t) {
    initBuddies();

    // Person methods:
    // test addBuddy method
    t.checkExpect(ann.buddies, new ConsLoBuddy(bob, new ConsLoBuddy(cole, new MTLoBuddy())));
    t.checkExpect(hank.buddies, new MTLoBuddy());

    // test hasDirectBuddy method
    t.checkExpect(ann.hasDirectBuddy(bob), true);
    t.checkExpect(ann.hasDirectBuddy(cole), true);
    t.checkExpect(ann.hasDirectBuddy(hank), false);
    t.checkExpect(hank.hasDirectBuddy(hank), false);
    t.checkExpect(hank.hasDirectBuddy(kim), false);
    t.checkExpect(ed.hasDirectBuddy(gabi), false);

    // test countCommonBuddies method
    t.checkExpect(kim.countCommonBuddies(len), 1);
    t.checkExpect(bob.countCommonBuddies(bob), 3);
    t.checkExpect(hank.countCommonBuddies(bob), 0);
    t.checkExpect(dan.countCommonBuddies(ann), 1);

    // test hasExtendedBuddy method
    t.checkExpect(ann.hasExtendedBuddy(kim), false);
    t.checkExpect(ann.hasExtendedBuddy(ann), false);
    t.checkExpect(kim.hasExtendedBuddy(len), true);
    t.checkExpect(ann.hasExtendedBuddy(gabi), true);

    // test partyCount method
    t.checkExpect(ann.partyCount(), 8);
    t.checkExpect(hank.partyCount(), 1);
    t.checkExpect(len.partyCount(), 3);

    // test countBuddiesExtended method
    visitedList = new VisitedList(new ConsLoBuddy(ann, new MTLoBuddy()));
    t.checkExpect(ann.countBuddiesExtended(visitedList), 7);
    visitedList = new VisitedList(new ConsLoBuddy(hank, new MTLoBuddy()));
    t.checkExpect(hank.countBuddiesExtended(visitedList), 0);

    // test searchBuddiesFor method
    visitedList = new VisitedList(new ConsLoBuddy(ed, new MTLoBuddy()));
    t.checkExpect(ann.searchBuddiesFor(ed, visitedList), false);
    visitedList = new VisitedList(new MTLoBuddy());
    t.checkExpect(ann.searchBuddiesFor(ed, visitedList), true);
    visitedList = new VisitedList(new MTLoBuddy());
    t.checkExpect(ann.searchBuddiesFor(kim, visitedList), false);

    // ILoBuddyMethods:
    // test addBuddy method
    t.checkExpect(new MTLoBuddy().addBuddy(ann), (new ConsLoBuddy(ann, new MTLoBuddy())));
    t.checkExpect(new MTLoBuddy().addBuddy(ann).addBuddy(bob),
        (new ConsLoBuddy(ann, (new ConsLoBuddy(bob, new MTLoBuddy())))));

    // test checkFor method
    t.checkExpect(new MTLoBuddy().checkFor(ann), false);
    t.checkExpect(new ConsLoBuddy(ann, new MTLoBuddy()).checkFor(ann), true);
    t.checkExpect(new ConsLoBuddy(ann, (new ConsLoBuddy(bob, new MTLoBuddy()))).checkFor(bob),
        true);

    // test compareList method
    t.checkExpect(new MTLoBuddy().compareList(ann), 0);
    t.checkExpect(new ConsLoBuddy(len, (new ConsLoBuddy(kim, new MTLoBuddy()))).compareList(jan),
        2);
    t.checkExpect(new ConsLoBuddy(len, new MTLoBuddy()).compareList(jan), 1);
    t.checkExpect(hank.buddies.compareList(bob), 0);

    // test searchFor
    visitedList = new VisitedList(new MTLoBuddy());
    t.checkExpect(new MTLoBuddy().searchFor(ann, visitedList), false);
    visitedList = new VisitedList(new ConsLoBuddy(ed, new MTLoBuddy()));
    t.checkExpect(ann.buddies.searchFor(ed, visitedList), false);
    visitedList = new VisitedList(new MTLoBuddy());
    t.checkExpect(ann.buddies.searchFor(ed, visitedList), true);
    visitedList = new VisitedList(new MTLoBuddy());
    t.checkExpect(ann.buddies.searchFor(kim, visitedList), false);

    // test countExtended
    visitedList = new VisitedList(new MTLoBuddy());
    t.checkExpect(new MTLoBuddy().countExtended(visitedList), 0);
    visitedList = new VisitedList(new ConsLoBuddy(ann, new MTLoBuddy()));
    t.checkExpect(ann.buddies.countExtended(visitedList), 7);
    t.checkExpect(hank.buddies.countExtended(visitedList), 0);
    visitedList = new VisitedList(new ConsLoBuddy(hank, new MTLoBuddy()));
    t.checkExpect(hank.buddies.countExtended(visitedList), 0);
    t.checkExpect(len.buddies.countExtended(visitedList), 3);
    visitedList = new VisitedList(new ConsLoBuddy(len, new MTLoBuddy()));
    t.checkExpect(len.buddies.countExtended(visitedList), 2);
  }
}