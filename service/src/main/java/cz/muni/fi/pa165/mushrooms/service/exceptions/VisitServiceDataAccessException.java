package cz.muni.fi.pa165.mushrooms.service.exceptions;

import org.springframework.dao.DataAccessException;

/**
 * @author Buvko
 */
public class VisitServiceDataAccessException extends DataAccessException {
    public VisitServiceDataAccessException(String msg) {
        super(msg);
    }

    public VisitServiceDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}