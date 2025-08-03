package raisetech.StudentManagement1.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement1.controller.converter.StudentConverter;
import raisetech.StudentManagement1.data.Student;
import raisetech.StudentManagement1.data.StudentsCourses;
import raisetech.StudentManagement1.domain.StudentDetail;
import raisetech.StudentManagement1.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  //メニュー
  @GetMapping("/menu")
  public String menu(){
    return "menu";
  }

  // 受講生一覧表示（HTML出力）
  @GetMapping(value = "/studentList", produces = "application/json; charset=UTF-8")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCoursesList();
    model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
    return "studentList"; // ← HTMLテンプレート名（拡張子不要）
  }

  // 受講生のコース一覧表示（HTML出力）
  @GetMapping(value = "/studentsCoursesList", produces = "application/json; charset=UTF-8")
  public String getStudentsCoursesList(Model model) {
    List<StudentsCourses> list = service.searchStudentsCoursesList();
    model.addAttribute("coursesList", list);
    return "studentsCoursesList"; // ← HTMLテンプレート名（拡張子不要）
  }

  // 新規受講生登録画面の表示
  @GetMapping(value = "/newStudent", produces = "application/json; charset=UTF-8")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent"; // ← HTMLテンプレート名（拡張子不要）
  }

  
  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result, Model model) {
    if (result.hasErrors()) {
      return "registerStudent";
    }

    Student student = studentDetail.getStudent();

    List<StudentsCourses> rawCoursesList = studentDetail.getStudentsCoursesList();
    List<StudentsCourses> filteredCourses = new ArrayList<>();

    if (rawCoursesList != null) {
      for (StudentsCourses course : rawCoursesList) {
        boolean allEmpty = (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) &&
            course.getCourseStartAt() == null &&
            course.getCourseEndAt() == null;

        boolean allFilled = (course.getCourseName() != null && !course.getCourseName().trim().isEmpty()) &&
            course.getCourseStartAt() != null &&
            course.getCourseEndAt() != null;

        if (allFilled) {
          filteredCourses.add(course);
        } else if (!allEmpty) {
          result.rejectValue("studentsCoursesList", "incompleteCourse", "受講コースの入力が不完全です。すべての項目を入力してください。");
          model.addAttribute("studentDetail", studentDetail);
          return "registerStudent";
        }
      }
    }

    if (filteredCourses.isEmpty()) {
      result.rejectValue("studentsCoursesList", "emptyCourse", "少なくとも1つ以上の受講コースを入力してください。");
      model.addAttribute("studentDetail", studentDetail);
      return "registerStudent";
    }

    // student保存
    service.insertStudent(student);

    // student.getId() が DB自動採番で入るならここでIDが入っている想定
    for (StudentsCourses course : filteredCourses) {
      course.setStudentId(student.getId());
      service.insertStudentsCourses(course);
    }

    System.out.println(student.getName() + "さんが新規受講生として登録されました。");

    return "redirect:/studentList";
  }
}