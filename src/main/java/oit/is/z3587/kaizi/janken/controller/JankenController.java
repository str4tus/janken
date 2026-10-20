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
/*ランダム用 */
import java.util.concurrent.ThreadLocalRandom;

import oit.is.z3587.kaizi.janken.model.Janken;

import oit.is.z3587.kaizi.janken.model.Entry;

@Controller
/* 全てのメソッドは/jankenで呼び出される */
@RequestMapping("/janken")
public class JankenController {

  @Autowired
  private Entry entry;

  private static final List<String> HANDS = List.of("グー", "チョキ", "パー");

  @GetMapping
  public String janken1(Principal prin, ModelMap model) {
    /* task2で追加 */
    String loginUser = prin.getName();
    this.entry.addUser(loginUser);
    model.addAttribute("entry", this.entry);
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
    return "janken.html";
  }
}
