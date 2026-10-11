package oit.is.z3587.kaizi.janken.model;

public class Janken {

  private final String myHand;
  private final String comHand;
  private final String result;

  public Janken(String myHand, String comHand) {
    this.myHand = myHand;
    this.comHand = comHand;
    this.result = judge(myHand, comHand);
  }

  private String judge(String myHand, String comHand) {
    String[] hands = { "グー", "チョキ", "パー" };
    int myIndex = indexOf(hands, myHand);
    int comIndex = indexOf(hands, comHand);
    int outcome = (comIndex - myIndex + hands.length) % hands.length;

    if (outcome == 0) {
      return "あいこ";
    }
    return outcome == 1 ? "勝ち" : "負け";
  }

  private int indexOf(String[] hands, String hand) {
    for (int i = 0; i < hands.length; i++) {
      if (hands[i].equals(hand)) {
        return i;
      }
    }
    return -1;
  }

  public String getMyHand() {
    return myHand;
  }

  public String getComHand() {
    return comHand;
  }

  public String getResult() {
    return result;
  }
}
