package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;

public class ApiQuestionControllerTest extends AcceptanceTest {
    private static final Logger logger = getLogger(ApiQuestionControllerTest.class);

    @Autowired
    QuestionRepository questionRepository;

    @Test
    public void create() {
        Question newQuestion = new Question("newTitle", "newContents");
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, Void.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        softly.assertThat(questionRepository.findByTitle("newTitle").isPresent()).isTrue();
    }

    @Test
    public void createWithoutLogin() {
        Question newQuestion = new Question("newTitle", "newContents");
        ResponseEntity<Void> response = template().postForEntity("/api/questions", newQuestion, Void.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}