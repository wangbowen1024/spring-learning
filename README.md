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
6. [动态代理](#动态代理)
	* Proxy
	* cglib
7. [AOP面向切面编程](#AOP面向切面编程)
	* 专属名词
	* 基于XML的AOP
	* 基于注解的AOP
8. [Spring中的JdbcTemplate](#Spring中的JdbcTemplate)
	* 添加依赖
	* 使用JdbcTmplate进行CRUD
	* 抽取公共代码JdbcTemplate
9. [声明式事务控制](#声明式事务控制)
	* 事务的一些基本知识
	* 基于XML事务控制
	* 基于纯注解的事务控制
10. [基于编程式事务控制（了解）](#基于编程式事务控制（了解）)

## IOC控制反转
* 概括：就是把创建对象的工作交给spring容器，以此来降低耦合

### 入门使用：
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

### 创建bean的三种方式
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

### bean的作用范围调整
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

### bean对象的生命周期
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

### 注入的方式：有三种
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

### 集合类型的注入
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
### xml约束
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

### 曾经XML的配置
```xml
<bean id="accountService" class="com.itheima.service.impl.AccountServiceImpl" scope=""  init-method="" destroy-method="">
   <property name=""  value="" | ref=""></property>
</bean>
```

### 用于创建对象的注解（他们的作用就和在XML配置文件中编写一个<bean>标签实现的功能是一样的）
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

### 用于注入数据的注解（他们的作用就和在xml配置文件中的bean标签中写一个<property>标签的作用是一样的）
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

### 和生命周期相关的注解（他们的作用就和在bean标签中使用init-method和destroy-method的作用是一样的）
	* PreDestroy（用于指定销毁方法）
	* PostConstruct（用于指定初始化方法）

## 新注解（用于配置类）
### 主配置类
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
 *                      <context:component-scan base-package="com.spring"></context:component-scan>
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
### 子配置类
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
### 单测入口
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
### 添加依赖
```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.0.2.RELEASE</version>
</dependency>
```
### 实例
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

## 动态代理
### Proxy
```java
public static void main(String[] args) {
    final Producer producer = new Producer();

    /**
     * 动态代理：
     *  特点：字节码随用随创建，随用随加载
     *  作用：不修改源码的基础上对方法增强
     *  分类：
     *      基于接口的动态代理
     *      基于子类的动态代理
     *  基于接口的动态代理：
     *      涉及的类：Proxy
     *      提供者：JDK官方
     *  如何创建代理对象：
     *      使用Proxy类中的newProxyInstance方法
     *  创建代理对象的要求：
     *      被代理类最少实现一个接口，如果没有则不能使用
     *  newProxyInstance方法的参数：
     *      ClassLoader：类加载器
     *          它是用于加载代理对象字节码的。和被代理对象使用相同的类加载器。固定写法。
     *      Class[]：字节码数组
     *          它是用于让代理对象和被代理对象有相同方法。固定写法。
     *      InvocationHandler：用于提供增强的代码
     *          它是让我们写如何代理。我们一般都是些一个该接口的实现类，通常情况下都是匿名内部类，但不是必须的。
     *          此接口的实现类都是谁用谁写。
     */
   IProducer proxyProducer = (IProducer) Proxy.newProxyInstance(producer.getClass().getClassLoader(),
            producer.getClass().getInterfaces(),
            new InvocationHandler() {
                /**
                 * 作用：执行被代理对象的任何接口方法都会经过该方法
                 * 方法参数的含义
                 * @param proxy   代理对象的引用
                 * @param method  当前执行的方法
                 * @param args    当前执行方法所需的参数
                 * @return        和被代理对象方法有相同的返回值
                 * @throws Throwable
                 */
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    //提供增强的代码
                    Object returnValue = null;

                    //1.获取方法执行的参数
                    Float money = (Float)args[0];
                    //2.判断当前方法是不是销售
                    if("saleProduct".equals(method.getName())) {
                        returnValue = method.invoke(producer, money*0.8f);
                    }
                    return returnValue;
                }
            });
    proxyProducer.saleProduct(10000f);
}
```
### cglib
```xml
<dependency>
        <groupId>cglib</groupId>
        <artifactId>cglib</artifactId>
        <version>2.1_3</version>
    </dependency>
```
```java
public static void main(String[] args) {
    final Producer producer = new Producer();

    /**
     * 动态代理：
     *  特点：字节码随用随创建，随用随加载
     *  作用：不修改源码的基础上对方法增强
     *  分类：
     *      基于接口的动态代理
     *      基于子类的动态代理
     *  基于子类的动态代理：
     *      涉及的类：Enhancer
     *      提供者：第三方cglib库
     *  如何创建代理对象：
     *      使用Enhancer类中的create方法
     *  创建代理对象的要求：
     *      被代理类不能是最终类
     *  create方法的参数：
     *      Class：字节码
     *          它是用于指定被代理对象的字节码。
     *
     *      Callback：用于提供增强的代码
     *          它是让我们写如何代理。我们一般都是些一个该接口的实现类，通常情况下都是匿名内部类，但不是必须的。
     *          此接口的实现类都是谁用谁写。
     *          我们一般写的都是该接口的子接口实现类：MethodInterceptor
     */
    Producer cglibProducer = (Producer)Enhancer.create(producer.getClass(), new MethodInterceptor() {
        /**
         * 执行被代理对象的任何方法都会经过该方法
         * @param proxy
         * @param method
         * @param args
         *    以上三个参数和基于接口的动态代理中invoke方法的参数是一样的
         * @param methodProxy ：当前执行方法的代理对象
         * @return
         * @throws Throwable
         */
        @Override
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            //提供增强的代码
            Object returnValue = null;

            //1.获取方法执行的参数
            Float money = (Float)args[0];
            //2.判断当前方法是不是销售
            if("saleProduct".equals(method.getName())) {
                returnValue = method.invoke(producer, money*0.8f);
            }
            return returnValue;
        }
    });
    cglibProducer.saleProduct(12000f);
}
```

## AOP面向切面编程
### 专属名词
* 连接点：接口中所有的方法
* 切入点：被增强的方法
* 通知/增强：拦截到连接点之后要做的事情
	* 通知的类型：
	```java
	@Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object rtValue = null;
        try {
            // ...
            rtValue = method.invoke(accountService, args);
            // ...
        } catch (Exception e) {
            // ...
        } finally {
            // ...
        }
    }
	```
		* 前置通知：method.invoke之前执行的
		* 后置通知：method.invoke之后执行的
		* 异常通知：catch里面的
		* 最终通知：finally里面的
		* 环绕通知：整个invoke方法
* 引介：是一种特殊的通知在不修改类代码的前提下，Introduction可以在运行期为类动态地添加一些方法或Field
* 目标对象：被代理对象
* 织入：增强的过程
* 代理：代理对象，即一个类被AOP织入后，产生的一个结果代理类
* 切面：切入点和通知（引介）的结合

### 基于XML的AOP
* AOP的XML约束
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">
	<!-- AOP配置... -->
</beans>
```
* AOP配置步骤
```xml
<!--spring中基于XML的AOP配置步骤
    1、把通知Bean也交给spring来管理
    2、使用aop:config标签表明开始AOP的配置
    3、使用aop:aspect标签表明配置切面
            id属性：是给切面提供一个唯一标识
            ref属性：是指定通知类bean的Id。
    4、在aop:aspect标签的内部使用对应标签来配置通知的类型
           我们现在示例是让printLog方法在切入点方法执行之前之前：所以是前置通知
           aop:before：表示配置前置通知
                method属性：用于指定Logger类中哪个方法是前置通知
                pointcut属性：用于指定切入点表达式，该表达式的含义指的是对业务层中哪些方法增强
-->
<!-- 配置IOC，装配AccountServiceImpl -->
<bean id="accountService" class="com.spring.service.impl.AccountServiceImpl"/>
<!-- 配置IOC，装配Logger -->
<bean id="logger" class="com.spring.utils.Logger"/>
<!-- 配置AOP -->
<aop:config>
    <!-- 配置切入点表达式 id属性用于指定表达式的唯一标识。expression属性用于指定表达式内容
          此标签写在aop:aspect标签内部只能当前切面使用。
          它还可以写在aop:aspect外面，此时就变成了所有切面可用(此时，必须要在aspect标签之前才可以)
      -->
    <aop:pointcut id="pt1" expression="execution(* *..*.*(..))"/>
    <!--配置切面：id为名称，ref为容器中的通知bean -->
    <aop:aspect id="logActive" ref="logger">
        <!-- 配置前置通知：在切入点方法执行之前执行【设置切入点方法一】 -->
        <aop:before method="beforePrintLog" pointcut="execution(* *..*.*(..))"/>
        <!-- 配置后置通知：在切入点方法正常执行之后值。它和异常通知永远只能执行一个【设置切入点方法二】 -->
        <aop:after-returning method="afterReturningPrintLog" pointcut-ref="pt1"/>
        <!-- 配置异常通知：在切入点方法执行产生异常之后执行。它和后置通知永远只能执行一个 -->
        <aop:after-throwing method="afterThrowingPrintLog" pointcut-ref="pt1"/>
        <!-- 配置最终通知：无论切入点方法是否正常执行它都会在其后面执行 -->
        <aop:after method="afterPrintLog" pointcut-ref="pt1"/>
    </aop:aspect>
</aop:config>
```
* 切入点表达式
```
切入点表达式的写法：
	关键字：execution(表达式)
	表达式：
	    访问修饰符  返回值  包名.包名.包名...类名.方法名(参数列表)
	标准的表达式写法：
	    public void com.spring.service.impl.AccountServiceImpl.saveAccount()
	访问修饰符可以省略
	    void com.spring.service.impl.AccountServiceImpl.saveAccount()
	返回值可以使用通配符，表示任意返回值
	    * com.spring.service.impl.AccountServiceImpl.saveAccount()
	包名可以使用通配符，表示任意包。但是有几级包，就需要写几个*.
	    * *.*.*.*.AccountServiceImpl.saveAccount())
	包名可以使用..表示当前包及其子包
	    * *..AccountServiceImpl.saveAccount()
	类名和方法名都可以使用*来实现通配
	    * *..*.*()
	参数列表：
	    可以直接写数据类型：
	        基本类型直接写名称           int
	        引用类型写包名.类名的方式   java.lang.String
	    可以使用通配符表示任意类型，但是必须有参数
	    可以使用..表示有无参数均可，有参数可以是任意类型
	全通配写法：
	    * *..*.*(..)
	实际开发中切入点表达式的通常写法：
	    切到业务层实现类下的所有方法
	        * com.spring.service.impl.*.*(..)
```
* 环绕通知（它是spring框架为我们提供的一种可以在代码中【手动】控制增强方法何时执行的方式。）
```xml
<!-- 配置环绕通知-->
<aop:around method="aroundPrintLog" pointcut-ref="pt1"/>
```
```java
/**
 * 环绕通知
 * 问题：
 *      当我们配置了环绕通知之后，切入点方法没有执行，而通知方法执行了。
 * 分析：
 *      通过对比动态代理中的环绕通知代码，发现动态代理的环绕通知有明确的切入点方法调用，而我们的代码中没有。
 * 解决：
 *      Spring框架为我们提供了一个接口：ProceedingJoinPoint。该接口有一个方法proceed()，此方法就相当于明确调用切入点方法。
 *      该接口可以作为环绕通知的方法参数，在程序执行时，spring框架会为我们提供该接口的实现类供我们使用。
 */
public Object aroundPrintLog(ProceedingJoinPoint pjp) {
    Object rtValue = null;
    try {
        // 得到方法执行所需的参数
        Object[] args = pjp.getArgs();
        System.out.println("环绕通知——前置通知");
        // 明确调用业务层方法（切入点方法）
        rtValue = pjp.proceed(args);
        System.out.println("环绕通知——后置通知");
    } catch (Throwable throwable) {
        System.out.println("环绕通知——异常通知");
        throwable.printStackTrace();
    }finally {
        System.out.println("环绕通知——最终通知");
    }
    return rtValue;
}
```

### 基于注解的AOP
* xml配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 配置spring创建容器时要扫描的包-->
    <context:component-scan base-package="com.spring"/>

    <!-- 配置spring开启注解AOP的支持 -->
    <aop:aspectj-autoproxy/>
</beans>
```
* 切面类
```java
/**
 * Logger class
 * 日志类，用于存放公共代码
 *
 * @author BowenWang
 * @date 2019/07/25
 */
@Component("logger")
/**
 * 表示当前类，是一个切面类
 */
@Aspect
public class Logger {

    /**
     * 设置切点
     */
    @Pointcut("execution(* com.spring.service.impl.*.*(..))")
    private void pt1(){}

    /**
     * 前置通知
     */
    @Before("pt1()")
    public void beforePrintLog() {
        System.out.println("前置通知...");
    }
    /**
     * 后置通知
     */
    @AfterReturning("pt1()")
    public void afterReturningPrintLog() {
        System.out.println("后置通知...");
    }
    /**
     * 异常通知
     */
    @AfterThrowing("pt1()")
    public void afterThrowingPrintLog() {
        System.out.println("异常通知...");
    }
    /**
     * 最终通知
     */
    @After("pt1()")
    public void afterPrintLog() {
        System.out.println("最终通知...");
    }

    /**
     * 环绕通知
     */
    //@Around("pt1()")
    public Object aroundPrintLog(ProceedingJoinPoint pjp) {
        Object rtValue = null;
        try {
            // 得到方法执行所需的参数
            Object[] args = pjp.getArgs();
            System.out.println("环绕通知——前置通知");
            // 明确调用业务层方法（切入点方法）
            rtValue = pjp.proceed(args);
            System.out.println("环绕通知——后置通知");
        } catch (Throwable throwable) {
            System.out.println("环绕通知——异常通知");
            throwable.printStackTrace();
        }finally {
            System.out.println("环绕通知——最终通知");
        }
        return rtValue;
    }
```
* 注意事项
	1. 基于注解的aop，通知在执行顺序上存在问题，由运行结果可以看到最终通知优先于后置通知执行（所以在选择注解还是XML上需要考虑，此外也可以用环绕通知解决上述问题 ）
	```
	【运行结果】
	前置通知...
	保存账户。。。
	最终通知...
	后置通知...
	```
	2. 消除xml——扫描包标签：在主配置类上添加 @ComponentScan("com.spring")
	```xml
	<!-- 配置spring创建容器时要扫描的包-->
  	<context:component-scan base-package="com.spring"/>
	```
	3. 消除xml——开启AOP标签：在主配置类上添加 @EnableAspectJAutoProxy
	```xml
    <!-- 配置spring开启注解AOP的支持 -->
    <aop:aspectj-autoproxy/>
	```

## Spring中的JdbcTemplate
### 添加依赖
```xml
<dependencies>
    <!-- spring核心 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.0.2.RELEASE</version>
    </dependency>

    <!-- jdbc二件套 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>5.0.2.RELEASE</version>
    </dependency>

    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-tx</artifactId>
        <version>5.0.2.RELEASE</version>
    </dependency>

    <!-- 数据库驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.6</version>
    </dependency>
</dependencies>
``` 

### bean.xml示例
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 使用注解配置的时候需要 -->
    <context:component-scan base-package="com.spring"/>

    <!-- 配置DAO -->
    <bean id="accountDao" class="com.spring.dao.impl.AccountDaoImpl">
        <!-- 方式一：通过jdbcTemplate配置
        <property name="jdbcTemplate" ref="jdbcTemplate"/>-->
        <!-- 方式二：直接通过数据源配置 -->
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- 配置jdbcTemplate -->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- 配置数据源 -->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/spring"/>
        <property name="username" value="root"/>
        <property name="password" value="Bow1024"/>
    </bean>
</beans>
```

### 使用JdbcTmplate进行CRUD
* 其中添加、更新、删除均为 update(sql语句,参数列表)，如：
```java
public void updateAccount(Account account) {
    jdbcTemplate.update("update account set name = ?,money = ? where id = ?", account.getName(),
            account.getMoney(), account.getId());
}
```
* 查询都是返回List集合,可以通过get(0)来获取查询返回单一值，且指定返回结果类型需要使用new BeanPropertyRowMapper<XXX>(XXX.class),如：
```java
public Account findByName(String name) {
    List<Account> accounts = jdbcTemplate.query("select * from account where name = ?",
            new BeanPropertyRowMapper<Account>(Account.class), name);
    if (accounts == null) {
        return null;
    }
    if (accounts.size() > 1) {
        throw new RuntimeException("结果不唯一");
    }
    return accounts.get(0);
}
```
* 查询返回一行一列数据，使用queryForObject（SQL语句，XXX.class）前提是可以XXX可转化,如：
```java
public long totalAccount() {
    return jdbcTemplate.queryForObject("select count(*) from account",Long.class);
}
```

### 抽取公共代码JdbcTemplate
* 使用XML配置时：继承JdbcDaoSupport，通过getJdbcTemplate()获取：
```java
public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {
    public Account findById(int id) {
        List<Account> accounts = getJdbcTemplate().query("select * from account where id = ?",
                new BeanPropertyRowMapper<Account>(Account.class), id);
        return accounts == null ? null : accounts.get(0);
    }

    // 其他代码...
}
```
* 使用注解配置时：不继承（因为如果再使用上面的代码，注解不好添加，因为JdbcDaoSupport是别人的类）
```java
@Repository("accountDao2")
public class AccountDaoImpl2 implements AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Account findById(int id) {
        List<Account> accounts = jdbcTemplate.query("select * from account where id = ?",
                new BeanPropertyRowMapper<Account>(Account.class), id);
        return accounts == null ? null : accounts.get(0);
    }

    // 其他代码...
}
```

## 声明式事务控制
### 概念
事务的配置通常是在service层，用来保证业务逻辑上数据的原子性。因为在service层有可能会调用多个dao中的方法操作数据库，这些方法的操作就需要事务来保证其一致性。

### 事务管理相关API
* spring中有一个PlatformTransactionManager接口，该接口叫做事务管理器接口，我们会使用它的两个实现类来完成事务的控制：
	* DataSourceTransactionManager：使用 JDBC 或 myBatis 进行持久化数据时使用。
	* HibernateTransactionManager：使用 Hibernate 进行持久化数据时使用。
* Spring 事务的默认回滚方式是：发生运行时异常时回滚，发生一般性异常时提交。不过，对于一般性异常，我们也可以手工设置其回滚方式。
* 复习一下异常方面的知识：
	* 运行时异常：程序在运行时才会出现的异常，是RuntimeException的子类，例如NullPointerException空指针异常。
	* 一般性异常：即在代码编写时要求必须捕获或抛出的异常，若不处理，则无法通过编译，例如IOException。

### 五个事务隔离级别常量
* 这些常量均是以 ISOLATION_开头。例如 ISOLATION_REPEAT ABLE_READ。

	* DEFAULT：采用 DB 默认的事务隔离级别。MySql 的默认为 REPEATABLE_READ；Oracle默认为 READ_COMMITTED。
	* READ_UNCOMMITTED：读未提交。未解决任何问题。
	* READ_COMMITTED：读已提交。解决脏读，存在不可重复读与幻读。
	* REPEATABLE_READ：可重复读。解决脏读、不可重复读，存在幻读
	* SERIALIZABLE：串行化。解决脏读、不可重复读，幻读的问题，效率低。

### 七个事务传播行为常量
* 事务的传播行为指的是处于不同事务中的方法在相互调用时，执行期间事务的维护情况。如，A 事务中的方法 doSome()调用 B 事务中的方法 doOther()，在调用执行期间事务的维护情况，就称为事务传播行为。事务传播行为是加在方法上的。
* 事务传播行为常量都是以 PROPAGATION_ 开头，例如 PROPAGATION_REQUIRED。

	* REQUIRED
指定的方法必须在事务内执行。若当前存在事务，就加入到当前事务中；若当前没有事
务，则创建一个新事务。这种传播行为是最常见的选择，也是 Spring 默认的事务传播行为。
如该传播行为加在 doOther()方法上。若 doSome()方法在调用 doOther()方法时就是在事
务内运行的，则 doOther()方法的执行也加入到该事务内执行。若 doSome()方法在调用
doOther()方法时没有在事务内执行，则 doOther()方法会创建一个事务，并在其中执行。

	* SUPPORTS
指定的方法支持当前事务，但若当前没有事务，也可以以非事务方式执行。

	* MANDATORY
指定的方法必须在当前事务内执行，若当前没有事务，则直接抛出异常。

	* REQUIRES_NEW
总是新建一个事务，若当前存在事务，就将当前事务挂起，直到新事务执行完毕。

	* NOT_SUPPORTED
指定的方法不能在事务环境中执行，若当前存在事务，就将当前事务挂起。

	* NEVER
指定的方法不能在事务环境下执行，若当前存在事务，就直接抛出异常。

	* NESTED
指定的方法必须在事务内执行。若当前存在事务，则在嵌套事务内执行；若当前没有事务，则创建一个新事务。

### 默认事务超时时限
* 常量 TIMEOUT_DEFAULT 定义了事务底层默认的超时时限，及不支持事务超时时限设置的 none 值。
* 注意，事务的超时时限起作用的条件比较多，且超时的时间计算点较复杂。所以，该值一般就使用默认值即可，默认值是-1。

### 相关依赖(有一些是项目需要的依赖)
```xml
<dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.6</version>
        </dependency>

        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.8.7</version>
        </dependency>
    </dependencies>
```

### 基于XML的事务控制
```xml
<!-- spring中基于XML的声明式事务控制配置步骤
    1、配置事务管理器
    2、配置事务的通知
            此时我们需要导入事务的约束 tx名称空间和约束，同时也需要aop的
            使用tx:advice标签配置事务通知
                属性：
                    id：给事务通知起一个唯一标识
                    transaction-manager：给事务通知提供一个事务管理器引用
    3、配置AOP中的通用切入点表达式
    4、建立事务通知和切入点表达式的对应关系
    5、配置事务的属性
           是在事务的通知tx:advice标签的内部

 -->
<!-- 1、配置事务管理器 -->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>
<!-- 2、配置事务的通知 -->
<tx:advice id="txAdvice" transaction-manager="transactionManager">
    <!-- 配置事务的属性
            isolation：用于指定事务的隔离级别。默认值是DEFAULT，表示使用数据库的默认隔离级别。
            propagation：用于指定事务的传播行为。默认值是REQUIRED，表示一定会有事务，增删改的选择。查询方法可以选择SUPPORTS。
            read-only：用于指定事务是否只读。只有查询方法才能设置为true。默认值是false，表示读写。
            timeout：用于指定事务的超时时间，默认值是-1，表示永不超时。如果指定了数值，以秒为单位。
            rollback-for：用于指定一个异常，当产生该异常时，事务回滚，产生其他异常时，事务不回滚。没有默认值。表示任何异常都回滚。
            no-rollback-for：用于指定一个异常，当产生该异常时，事务不回滚，产生其他异常时事务回滚。没有默认值。表示任何异常都回滚。
    -->
    <tx:attributes>
        <tx:method name="find*" read-only="true" propagation="SUPPORTS"/>
        <tx:method name="transfer"/>
    </tx:attributes>
</tx:advice>
<!-- 3、配置AOP中的通用切入点表达式 -->
<aop:config>
    <!-- 配置切入点表达式-->
    <aop:pointcut id="pt1" expression="execution(* com.spring.service.*.*(..))"/>
    <!--建立切入点表达式和事务通知的对应关系 -->
    <aop:advisor advice-ref="txAdvice" pointcut-ref="pt1"/>
</aop:config>
```

### 基于注解的事务控制
1. 创建相应的配置类
```java
/**
 * SpringConfiguration class
 * 主配置类
 */
@Configuration
@ComponentScan("com.spring")
@Import({JdbcConfig.class, TransactionConfig.class})
@PropertySources({@PropertySource("jdbcConfig.properties")})
@EnableTransactionManagement
public class SpringConfiguration {
}
```
```java
/**
 * JdbcConfig class
 * 数据库相关配置
 */
public class JdbcConfig {

    @Value("${jdbc.driverClass}")
    private String driverClass;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;
    /**
     * 创建JdbcTemplate
     * @param dataSource
     * @return
     */
    @Bean(name = "jdbcTemplate")
    public JdbcTemplate createJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * 创建数据源
     * @return
     */
    @Bean(name = "dataSource")
    public DataSource createDataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(driverClass);
        driverManagerDataSource.setUrl(url);
        driverManagerDataSource.setUsername(username);
        driverManagerDataSource.setPassword(password);
        return driverManagerDataSource;
    }
}
```
```java
/**
 * TransactionConfig class
 * 事务管理配置类
 */
public class TransactionConfig {

    /**
     * 创建事务管理器
     * @param dataSource
     * @return
     */
    @Bean("transactionManager")
    public PlatformTransactionManager createPlatformTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```
2. 对应Service和Dao层实现类进行注解IOC和DI
3. 对需要添加的事务方法上添加注解@Transactional【可以在类上，也可以在方法上】
```java
/**
 * AccountServiceImpl class
 * 账户业务实现类
 */
@Service("accountService")
@Transactional(propagation= Propagation.SUPPORTS, readOnly=true)//只读型事务的配置
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public Account findAccountByName(String name) {
        return accountDao.findAccountByName(name);
    }

    @Override
    //需要的是读写型事务配置
    @Transactional(propagation = Propagation.REQUIRED)
    public void transfer(String outAccount, String inAccount, float money) {
        Account out = accountDao.findAccountByName(outAccount);
        Account in = accountDao.findAccountByName(inAccount);
        out.setMoney(out.getMoney() - money);
        in.setMoney(in.getMoney() + money);
        accountDao.updateAccount(out);
        //int error = 1 / 0;
        accountDao.updateAccount(in);
    }
}
```

### 基于编程式事务控制（了解）
编程式事务：就是直接在代码里手动开启事务，手动提交，手动回滚。优点就是可以灵活控制，缺点就是太麻烦了，太多重复的代码了。
```java
/**
 * 账户的业务层实现类
 * 缺点：
 * 每个方法都要写transactionTemplate.execute(new TransactionCallback<Account>() {...} 
 */
public class AccountServiceImpl implements IAccountService{

    private IAccountDao accountDao;

    private TransactionTemplate transactionTemplate;

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public void setAccountDao(IAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Account findAccountById(Integer accountId) {
      return  transactionTemplate.execute(new TransactionCallback<Account>() {
            @Override
            public Account doInTransaction(TransactionStatus status) {
                return accountDao.findAccountById(accountId);
            }
        });
    }

    @Override
    public void transfer(String sourceName, String targetName, Float money) {
        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                // 调用Dao层...
                return null;
            }
        });

    }
}
```