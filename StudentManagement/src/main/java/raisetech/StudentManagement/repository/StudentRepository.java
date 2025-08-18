package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.BasicCourse;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.data.BasicCourse;

@Mapper
public interface StudentRepository {

    @Select("SELECT * FROM students WHERE is_deleted = false")
    List<Student> searchActiveStudents();

    @Select("""
    SELECT sc.*, s.name AS student_name, bc.course_name,bc.is_end
    FROM students_courses sc
    JOIN students s ON sc.student_id = s.id
    JOIN basic_courses bc ON sc.course_id = bc.id
""")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "studentId", column = "student_id"),
        @Result(property = "courseId", column = "course_id"),
        @Result(property = "studentName", column = "student_name"),
        @Result(property = "courseName", column = "course_name"),
        @Result(property = "isEnd", column = "is_end"),  // ← 追加
        @Result(property = "courseStartAt", column = "course_start_at"),
        @Result(property = "courseEndAt", column = "course_end_at")
    })
    List<StudentsCourses> searchStudentsCourses();
    @Select("SELECT * FROM students WHERE is_deleted = true")
    List<Student> searchDeletedStudents();

    @Insert("INSERT INTO students (name, kana_name, nickname, email, area, age, sex, remark,is_deleted) " +
        "VALUES (#{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{sex}, #{remark},false)")
    @Options(useGeneratedKeys = true, keyProperty = "id")  // 追加
    void insertStudent(Student student);  // ← 追加部分

    @Insert("""
    INSERT INTO students_courses (student_id, course_id, course_start_at, course_end_at)
    VALUES (#{studentId}, #{courseId}, #{courseStartAt}, #{courseEndAt})
    """)
    void insertStudentsCourses(StudentsCourses course);//course_name →course_id変更

    @Select("SELECT * FROM students WHERE id = #{id}")
    Student findStudentById(Long id);

    @Select("""
    SELECT sc.*, bc.course_name
    FROM students_courses sc
    JOIN basic_courses bc ON sc.course_id = bc.id
    WHERE sc.student_id = #{studentId}
""")
    @Results(id = "CoursesResultMap", value = {
        @Result(property = "id", column = "id"),
        @Result(property = "studentId", column = "student_id"),
        @Result(property = "courseId", column = "course_id"),
        @Result(property = "courseName", column = "course_name"),  // course_name をマッピング
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

    @Select("SELECT * FROM basic_courses")
    List<BasicCourse> findAllCourses();//→ フォームで表示するための全コース一覧取得

    @Select("SELECT * FROM basic_courses WHERE id = #{id}")
    BasicCourse findCourseById(Integer id);//→ 選択されたコースのデフォルト日付を取得

    @Insert("""
        INSERT INTO basic_courses (course_name, default_start_at, default_end_at, is_end)
        VALUES (#{courseName}, #{defaultStartAt}, #{defaultEndAt}, #{isEnd})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertBasicCourse(BasicCourse course);
}