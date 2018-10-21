package com.ch.demo;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.junit.Test;

import java.io.IOException;

/**
 * @Auther: pch
 * @Date: 2018/10/19 19:10
 * @Description:
 */
public class SolrTest {


	/**
	 * 删除所有索引
	 * @throws IOException
	 * @throws SolrServerException
	 */
	@Test
	public void deleteAllIndex() throws IOException, SolrServerException {
		String url = "http://localhost:8877/solr/core";
		HttpSolrClient client = new HttpSolrClient.Builder(url).build();
		client.deleteByQuery("*:*");
		client.commit();
		client.close();
	}
}
