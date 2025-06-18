package be.restiau.naturalog_backend.bll.exceptions.user;

import be.restiau.naturalog_backend.bll.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class BirthDateAfterNowException extends BaseException {
    public BirthDateAfterNowException(HttpStatus status, String error) {
        super(status, error);
    }
}
