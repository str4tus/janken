package oit.is.z3587.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface MatchMapper {
  /* 手の名前の大きさ(a,A)が異なるがその区別は無視される */
  @Select("SELECT * from matches")
  ArrayList<Match> selectAllMatches();

}
