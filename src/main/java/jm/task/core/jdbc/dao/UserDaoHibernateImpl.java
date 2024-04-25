package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private HibernateUtil hibernateUtil = new HibernateUtil();
    private SessionFactory sessionFactory;

    public UserDaoHibernateImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void createUsersTable() {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String sql = "CREATE TABLE Users (\n" +
                    "`id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "`name` VARCHAR(45) NOT NULL,\n" +
                    "`lastName` VARCHAR(45) NOT NULL,\n" +
                    "`age` TINYINT NOT NULL,\n" +
                    "PRIMARY KEY (`id`));";
            Query query = session.createSQLQuery(sql).addEntity(User.class);
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                String SQl = "DROP TABLES IF EXISTS Users;";
                Query query = session.createSQLQuery(SQl);
                query.executeUpdate();
                transaction.commit();
            } catch (HibernateException e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new HibernateException(e);
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                User user = new User(name, lastName, age);
                session.save(user);
                transaction.commit();
                System.out.println("User с именем —" + name + " добавлен в базу данных");
            } catch (HibernateException e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new HibernateException(e);
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = hibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {

            throw new HibernateException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Session session = hibernateUtil.getSessionFactory().getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            list = session.createQuery("FROM User").getResultList();
            transaction.commit();
            list.forEach(System.out::println);
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                session.createQuery("Delete User").executeUpdate();
                transaction.commit();
            } catch (HibernateException e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new HibernateException(e);
            }
        }
    }
}
