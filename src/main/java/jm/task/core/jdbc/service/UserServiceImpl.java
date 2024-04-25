package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserServiceImpl implements UserService {
    private HibernateUtil hibernateUtil = new HibernateUtil();
    private SessionFactory sessionFactory = hibernateUtil.getSessionFactory();
    private UserDao hibernate = new UserDaoHibernateImpl(sessionFactory);

    @Override
    public void createUsersTable() {
        hibernate.createUsersTable();
    }

    @Override
    public void dropUsersTable() {
        hibernate.dropUsersTable();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        hibernate.saveUser(name, lastName, age);
    }

    @Override
    public void removeUserById(long id) {
        hibernate.removeUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return hibernate.getAllUsers();
    }

    @Override
    public void cleanUsersTable() {
        hibernate.cleanUsersTable();
    }
}
