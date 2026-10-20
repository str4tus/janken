package oit.is.z3587.kaizi.janken.model;

import java.util.ArrayList;

//import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

  /**
   * IDを指定してユーザーを取得する
   * #{・・・}はUSERクラスのフィールドを示す
   * javaとDBのカラム名が異なる場合はasを使いDBのカラム名を
   * javaのフィールド名に合わせる必要がある
   *
   * @param id ユーザーのID
   * @return ユーザー情報
   */
  @Select("SELECT id,name from users where id = #{id}")
  User selectById(int id);

  @Select("SELECT id, name from users where name = #{name}")
  User selectByName(String name);

  @Select("SELECT * from users")
  ArrayList<User> selectAllUserName();

}
