package de.uni_leipzig.crypto_news_docs.common;

import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

	/**
	 * Universal Exception Handler
	 * @return ResponseEntity
	 */
	@org.springframework.web.bind.annotation.ExceptionHandler({ DataAccessException.class, PSQLException.class, IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "The Application have a INTERNAL_SERVER_ERROR, for more details see logs" + "\n" + ex.getMessage();
        LOGGER.error(ex.getMessage());
        return handleExceptionInternal(ex, bodyOfResponse,
          new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
