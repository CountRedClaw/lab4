package webapp;

import Entity.GroupEntity;
import Utils.ListWrapper;
import Utils.MarshallingBean;

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
public class BackingCDI implements Serializable{

    private String groupName;
    private String studentName;
    private String studentSurname;
    private Integer groupId;
    private Integer studentId;

    private List<GroupEntity> allStudents;

    @EJB
    private DatabaseOperationsBean databaseOperationsBean;

    @EJB
    private MarshallingBean marshallingBean;

    @PostConstruct
    public void init() {
        this.allStudents = databaseOperationsBean.getAllStudents();
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
    }

    public void deleteStudent(Integer id) {
            databaseOperationsBean.deleteStudent(id);
            init();
    }

    public void deleteGroup(Integer id) {
            databaseOperationsBean.deleteGroup(id);
            init();
    }

    public String editGroup() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
        this.groupName = params.get("groupName");
        this.groupId = Integer.parseInt(params.get("groupId"));
        return "editGroup";
    }

    public String createNewGroup() {
        clearFields();
        return "editGroup";
    }

    public String createGroup(String groupName) {
        if(groupId != null) {
            databaseOperationsBean.updateGroup(groupName, groupId);
        } else {
            databaseOperationsBean.createGroup(groupName);
        }
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
        this.studentId = Integer.parseInt(params.get("studentId"));
        return "editStudent";
    }

    public String createNewStudent() {
        clearFields();
        return "editStudent";
    }

    public String createStudent(String studentName, String studentSurname, Integer groupId) {
        if(studentId != null) {
            databaseOperationsBean.updateStudent(studentName, studentSurname, groupId, studentId);
        } else {
            databaseOperationsBean.createStudent(studentName, studentSurname, groupId);
        }
        init();
        clearFields();
        return "index";
    }

    public void search(String groupName, String studentName, String studentSurname) {
        this.allStudents = databaseOperationsBean.getStudentsBy(groupName, studentName, studentSurname);
        clearFields();
    }

    public void clearFields() {
        this.studentName = "";
        this.studentSurname = "";
        this.groupName = "";
        this.groupId = null;
        this.studentId = null;
    }

    public void importList() {
        ListWrapper listWrapper = marshallingBean.readFromXML();
        List<GroupEntity> result = listWrapper.getGroups();
        if (result == null) {
            return;
        }
        for (GroupEntity temp : result) {
            if (!databaseOperationsBean.isGroupExists(temp.getId())) {
                System.out.println("не существует");
                temp.setId(null);

                for (int i = 0; i < temp.getStudents().size(); i++) {
                    temp.getStudents().get(i).setId(null);
                    temp.getStudents().get(i).setGroup(temp);
                }
                databaseOperationsBean.persistGroup(temp);
            } else {
                System.out.println("уже существует");
                for (int i = 0; i < temp.getStudents().size(); i++) {
                    temp.getStudents().get(i).setGroup(temp);
                    databaseOperationsBean.updateStudent(temp.getStudents().get(i));
                }
                databaseOperationsBean.updateGroup(temp.getName(), temp.getId());
            }
        }
        allStudents = databaseOperationsBean.getAllStudents();
    }

    public void exportList() {
        marshallingBean.saveToXML(allStudents);
    }
}
