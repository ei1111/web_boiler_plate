package com.web.board.repository;

import static com.web.board.domain.QBoard.board;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.web.board.domain.Board;
import com.web.board.form.BoardResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BoardResponse> findByTitleContainingOrContentContaining(String serachWord,
            Pageable pageable) {
        List<BoardResponse> boardResponse = jpaQueryFactory
                .selectFrom(board)
                .where(board.title.contains(serachWord).or(board.content.contains(serachWord)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.createdDate.asc())
                .fetch()
                .stream()
                .map(BoardResponse::from)
                .toList();

        JPAQuery<Board> countQuery = jpaQueryFactory.selectFrom(board)
                .where(
                        board.title.contains(serachWord)
                        , board.content.contains(serachWord)
                );

        return PageableExecutionUtils.getPage(boardResponse, pageable,
                countQuery::fetchCount);
    }
}
