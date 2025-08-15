package raisetech.StudentManagement1.repository;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement1.data.Student;
import raisetech.StudentManagement1.data.StudentsCourses;

@Mapper
public interface StudentRepository {

    @Select("SELECT * FROM students WHERE is_deleted = false")
    List<Student> searchActiveStudents();

    @Select("""
      SELECT sc.*, s.name AS student_name
      FROM students_courses sc
      JOIN students s ON sc.student_id = s.id
      """)
    List<StudentsCourses> searchStudentsCourses();

    @Select("SELECT * FROM students WHERE is_deleted = true")
    List<Student> searchDeletedStudents();

    @Insert("INSERT INTO students (name, kana_name, nickname, email, area, age, sex, remark,is_deleted) " +
        "VALUES (#{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{sex}, #{remark},false)")
    @Options(useGeneratedKeys = true, keyProperty = "id")  // 追加
    void insertStudent(Student student);  // ← 追加部分

    @Insert("INSERT INTO students_courses (student_id, course_name, course_start_at, course_end_at) " +
        "VALUES (#{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt})")
    void insertStudentsCourses(StudentsCourses course);

    @Select("SELECT * FROM students WHERE id = #{id}")
    Student findStudentById(Long id);

    @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
    @Results(id = "CoursesResultMap", value = {
        @Result(property = "id", column = "id"),
        @Result(property = "studentId", column = "student_id"),
        @Result(property = "courseName", column = "course_name"),
        @Result(property = "courseStartAt", column = "course_start_at"),
        @Result(property = "courseEndAt", column = "course_end_at")
    })
    List<StudentsCourses> findCoursesByStudentId(Long studentId);

    @Update("""
      UPDATE students
      SET name = #{name},
          kana_name = #{kanaName},
          nickname = #{nickname},
          email = #{email},
          area = #{area},
          age = #{age},
          sex = #{sex},
          remark = #{remark},
          is_deleted = #{isDeleted}
      WHERE id = #{id}
      """)
    void updateStudent(Student student);

    @Delete("DELETE FROM students WHERE id = #{studentId}")
    void deleteStudentsByStudentId(Integer studentId);

    @Delete("DELETE FROM students_courses WHERE student_id = #{studentId}")
    void deleteCoursesByStudentId(Integer studentId);
}
