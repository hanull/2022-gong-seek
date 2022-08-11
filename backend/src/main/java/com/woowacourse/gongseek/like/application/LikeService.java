package com.woowacourse.gongseek.like.application;

import com.woowacourse.gongseek.article.domain.Article;
import com.woowacourse.gongseek.article.domain.repository.ArticleRepository;
import com.woowacourse.gongseek.article.exception.ArticleNotFoundException;
import com.woowacourse.gongseek.auth.exception.NoAuthorizationException;
import com.woowacourse.gongseek.auth.presentation.dto.AppMember;
import com.woowacourse.gongseek.like.domain.Like;
import com.woowacourse.gongseek.like.domain.repository.LikeRepository;
import com.woowacourse.gongseek.member.domain.Member;
import com.woowacourse.gongseek.member.domain.repository.MemberRepository;
import com.woowacourse.gongseek.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    public void likeArticle(AppMember appMember, Long articleId) {
        validateGuest(appMember);
        Member member = getMember(appMember);
        Article article = getArticle(articleId);

        saveByExistsLike(member, article);
    }

    private void saveByExistsLike(Member member, Article article) {
        if (!likeRepository.existsByArticleIdAndMemberId(article.getId(), member.getId())) {
            likeRepository.save(new Like(article, member));
        }
    }

    private void validateGuest(AppMember appMember) {
        if (appMember.isGuest()) {
            throw new NoAuthorizationException();
        }
    }

    private Member getMember(AppMember appMember) {
        return memberRepository.findById(appMember.getPayload())
                .orElseThrow(MemberNotFoundException::new);
    }

    private Article getArticle(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
    }

    public void unlikeArticle(AppMember appMember, Long articleId) {
        validateGuest(appMember);
        Member member = getMember(appMember);
        Article article = getArticle(articleId);

        likeRepository.findByArticleIdAndMemberId(article.getId(), member.getId())
                .ifPresent(likeRepository::delete);
    }
}