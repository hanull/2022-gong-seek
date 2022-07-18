package com.woowacourse.gongseek.article.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.gongseek.article.presentation.dto.ArticleIdResponse;
import com.woowacourse.gongseek.article.presentation.dto.ArticleRequest;
import com.woowacourse.gongseek.article.presentation.dto.ArticleResponse;
import com.woowacourse.gongseek.auth.presentation.dto.GuestMember;
import com.woowacourse.gongseek.auth.presentation.dto.LoginMember;
import com.woowacourse.gongseek.auth.presentation.dto.AppMember;
import com.woowacourse.gongseek.member.domain.Member;
import com.woowacourse.gongseek.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원은_게시물을_저장한다() {
        String title = "질문합니다.";
        String content = "내용입나다....";
        String category = "question";
        Member member = new Member("slo", "hanull", "avatar.com");
        memberRepository.save(member);
        ArticleRequest articleRequest = new ArticleRequest(title, content, category);
        AppMember appMember = new LoginMember(member.getId());

        ArticleIdResponse articleIdResponse = articleService.save(appMember, articleRequest);

        assertThat(articleIdResponse.getId()).isNotNull();
    }

    @Test
    void 비회원은_게시물을_저장할_수_없다() {
        String title = "질문합니다!.";
        String content = "난 비회원인데....";
        String category = "question";
        ArticleRequest articleRequest = new ArticleRequest(title, content, category);
        AppMember appMember = new GuestMember();

        assertThatThrownBy(() -> articleService.save(appMember, articleRequest))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("권한이 없는 사용자입니다.");
    }

    @Test
    void 존재하지_않는_회원이_질문을_저장하면_예외가_발생한다() {
        String title = "질문합니다.";
        String content = "내용입나다....";
        String category = "question";
        ArticleRequest articleRequest = new ArticleRequest(title, content, category);

        assertThatThrownBy(() -> articleService.save(new GuestMember(), articleRequest))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("권한이 없는 사용자입니다.");
    }

    @Test
    void 회원이_게시물을_조회한다() {
        String title = "질문합니다.";
        String content = "내용입나다....";
        String category = "question";
        ArticleRequest articleRequest = new ArticleRequest(title, content, category);

        Member member = new Member("slo", "hanull", "avatar.com");
        memberRepository.save(member);

        ArticleIdResponse savedArticle = articleService.save(new LoginMember(member.getId()), articleRequest);
        ArticleResponse articleResponse = articleService.findOne(new LoginMember(member.getId()), savedArticle.getId(),
                category);

        assertAll(
                () -> assertThat(articleResponse.getTitle()).isEqualTo(articleRequest.getTitle()),
                () -> assertThat(articleResponse.getContent()).isEqualTo(articleRequest.getContent()),
                () -> assertThat(articleResponse.getCreatedAt()).isNotNull()
        );
    }

    @Test
    void 비회원이_게시물을_조회한다() {
        String title = "질문합니다.";
        String content = "내용입나다....";
        String category = "question";
        ArticleRequest articleRequest = new ArticleRequest(title, content, category);

        Member member = new Member("slo", "hanull", "avatar.com");
        memberRepository.save(member);

        ArticleIdResponse savedArticle = articleService.save(new LoginMember(member.getId()), articleRequest);
        ArticleResponse articleResponse = articleService.findOne(new GuestMember(), savedArticle.getId(), category);

        assertAll(
                () -> assertThat(articleResponse.getTitle()).isEqualTo(articleRequest.getTitle()),
                () -> assertThat(articleResponse.getContent()).isEqualTo(articleRequest.getContent()),
                () -> assertThat(articleResponse.getCreatedAt()).isNotNull()
        );
    }

    @Test
    void 게시물을_조회하면_조회수가_올라간다() {
        String title = "질문합니다.";
        String content = "내용입나다....";
        String category = "question";
        ArticleRequest articleRequest = new ArticleRequest(title, content, category);

        Member member = new Member("slo", "hanull", "avatar.com");
        memberRepository.save(member);

        ArticleIdResponse savedArticle = articleService.save(new LoginMember(member.getId()), articleRequest);
        articleService.findOne(new GuestMember(), savedArticle.getId(), category);
        ArticleResponse articleResponse = articleService.findOne(new GuestMember(), savedArticle.getId(), category);

        assertAll(
                () -> assertThat(articleResponse.getTitle()).isEqualTo(articleRequest.getTitle()),
                () -> assertThat(articleResponse.getContent()).isEqualTo(articleRequest.getContent()),
                () -> assertThat(articleResponse.getViews()).isEqualTo(2),
                () -> assertThat(articleResponse.getCreatedAt()).isNotNull()
        );
    }

    @Test
    void 카테고리가_올바르지_않으면_예외가_발생한다() {
        String title = "질문합니다.";
        String content = "내용입나다....";
        String category = "question";
        ArticleRequest articleRequest = new ArticleRequest(title, content, category);

        Member member = new Member("rennon", "brorae", "avatar.com");
        memberRepository.save(member);

        ArticleIdResponse savedArticle = articleService.save(new LoginMember(member.getId()), articleRequest);
        assertThatThrownBy(() -> articleService.findOne(new GuestMember(), savedArticle.getId(), "question1"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 카테고리입니다.");
    }
}
