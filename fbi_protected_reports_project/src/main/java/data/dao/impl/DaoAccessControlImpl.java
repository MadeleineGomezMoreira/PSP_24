package data.dao.impl;

import data.connection.JPAUtil;
import data.dao.DaoAccessControl;
import data.model.AccessControlEntity;
import domain.error.AppError;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

public class DaoAccessControlImpl implements DaoAccessControl {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoAccessControlImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    //get a user's access control on a specific report (taking user's id and report's id)
    @Override
    public Either<AppError, AccessControlEntity> get(AccessControlEntity accessControl) {
        Either<AppError, AccessControlEntity> result;

        em = jpaUtil.getEntityManager();
        AccessControlEntity ac;

        try {
            ac = em.createNamedQuery("HQL_GET_ACCESS_CONTROL_BY_USER_AND_REPORT_ID", AccessControlEntity.class).setParameter("userId", accessControl.getUserId()).setParameter("reportId", accessControl.getReportId()).getSingleResult();
            result = Either.right(ac);
        } catch (PersistenceException e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    //save new access control credentials
    @Override
    public Either<AppError, Integer> save(AccessControlEntity accessControl) {
        Either<AppError, Integer> result;

        em = jpaUtil.getEntityManager();

        try {
            em.persist(accessControl);
            result = Either.right(1);
        } catch (PersistenceException e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    //update access control credentials
    @Override
    public Either<AppError, Integer> update(AccessControlEntity accessControl) {
        Either<AppError, Integer> result;

        em = jpaUtil.getEntityManager();

        try {
            em.merge(accessControl);
            result = Either.right(1);
        } catch (PersistenceException e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }
}
