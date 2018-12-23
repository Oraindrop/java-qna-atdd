package codesquad.service;

import codesquad.CannotDeleteException;
import codesquad.UnAuthorizedException;
import codesquad.domain.QuestionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import support.test.AcceptanceTest;

import java.util.Optional;

import static codesquad.domain.QuestionTest.QUESTION_FOR_DELETE;
import static codesquad.domain.QuestionTest.QUESTION;
import static codesquad.domain.QuestionTest.QUESTION_FOR_UPDATE;
import static codesquad.domain.QuestionTest.QUESTION_FOR_UPDATE_OTHER_USER;
import static codesquad.domain.UserTest.CHOI;
import static codesquad.domain.UserTest.SANJIGI;
import static codesquad.domain.UserTest.SING;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest extends AcceptanceTest {
    @Mock
    QuestionRepository questionRepository;

    @InjectMocks
    QnaService qnaService;

    @Before
    public void setUp() throws Exception {
        when(questionRepository.findById((long)1)).thenReturn(Optional.of(QUESTION));
        when(questionRepository.findById((long)4)).thenReturn(Optional.of(QUESTION_FOR_DELETE));
    }

    @Test
    public void update() {
        qnaService.update(CHOI, 1, QUESTION_FOR_UPDATE);
        softly.assertThat(QUESTION.getTitle()).isEqualTo(QUESTION_FOR_UPDATE.getTitle());
        softly.assertThat(QUESTION.getContents()).isEqualTo(QUESTION_FOR_UPDATE.getContents());
    }

    @Test(expected = UnAuthorizedException.class)
    public void updateWithInvalidUser() {
        qnaService.update(SANJIGI, 1, QUESTION_FOR_UPDATE_OTHER_USER);
    }

    @Test
    public void deleteQuestion() throws CannotDeleteException {
        qnaService.deleteQuestion(CHOI, (long)4);
        softly.assertThat(QUESTION_FOR_DELETE.isDeleted()).isTrue();
    }

    @Test(expected = CannotDeleteException.class)
    public void deleteQuestionWithInvalidUser() throws CannotDeleteException {
        qnaService.deleteQuestion(SING, (long)1);
    }
}