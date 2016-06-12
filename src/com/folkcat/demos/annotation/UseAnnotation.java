package com.folkcat.demos.annotation;

import java.util.HashMap;
import java.util.Map;


 
/**
 * 这个类专门用来测试注解使用
 * @author tamas
 */
 
@TestA(name="type",gid=Long.class) //类成员注解
public class UseAnnotation {
	
	@TestA(name="param",id=1,gid=Long.class) //类成员注解
	private Integer age;
	
	@TestA (name="construct",id=2,gid=Long.class)//构造方法注解
	public UseAnnotation(){
		
	}
	@TestA(name="public method",id=3,gid=Long.class) //类方法注解
	public void a(){
		Map<String,String> m = new HashMap<String,String>(0);
	}
	
	@TestA(name="protected method",id=4,gid=Long.class) //类方法注解
	protected void b(){
		Map<String,String> m = new HashMap<String,String>(0);
	}
	
	@TestA(name="private method",id=5,gid=Long.class) //类方法注解
	private void c(){
		Map<String,String> m = new HashMap<String,String>(0);
	}
	
	public void b(Integer a){ 
		
	}
}