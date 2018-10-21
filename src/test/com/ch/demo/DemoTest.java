package com.ch.demo;

import cn.hutool.core.util.HashUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.ch.solrJD.SolrJdApplication;
import com.ch.solrJD.dao.GoodsDao;
import com.ch.solrJD.entity.Goods;
import com.ch.solrJD.util.JDUtils;
import com.ch.solrJD.util.SolrUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: pch
 * @Date: 2018/10/19 17:13
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SolrJdApplication.class)
public class DemoTest {
	@Autowired
	private GoodsDao goodsDao;


	/**
	 * 将数据库信息 存储到solr索引库
	 */
	@Test
	public void addIndex() throws IOException, SolrServerException {
		HttpSolrClient client = SolrUtils.getSolrClient();
		List<Goods> goodsList = goodsDao.getGoods();
		SolrInputDocument document;
		for (Goods goods : goodsList) {
			document = new SolrInputDocument();
			document.addField("gid", goods.getGid());
			document.addField("gcode", goods.getGcode());
			document.addField("gname", goods.getGname());
			if (goods.getPrice() != null) {
				document.addField("price", String.valueOf(goods.getPrice()));
			}
			document.addField("storeName", goods.getStoreName());
			document.addField("intro", goods.getIntro());
			document.addField("imageName", goods.getImageName());
			document.addField("imageUrl",goods.getImageUrl());
			client.add(document);
		}
		client.commit();
		client.close();
	}



}
