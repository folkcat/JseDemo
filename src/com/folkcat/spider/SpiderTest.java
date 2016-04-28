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

public class SpiderTest {

	public static void main(String[] args) {
		String urlStr = "https://metersbonwe.world.tmall.com/i/asynSearch.htm?_ksTS=1461711295301_372&callback=jsonp373&mid=w-10803134389-0&wid=10803134389&path=/category.htm&&spm=a1z10.3-b.w4011-10803134389.509.ObcWZy&search=y&scene=taobao_shop&pageNo=3";
		TinyHttpUtil httpUtil = new TinyHttpUtil("gbk");
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("user-agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2684.0 Safari/537.36");

		headers.put("cookie",
				"_tb_token_=hWovouV5FYaI; cookie2=b01b78a032a0e29e7735603389a5e46c; t=616ce949237b86d5ecb8bd2a6af38adf");
		String content = httpUtil.sendPost(urlStr, null, true, headers);

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
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void printNodeInfo(Node node){
		try{
			String itemHtml=node.toHtml(true);
			itemHtml=itemHtml.replace("\\", "");
			
			//提取url
			NodeFilter urlFilter= new HasAttributeFilter( "class", "J_TGoldData" );
			NodeList list=new Parser(itemHtml).parse(urlFilter);
			int size=list.size();
			for (int i = 0; i < size; i++) {// i-1表示节数
				LinkTag linkTag = (LinkTag)list.elementAt(i);
				String url=linkTag.getAttribute("href");
				int a=url.indexOf('/');
				System.out.println("URL:"+url.substring(a+2));
				
			}
			
			//提取价格
			NodeFilter priceFilter= new HasAttributeFilter( "class", "c-price" );
			NodeList list2=new Parser(itemHtml).parse(priceFilter);
			size=list2.size();
			for(int i=0;i<size;i++){
				Node n=list2.elementAt(i);
				String priceStr=n.toPlainTextString();
				priceStr=priceStr.replace(" ", "");
				System.out.println("Price:"+priceStr);
			}
			
			
			//提取图片url、title
			NodeFilter strFilter= new TagNameFilter("img");
			NodeList list4=new Parser(itemHtml).parse(strFilter);
			size=list4.size();
			for(int i=0;i<size;i++){
				Node n=list4.elementAt(i);
				Tag tag=(Tag)n;
				String imgUrl=tag.getAttribute("data-ks-lazyload");
				int a=imgUrl.indexOf('/');
				System.out.println("Image:"+imgUrl.substring(a+2));
				String title=tag.getAttribute("alt");
				System.out.println("Title:"+title);
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
