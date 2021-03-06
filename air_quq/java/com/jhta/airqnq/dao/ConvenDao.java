package com.jhta.airqnq.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jhta.airqnq.vo.ConvenDetailVo;

@Repository
public class ConvenDao {
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	private final String NAMESPACE = "com.jhta.airqnq.mapper.HostInfoMapper";
	
	public int insertConven(int hinum) {
		return sqlSessionTemplate.insert(NAMESPACE + ".convenInsert", hinum);
	}
	
	public int insertConvenDetail(ConvenDetailVo vo) {
		return sqlSessionTemplate.insert(NAMESPACE + ".convenDetailInsert", vo);
	}
	
	public int selectConvenNumber(int hinum) {
		return sqlSessionTemplate.selectOne(NAMESPACE + ".convenSelect", hinum);
	}
	
	/* 승진 */
	public ConvenDetailVo selectconvendetail(int hinum) {
		return sqlSessionTemplate.selectOne(NAMESPACE+".convendetailselect", hinum);
	}
}
