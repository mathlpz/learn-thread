package com.lpz.excel.test0;

import java.util.Date;


/**
 * 
 * @author lpz
 *
 */
public class Student {
	
	
	private Integer id;
	private String name;
	private Integer age;
	private Boolean Sex;
	private Date birthday;
	
	
	
	public Student() {
		super();
	}
	
	public Student(Integer id, String name, Integer age, Boolean sex, Date birthday) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		Sex = sex;
		this.birthday = birthday;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Boolean getSex() {
		return Sex;
	}
	public void setSex(Boolean sex) {
		Sex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
}

