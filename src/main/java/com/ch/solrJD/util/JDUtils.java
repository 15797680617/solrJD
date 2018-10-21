package com.ch.solrJD.util;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ch.solrJD.entity.Goods;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: pch
 * @Date: 2018/10/19 19:20
 * @Description:
 */
public class JDUtils {

	// 获取京东实时搜索热词URL
	private final static String HOT_WORDS_URL = "https://f.3.cn/recommend/hotwords/get?pin=&uuid=1055742474&callback=jsonpHotWords";
	// 热词集合
	public static List<String> hotWords = new ArrayList<String>();
	// 每次搜索抓取商品的页数
	private final static int PAGENUM = 5;

	// 获取实时搜索热词
	public static void getHotWord(){
		hotWords.clear();
		String response = HttpUtil.get(HOT_WORDS_URL);
		response = response.substring(14, response.lastIndexOf(")"));
		JSONObject jsonObject = JSONUtil.parseObj(response);
		JSONArray jsonArray = (JSONArray) jsonObject.get("data");
		JSONObject json;
		for (int i = 1; i < jsonArray.size(); i++) {
			json = (JSONObject) jsonArray.get(i);
			hotWords.add((String) json.get("n"));
		}
		// 添加一些
		hotWords.add("手机");
		hotWords.add("电脑");
		hotWords.add("水果");
		hotWords.add("数码");
		hotWords.add("家具");
		hotWords.add("汽车");
		hotWords.add("母婴");
		hotWords.add("艺术");
		hotWords.add("珠宝");
	}

	public static List<Goods> crawlGoodsByJD(String hotWord){
		List<String> responseList;
		List<String> regexResult;
		List<String> gcodes = null;
		List<String> gnames = new ArrayList<>();
		List<String> prices = null;
		List<String> storeNames = null;
		List<String> intros = null;
		List<String> imageNames = new ArrayList<>();
		List<String> imageUrls = null;
		List<Goods> goodsList = new ArrayList<>();
		responseList = getGoodsResponse(hotWord);
		Goods goods;
		for (String response : responseList) {
			// 商品编号
			gcodes = ReUtil.findAll("<a id=\"J_comment_(.*?)\"", response, 1);
			regexResult = ReUtil.findAll("<div class=\"p-name p-name-type-2\">(.*?)<em>(.*?)</em>", response, 2);
			// 商品名称
			for (String gname : regexResult) {
				gnames.add(ReUtil.delAll("<(.*?)>", gname));
			}
			// 价格
			prices = ReUtil.findAll("<em>￥</em><i>(.*?)</i>", response, 1);

			// 店铺名
			storeNames = ReUtil.findAll("<span class=\"J_im_icon\">(.*?)title=\"(.*?)\">", response, 2);

			// 商品简介
			intros = ReUtil.findAll("<i class=\"promo-words\"(.*?)>(.*?)</i>", response, 2);

			// 图片url
			imageUrls = ReUtil.findAll("source-data-lazy-img=\"(.*?)\"", response, 1);
			for (String imageUrl : imageUrls) {
				imageNames.add(imageUrl.substring(imageUrl.lastIndexOf("/") + 1));
			}
			for (int i = 0; i < gcodes.size(); i++) {
				goods = new Goods();
				goods.setGcode(gcodes.get(i));
				if (gnames.size() > i) {
					goods.setGname(gnames.get(i));
				}
				if (prices.size() > i) {
					if (StrUtil.isNotEmpty(prices.get(i))){
						goods.setPrice(new BigDecimal(prices.get(i)));
					}
				}
				if (storeNames.size() > i) {
					goods.setStoreName(storeNames.get(i));
				}
				if (intros.size() > i) {
					goods.setIntro(intros.get(i));
				}
				if (imageNames.size() > i) {
					goods.setImageName(imageNames.get(i));
				}
				if (imageUrls.size() > i) {
					goods.setImageUrl(URLUtil.formatUrl(imageUrls.get(i)));
				}
				goodsList.add(goods);
			}
		}
		System.out.println(goodsList.size());
		return goodsList;
	}


	private static List<String> getGoodsResponse(String keyWord) {
		String url;
		String response;
		List<String> responseList = new ArrayList<>();
		for (int i = 1; i <= PAGENUM; i++) {
			url = "https://search.jd.com/Search?keyword="+keyWord+"&enc=utf-8&page="+i;
			response = HttpUtil.get(url);
			if (StrUtil.isNotEmpty(response)) {
				responseList.add(response);
			}
		}
		return responseList;
	}

}
