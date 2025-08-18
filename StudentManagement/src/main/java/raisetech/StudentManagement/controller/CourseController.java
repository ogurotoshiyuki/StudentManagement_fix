package raisetech.StudentManagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  // ← 必須！
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

import raisetech.StudentManagement.data.BasicCourse;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.service.BasicCourseService;

@Controller
public class CourseController {
  private final BasicCourseService courseService;

  public CourseController(BasicCourseService courseService) {
    this.courseService = courseService;
  }

  // 一覧表示
  @GetMapping("/basicCourses")
  public String basicCoursesList(Model model) {
    List<BasicCourse> courses = courseService.getAllCourses();
    model.addAttribute("coursesList", courses);
    for (BasicCourse sc : courses) {
      System.out.println("コースID=" + sc.getId() + " コース名=" + sc.getCourseName()+" 終了済=" + sc.getIsEnd());
    }
    return "basicCoursesList"; // Thymeleafテンプレート名
  }

  // 編集画面表示
  @GetMapping("/editCourse/{id}")
  public String editCourse(@PathVariable("id") Integer id, Model model) {
    BasicCourse course = courseService.getCourseById(id);
    model.addAttribute("course", course);
    return "editCourse"; // 編集用テンプレート
  }

  // 新規登録画面
  @GetMapping("/newCourse")
  public String newCourse(Model model) {
    model.addAttribute("course", new BasicCourse()); // ←修正
    return "editCourse"; // 編集テンプレートを流用
  }

  // 保存（更新 or 新規）
  @PostMapping("/saveCourse")
  public String saveCourse(@ModelAttribute BasicCourse course) {
    courseService.saveCourse(course);
    return "redirect:/basicCourses";
  }
}
