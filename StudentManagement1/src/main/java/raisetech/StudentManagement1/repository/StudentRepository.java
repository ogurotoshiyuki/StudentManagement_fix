package raisetech.StudentManagement1.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement1.data.Student;
import raisetech.StudentManagement1.data.StudentsCourses;

@Mapper
public interface StudentRepository {

    @Select("SELECT * FROM students")                        //2025/07/10追加
    List<Student> search();

//    @Select("SELECT * FROM students_courses")
//    List<StudentsCourses> searchStudentsCourses();
    @Select("""
      SELECT sc.*, s.name AS student_name
      FROM students_courses sc
      JOIN students s ON sc.student_id = s.id
      """)
    List<StudentsCourses> searchStudentsCourses();

    @Insert("INSERT INTO students (name, kana_name, nickname, email, area, age, sex, remark) " +
        "VALUES (#{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{sex}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")  // 追加
    void insertStudent(Student student);  // ← 追加部分

    @Insert("INSERT INTO students_courses (student_id, course_name, course_start_at, course_end_at) " +
        "VALUES (#{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt})")
    void insertStudentsCourses(StudentsCourses course);
}
