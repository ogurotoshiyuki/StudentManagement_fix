package raisetech.StudentManagement1.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement1.data.Student;
import raisetech.StudentManagement1.data.StudentsCourses;
import raisetech.StudentManagement1.repository.StudentRepository;

@Service
public class StudentService {
  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository){
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.search();
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

  @Transactional
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

  public void deleteCoursesByStudentId(Integer studentId) {
    repository.deleteCoursesByStudentId(studentId);
  }
}
