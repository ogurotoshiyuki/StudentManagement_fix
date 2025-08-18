package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.BasicCourse;

@Mapper
public interface BasicCourseMapper {
  @Select("SELECT * FROM basic_courses")
  List<BasicCourse> findAllCourses();

  @Select("SELECT * FROM basic_courses WHERE id = #{id}")
  BasicCourse findById(Integer id);

  @Update("""
        UPDATE basic_courses
        SET course_name = #{courseName},
            default_start_at = #{defaultStartAt},
            default_end_at = #{defaultEndAt},
            is_end = #{isEnd}
        WHERE id = #{id}
    """)
  void updateCourse(BasicCourse course);

  @Insert("""
        INSERT INTO basic_courses (course_name, default_start_at, default_end_at, is_end)
        VALUES (#{courseName}, #{defaultStartAt}, #{defaultEndAt}, #{isEnd})
    """)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertCourse(BasicCourse course);
}
