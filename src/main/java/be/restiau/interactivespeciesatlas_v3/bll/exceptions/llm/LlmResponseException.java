package be.restiau.interactivespeciesatlas_v3.bll.exceptions.llm;

import be.restiau.interactivespeciesatlas_v3.bll.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class LlmResponseException extends BaseException {
    public LlmResponseException(HttpStatus status, String error) {
        super(status, error);
    }
}
