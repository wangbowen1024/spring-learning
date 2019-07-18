# spring 学习笔记

## 目录
1. [IOC控制反转](#IOC控制反转)
	* 入门使用
	* ApplicationContext的三个常用实现类
	* ApplicationContext和BeanFactory的区别
	* 创建bean的三种方式
	* bean的作用范围调整
	* bean对象的生命周期
2. [DI依赖注入](#DI依赖注入)
	* 注入的三种方式
	* 集合类型的注入
3. [IOC、DI注解开发（还存在XML）](#IOC、DI注解开发)
	* XML约束（注解）
	* 创建对象的注解
	* 创建对象的注解
	* 改变作用范围的注解
	* 生命周期相关注解
4. [新注解（用于配置类，解决上诉XML）](#新注解（用于配置类）)
5. [spring集成junit](#spring集成junit)

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

## DI依赖注入
* 概括：在当前类需要用到其他类的对象，由spring为我们提供，我们只需要在配置文件中说明依赖关系的维护，就称之为依赖注入。（即，IOC为我们创建对象，而DI则是给对象中的成员赋值。）适用于不经常改变的数据（单例）
* 能注入的数据：有三类
	* 基本类型和String
	* 其他bean类型（在配置文件中或者注解配置过的bean）
	* 复杂类型/集合类型
* 注入的方式：有三种
	* 第一种：使用构造函数提供
	```xml
	<!--构造函数注入：
        使用的标签:constructor-arg
        标签出现的位置：bean标签的内部
        标签中的属性
            type：用于指定要注入的数据的数据类型，该数据类型也是构造函数中某个或某些参数的类型
            index：用于指定要注入的数据给构造函数中指定索引位置的参数赋值。索引的位置是从0开始
            name：用于指定给构造函数中指定名称的参数赋值（常用的）
            =============以上三个用于指定给构造函数中哪个参数赋值===============================
            value：用于提供基本类型和String类型的数据(其中基本数据类型和String可以自动转化)
            ref：用于指定其他的bean类型数据。它指的就是在spring的Ioc核心容器中出现过的bean对象

        优势：
            在获取bean对象时，注入数据是必须的操作，否则对象无法创建成功。
        弊端：
            改变了bean对象的实例化方式，使我们在创建对象时，如果用不到这些数据，也必须提供。
    -->
    <bean id="studentService" class="com.spring.service.impl.StudentServiceImpl">
        <constructor-arg name="name" value="test"/>
        <constructor-arg name="age" value="50"/>
        <constructor-arg name="birthday" ref="now"/>
    </bean>
	<!-- 配置一个日期对象 -->
    <bean id="now" class="java.util.Date"/>
	```
	* 第二种：使用set方法提供
	```xml
	<!-- set方法注入（更常用的方式）
        涉及的标签：property
        出现的位置：bean标签的内部
        标签的属性
            name：用于指定注入时所调用的set方法名称
            value：用于提供基本类型和String类型的数据
            ref：用于指定其他的bean类型数据。它指的就是在spring的Ioc核心容器中出现过的bean对象
        优势：
            创建对象时没有明确的限制，可以直接使用默认构造函数
        弊端：
            如果有某个成员必须有值，则获取对象是有可能set方法没有执行。
    -->
    <bean id="studentService2" class="com.spring.service.impl.StudentServiceImpl2">
        <property name="name" value="test2"/>
        <property name="age" value="15"/>
        <property name="birthday" ref="now"/>
    </bean>
	```
	* 第三种：使用注解提供(下面介绍)
* 集合类型的注入
```xml
<!-- 复杂类型的注入/集合类型的注入
    用于给List结构集合注入的标签：
        list array set
    用于个Map结构集合注入的标签:
        map  props
    ************!结构相同，标签可以互换,所以记List和Map就够了!*****************
-->
<bean id="studentService3" class="com.spring.service.impl.StudentServiceImpl3">
    <property name="myArray">
        <array>
            <value>AAA</value>
            <value>BBB</value>
            <value>CCC</value>
        </array>
    </property>
    <property name="myList">
        <list>
            <value>DDD</value>
            <value>EEE</value>
            <value>FFF</value>
        </list>
    </property>
    <property name="mySet">
        <set>
            <value>QQQ</value>
            <value>WWW</value>
        </set>
    </property>
    <property name="myMap">
        <map>
            <entry key="NO_1" value="GGG"/>
            <entry key="NO_2" value="HHH"/>
        </map>
    </property>
    <property name="myProperties">
        <props>
            <prop key="NO_3">III</prop>
            <prop key="NO_4">JJJ</prop>
        </props>
    </property>
</bean>
```

## IOC、DI注解开发
* xml约束
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    <!--告知spring在创建容器时要扫描的包，配置所需要的标签不是在beans的约束中，而是一个名称为context名称空间和约束中-->
    <context:component-scan base-package="com.spring"/>
</beans>
```
* 曾经XML的配置
```xml
<bean id="accountService" class="com.itheima.service.impl.AccountServiceImpl" scope=""  init-method="" destroy-method="">
   <property name=""  value="" | ref=""></property>
</bean>
```
* 用于创建对象的注解（他们的作用就和在XML配置文件中编写一个<bean>标签实现的功能是一样的）
	* Component:
	```
	作用：用于把当前类对象存入spring容器中
	属性：
	value：用于指定bean的id。当我们不写时，它的默认值是当前类名，且首字母改小写。
	```
	* Controller：一般用在表现层
	* Service：一般用在业务层
	* Repository：一般用在持久层
	```
	以上三个注解他们的作用和属性与Component是一模一样。
	他们三个是spring框架为我们提供明确的三层使用的注解，使我们的三层对象更加清晰
	```
* 用于注入数据的注解（他们的作用就和在xml配置文件中的bean标签中写一个<property>标签的作用是一样的）
	* Autowired:
	```
	作用：自动按照类型注入。只要容器中有唯一的一个bean对象类型和要注入的变量类型匹配，就可以注入成功
	如果ioc容器中没有任何bean的类型和要注入的变量类型匹配，则报错。
	如果Ioc容器中有多个类型匹配时：
	出现位置：
		可以是变量上，也可以是方法上
	细节：
		在使用注解注入时，set方法就不是必须的了。
	```
	* Qualifier:
	```
	作用：在按照类中注入的基础之上再按照名称注入。它在给类成员注入时不能单独使用。但是在给方法参数注入时可以
	属性：
		value：用于指定注入bean的id。
	注意：必须配合Autowired注解使用
	```
	* Resource
	```
	作用：直接按照bean的id注入。它可以独立使用
	属性：
		name：用于指定bean的id。
	```
	```
	【注意】以上三个注入都只能注入其他bean类型的数据，而基本类型和String类型无法使用上述注解实现。另外，集合类型的注入只能通过XML来实现。
	```
	* Value
	```
	作用：用于注入基本类型和String类型的数据
	属性：
		value：用于指定数据的值。它可以使用spring中SpEL(也就是spring的el表达式）。SpEL的写法：${表达式}
	```
* 用于改变作用范围的注解（他们的作用就和在bean标签中使用scope属性实现的功能是一样的）
	* Scope
	```
	作用：用于指定bean的作用范围
	属性：
		value：指定范围的取值。常用取值：singleton prototype
	```
* 和生命周期相关的注解（他们的作用就和在bean标签中使用init-method和destroy-method的作用是一样的）
	* PreDestroy（用于指定销毁方法）
	* PostConstruct（用于指定初始化方法）3

## 新注解（用于配置类）
* 主配置类
```java
/**
 * 该类是一个配置类，它的作用和bean.xml是一样的
 * spring中的新注解
 * Configuration
 *     作用：指定当前类是一个配置类
 *     细节：当配置类作为AnnotationConfigApplicationContext对象创建的参数时，该注解可以不写。
 * ComponentScan
 *      作用：用于通过注解指定spring在创建容器时要扫描的包
 *      属性：
 *          value：它和basePackages的作用是一样的，都是用于指定创建容器时要扫描的包。
 *                 我们使用此注解就等同于在xml中配置了:
 *                      <context:component-scan base-package="com.itheima"></context:component-scan>
 *  Bean
 *      作用：用于把当前方法的返回值作为bean对象存入spring的ioc容器中
 *      属性:
 *          name:用于指定bean的id。当不写时，默认值是当前方法的名称
 *      细节：
 *          当我们使用注解配置方法时，如果方法有参数，spring框架会去容器中查找有没有可用的bean对象。
 *          查找的方式和Autowired注解的作用是一样的
 *  Import
 *      作用：用于导入其他的配置类
 *      属性：
 *          value：用于指定其他配置类的字节码。
 *                  当我们使用Import的注解之后，有Import注解的类就父配置类，而导入的都是子配置类
 *  PropertySource
 *      作用：用于指定properties文件的位置
 *      属性：
 *          value：指定文件的名称和路径。
 *                  关键字：classpath，表示类路径下
 */
//@Configuration
@ComponentScan("com.itheima")
@Import(JdbcConfig.class)
@PropertySource("classpath:jdbcConfig.properties")
public class SpringConfiguration {
}
```

* 子配置类
```java
/**
 * 和spring连接数据库相关的配置类
 */
public class JdbcConfig {

    @Value("${jdbc.driver}")
    private String driver;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    /**
     * 用于创建一个QueryRunner对象
     * @param dataSource
     * @return
     */
    @Bean(name="runner")
    @Scope("prototype")
    public QueryRunner createQueryRunner(@Qualifier("ds2") DataSource dataSource){
        return new QueryRunner(dataSource);
    }

    /**
     * 创建数据源对象
     * @return
     */
    @Bean(name="ds2")
    public DataSource createDataSource(){
        try {
            ComboPooledDataSource ds = new ComboPooledDataSource();
            ds.setDriverClass(driver);
            ds.setJdbcUrl(url);
            ds.setUser(username);
            ds.setPassword(password);
            return ds;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Bean(name="ds1")
    public DataSource createDataSource1(){
        try {
            ComboPooledDataSource ds = new ComboPooledDataSource();
            ds.setDriverClass(driver);
            ds.setJdbcUrl("jdbc:mysql://localhost:3306/eesy02");
            ds.setUser(username);
            ds.setPassword(password);
            return ds;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
```

* 单测入口
```java
/**
 * 测试queryrunner是否单例
 */
public class QueryRunnerTest {

    @Test
    public  void  testQueryRunner(){
        //1.获取容易
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        //2.获取queryRunner对象
        QueryRunner runner = ac.getBean("runner",QueryRunner.class);
        QueryRunner runner1 = ac.getBean("runner",QueryRunner.class);
        System.out.println(runner == runner1);
    }
}
``` 

## spring集成junit
* 添加依赖
```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.0.2.RELEASE</version>
</dependency>
```
* 实例

```java
/**
 * 使用Junit单元测试：测试我们的配置
 * Spring整合junit的配置
 *      1、导入spring整合junit的jar(坐标)
 *      2、使用Junit提供的一个注解把原有的main方法替换了，替换成spring提供的
 *             @Runwith
 *      3、告知spring的运行器，spring和ioc创建是基于xml还是注解的，并且说明位置
 *          @ContextConfiguration
 *                  locations：指定xml文件的位置，加上classpath关键字，表示在类路径下
 *                  classes：指定注解类所在地位置
 *
 *   当我们使用spring 5.x版本的时候，要求junit的jar必须是4.12及以上
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class AccountServiceTest {

    @Autowired
    private IAccountService as = null;


    @Test
    public void testFindAll() {
        //3.执行方法
        List<Account> accounts = as.findAllAccount();
        for(Account account : accounts){
            System.out.println(account);
        }
    }

    // ...
}
```