package raisetech.StudentManagement1.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

  @Getter
  @Setter
  public class StudentsCourses {

    private Integer id;
    private Integer studentId;
    private String studentName; // ← 追加
    private String courseName;
    private LocalDate courseStartAt;
    private LocalDate courseEndAt;
  }

