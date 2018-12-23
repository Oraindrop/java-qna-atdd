package codesquad.web;

import codesquad.UnAuthorizedException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.domain.UserTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;

public class ApiQuestionControllerTest extends AcceptanceTest {
    private static final Logger logger = getLogger(ApiQuestionControllerTest.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void create() {
        createQuestionWithPost();
        softly.assertThat(questionRepository.findByTitle("newTitle").isPresent()).isTrue();
    }

    @Test
    public void createWithoutLogin() {
        Question newQuestion = new Question("newTitle", "newContents");
        ResponseEntity<Void> response = template().postForEntity("/api/questions", newQuestion, Void.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void show() {
        long id = 1L;
        ResponseEntity<Question> response = template().getForEntity("/api/questions/" + id, Question.class);
        logger.debug("get Question : {}", response.getBody());
        softly.assertThat(response.getBody().getId()).isEqualTo(id); 
    }

    @Test
    public void update() {
        ResponseEntity<Void> response = createQuestionWithPost();
        String location = response.getHeaders().getLocation().getPath();
        logger.debug("location : {} ", location);

        Question updateQuestion = new Question("newnewTitle", "newnewContents");

        ResponseEntity<Question> responseEntity =
                basicAuthTemplate().exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), Question.class);

        logger.debug("responseEntity body : {}", responseEntity.getBody());
        softly.assertThat(responseEntity.getBody().getTitle()).isEqualTo(updateQuestion.getTitle());
        softly.assertThat(responseEntity.getBody().getContents()).isEqualTo(updateQuestion.getContents());
    }

    @Test
    public void updateWithOtherUser() {
        ResponseEntity<Void> response = createQuestionWithPost();
        String location = response.getHeaders().getLocation().getPath();
        logger.debug("location : {} ", location);

        Question updateQuestion = new Question("newnewTitle", "newnewContents");

        ResponseEntity<Question> responseEntity =
                basicAuthTemplate(UserTest.SING).exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), Question.class);

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void updateWithoutLogin() {
        ResponseEntity<Void> response = createQuestionWithPost();
        String location = response.getHeaders().getLocation().getPath();
        logger.debug("location : {} ", location);

        Question updateQuestion = new Question("newnewTitle", "newnewContents");

        ResponseEntity<Question> responseEntity =
                template().exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), Question.class);

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Void> createQuestionWithPost() {
        Question createQuestion = new Question("newTitle", "newContents");
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity("/api/questions", createQuestion, Void.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return response;
    }

    private HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }
}