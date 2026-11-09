package oit.is.z3587.kaizi.janken.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.security.Principal;

import java.util.List;
import java.util.ArrayList;
/*ランダム用 */
import java.util.concurrent.ThreadLocalRandom;

import oit.is.z3587.kaizi.janken.model.Janken;

import oit.is.z3587.kaizi.janken.model.Entry;

import oit.is.z3587.kaizi.janken.model.User;

import oit.is.z3587.kaizi.janken.model.UserMapper;

import oit.is.z3587.kaizi.janken.model.Match;
import oit.is.z3587.kaizi.janken.model.MatchMapper;
import oit.is.z3587.kaizi.janken.model.MatchInfo;
import oit.is.z3587.kaizi.janken.model.MatchInfoMapper;
import oit.is.z3587.kaizi.janken.service.AsyncKekka;

@Controller
/* 全てのメソッドは/jankenで呼び出される */
// @RequestMapping("/janken")
public class JankenController {

  @Autowired
  MatchMapper matchMapper;
  @Autowired
  UserMapper userMapper;
  @Autowired
  MatchInfoMapper matchInfoMapper;
  @Autowired
  AsyncKekka asyncKekka;
  // private Entry entry;

  private static final List<String> HANDS = List.of("Gu", "Choki", "Pa");

  @GetMapping("/janken")
  public String janken1(ModelMap model) {
    /* task2で追加 */
    /*
     * lec04-task3で削除
     * String loginUser = prin.getName();
     * this.entry.addUser(loginUser);
     * odel.addAttribute("entry", this.entry);
     */
    /* ここまで */
    /* lec04-task3で追加 */
    ArrayList<User> Users = userMapper.selectAllUserName();
    model.addAttribute("users", Users);
    /* ここまで */
    /* lec04-task4で追加 */
    ArrayList<Match> Matches = matchMapper.selectAllMatches();
    model.addAttribute("Matches", Matches);
    /* ここまで */
    /* lec05-task2で追加 */
    ArrayList<MatchInfo> MatchInfos = matchInfoMapper.selectActiveMatchInfo();
    model.addAttribute("MatchInfos", MatchInfos);
    /* ここまで */

    return "janken.html";
  }

  /**
   * @param name
   * @return
   */
  @PostMapping("/janken")
  public String janken2(@RequestParam String name, ModelMap model) {
    model.addAttribute("name", name);
    return "janken.html";
  }

  @GetMapping("/match")
  public String match(@RequestParam int id, ModelMap model) {
    User user = userMapper.selectById(id);
    model.addAttribute("username", user.getName());
    model.addAttribute("id", user.getId());
    return "match.html";
  }

  @GetMapping("/fight")
  public String fight(@RequestParam int id, @RequestParam String hand, Principal prin, ModelMap model) {
    Match match = new Match();
    /* ログインユーザ */
    User userinfo1 = userMapper.selectByName(prin.getName());
    /* 対戦相手 */
    User userinfo2 = userMapper.selectById(id);

    match.setUser1(userinfo1.getId());
    match.setUser2(userinfo2.getId());
    match.setUser1Hand(hand);
    Janken janken = null;
    if (userinfo2.getName().equals("CPU")) {
      if (hand.equals("Gu")) {
        janken = play("Gu");
      } else if (hand.equals("Choki")) {
        janken = play("Choki");
      } else if (hand.equals("Pa")) {
        janken = play("Pa");
      }
      if (janken.getComHand().equals("Gu")) {
        match.setUser2Hand("Gu");
      } else if (janken.getComHand().equals("Choki")) {
        match.setUser2Hand("Choki");
      } else if (janken.getComHand().equals("Pa")) {
        match.setUser2Hand("Pa");
      }
      matchMapper.insert(match);

    } else { /* 対戦相手がCPUでないとき */

      // Aによって対戦相手Bが指定されているアクティブな試合があるか確認(引数はBのID)
      if (matchInfoMapper.existsActiveMatchInfo(userinfo1.getId())) {
        MatchInfo activeMatchInfo = matchInfoMapper.getActiveMatchInfoById(userinfo1.getId());
        // 対戦が成立したため対象のmatchinfoを非アクティブにする
        matchInfoMapper.updateInactive(activeMatchInfo.getId());
        Match match2 = new Match();
        match2.setUser1(activeMatchInfo.getUser1());
        match2.setUser2(activeMatchInfo.getUser2());
        match2.setUser1Hand(activeMatchInfo.getUser1Hand());
        match2.setUser2Hand(hand);
        match2.setisActive(true);
        matchMapper.insertPlayerMatch(match2);
        janken = playerPlay(hand, activeMatchInfo.getUser1Hand(), model);

      } else {/* 対戦相手が指定されているアクティブな試合がない場合は待機画面に遷移 */
        MatchInfo matchInfo = new MatchInfo(userinfo1.getId(), userinfo2.getId(), hand);
        matchInfoMapper.insert(matchInfo);
        model.addAttribute("username", userinfo2.getName());
        model.addAttribute("id", userinfo2.getId());
        return "wait.html";
      }
    }

    model.addAttribute("username", userinfo2.getName());
    model.addAttribute("id", userinfo2.getId());

    model.addAttribute("result", janken);

    return "match.html";
  }

  @GetMapping("/fight/sse")
  public SseEmitter fightSse(Principal prin) {
    User user = userMapper.selectByName(prin.getName());
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    asyncKekka.asyncKekka(emitter, user.getId());
    return emitter;
  }

  @GetMapping("/fight/gu")
  public String gu(@RequestParam String name, ModelMap model) {
    return play("Gu", name, model);
  }

  @GetMapping("/fight/choki")
  public String choki(@RequestParam String name, ModelMap model) {
    return play("Choki", name, model);
  }

  @GetMapping("/fight/pa")
  public String pa(@RequestParam String name, ModelMap model) {
    return play("Pa", name, model);
  }

  private String play(String myHand, String name, ModelMap model) {
    // String comHand =
    // HANDS.get(ThreadLocalRandom.current().nextInt(HANDS.size()));
    String comHand = HANDS.get(0);
    model.addAttribute("name", name);
    model.addAttribute("result", new Janken(myHand, comHand));
    return "match.html";
  }

  private Janken play(String myHand) {
    String comHand = HANDS.get(ThreadLocalRandom.current().nextInt(HANDS.size()));
    // String comHand = HANDS.get(0);
    Janken janken = new Janken(myHand, comHand);
    return janken;
  }

  private Janken playerPlay(String myHand, String oppHand, ModelMap model) {
    Janken janken = new Janken(myHand, oppHand);
    model.addAttribute("result", janken);
    return janken;
  }
}
