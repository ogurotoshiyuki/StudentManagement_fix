package raisetech.StudentManagement.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.BasicCourse;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;
import org.springframework.web.bind.annotation.RequestParam;
import raisetech.StudentManagement.repository.CourseMapper;
import java.util.Comparator;

@Controller
public class StudentController {

  private final StudentService service;
  private final StudentConverter converter;
  private final CourseMapper courseMapper;  // ← ここも final に

  @Autowired
  public StudentController(StudentService service,
      StudentConverter converter,
      CourseMapper courseMapper) {
    this.service = service;
    this.converter = converter;
    this.courseMapper = courseMapper;
  }


  //メニュー
  @GetMapping("/menu")
  public String menu(){
    return "menu";
  }

  // 受講生一覧表示（HTML出力）
  @GetMapping(value = "/studentList", produces = "application/json; charset=UTF-8")
  public String getStudentList(Model model) {
    // isDeleted = false のみ取得
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCoursesList();

    List<StudentDetail> studentDetails = converter.convertStudentDetails(students, studentsCourses)
        .stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    model.addAttribute("studentList", studentDetails);

    // 追加: コース一覧も渡す
    List<StudentsCourses> list = service.searchStudentsCoursesList();
    model.addAttribute("coursesList", list);
    // 終了フラグを確認（スナップ出力）
    for (StudentsCourses sc : list) {
      System.out.println("コースID=" + sc.getCourseId() + " 終了済=" + sc.getIsEnd());
    }
    return "studentList"; // ← HTMLテンプレート名（拡張子不要）
  }

  // 受講生のコース一覧表示（HTML出力）
  @GetMapping(value = "/studentsCoursesList", produces = "application/json; charset=UTF-8")
  public String getStudentsCoursesList(Model model) {
    List<StudentsCourses> list = service.searchStudentsCoursesList();

    // 基本コースID順にソート
    list.sort(Comparator.comparing(StudentsCourses::getCourseId));
    // 終了フラグを確認（スナップ出力）
//    for (StudentsCourses sc : list) {
//      System.out.println("コースID=" + sc.getCourseId() + " 終了済=" + sc.getIsEnd());
//    }
    model.addAttribute("coursesList", list);
    return "studentsCoursesList";
  }

  // 受講生情報更新表示（HTML出力）
  @GetMapping("/editStudent/{id}")
  public String editStudent(@PathVariable("id") Long id, Model model) {
    Student student = service.findStudentById(id);
    List<StudentsCourses> courses = service.findCoursesByStudentId(id);

    // 受講コースが2件未満なら補充
    while (courses.size() < 2) {
      courses.add(new StudentsCourses());
    }

    StudentDetail detail = new StudentDetail(student, courses);
    model.addAttribute("studentDetail", detail);
    model.addAttribute("allCourses", courseMapper.findAllCourses());

    return "editStudent";
  }

  //受講生情報更新
  @PostMapping("/updateStudent")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result, Model model) {
    if (result.hasErrors()) {
      return "editStudent";
    }

    // 学生情報更新（isDeletedも含む）
    service.updateStudent(studentDetail.getStudent());

    // コース情報一度レコード削除(除く終了コース)して再登録
    service.deleteActiveCoursesByStudentId(studentDetail.getStudent().getId());

    List<StudentsCourses> courses = studentDetail.getStudentsCoursesList();
    if (courses != null) {
      for (StudentsCourses course : courses) {
        if (course == null) continue;

        // ❶ courseId が無ければ「削除扱い」＝何もしない（他の項目が入っていても無視）
        if (course.getCourseId() == null) {
          continue;
        }

        // ❷ 登録条件：courseId + start + end がすべてある
        if (course.getCourseStartAt() != null && course.getCourseEndAt() != null) {
          course.setStudentId(studentDetail.getStudent().getId());
          service.insertStudentsCourses(course);
        } else {
          // ❸ courseId はあるのに日付が欠けていたらエラー
          result.rejectValue("studentsCoursesList", "incompleteCourse",
              "受講コースの入力が不完全です。開始日と終了日は必須です。");
          model.addAttribute("studentDetail", studentDetail);
          // ← 重要：エラーで戻すときもセレクト表示用のリストを再セット
          model.addAttribute("allCourses", courseMapper.findAllCourses());
          return "editStudent";
        }
      }
    }
    return "redirect:/studentList";
  }

  // 削除受講生の一覧表示
  @GetMapping("/deletedStudentList")
  public String getDeletedStudentList(Model model) {
    // isDeleted = true のみ取得
    List<Student> students = service.searchDeletedStudents();
    List<StudentsCourses> studentsCourses = service.searchStudentsCoursesList();
    model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
    return "deletedStudentList"; // HTMLテンプレート名
  }
  //受講生情報削除復帰表示（HTML出力）
  @GetMapping("/restoreStudent/{id}")
  public String restoretStudent(@PathVariable("id") Long id, Model model) {
    Student student = service.findStudentById(id);
    List<StudentsCourses> courses = service.findCoursesByStudentId(id);
    //スナップ
//    System.out.println("student = " + student);
//    System.out.println("courses = " + courses);
    // 目的に合ったコンストラクタで生成
    StudentDetail detail = new StudentDetail(student, courses);
    model.addAttribute("studentDetail", detail);
    return "restoreStudent";// ← Thymeleafテンプレート名
  }
  //　論理削除復活用ＰＯＳＴ
  @PostMapping("/restoreStudents")
  public String restoreStudents(@RequestParam(required = false) List<Long> restoreIds) {
    if (restoreIds != null) {
      for (Long id : restoreIds) {
        Student student = service.findStudentById(id);
        if (student != null) {
          student.setDeleted(false); // 論理削除フラグ解除
          service.updateStudent(student);
        }
      }
    }
    return "redirect:/studentList";
  }

  // 新規受講生登録画面の表示
  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail detail = new StudentDetail();

    // 空なら2件分のコース欄を追加しておく
    List<StudentsCourses> list = new ArrayList<>();
    list.add(new StudentsCourses());
    list.add(new StudentsCourses());
    detail.setStudentsCoursesList(list);

    model.addAttribute("studentDetail", detail);
    model.addAttribute("allCourses", courseMapper.findAllCourses());

    return "registerStudent";
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
        boolean allEmpty = course.getCourseId() == null &&
            course.getCourseStartAt() == null &&
            course.getCourseEndAt() == null;

        boolean allFilled = course.getCourseId() != null &&
            course.getCourseStartAt() != null &&
            course.getCourseEndAt() != null;

        if (allFilled) {
          filteredCourses.add(course); // 登録対象
        } else if (!allEmpty) {
          // 一部だけ入力されている場合はエラー
          result.rejectValue("studentsCoursesList", "incompleteCourse",
              "受講コースの入力が不完全です。すべての項目を入力してください。");
          model.addAttribute("studentDetail", studentDetail);
          model.addAttribute("allCourses", courseMapper.findAllCourses());
          return "registerStudent";
        }
        // allEmpty は無視（未入力は登録しない）
      }
    }

    // 「少なくとも1件は登録必須」とする場合
    if (filteredCourses.isEmpty()) {
      result.rejectValue("studentsCoursesList", "firstCourseEmpty",
          "少なくとも1つ目の受講コースは必須です。");
      model.addAttribute("studentDetail", studentDetail);
      model.addAttribute("allCourses", courseMapper.findAllCourses());
      return "registerStudent";
    }

    // student保存
    service.insertStudent(student);

    // 受講コース保存
    for (StudentsCourses course : filteredCourses) {
      course.setStudentId(student.getId());
      service.insertStudentsCourses(course);
    }
    return "redirect:/studentList";
  }
  // 受講生の完全削除
  @GetMapping("/deleteStudent/{id}")
  public String deleteStudent(@PathVariable("id") Integer id) {
    // 関連コースを先に削除
    service.deleteCoursesByStudentId(id);

    // 本体を削除
    service.deleteStudentsByStudentId(id);

    return "redirect:/deletedStudentList";
  }
}