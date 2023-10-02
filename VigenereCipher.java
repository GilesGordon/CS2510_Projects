import tester.*;
import java.util.*;

class Vigenere {
  ArrayList<Character> alphabet = new ArrayList<Character>(
      Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
          'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));
  ArrayList<ArrayList<Character>> table;

  /*
   * TEMPLATE FIELDS: ... this.alphabet ... -- ArrayList<Character> ... this.table
   * ... -- ArrayList<ArrayList<Character>>
   * 
   * METHODS: ... this.initVigenere() ... -- ArrayList<ArrayList<Character>> ...
   * this.decode(String msg, String key) ... -- String ... this.encode(String msg,
   * String key) ... -- String ... this.generateKeyword(String key, int len) ...
   * -- String
   * 
   * METHODS FOR FIELDS:
   */

  // constructor for Vigenere class
  Vigenere() {
    this.table = initVigenere();
  }

  // returns the Vigenere table
  ArrayList<ArrayList<Character>> initVigenere() {
    ArrayList<ArrayList<Character>> currentTable = new ArrayList<ArrayList<Character>>();
    ArrayList<Character> listToAdd = this.alphabet;
    for (int i = 0; i < 26; i++) {
      currentTable.add(new ArrayList<Character>());
      for (int j = 0; j < listToAdd.size(); j++) {
        currentTable.get(i).add(listToAdd.get(j));
      }
      listToAdd.add(listToAdd.get(0));
      listToAdd.remove(0);
    }
    return currentTable;
  }

  // decodes a message given a key using the Vigenere table
  String decode(String msg, String key) {
    String keyword = this.generateKeyword(key, msg.length());
    String unencryptedMsg = "";
    for (int i = 0; i < msg.length(); i++) {
      unencryptedMsg = unencryptedMsg + this.alphabet
          .get(table.get(this.alphabet.indexOf(keyword.charAt(i))).indexOf(msg.charAt(i)));
    }
    return unencryptedMsg;
  }

  // encrypts a message given a key using the Vigenere table
  String encode(String msg, String key) {
    String keyword = this.generateKeyword(key, msg.length());
    String encryptedMsg = "";
    for (int i = 0; i < msg.length(); i++) {
      encryptedMsg = encryptedMsg + this.table.get(this.alphabet.indexOf(msg.charAt(i)))
          .get(this.alphabet.indexOf(keyword.charAt(i)));
    }
    return encryptedMsg;
  }

  // adjusts the length of the key to be the length of the message by duplicating
  // it or shortening it
  String generateKeyword(String key, int len) {
    String keyword = key;
    while (keyword.length() < len) {
      keyword = keyword + key;
    }
    return keyword.substring(0, keyword.length() - (keyword.length() - len));
  }
}

class VigenereExamples {
  void testMethods(Tester t) {
    Vigenere v = new Vigenere();

    // tests
    // test table
    t.checkExpect(new Vigenere().initVigenere().size(), 26);
    for (int i = 0; i < 26; i++) {
      t.checkExpect(new Vigenere().initVigenere().get(i).size(), 26);
      t.checkExpect(new Vigenere().initVigenere().get(i).get(0), new Vigenere().alphabet.get(i));
      t.checkExpect(new Vigenere().initVigenere().get(i).get(3),
          new Vigenere().alphabet.get((i + 3) % 26));
      t.checkExpect(new Vigenere().initVigenere().get(i).get(25),
          new Vigenere().alphabet.get((i + 25) % 26));
    }

    // test generateKeyword
    t.checkExpect(v.generateKeyword("happy", 12), "happyhappyha");
    t.checkExpect(v.generateKeyword("awesome", 20), "awesomeawesomeawesom");
    t.checkExpect(v.generateKeyword("excellent", 4), "exce");

    // test decode
    t.checkExpect(v.decode("ahpcizgxkgug", "happy"), "thanksgiving");

    // test encode
    t.checkExpect(v.encode("thanksgiving", "happy"), "ahpcizgxkgug");

    // print table
    for (int i = 0; i < v.table.size(); i++) {
      for (int j = 0; j < v.table.get(i).size(); j++) {
        System.out.print(v.table.get(i).get(j) + " ");
      }
      System.out.println();
    }
  }
}
