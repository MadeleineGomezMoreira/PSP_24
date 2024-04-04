package data.dao.impl;

import data.connection.JPAUtil;
import data.dao.DaoUser;
import data.model.UserEntity;
import domain.error.AppError;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

public class DaoUserImpl implements DaoUser {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoUserImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    //get user by username
    @Override
    public Either<AppError, UserEntity> get(UserEntity user) {
        Either<AppError, UserEntity> result;

        em = jpaUtil.getEntityManager();
        UserEntity u;

        try {
            u = em.createNamedQuery("HQL_GET_USER_BY_USERNAME", UserEntity.class).setParameter("username", user.getUsername()).getSingleResult();
            result = Either.right(u);
        } catch (PersistenceException e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    //get user by id
    @Override
    public Either<AppError, UserEntity> get(int id) {
        Either<AppError, UserEntity> result;

        em = jpaUtil.getEntityManager();
        UserEntity u;

        try {
            u = em.find(UserEntity.class, id);
            result = Either.right(u);
        } catch (PersistenceException e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    //save new user
    @Override
    public Either<AppError, Integer> save(UserEntity user) {
        Either<AppError, Integer> result;

        em = jpaUtil.getEntityManager();

        try {
            em.persist(user);
            result = Either.right(1);
        } catch (PersistenceException e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

}
