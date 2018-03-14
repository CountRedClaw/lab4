package Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@Entity
public class GroupEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "groupId")
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gr_id")
    private Integer id;

    @XmlElement(name = "name")
    @Column(name = "gr_name", nullable = false)
    private String name;

    @XmlElementWrapper(name = "students")
    @XmlElement(name = "student")
    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<StudentEntity> students;

    public GroupEntity() {}

    public GroupEntity(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StudentEntity> getStudents() {
        return students;
    }

    public void setStudents(List<StudentEntity> students) {
        this.students = students;
    }
}
