package webapp;

import Entity.GroupEntity;
import Entity.StudentEntity;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class DatabaseOperationsBean {

    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;

    public void persistGroup(GroupEntity groupEntity) {
        entityManager.persist(groupEntity);
    }

    public void createGroup(String name){
        if(StringUtils.isEmpty(name)){
            return;
        }

        if (!isGroupExists(name)) {
            GroupEntity groupEntity = new GroupEntity(name);
            entityManager.persist(groupEntity);
        }
    }

    public void updateGroup(String groupName, Integer groupId) {
        if(StringUtils.isEmpty(groupName)){
            return;
        }

        GroupEntity groupEntity = getGroupById(groupId);
        groupEntity.setName(groupName);
        entityManager.merge(groupEntity);
    }

    public void createStudent(String studentName, String studentSurname, Integer groupId) {
        if(StringUtils.isEmpty(studentName) && StringUtils.isEmpty(studentSurname)){
            return;
        }

        if (!isStudentExists(studentName, studentSurname)) {
            GroupEntity groupEntity = entityManager.find(GroupEntity.class, groupId);
            StudentEntity studentEntity = new StudentEntity(studentName, studentSurname, groupEntity);
            entityManager.persist(studentEntity);
        }
    }

    public void updateStudent(String studentName, String studentSurname, Integer groupId, Integer studentId) {
        if(StringUtils.isEmpty(studentName) && StringUtils.isEmpty(studentSurname)){
            return;
        }
        GroupEntity groupEntity = entityManager.find(GroupEntity.class, groupId);

        StudentEntity studentEntity = getStudentById(studentId);
        studentEntity.setName(studentName);
        studentEntity.setSurname(studentSurname);
        studentEntity.setGroup(groupEntity);
        entityManager.merge(studentEntity);
    }

    public void updateStudent(StudentEntity studentEntity) {
        entityManager.merge(studentEntity);
    }

    public void deleteStudent(Integer id) {
        StudentEntity studentEntity;
        if (id != null) {
            studentEntity = entityManager.find(StudentEntity.class, id);

            GroupEntity groupEntity = studentEntity.getGroup();
            groupEntity.getStudents().remove(studentEntity);

            System.out.println(studentEntity);
            if (studentEntity == null) {
                return;
            }

            entityManager.remove(studentEntity);
            entityManager.flush();
            entityManager.clear();
        }
    }

    public void deleteGroup(Integer id) {
        GroupEntity groupEntity;
        if (id != null) {
            groupEntity = entityManager.find(GroupEntity.class, id);

            System.out.println(groupEntity);
            if (groupEntity == null) {
                return;
            }

            entityManager.remove(groupEntity);
            entityManager.flush();
            entityManager.clear();
        }
    }

    public List<GroupEntity> getAllStudents(){
        Query query = entityManager.createQuery("from GroupEntity entity order by id");
        return query.getResultList();
    }

    public boolean isGroupExists(String name) {
        Query query = entityManager.createQuery("select count(*) from GroupEntity where name like :name");
        query.setParameter("name", name);
        return (Long) query.getSingleResult() > 0;
    }

    public boolean isGroupExists(Integer grid) {
        Query query = entityManager.createQuery("select count(*) from GroupEntity where id = :grid");
        query.setParameter("grid", grid);
        return (Long) query.getSingleResult() > 0;
    }

    public boolean isStudentExists(String name, String surname) {
        Query query = entityManager.createQuery("select count(*) from StudentEntity where name like :name and surname like :surname");
        query.setParameter("name", name);
        query.setParameter("surname", surname);
        return (Long) query.getSingleResult() > 0;
    }

    public GroupEntity getGroupById(int id) {
        Query query = entityManager.createQuery("from GroupEntity where id = :id");
        query.setParameter("id", id);
        return (GroupEntity) query.getSingleResult();
    }

    public StudentEntity getStudentById(Integer id) {
        StudentEntity studentEntity = null;
        if (id != null) {
            studentEntity = entityManager.find(StudentEntity.class, id);
        }
        return studentEntity;
    }

    public List<GroupEntity> getStudentsBy(String groupName, String studentName, String studentSurname) {
        System.out.println(groupName);

        /*from Book b inner join Author a
        where b.id  = a.bookId
        and a.lastname = :ln
        and a.firstname = :fn*/

        /*Query query = entityManager.createQuery("from GroupEntity g inner join g.students s " +
                                                    "where g.id = s.group.id " +
                                                    "and s.name like :studentName");
        //query.setParameter("groupName", groupName);
        query.setParameter("studentName", studentName);*/

        //query.setParameter("groupName", groupName);
        //query.setParameter("studentName", studentName);

        /*from Book b inner join Author a
        where b.id  = a.bookId
        and a.lastname = :ln
        and a.firstname = :fn*/

        /*Query query = entityManager.createQuery("from GroupEntity g inner join g.students s " +
                                                    //"where g.id = s.group.id ");// +
                                                    "where s.name like :studentName");
        //query.setParameter("groupName", groupName);
        query.setParameter("studentName", studentName);*/

        /*Query query = entityManager.createQuery("select g from GroupEntity g left outer join g.students s " +
                "where s.name like :studentName");
        query.setParameter("studentName", studentName);*/

        /*Configuration cfg = new Configuration();
        SessionFactory sessionFactory = cfg.configure().buildSessionFactory();
        Session session = sessionFactory.openSession();*/
/*
        List<GroupEntity> list = new ArrayList<GroupEntity>();

        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(GroupEntity.class);
            criteria.add(Restrictions.eq("name", groupName));
            criteria.createAlias("students", "child");
            criteria.add(Restrictions.eq("child.name", studentName).ignoreCase());

            list = criteria.list();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }*/

        /*Query query = entityManager.createQuery("select g from GroupEntity g join fetch g.students s " +
                                                    "where s.name like :studentName and g.name like :groupName");
        query.setParameter("groupName", groupName);
        query.setParameter("studentName", studentName);*/

        if (groupName.equals("") && studentName.equals("") && studentSurname.equals("")) {
            return getAllStudents();
        }

        groupName = (groupName.equals("")) ? "%" : groupName;
        studentName = (studentName.equals("")) ? "%" : studentName;
        studentSurname = (studentSurname.equals("")) ? "%" :studentSurname;

         /*else if (groupName.equals("")) {
            Query query = entityManager.createQuery("select g from GroupEntity g join fetch g.students s " +
                    "where s.name like :studentName and g.name like :groupName");
            query.setParameter("studentName", studentName);
            return query.getResultList();
        } else if (studentName.equals("")) {
            Query query = entityManager.createQuery("select g from GroupEntity g join fetch g.students s " +
                    "where s.name like :studentName and g.name like :groupName");
            query.setParameter("groupName", groupName);
            return query.getResultList();

            /*org.hibernate.Query query = session.createSQLQuery(
                    "select * from groupentity p join (select * from studententity where studententity.st_name like :studentName) c on p.gr_id = c.st_group where p.gr_name like :groupName")
                    .addEntity(GroupEntity.class)
                    .setParameter("groupName", groupName)
                    .setParameter("studentName", studentName);
            List result = query.list();*/

        Query query = entityManager.createQuery("select distinct g from GroupEntity g join fetch g.students s " +
                                                    "where s.name like :studentName and s.surname like :studentSurname and g.name like :groupName");
        query.setParameter("groupName", groupName);
        query.setParameter("studentName", studentName);
        query.setParameter("studentSurname", studentSurname);
        return query.getResultList();
    }
}
