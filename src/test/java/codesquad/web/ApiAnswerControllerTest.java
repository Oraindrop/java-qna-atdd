package codesquad.web;

import codesquad.domain.AnswerRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import java.sql.Clob;

import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;

public class ApiAnswerControllerTest extends AcceptanceTest {
    private static final Logger logger = getLogger(ApiAnswerControllerTest.class);

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    public void create() {
        String contents = "createAnswerTest";
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity(getCreatePath(1L), contents, Void.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createWithoutLogin() {
        String contents = "createAnswerTest";
        ResponseEntity<Void> response = template().postForEntity(getCreatePath(1L), contents, Void.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private String getCreatePath(long questionId){
        return "/api/questions/" + questionId + "/answers";
    }
}