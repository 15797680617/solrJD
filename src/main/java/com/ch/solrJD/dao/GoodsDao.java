package com.ch.solrJD.dao;

import com.ch.solrJD.entity.Goods;
import com.ch.solrJD.mapper.GoodsMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Auther: pch
 * @Date: 2018/10/19 17:49
 * @Description:
 */
@Mapper
public interface GoodsDao extends GoodsMapper {

	@Select("select * from goods")
	List<Goods> getGoods();



}
