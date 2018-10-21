package com.ch.solrJD.util;

import cn.hutool.core.util.StrUtil;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Auther: pch
 * @Date: 2018/10/21 14:09
 * @Description:
 */
@Component
public class SolrUtils {

	private static String solrUrl;
	@Value("${SOLR_URL}")
	public void setSolrUrl(String solrUrl){
		SolrUtils.solrUrl = solrUrl;
	}

	public static HttpSolrClient getSolrClient() {
		if (StrUtil.isEmpty(solrUrl)) {
			return null;
		}
		HttpSolrClient client = new HttpSolrClient.Builder(solrUrl).build();
		return client;
	}
}
