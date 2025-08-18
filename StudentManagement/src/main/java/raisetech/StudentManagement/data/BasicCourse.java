package raisetech.StudentManagement.data;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class BasicCourse {
  private Integer id; // DBのidカラムと対応（＝コースID）
  private String courseName;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate defaultStartAt;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate defaultEndAt;
  private String isEnd;
//  // 追加：受講生リスト
//  private List<StudentsCourses> studentsCourses;

  // Getter / Setter

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }

  public String getCourseName() { return courseName; }
  public void setCourseName(String courseName) { this.courseName = courseName; }

  public LocalDate getDefaultStartAt() { return defaultStartAt; }
  public void setDefaultStartAt(LocalDate defaultStartAt) { this.defaultStartAt = defaultStartAt; }

  public LocalDate getDefaultEndAt() { return defaultEndAt; }
  public void setDefaultEndAt(LocalDate defaultEndAt) { this.defaultEndAt = defaultEndAt; }

  public String getIsEnd() { return isEnd; }
  public void setIsEnd(String isEnd) { this.isEnd = isEnd; }

}