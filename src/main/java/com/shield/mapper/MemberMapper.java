package com.shield.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shield.domain.EsMember;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by yajun.dong on 2019/1/23 0023.
 */
@Mapper
public interface MemberMapper extends BaseMapper<EsMember> {

}
