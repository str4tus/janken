package oit.is.z3587.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchInfoMapper {
  @Select("SELECT * from matchinfo where isActive = true")
  ArrayList<MatchInfo> selectActiveMatchInfo();

  /* 対象の対戦相手が指定されアクティブである試合があるか */
  @Select("SELECT EXISTS(SELECT 1 FROM matchinfo WHERE isActive = true AND user2 = #{user2})")
  boolean existsActiveMatchInfo(int user2);

  /* 対戦相手が指定されている特定のアクティブな試合を取得 */
  @Select("SELECT * FROM matchinfo WHERE isActive = true AND user2 = #{user2}")
  MatchInfo getActiveMatchInfoById(int user2);

  /**
   * 下記のOptionsを指定すると，insert実行時にAuto incrementされたIDの情報を取得できるようになる useGeneratedKeys
   * = true -> Keyは自動生成されることを表す keyColumn : keyになるテーブルのカラム名 keyProperty :
   * keyになるJavaクラスのフィールド名
   *
   * @param matchinfo
   */
  @Insert("INSERT INTO matchinfo(user1, user2, user1Hand, isActive) VALUES(#{user1}, #{user2}, #{user1Hand}, #{isActive})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(MatchInfo matchinfo);

  @Update("UPDATE matchinfo SET isActive = false WHERE id = #{id}")
  void updateInactive(int id);

}
