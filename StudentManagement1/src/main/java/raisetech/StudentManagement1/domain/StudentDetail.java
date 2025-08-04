package raisetech.StudentManagement1.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagement1.data.Student;
import raisetech.StudentManagement1.data.StudentsCourses;

@Getter
@Setter
  public class StudentDetail {

    private Student student;
    private List<StudentsCourses> studentsCoursesList;

    // 新規用コンストラクタ
    public StudentDetail() {
      this.student = new Student();
      this.studentsCoursesList = new ArrayList<>(
          Arrays.asList(new StudentsCourses(), new StudentsCourses()));
    }

    // DBから取得したデータをセットする用コンストラクタ
    public StudentDetail(Student student, List<StudentsCourses> coursesList) {
      this.student = student;
      this.studentsCoursesList = coursesList != null ? coursesList : new ArrayList<>();
    }
  }

