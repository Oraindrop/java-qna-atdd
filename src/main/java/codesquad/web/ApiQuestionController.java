package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import javax.validation.Valid;

import static org.slf4j.LoggerFactory.getLogger;

@RequestMapping("/api/questions")
@RestController
public class ApiQuestionController {
    private static final Logger logger = getLogger(ApiQuestionController.class);

    @Autowired
    private QnaService qnaService;

    @PostMapping
    public ResponseEntity<Void> create(@LoginUser User loginUser, @Valid @RequestBody Question question){
        qnaService.create(loginUser, question);
        logger.debug("question : {}", question);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
