package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    private static final Logger logger = getLogger(QuestionController.class);

    @Autowired
    private QnaService qnaService;

    @GetMapping("/form")
    public String createForm(@LoginUser User loginUser){
        return "qna/form";
    }

    @PostMapping
    public String create(@LoginUser User loginUser, Question question){
        qnaService.create(loginUser, question);
        logger.debug("question : {}", question);
        return "redirect:/";
    }
}
