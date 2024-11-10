package com.large.board.domain.repository;

import com.large.board.common.code.SortStatus;
import com.large.board.domain.entity.PostEntity;
import com.large.board.dto.request.PostSearchRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.large.board.domain.entity.QPostEntity.postEntity;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostEntity> searchPosts(PostSearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());

        List<PostEntity> postEntities = queryFactory
                .selectFrom(postEntity)
                .where(
                        containsByTitle(searchRequest.getTitleKeyword()),
                        eqCategoryId(searchRequest.getCategoryId())
                )
                .orderBy(getOrderSpecifiers(searchRequest.getSortStatus()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(postEntity.count())
                .from(postEntity)
                .where(
                        containsByTitle(searchRequest.getTitleKeyword()),
                        eqCategoryId(searchRequest.getCategoryId())
                )
                .fetchOne();

        return new PageImpl<>(postEntities, pageable, total);
    }

    private BooleanExpression containsByTitle(String keyword) {
        return Strings.isBlank(keyword) ? null : postEntity.title.contains(keyword);
    }

    private BooleanExpression eqCategoryId(Long id) {
        return (id != null && id != 0L)? postEntity.categoryEntity.id.eq(id) : null;
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(SortStatus sortStatus) {
        switch (sortStatus) {
            case CATEGORIES:
                return new OrderSpecifier<?>[]{postEntity.categoryEntity.name.asc()};
            case NEWEST:
                return new OrderSpecifier<?>[]{postEntity.createdDate.desc()};
            case OLDEST:
                return new OrderSpecifier<?>[]{postEntity.createdDate.asc()};
            default:
                return new OrderSpecifier<?>[]{postEntity.createdDate.desc()}; // 기본: 최신순
        }
    }
}
