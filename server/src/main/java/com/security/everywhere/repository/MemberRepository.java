package com.security.everywhere.repository;

import com.security.everywhere.model.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {

    int countByNickName(String nickName);

    @Nullable
    Member findByNickName(String nickName);
}
