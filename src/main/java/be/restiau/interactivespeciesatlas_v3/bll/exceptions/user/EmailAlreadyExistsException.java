package be.restiau.interactivespeciesatlas_v3.bll.exceptions.user;

import be.restiau.interactivespeciesatlas_v3.bll.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException(HttpStatus status, String error) {
        super(status, error);
    }
}
