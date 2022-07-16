package com.woowacourse.gongseek.comment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.gongseek.article.domain.Article;
import com.woowacourse.gongseek.article.domain.Category;
import com.woowacourse.gongseek.article.domain.repository.ArticleRepository;
import com.woowacourse.gongseek.auth.presentation.dto.LoginMember;
import com.woowacourse.gongseek.comment.domain.Comment;
import com.woowacourse.gongseek.comment.domain.repository.CommentRepository;
import com.woowacourse.gongseek.comment.presentation.dto.CommentRequest;
import com.woowacourse.gongseek.comment.presentation.dto.CommentResponse;
import com.woowacourse.gongseek.member.domain.Member;
import com.woowacourse.gongseek.member.domain.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CommentServiceTest {

    private final Member member = new Member("slow", "hanull", "avatarUrl");
    private final Article article = new Article("title", "content", Category.QUESTION, member);
    private final Comment comment = new Comment("content", member, article);

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(member);
        articleRepository.save(article);
        commentRepository.save(comment);
    }

    @Test
    void 댓글을_생성한다() {
        CommentRequest request = new CommentRequest("content2");

        commentService.save(new LoginMember(member.getId()), article.getId(), request);
        List<CommentResponse> savedComments = commentService.findByArticleId(article.getId());

        assertAll(
                () -> assertThat(savedComments).hasSize(2),
                () -> assertThat(savedComments.get(1).getAuthorName()).isEqualTo(member.getName()),
                () -> assertThat(savedComments.get(1).getContent()).isEqualTo(request.getContent())
        );
    }

    @Test
    void 회원이_존재하지_않는_경우_댓글을_생성할_수_없다() {
        CommentRequest request = new CommentRequest("content");

        assertThatThrownBy(() -> commentService.save(new LoginMember(-1L), article.getId(), request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }

    @Test
    void 게시글이_존재하지_않는_경우_댓글을_생성할_수_없다() {
        CommentRequest request = new CommentRequest("content");

        assertThatThrownBy(() -> commentService.save(new LoginMember(member.getId()), 2L, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("게시글이 존재하지 않습니다.");
    }

    @Test
    void 댓글을_수정한다() {
        List<CommentResponse> comments = commentService.findByArticleId(article.getId());
        String updateContent = "update";
        CommentRequest updateRequest = new CommentRequest(updateContent);

        commentService.update(new LoginMember(member.getId()), updateRequest, comments.get(0).getId());
        List<CommentResponse> savedComments = commentService.findByArticleId(article.getId());

        assertThat(savedComments.get(0).getContent()).isEqualTo(updateContent);
    }

    @Test
    void 댓글이_존재하지_않는_경우_수정할_수_없다() {
        assertThatThrownBy(
                () -> commentService.update(new LoginMember(member.getId()), new CommentRequest("update content"), -1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("댓글이 존재하지 않습니다.");
    }

    @Test
    void 회원이_아닌_경우_댓글을_수정할_수_없다() {
        assertThatThrownBy(() -> commentService.update(new LoginMember(-1L), new CommentRequest("update content"),
                comment.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }

    @Test
    void 댓글을_작성한_회원이_아닌_경우_수정할_수_없다() {
        Member newMember = memberRepository.save(new Member("judy", "judyhithub", "avatarUrl"));
        assertThatThrownBy(
                () -> commentService.update(new LoginMember(newMember.getId()), new CommentRequest("update content"),
                        comment.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글을 작성한 회원만 수정할 수 있습니다.");
    }

    @Test
    void 댓글을_삭제한다() {
        commentService.delete(new LoginMember(member.getId()), comment.getId());
        List<CommentResponse> comments = commentService.findByArticleId(article.getId());

        boolean isFind = comments.stream()
                .anyMatch(comment -> comment.getId().equals(this.comment.getId()));

        assertThat(isFind).isFalse();
    }

    @Test
    void 댓글이_존재하지_않는_경우_삭제할_수_없다() {
        assertThatThrownBy(
                () -> commentService.delete(new LoginMember(member.getId()), -1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("댓글이 존재하지 않습니다.");
    }

    @Test
    void 회원이_아닌_경우_댓글을_삭제할_수_없다() {
        assertThatThrownBy(() -> commentService.delete(new LoginMember(-1L), comment.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }

    @Test
    void 댓글을_작성한_회원이_아닌_경우_삭제할_수_없다() {
        Member newMember = memberRepository.save(new Member("judy", "judyhithub", "avatarUrl"));
        assertThatThrownBy(
                () -> commentService.delete(new LoginMember(newMember.getId()), comment.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글을 작성한 회원만 삭제할 수 있습니다.");
    }
}
