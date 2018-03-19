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

    private String labelValue;

    private List<GroupEntity> allGroupsStudents;

    @EJB
    private DatabaseOperationsBean databaseOperationsBean;

    @EJB
    private MarshallingBean marshallingBean;

    @PostConstruct
    public void init() {
        this.allGroupsStudents = databaseOperationsBean.getAllGroupsStudents();
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

    public void setAllGroupsStudents(List<GroupEntity> allGroupsStudents) {
        this.allGroupsStudents = allGroupsStudents;
    }

    public List<GroupEntity> getAllGroupsStudents(){
        return allGroupsStudents;
    }

    public String getLabelValue() {
        return labelValue;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue;
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
        allGroupsStudents = databaseOperationsBean.getStudentsBy(groupName, studentName, studentSurname);
        clearFields();
    }

    public void clearFields() {
        this.studentName = "";
        this.studentSurname = "";
        this.groupName = "";
        this.groupId = null;
        this.studentId = null;
        this.labelValue = "";
    }

    public void importList() {
        this.labelValue = "";
        ListWrapper listWrapper = new ListWrapper();
        try {
            listWrapper = marshallingBean.readFromXML();
        } catch (Exception e) {
            e.printStackTrace();
            this.labelValue = "File is not correct";
        }
        List<GroupEntity> result = listWrapper.getGroups();
        if (result == null) {
            return;
        }
        for (GroupEntity temp : result) {
            if (!databaseOperationsBean.isGroupExists(temp.getId())) {
                temp.setId(null);

                for (int i = 0; i < temp.getStudents().size(); i++) {
                    temp.getStudents().get(i).setId(null);
                    temp.getStudents().get(i).setGroup(temp);
                }
                databaseOperationsBean.persistGroup(temp);
            } else {
                for (int i = 0; i < temp.getStudents().size(); i++) {
                    temp.getStudents().get(i).setGroup(temp);
                    databaseOperationsBean.updateStudent(temp.getStudents().get(i));
                }
                databaseOperationsBean.updateGroup(temp.getName(), temp.getId());
            }
        }
        allGroupsStudents = databaseOperationsBean.getAllGroupsStudents();
    }

    public void exportList() {
        marshallingBean.saveToXML(allGroupsStudents);
    }

    public void updateList() {
        allGroupsStudents = databaseOperationsBean.getAllGroupsStudents();
    }
}
