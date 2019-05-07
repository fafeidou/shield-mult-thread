package com.shield.repository;

import com.shield.domain.EsMember;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by yajun.dong on 2019/1/23 0023.
 */
public interface MemberRepository extends MongoRepository<EsMember, String> {
}
