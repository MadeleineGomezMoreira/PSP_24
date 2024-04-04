package data.dao;

import data.model.AccessControlEntity;
import domain.error.AppError;
import io.vavr.control.Either;

public interface DaoAccessControl {
    //get a user's access control on a specific report (taking user's id and report's id)
    Either<AppError, AccessControlEntity> get(AccessControlEntity accessControl);

    //save new access control credentials
    Either<AppError, Integer> save(AccessControlEntity accessControl);

    //update access control credentials
    Either<AppError, Integer> update(AccessControlEntity accessControl);
}
