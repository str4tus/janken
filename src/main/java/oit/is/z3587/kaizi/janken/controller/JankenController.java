package oit.is.z3587.kaizi.janken.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

@Controller
/* 全てのメソッドは/jankenで呼び出される */
@RequestMapping("/janken")
public class JankenController {

  @Autowired
  MatchMapper matchMapper;
  @Autowired
  UserMapper userMapper;
  // private Entry entry;

  private static final List<String> HANDS = List.of("グー", "チョキ", "パー");

  @GetMapping
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

    return "janken.html";
  }

  /**
   * @param name
   * @return
   */
  @PostMapping
  public String janken2(@RequestParam String name, ModelMap model) {
    model.addAttribute("name", name);
    return "janken.html";
  }

  @GetMapping("/match")
  public String match(@RequestParam int id, ModelMap model) {
    User user = userMapper.selectById(id);
    model.addAttribute("username", user.getName());
    return "match.html";
  }

  @GetMapping("/gu")
  public String gu(@RequestParam String name, ModelMap model) {
    return play("グー", name, model);
  }

  @GetMapping("/choki")
  public String choki(@RequestParam String name, ModelMap model) {
    return play("チョキ", name, model);
  }

  @GetMapping("/pa")
  public String pa(@RequestParam String name, ModelMap model) {
    return play("パー", name, model);
  }

  private String play(String myHand, String name, ModelMap model) {
    // String comHand =
    // HANDS.get(ThreadLocalRandom.current().nextInt(HANDS.size()));
    String comHand = HANDS.get(0);
    model.addAttribute("name", name);
    model.addAttribute("result", new Janken(myHand, comHand));
    return "match.html";
  }
}
