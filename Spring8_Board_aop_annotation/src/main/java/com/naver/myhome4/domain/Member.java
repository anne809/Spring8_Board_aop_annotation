package com.naver.myhome4.domain;

public class Member {
	private String id ;
	
	/*
		데이터베이스, 비즈니스 객체, 뷰 객체에서 가져온 값을 저장하거나
		데이터베이스, 비즈니스 객체, 뷰 객체에 보낼 값을 저장하는 객체를 
		도메인 객체(Domain Object) 또는 도메인 모델(Domain model)이라 한다.
	
	
	*/
	//joinForm.jsp에서 비밀번호 name속성값을 확인해주세요
	private String password;
	private String name;
	private int    age;
	private String gender;
	private String email;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	

}
