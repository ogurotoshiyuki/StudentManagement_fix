package raisetech.StudentManagement1.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;



@Getter
  @Setter
  public class StudentsCourses {
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate courseStartAt;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate courseEndAt;
    private Integer id;
    private Integer studentId;
    private String studentName; // ← 追加
    private String courseName;
//    private LocalDate courseStartAt;
//    private LocalDate courseEndAt;
  }

