package data.dao.impl;

import data.connection.JPAUtil;
import data.dao.DaoReport;
import data.model.ReportEntity;
import domain.error.AppError;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

public class DaoReportImpl implements DaoReport {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoReportImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    //get report by id
    @Override
    public Either<AppError, ReportEntity> get(ReportEntity report) {
        Either<AppError, ReportEntity> result;

        em = jpaUtil.getEntityManager();
        ReportEntity r;

        try {
            r = em.find(ReportEntity.class, report.getReportId());
            result = Either.right(r);
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    //save new report
    @Override
    public Either<AppError, Integer> save(ReportEntity report) {
        Either<AppError, Integer> result;

        em = jpaUtil.getEntityManager();

        try {
            em.persist(report);
            result = Either.right(1);
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    //update report
    @Override
    public Either<AppError, Integer> update(ReportEntity report) {
        Either<AppError, Integer> result;

        em = jpaUtil.getEntityManager();

        try {
            em.merge(report);
            result = Either.right(1);
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

}
