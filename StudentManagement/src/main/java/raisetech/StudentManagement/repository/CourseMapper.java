package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.data.BasicCourse;


@Mapper
public interface CourseMapper {
  @Select("SELECT id, course_name AS courseName, default_start_at AS defaultStartAt, default_end_at AS defaultEndAt FROM basic_courses")
  List<BasicCourse> findAllCourses();

  @Select("SELECT id, course_name AS courseName, default_start_at AS defaultStartAt, default_end_at AS defaultEndAt FROM basic_courses WHERE id = #{id}")
  BasicCourse findById(Integer id);
}