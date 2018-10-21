package com.ch.solrJD.mapper;

import com.ch.solrJD.entity.Goods;

import java.util.List;

public interface GoodsMapper {

    int deleteByPrimaryKey(Integer gid);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer gid);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

    int batchInsert(List<Goods> list);

}