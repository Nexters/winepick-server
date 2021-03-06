package com.nexters.winepick.wine.domain;

import static com.nexters.winepick.wine.domain.QWine.wine;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
public class WineRepositoryImpl implements WineRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Wine> findByCondition(Pageable pageable, String wineName, String category,
      String[] food, String store, String start, String end) {

    List<Wine> wines = queryFactory
        .selectFrom(wine)
        .where(eqName(wineName)
            , eqCategory(category)
            , likeFood(food)
            , likeStore(store)
            , degree(start, end))
        .fetch();

    return wines;

  }

  // 한국이름
  private BooleanExpression eqName(String wineName) {
    if (ObjectUtils.isEmpty(wineName)) {
      return null;
    }
    return wine.nmKor.eq(wineName).or(wine.nmEng.eq(wineName));
  }

  // category
  private BooleanExpression eqCategory(String category) {
    if (ObjectUtils.isEmpty(category)) {
      return null;
    }
    return wine.category.eq(category);
  }

  // 음식
  private BooleanBuilder likeFood(String[] foods) {
    if (ObjectUtils.isEmpty(foods)) {
      return null;
    }
    BooleanBuilder b = new BooleanBuilder();
    for(String food : foods) {
      b.or(wine.suitFood.contains(food));
    }
    return b;
  }

  // 가게
  private BooleanExpression likeStore(String store) {
    if (ObjectUtils.isEmpty(store)) {
      return null;
    }
    return wine.store.contains(store);
  }

  // 도수
  private BooleanExpression degree(String start, String end) {
    if (ObjectUtils.isEmpty(start) || ObjectUtils.isEmpty(end)) {
      return null;
    }
    return wine.degree.between(Integer.parseInt(start), Integer.parseInt(end));
  }
}
