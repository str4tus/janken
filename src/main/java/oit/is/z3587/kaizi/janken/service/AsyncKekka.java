package oit.is.z3587.kaizi.janken.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import oit.is.z3587.kaizi.janken.model.Match;
import oit.is.z3587.kaizi.janken.model.MatchMapper;

@Service
public class AsyncKekka {

  @Autowired
  MatchMapper matchMapper;

/**
   * 非同期で試合結果を監視し、待機中のプレイヤーへ送信する
   *
   * @param emitter SSEの送信先
   * @param userId 待機中のプレイヤーのID
   */
  @Async
  public void asyncKekka(SseEmitter emitter, int userId) {
    try {
      while (true) {
        ArrayList<Match> matches = matchMapper.selectAllMatches();
        for (Match match : matches) {
          boolean isWaitingPlayer = match.getUser1() == userId || match.getUser2() == userId;
          if (match.getisActive() && isWaitingPlayer) {
            emitter.send(match);
            match.setisActive(false);
            matchMapper.updateIsActive(match);
            emitter.complete();
            return;
          }
        }
        TimeUnit.MILLISECONDS.sleep(500);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      emitter.completeWithError(e);
    } catch (IOException e) {
      emitter.completeWithError(e);
    }
  }
}
