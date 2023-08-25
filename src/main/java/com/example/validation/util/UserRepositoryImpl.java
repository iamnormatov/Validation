package com.example.validation.util;

import com.example.validation.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl {
    private final EntityManager entityManager;

    public Page<User> advanced(Map<String, String> params){
        int page = 0, size = 10;
        if (params.containsKey("page")){
            page = Integer.parseInt(params.get("page"));
        }
        if (params.containsKey("size")){
            size = Integer.parseInt(params.get("size"));
        }

        String firstQuery = "select u from User as u where true ";
        String secondQuery = "select count(userId) from User ";

        StringBuilder stringBuilder = buildParams(params);

        Query queryOne = this.entityManager.createQuery(firstQuery + stringBuilder);
        Query queryTwo = this.entityManager.createQuery(secondQuery + stringBuilder);

        queryOne.setFirstResult(page * size);
        queryTwo.setMaxResults(size);

        setMoreParams(params, queryOne);
        setMoreParams(params, queryTwo);

        long total = (long) queryTwo.getSingleResult();


        return new PageImpl<User>(queryOne.getResultList(), PageRequest.of(page, size), total);
    }

    private void setMoreParams(Map<String, String> params, Query query){
        if (params.containsKey("id")){
            query.setParameter("id", params.get("id"));
        }
        if (params.containsKey("f")){
            query.setParameter("f", params.get("f"));
        }
        if (params.containsKey("l")){
            query.setParameter("l", params.get("l"));
        }
        if (params.containsKey("a")){
            query.setParameter("a", params.get("a"));
        }
        if (params.containsKey("e")){
            query.setParameter("e", params.get("e"));
        }

    }

    private StringBuilder buildParams(Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        if (params.containsKey("id")){
            stringBuilder.append(" and u.userId = :id");
        }
        if (params.containsKey("f")){
            stringBuilder.append(" and u.firstName = :f");
        }
        if (params.containsKey("l")){
            stringBuilder.append(" and u.lastName = :l");
        }
        if (params.containsKey("a")){
            stringBuilder.append(" and u.age = :a");
        }
        if (params.containsKey("e")){
            stringBuilder.append(" and u.email = :e");
        }
        return stringBuilder;
    }
}
