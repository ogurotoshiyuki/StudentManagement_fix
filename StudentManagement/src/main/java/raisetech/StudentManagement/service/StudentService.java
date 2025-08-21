package raisetech.StudentManagement.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.BasicCourse;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.repository.BasicCourseMapper;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
@Transactional
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.searchActiveStudents();
  }

  public List<Student> searchDeletedStudents() {
    return repository.searchDeletedStudents();
  }

  public List<StudentsCourses> searchStudentsCoursesList() {
    return repository.searchStudentsCourses();
  }

  public Student findStudentById(Long id) {
    return repository.findStudentById(id);
  }

  public List<StudentsCourses> findCoursesByStudentId(Long studentId) {
    return repository.findCoursesByStudentId(studentId);
  }

  public void registerStudent(Student student) {
    repository.insertStudent(student);
  }

  public void insertStudent(Student student) {
    repository.insertStudent(student);
  }

  public void insertStudentsCourses(StudentsCourses course) {
    repository.insertStudentsCourses(course);
  }

  public void updateStudent(Student student) {
    repository.updateStudent(student);
  }

  public void deleteStudentsByStudentId(Integer studentId) {
    repository.deleteStudentsByStudentId(studentId);
  }

  public void deleteCoursesByStudentId(Integer studentId) {
    repository.deleteCoursesByStudentId(studentId);
  }

  public void deleteActiveCoursesByStudentId(Integer studentId) {
    repository.deleteActiveCoursesByStudentId(studentId);
  }
}





