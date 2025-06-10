package be.restiau.interactivesatlasspecies_v3.bll.exceptions.user;

import be.restiau.interactivesatlasspecies_v3.bll.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends BaseException {
    public UsernameAlreadyExistsException(HttpStatus status, String error) {
        super(status, error);
    }

}
