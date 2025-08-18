package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.BasicCourse;
import raisetech.StudentManagement.repository.BasicCourseMapper;

// --- BasicCourse 関連 ---
@Service
@Transactional
public class BasicCourseService {

  private final BasicCourseMapper courseMapper;

  @Autowired
  public BasicCourseService(BasicCourseMapper courseMapper) {
    this.courseMapper = courseMapper;
  }

  public List<BasicCourse> getAllCourses() {
    return courseMapper.findAllCourses();
  }

  public BasicCourse getCourseById(Integer id) {
    return courseMapper.findById(id);
  }

  public void saveCourse(BasicCourse course) {
    if (course.getId() == null) {
      courseMapper.insertCourse(course);
    } else {
      courseMapper.updateCourse(course);
    }
  }
}