package raisetech.StudentManagement.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;



  @Getter
  @Setter
  public class StudentsCourses {
    private Integer id;
    private Integer studentId;
    private Integer courseId;       // ← 修正
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate courseStartAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate courseEndAt;

    // JOIN 用
    private String studentName;
    private String courseName; // 表示用

    // BasicCourse の情報をコピーして表示用に持つ
    private LocalDate defaultStartAt;
    private LocalDate defaultEndAt;
    private String isEnd;
  }
