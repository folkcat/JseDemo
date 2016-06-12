package com.folkcat.spider;

import java.util.HashMap;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

public class Extractor {
	public static TaobaoDBController mTaobaoDBController;
	private String mUrlStr;
	public Extractor(String url){
		mUrlStr=url;
		mTaobaoDBController=new TaobaoDBController();
	}
	public void extract(){
		TinyHttpUtil httpUtil = new TinyHttpUtil("gbk");
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("user-agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2684.0 Safari/537.36");

		headers.put("cookie",
				"_tb_token_=hWovouV5FYaI; cookie2=b01b78a032a0e29e7735603389a5e46c; t=616ce949237b86d5ecb8bd2a6af38adf");
		String content = httpUtil.sendPost(mUrlStr, null, true, headers);

		// System.out.println(content);
		int a=content.indexOf('<');
		int b=content.lastIndexOf('\"');
		content=content.substring(a,b-1);

		try {
			NodeFilter filter = new TagNameFilter("dl");
			Parser p = new Parser(content);
			NodeList list = p.parse(filter);
			int size=list.size();
			//printNodeList(list);
			for (int i = 0; i < size; i++) {// i-1表示节数
				Node node = list.elementAt(i);
				printNodeInfo(node);
				//Thread.sleep(200);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		/*
		String urlStrPrefix = "https://metersbonwe.world.tmall.com/i/asynSearch.htm?_ksTS=1461711295301_372&callback=jsonp373&mid=w-10803134389-0&wid=10803134389&path=/category.htm&&spm=a1z10.3-b.w4011-10803134389.509.ObcWZy&search=y&scene=taobao_shop&pageNo=";
		String url="";
		for(int i=2;i<22;i++){
			url=urlStrPrefix+i;
			Extractor extractor=new Extractor(url);
			extractor.extract();
		}
		*/
		Extractor extrator=new Extractor("https://metersbonwe.world.tmall.com/i/asynSearch.htm?_ksTS=1461711295301_372&callback=jsonp373&mid=w-10803134389-0&wid=10803134389&path=/category.htm&&spm=a1z10.3-b.w4011-10803134389.509.ObcWZy&search=y&scene=taobao_shop&pageNo=1");
		extrator.extract();
		
	}
	
	private  void printNodeInfo(Node node){
		try{
			String goodsPrice="";
			String goodsUrl="";
			String picUrl="";
			String goodsTitle="";
			
			String itemHtml=node.toHtml(true);
			itemHtml=itemHtml.replace("\\", "");
			
			//提取url
			NodeFilter urlFilter= new HasAttributeFilter( "class", "J_TGoldData" );
			NodeList list=new Parser(itemHtml).parse(urlFilter);
			LinkTag linkTag = (LinkTag)list.elementAt(0);
			String url=linkTag.getAttribute("href");
			int a=url.indexOf('/');
			goodsUrl=url.substring(a+2);
			
			//提取价格
			NodeFilter priceFilter= new HasAttributeFilter( "class", "c-price" );
			NodeList list2=new Parser(itemHtml).parse(priceFilter);
			Node n=list2.elementAt(0);
			String priceStr=n.toPlainTextString();
			goodsPrice=priceStr.replace(" ", "");
			
			//提取图片url、title
			NodeFilter strFilter= new TagNameFilter("img");
			NodeList list4=new Parser(itemHtml).parse(strFilter);
			n=list4.elementAt(0);
			Tag tag=(Tag)n;
			String imgUrl=tag.getAttribute("data-ks-lazyload");
			a=imgUrl.indexOf('/');
			picUrl=imgUrl.substring(a+2);
			goodsTitle=tag.getAttribute("alt");
			
			System.out.println("Price:"+goodsPrice);
			System.out.println("Title:"+goodsTitle);
			System.out.println("URL:"+goodsUrl);
			System.out.println("Image:"+picUrl);
			mTaobaoDBController.insert(goodsUrl, picUrl, goodsPrice, goodsTitle);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
