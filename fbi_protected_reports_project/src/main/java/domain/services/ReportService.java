package domain.services;

import data.model.ReportEntity;
import domain.error.AppError;
import io.vavr.control.Either;

public interface ReportService {
    Either<AppError, ReportEntity> accessReport(String userPass, String username, int reportId, int userId);

    Either<AppError, Integer> saveReport(ReportEntity report);
}
