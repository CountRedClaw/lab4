package Utils;

import Entity.GroupEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "groups")
public class ListWrapper {

    @XmlElement(name = "group")
    public List<GroupEntity> groups;

    public ListWrapper() {
    }

    public ListWrapper(List<GroupEntity> taskList) {
        this.groups = taskList;
    }

    public List<GroupEntity> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupEntity> groups) {
        this.groups = groups;
    }
}
