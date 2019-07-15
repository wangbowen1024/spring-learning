# spring 学习笔记

## 目录
1. [IOC控制反转](#IOC控制反转)
	* 入门使用
	* ApplicationContext的三个常用实现类
	* ApplicationContext和BeanFactory的区别
	* 创建bean的三种方式
	* bean的作用范围调整
	* bean对象的生命周期

## IOC控制反转
* 概括：就是把创建对象的工作交给spring容器，以此来降低耦合
* 入门使用：
	1. 导入maven
	```xml
	<dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.0.2.RELEASE</version>
    </dependency>
	```
	2. 配置bean.xml
	```xml
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    	<bean id="studentService" class="com.spring.service.impl.StudentServiceImpl"/>
	</beans>
	```
	3. 使用
	```java
	// 1.获取核心容器
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
    // 2.根据ID获取bean对象
    StudentService studentService = (StudentService) applicationContext.getBean("studentService");
    studentService.saveStudent();
	```
* ApplicationContext的三个常用实现类
	* ClassPathXmlApplicationContext：它可以加载类路径下的配置文件，要求配置文件必须在类路径下。不在的话，加载不了。(更常用)
	* FileSystemXmlApplicationContext：它可以加载磁盘任意路径下的配置文件(必须有访问权限）
	* AnnotationConfigApplicationContext：它是用于读取注解创建容器的，是明天的内容。
* ApplicationContext和BeanFactory的区别
	*  ApplicationContext:     单例对象适用              采用此接口
           它在构建核心容器时，创建对象采取的策略是采用立即加载的方式。也就是说，只要一读取完配置文件马上就创建配置文件中配置的对象。
	*  BeanFactory:            多例对象使用
           它在构建核心容器时，创建对象采取的策略是采用延迟加载的方式。也就是说，什么时候根据id获取对象了，什么时候才真正的创建对象。
	* BeanFactory已经过时，ApplicationContext比较智能，可以自动判断单例和多例，应该用这个
* 创建bean的三种方式
	* 使用默认构造函数
	```xml
	<!-- 第一种方式：使用默认构造函数创建。
            在spring的配置文件中使用bean标签，配以id和class属性之后，且没有其他属性和标签时。
            采用的就是默认构造函数创建bean对象，此时如果类中没有默认构造函数，则对象无法创建。
	-->
    <bean id="accountService" class="com.itheima.service.impl.AccountServiceImpl"/>
	```
	* 使用普通工厂中的方法
	```xml
	<!-- 第二种方式： 使用普通工厂中的方法创建对象（使用某个类中的方法创建对象，并存入spring容器）-->
    <bean id="instanceFactory" class="com.itheima.factory.InstanceFactory"/>
    <bean id="accountService" factory-bean="instanceFactory" factory-method="getAccountService"/>
	```
	* 使用工厂中的静态方法
	```xml
	<!-- 第三种方式：使用工厂中的静态方法创建对象（使用某个类中的静态方法创建对象，并存入spring容器)-->
    <bean id="accountService" class="com.itheima.factory.StaticFactory" factory-method="getAccountService"/>
	```
* bean的作用范围调整
	```xml
	<!-- bean的作用范围调整
        bean标签的scope属性：
            作用：用于指定bean的作用范围
            取值： 常用的就是单例的和多例的
                singleton：单例的（默认值）
                prototype：多例的
                request：作用于web应用的请求范围
                session：作用于web应用的会话范围
                global-session：作用于集群环境的会话范围（全局会话范围），当不是集群环境时，它就是session
	-->
    <bean id="accountService" class="com.itheima.service.impl.AccountServiceImpl" scope="prototype"/>
	```
* bean对象的生命周期
	```xml
	<!-- bean对象的生命周期
        单例对象
            出生：当容器创建时对象出生
            活着：只要容器还在，对象一直活着
            死亡：容器销毁，对象消亡
            总结：单例对象的生命周期和容器相同
        多例对象
            出生：当我们使用对象时spring框架为我们创建
            活着：对象只要是在使用过程中就一直活着。
            死亡：当对象长时间不用，且没有别的对象引用时，由Java的垃圾回收器回收
    -->
	<!-- 其中init，destroy是类中的方法，在创建和销毁的时候会执行 -->
    <bean id="accountService" class="com.itheima.service.impl.AccountServiceImpl"
          scope="prototype" init-method="init" destroy-method="destroy"/>
	```