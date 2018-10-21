package com.ch.solrJD.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.ch.solrJD.dao.GoodsDao;
import com.ch.solrJD.entity.GoodsVO;
import com.ch.solrJD.util.SolrUtils;
import com.ch.solrJD.util.Utils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: pch
 * @Date: 2018/10/21 15:39
 * @Description:
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	private GoodsDao goodsDao;

	@RequestMapping("/search")
	public String onSearch(String gname, Model model) {
		HttpSolrClient client = SolrUtils.getSolrClient();
		SolrQuery query = new SolrQuery();
		query.set("q", "gname:" + gname);
		query.setStart(0);
		query.setRows(100);
		QueryResponse response = null;
		try {
			response = client.query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SolrDocumentList documentList = response.getResults();
		List<GoodsVO> goodsVOList = new ArrayList<>();
		for (SolrDocument document : documentList) {
			GoodsVO vo = new GoodsVO();
			String gid = (String) document.getFieldValue("gid");
			String gcode = (String) document.getFieldValue("gcode");
			System.out.println(gcode);
			List<String> gnames = (List<String>) document.getFieldValue("gname");
			List<String> storeNames = (List<String>) document.getFieldValue("storeName");
			if (document.getFieldValue("price") != null) {
				Double price = (double) document.getFieldValue("price");
				vo.setPriceStr(String.valueOf(new BigDecimal(price).setScale(2,BigDecimal.ROUND_DOWN)));
			}
			String intro = (String) document.getFieldValue("intro");
			String imageName = (String) document.getFieldValue("imageName");
			String imageUrl = (String) document.getFieldValue("imageUrl");
			vo.setGid(Integer.valueOf(gid));
			vo.setGcode(gcode);
			vo.setGname(gnames.get(0));
			if (storeNames!=null && storeNames.size() > 0) {
				vo.setStoreName(storeNames.get(0));
			}
			vo.setIntro(intro);
			vo.setImageName(imageName);
			vo.setImageUrl(imageUrl.replaceAll("\\\\","/"));
			goodsVOList.add(vo);
		}
		model.addAttribute("gname", gname);
		model.addAttribute("goodsVOList", goodsVOList);

		return "/index";
	}

	@RequestMapping("/getImage")
	public void getImage(String url, HttpServletResponse response) {
		if (StrUtil.isEmpty(url)) {
			return;
		}
		File file = new File(url);
		if (file.mkdirs()) {
			return;
		}
		try {
			Utils.responseFile(response,file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
