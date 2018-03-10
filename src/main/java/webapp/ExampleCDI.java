package webapp;

import Entity.GroupEntity;
import Entity.UserEntity;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class ExampleCDI implements Serializable{

    private String groupName;
    private String studentName;
    private String studentSurname;
    private Integer groupId;

    private List<GroupEntity> allStudents;

    @EJB
    private ExampleEJB exampleEJB;

    @PostConstruct
    public void init() {
        this.allStudents = exampleEJB.getAllStudents();
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname = studentSurname;
    }

    public void setAllStudents(List<GroupEntity> allStudents) {
        this.allStudents = allStudents;
    }

    public List<GroupEntity> getAllStudents(){
        return allStudents;
        //return exampleEJB.getAllStudents();
    }

    public List<UserEntity> getAllUsers(){
        return exampleEJB.getAllUsers();
    }

    public void deleteStudent(Integer id) {
        try {
            exampleEJB.deleteStudent(id);
            init();
        } catch (Exception e) {

        }
    }

    public void deleteGroup(Integer id) {
        try {
            exampleEJB.deleteGroup(id);
            init();
        } catch (Exception e) {

        }
    }

    public String editGroup() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
        this.groupName = params.get("groupName");

        return "editGroup";
    }

    public String postCreateGroup() {
        this.groupName = "";
        return "editGroup";
    }

    public String createGroup(String groupName) {
        exampleEJB.createGroup(groupName);
        init();
        clearFields();
        return "index";
    }

    public String editStudent() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
        this.studentName = params.get("studentName");
        this.studentSurname = params.get("studentSurname");
        this.groupId = Integer.parseInt(params.get("groupId"));
        return "editStudent";
    }

    public String postCreateStudent() {
        this.studentName = "";
        this.studentSurname = "";
        return "editStudent";
    }

    public String createStudent(String studentName, String studentSurname, Integer groupId) {

        exampleEJB.createStudent(studentName, studentSurname, groupId);
        init();
        clearFields();
        return "index";
    }

    public void search(String groupName, String studentName, String studentSurname) {
        this.allStudents = exampleEJB.getStudentsBy(groupName, studentName, studentSurname);
        clearFields();
    }

    public void clearFields() {
        this.studentName = "";
        this.studentSurname = "";
        this.groupName = "";
    }
}
