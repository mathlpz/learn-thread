package com.lpz.proxy.cglib;

import com.lpz.proxy.jdk.UserServiceImpl;

import net.sf.cglib.proxy.Enhancer;


/**
 * 
 * ASM是一个Java字节码操纵框架，它能被用来动态生成类或者增强既有类的功能。
 * ASM可以直接产生二进制class文件，也可以在类被加载入Java虚拟机之前动态改变类行为。
 * Java class被存储在严格格式定义的.class文件里，这些类文件拥有足够的元数据来解析类中的所有元素：类名称、方法、属性以及 Java 字节码（指令）。
 * ASM从类文件中读入信息后，能够改变类行为，分析类信息，甚至能够根据用户要求生成新类。

目前许多框架如cglib、Hibernate、Spring都直接或间接地使用ASM操作字节码，有些语言如Jython、JRuby、Groovy也是如此。
而类ASM字节码工具还有：
BCEL：Byte Code Engineering Library (BCEL)，这是Apache Software Foundation 的Jakarta 项目的一部分。BCEL是 Java classworking 最广泛使用的一种框架,它可以让您深入 JVM 汇编语言进行类操作的细节。
	BCEL与Javassist 有不同的处理字节码方法，BCEL在实际的JVM 指令层次上进行操作(BCEL拥有丰富的JVM 指令级支持)而Javassist 所强调的源代码级别的工作。
JBET：通过JBET(Java Binary Enhancement Tool )的API可对Class文件进行分解，重新组合，或被编辑。JBET也可以创建新的Class文件。JBET用一种结构化的方式来展现Javabinary (.class)文件的内容，并且可以很容易的进行修改。
Javassist：Javassist是一个开源的分析、编辑和创建Java字节码的类库。是由东京技术学院的数学和计算机科学系的 Shigeru Chiba 所创建的。
	它已加入了开放源代码JBoss 应用服务器项目,通过使用Javassist对字节码操作为JBoss实现动态AOP框架。
cglib：是一个强大的,高性能,高质量的"Code生成类库"。它可以在运行期扩展Java类与实现Java接口，cglib封装了asm，可以在运行期动态生成新的 class，Hibernate和Spring都用到过它。
	cglib用于AOP，jdk中的proxy必须基于接口，cglib却没有这个限制。
而ASM与cglib、serp和BCEL相比，ASM有以下的优点 :
	ASM 具有简单、设计良好的 API，这些 API 易于使用；
	ASM 有非常良好的开发文档，以及可以帮助简化开发的 Eclipse 插件；
	ASM 支持 Java 6(ASM3)、Java7(ASM4)、Java(ASM5)；
	ASM 很小、很快、很健壮；
	ASM 有很大的用户群，可以帮助新手解决开发过程中遇到的问题；
	ASM 的开源许可可以让你几乎以任何方式使用它；
	
	
 *	Cglib
 *	MethodInterceptor
 *	Enhancer
 * 
 * @author lpz
 *
 */
public class Main2 {
	
    public static void main(String[] args) {
    	//
        CglibProxy cglibProxy = new CglibProxy();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserServiceImpl.class);
        enhancer.setCallback(cglibProxy);

        UserServiceImpl o = (UserServiceImpl)enhancer.create();
        System.out.println(o.getName(1));
        System.out.println();
        System.out.println(o.getAge(1));
    }
}