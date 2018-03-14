package Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.NONE)
@Entity
public class StudentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "st_id")
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "st_id")
    private Integer id;

    @XmlElement(name = "st_name")
    @Column(name = "st_name")
    private String name;

    @XmlElement(name = "st_surname")
    @Column(name = "st_surname")
    private String surname;

    @XmlTransient
    @ManyToOne()
    @JoinColumn(name = "st_group", referencedColumnName = "gr_id")
    private GroupEntity group;

    public StudentEntity() {
    }

    public StudentEntity(String name, String surname, GroupEntity group) {
        this.name = name;
        this.surname = surname;
        this.group = group;
    }

    public Integer getId() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }
}
