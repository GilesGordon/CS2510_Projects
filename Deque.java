import tester.*;
import java.util.function.Predicate;

//represents a Deque
class Deque<T> {
  Sentinel<T> header;

  /*
   * TEMPLATE FIELDS:
   * 
   * ... this.header ... -- Sentinel<T> ...
   * 
   * METHODS:
   * 
   * ...this.size() ... -- int ... this.addAtHead(T data) ... -- void ...
   * this.addAtTail(T data) ... -- void ... this.removeFromHead() ... -- T ...
   * this.removeFromTail() ... -- T ... this.find(Predicate<T> pred) ... --
   * ANode<T> ...
   * 
   * METHODS FOR FIELDS: ... this.header.linkNodes(ANode<T> nextNode) ... -- void
   * ... this.header.size() ... -- int ... this.header.remove() ... -- T ...
   * this.header.find(Predicate<T> pred) ... -- ANode<T>
   */

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // gets the size of the list
  int size() {
    return this.header.next.size();
  }

  // adds a node to the head of the list
  void addAtHead(T data) {
    new Node<T>(data, this.header.next, this.header);
  }

  // adds a node to the tail of the list
  void addAtTail(T data) {
    new Node<T>(data, this.header, this.header.prev);
  }

  // removes the first node of the list and returns the data of that node
  T removeFromHead() {
    return this.header.next.remove();
  }

  // removes the last node of the list and returns the data of that node
  T removeFromTail() {
    return this.header.prev.remove();
  }

  // takes a Predicate<T> and produces the first node in this Deque for which the
  // given predicate returns true
  ANode<T> find(Predicate<T> pred) {
    return this.header.next.find(pred);
  }
}

//a helper function for testing the predicate implementation
class Helper implements Predicate<String> {

  /*
   * TEMPLATE FIELDS:
   * 
   * METHODS:
   * 
   * ... this.test() ... -- boolean ...
   * 
   * METHODS FOR FIELDS:
   * 
   */

  // checks if the string equals "bcd"
  @Override
  public boolean test(String t) {
    return t.equals("bcd");
  }
}

//a helper function for testing the predicate implementation
class Helper2 implements Predicate<Integer> {

  /*
   * TEMPLATE FIELDS:
   * 
   * METHODS:
   * 
   * ... this.test() ... -- boolean ...
   * 
   * METHODS FOR FIELDS:
   * 
   */

  // checks if the integer is greater than 12
  @Override
  public boolean test(Integer t) {
    return t > 12;
  }
}

abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  /*
   * TEMPLATE FIELDS: ... this.next ... -- ANode<T> ... this.prev ... -- ANode<T>
   * 
   * METHODS: ... this.linkNodes(ANode<T> nextNode) ... -- void ... this.size()
   * ... -- int ... this.remove() ... -- T ... this.find(Predicate<T> pred) ... --
   * ANode<T>
   * 
   * METHODS FOR FIELDS: ... this.next.linkNodes(ANode<T> nextNode) ... -- void
   * ... ... this.next.size() ... -- int ... this.next.remove() ... -- T ...
   * this.next.find(Predicate<T> pred) ... -- ANode<T> ...
   * this.prev.linkNodes(ANode<T> nextNode) ... -- void ... this.prev.size() ...
   * -- int ... this.prev.remove() ... -- T ... this.prev.find(Predicate<T> pred)
   * ... -- ANode<T>
   */

  ANode(ANode<T> next, ANode<T> prev) {
    this.next = next;
    this.prev = prev;
  }

  // makes the passed in node the next of the this node, and this node the
  // previous of the next node
  void linkNodes(ANode<T> nextNode) {

    /*
     * FIELDS ON PARAMETERS: ... nextNode.next ... -- ANode<T> ... nextNode.prev ...
     * -- ANode<T>
     * 
     * METHODS ON PARAMETERS: ... nextNode.linkNodes(ANode<T> nextNode) ... -- void
     * ... nextNode.size() ... -- int ... nextNode.remove() ... -- T ...
     * nextNode.find(Predicate<T> pred) ... -- ANode<T>
     * 
     */

    this.next = nextNode;
    nextNode.prev = this;
  }

  // helper method for the size method of the Deque class
  abstract int size();

  // helper method for the remove method of the Deque class
  abstract T remove();

  // helper method for the find method of the Deque class
  abstract ANode<T> find(Predicate<T> pred);

}

class Sentinel<T> extends ANode<T> {

  /*
   * TEMPLATE FIELDS: ... this.next ... -- ANode<T> ... this.prev ... -- ANode<T>
   * 
   * METHODS: ... this.linkNodes(ANode<T> nextNode) ... -- void ... this.size()
   * ... -- int ... this.remove() ... -- T ... this.find(Predicate<T> pred) ... --
   * ANode<T>
   * 
   * METHODS FOR FIELDS: ... this.next.linkNodes(ANode<T> nextNode) ... -- void
   * ... ... this.next.size() ... -- int ... this.next.remove() ... -- T ...
   * this.next.find(Predicate<T> pred) ... -- ANode<T> ...
   * this.prev.linkNodes(ANode<T> nextNode) ... -- void ... this.prev.size() ...
   * -- int ... this.prev.remove() ... -- T ... this.prev.find(Predicate<T> pred)
   * ... -- ANode<T>
   */

  Sentinel() {
    super(null, null);
    this.next = this;
    this.prev = this;
  }

  // helper method for the size method of the Deque class
  @Override
  int size() {
    return 0;
  }

  // helper method for the remove method of the Deque class
  @Override
  T remove() {
    throw new RuntimeException("cannot remove head/tail of empty list");
  }

  // helper method for the find method of the Deque class
  @Override
  ANode<T> find(Predicate<T> pred) {
    return this;
  }
}

class Node<T> extends ANode<T> {
  T data;

  /*
   * TEMPLATE FIELDS: ... this.next ... -- ANode<T> ... this.prev ... -- ANode<T>
   * ... this.data ... -- T
   * 
   * METHODS: ... this.linkNodes(ANode<T> nextNode) ... -- void ... this.size()
   * ... -- int ... this.remove() ... -- T ... this.find(Predicate<T> pred) ... --
   * ANode<T>
   * 
   * METHODS FOR FIELDS: ... this.next.linkNodes(ANode<T> nextNode) ... -- void
   * ... ... this.next.size() ... -- int ... this.next.remove() ... -- T ...
   * this.next.find(Predicate<T> pred) ... -- ANode<T> ...
   * this.prev.linkNodes(ANode<T> nextNode) ... -- void ... this.prev.size() ...
   * -- int ... this.prev.remove() ... -- T ... this.prev.find(Predicate<T> pred)
   * ... -- ANode<T>
   */

  Node(T data) {
    super(null, null);
    this.data = data;
  }

  Node(T data, ANode<T> next, ANode<T> prev) {
    super(next, prev);
    this.data = data;
    if (next == null || prev == null) {
      throw new IllegalArgumentException("node arguments cannot be null");
    }
    else {
      this.prev.linkNodes(this);
      this.linkNodes(this.next);
    }
  }

  // helper method for the size method of the Deque class
  @Override
  int size() {
    return 1 + this.next.size();
  }

  // helper method for the remove method of the Deque class
  T remove() {
    this.prev.linkNodes(this.next);
    return this.data;
  }

  // helper method for the find method of the Deque class
  @Override
  ANode<T> find(Predicate<T> pred) {
    if (pred.test(this.data)) {
      return this;
    }
    else {
      return this.next.find(pred);
    }
  }
}

class ExamplesDeque {
  Deque<String> deque1;
  Deque<String> deque2;
  Deque<String> deque3;
  Deque<Integer> deque4;
  Sentinel<String> s;
  Node<String> ns1;
  Node<String> ns2;
  Node<String> ns3;
  Node<String> ns4;
  Sentinel<String> s2;
  Node<String> nc1;
  Node<String> nc2;
  Node<String> nc3;
  Node<String> nc4;
  Sentinel<Integer> s3;
  Node<Integer> ni1;
  Node<Integer> ni2;
  Node<Integer> ni3;
  Node<Integer> ni4;
  Node<Integer> ni5;
  Node<Integer> ni6;
  Node<String> testNode1;
  Node<String> testNode2;
  Node<String> testNode3;
  Node<String> testNode4;
  Node<String> testNode5;
  Node<String> testNode6;
  Node<String> testNode7;
  Sentinel<String> sent;

  void initDeques() {
    deque1 = new Deque<String>();

    s = new Sentinel<String>();
    ns1 = new Node<String>("abc", s, s);
    ns2 = new Node<String>("bcd", s, ns1);
    ns3 = new Node<String>("cde", s, ns2);
    ns4 = new Node<String>("def", s, ns3);
    deque2 = new Deque<String>(s);

    s2 = new Sentinel<String>();
    nc1 = new Node<String>("b", s2, s2);
    nc2 = new Node<String>("a", s2, nc1);
    nc3 = new Node<String>("d", s2, nc2);
    nc4 = new Node<String>("c", s2, nc3);
    deque3 = new Deque<String>(s2);

    s3 = new Sentinel<Integer>();
    ni1 = new Node<Integer>(11, s3, s3);
    ni2 = new Node<Integer>(13, s3, ni1);
    ni3 = new Node<Integer>(20, s3, ni2);
    ni4 = new Node<Integer>(21, s3, ni3);
    ni5 = new Node<Integer>(30, s3, ni4);
    ni6 = new Node<Integer>(45, s3, ni5);
    deque4 = new Deque<Integer>(s3);

    testNode1 = new Node<String>("a");
    testNode1.next = ns1;
    testNode1.prev = s;

    testNode2 = new Node<String>("a");
    testNode2.next = deque1.header;
    testNode2.prev = deque1.header;

    testNode3 = new Node<String>("a");
    testNode3.next = s;
    testNode3.prev = ns4;

    testNode4 = new Node<String>("bcd");
    testNode4.next = ns3;
    testNode4.prev = s;

    testNode5 = new Node<String>("cde");
    testNode5.next = ns4;
    testNode5.prev = testNode4;

    testNode6 = new Node<String>("cde");
    testNode6.next = s;
    testNode6.prev = ns2;

    testNode7 = new Node<String>("bcd");
    testNode7.next = testNode6;
    testNode7.prev = ns1;

    sent = new Sentinel<String>();
  }

  void testMethods(Tester t) {

    // Deque method tests
    // test initializations
    initDeques();
    t.checkExpect(ns1.next.next, ns3);
    t.checkExpect(ns3.prev.prev, ns1);

    // test size method
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque3.size(), 4);
    t.checkExpect(deque4.size(), 6);

    // test addAtHead method
    deque1.addAtHead("a");
    t.checkExpect(deque1.header.next, testNode2);
    t.checkExpect(deque1.header.prev, testNode2);
    t.checkExpect(deque1.size(), 1);
    deque2.addAtHead("a");
    t.checkExpect(deque2.header.next, testNode1);
    t.checkExpect(ns1.prev, testNode1);
    t.checkExpect(deque2.size(), 5);

    // test addAtTail method
    initDeques();
    deque1.addAtTail("a");
    t.checkExpect(deque1.header.next, testNode2);
    t.checkExpect(deque1.header.prev, testNode2);
    t.checkExpect(deque1.size(), 1);
    deque2.addAtTail("a");
    t.checkExpect(deque2.header.prev, testNode3);
    t.checkExpect(ns4.next, testNode3);
    t.checkExpect(deque2.size(), 5);

    // test exceptions
    initDeques();
    t.checkConstructorException(new IllegalArgumentException("node arguments cannot be null"),
        "Node", "hi", null, ns3);
    t.checkConstructorException(new IllegalArgumentException("node arguments cannot be null"),
        "Node", "hi", ns3, null);
    t.checkConstructorException(new IllegalArgumentException("node arguments cannot be null"),
        "Node", "hi", null, null);
    t.checkException(new RuntimeException("cannot remove head/tail of empty list"), deque1,
        "removeFromHead");
    t.checkException(new RuntimeException("cannot remove head/tail of empty list"), deque1,
        "removeFromTail");
    t.checkException(new RuntimeException("cannot remove head/tail of empty list"),
        new Sentinel<String>(), "remove");

    // test removeFromHead method
    initDeques();
    t.checkExpect(deque2.removeFromHead(), "abc");
    t.checkExpect(deque2.header.next, testNode4);
    t.checkExpect(deque2.header.next.next, testNode5);

    // test removeFromTail method
    initDeques();
    t.checkExpect(deque2.removeFromTail(), "def");
    t.checkExpect(deque2.header.prev, testNode6);
    t.checkExpect(deque2.header.prev.prev, testNode7);

    // test find method
    initDeques();
    t.checkExpect(deque1.find(new Helper()), new Sentinel<String>());
    t.checkExpect(deque2.find(new Helper()), ns2);
    t.checkExpect(deque4.find(new Helper2()), ni2);

    // Helper class tests
    // test test method
    t.checkExpect(new Helper().test("r"), false);
    t.checkExpect(new Helper().test("bcd"), true);

    // Helper2 class tests
    // test test method
    t.checkExpect(new Helper2().test(3), false);
    t.checkExpect(new Helper2().test(20), true);

    // Sentinel class tests
    // test size method
    t.checkExpect(sent.size(), 0);
    t.checkExpect(s.size(), 0);

    // test find method
    t.checkExpect(sent.find(new Helper()), sent);
    t.checkExpect(s.find(new Helper()), s);

    // Node class tests
    // test size method
    t.checkExpect(ni4.size(), 3);
    t.checkExpect(ni3.size(), 4);
    t.checkExpect(ns4.size(), 1);

    // test remove method
    t.checkExpect(ns1.remove(), "abc");
    t.checkExpect(ns2.prev, s);
    t.checkExpect(s.next, ns2);

    // test find method
    initDeques();
    t.checkExpect(ns1.find(new Helper()), ns2);
    t.checkExpect(ns2.find(new Helper()), ns2);
    t.checkExpect(ni1.find(new Helper2()), ni2);
    t.checkExpect(ns4.find(new Helper()), s);

    // abstract methods
    initDeques();
    ns1.linkNodes(ns3);
    t.checkExpect(ns1.next, ns3);
    t.checkExpect(ns3.prev, ns1);
    t.checkExpect(ns1.prev, s);
    t.checkExpect(ns3.next, ns4);
  }
}