package dev.a2.estore.dao;

import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import dev.a2.estore.dto.SearchUsersDto;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.a2.estore.model.User;

/**
 * This class provides implementation for UserDao interface.
 *
 * @author Andrei Sidorov
 */
@Repository
public class UserDaoImpl implements UserDao {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(UserDao.class);

    /**
     * Injects bean SessionFactory.
     */
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(final User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    public void update(final User user) {
        sessionFactory.getCurrentSession().update(user);
    }

    @Override
    public User findByEmail(final String email) {
        @SuppressWarnings("unchecked")
        TypedQuery<User> query =  sessionFactory.getCurrentSession()
                .createQuery("FROM User u WHERE u.email = :email");
        query.setParameter("email", email);
        try {
            logger.info("Fetched by email '" + email + "' " + query.getSingleResult());
            return query.getSingleResult();
        } catch (NoResultException e) {
            logger.info("No user with email '" + email + "' has been found");
            return null;
        }
    }

    @Override
    public User findById(final Long id) {
        User user =  sessionFactory.getCurrentSession().get(User.class, id);
        logger.info("Fetched by id '" + id + "' " + user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        @SuppressWarnings("unchecked")
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("FROM User");
        logger.info("Fetched all " + query.getResultList());
        return query.getResultList();
    }

    @Override
    public List<User> findUsersByCriteria(final SearchUsersDto searchUsersDto) {
        @SuppressWarnings("unchecked")
        TypedQuery<User> query = sessionFactory
                .getCurrentSession()
                .createQuery("FROM User u " +
                        "WHERE (:firstName is '' or u.firstName = :firstName) " +
                        "AND (:lastName is '' or u.lastName = :lastName) " +
                        "AND (:email is '' or u.email = :email)");
        query.setParameter("firstName", searchUsersDto.getFirstName());
        query.setParameter("lastName", searchUsersDto.getLastName());
        query.setParameter("email", searchUsersDto.getEmail());
        logger.info("Fetched by critera " + searchUsersDto + " result: " + query.getResultList());
        return query.getResultList();
    }

}
