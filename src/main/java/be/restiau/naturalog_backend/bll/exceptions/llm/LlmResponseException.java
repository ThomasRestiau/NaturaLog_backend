package be.restiau.naturalog_backend.bll.exceptions.llm;

import be.restiau.naturalog_backend.bll.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class LlmResponseException extends BaseException {
    public LlmResponseException(HttpStatus status, String error) {
        super(status, error);
    }
}
