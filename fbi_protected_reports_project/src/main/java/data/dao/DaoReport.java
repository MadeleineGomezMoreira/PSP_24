package data.dao;

import data.model.ReportEntity;
import domain.error.AppError;
import io.vavr.control.Either;

public interface DaoReport {
    //get report by id
    Either<AppError, ReportEntity> get(ReportEntity report);

    //save new report
    Either<AppError, Integer> save(ReportEntity report);

    //update report
    Either<AppError, Integer> update(ReportEntity report);
}
