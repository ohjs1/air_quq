package com.jhta.airqnq.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jhta.airqnq.vo.JoinVo;

@Repository
public class MemberDao {
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	private final String NAMESPACE = "com.jhta.airqnq.mapper.MemberMapper";
	
	public List<JoinVo> Memberlist(){
		return sqlSessionTemplate.selectList(NAMESPACE+".listMember");
	}
	
}