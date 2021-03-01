---
layout: post
title:  "log4jdbc를 이용한 SQL Debugging "
categories: DB
tags: [JDBC, Debugging, SQL, log4jdbc]
---
개발시 SQL 을 쉽게 debugging 하기위해 DriverSpy 를 사용한다.  
JDBC driver를 DriverSpy 로 설정하고 실제 사용하는  DB종류에 따라 JDBC URL 을 설정한다.
상세한 로그레벨 설정은 log4j2를 이용해서 한다.

# Requirements
* log4jdbc-log4j2-jdbc4 1.16
* log4j-slf4j-impl 2.8
* log4j-core 2.8

####  JDBC  driver  및 URL 변경
 {% highlight xml %}
<bean id="dataSource.main" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close" >
	<property name="driverClassName" value="net.sf.log4jdbc.sql.jdbcapi.DriverSpy" /> 
	<property name="url" value="jdbc:log4jdbc:oracle:thin:@128.0.0.1:3360:TEST" /> 
	</bean> 
{% endhighlight %}

####  log4jdbc.log4j2.properties 파일생성
java resource 폴더에 파일을 생성한다.
{% highlight properties %}
log4jdbc.spylogdelegator.name = net.sf.log4jdbc.log.slf4j.Slf4jSpyLogDelegator
log4jdbc.dump.sql.maxlinelength=0
{% endhighlight %}

#### log4j2.xml  상세설정
{% highlight xml %}
<?xml version="1.0" encoding="UTF-8"?>
<Configuration>
	<!-- Appender, Layout 설정 -->
	<Appenders>
		<Console name="console" target="SYSTEM_OUT">
			<PatternLayout pattern="[%5p][%d{yyyy-MM-dd HH:mm:ss,SSS}][%C{1}:%4L] %m%n" />
		</Console>
		<RollingFile name="file" fileName="logs/rtas.log"  filePattern="logs/rtas.log.%d{yyyy-MM-dd}" append="false">
			<PatternLayout pattern="[%5p][%d{yyyy-MM-dd HH:mm:ss,SSS}][%C{1}:%4L] %m%n" />
			<Policies>
				<TimeBasedTriggeringPolicy interval="1" modulate="true"/>
			</Policies>
		</RollingFile>
	</Appenders>
	
	<Loggers>
		<Logger name="com.k4m.rtas" level="DEBUG"/>
		<Logger name="org.springframework" level="WARN" />
		<Logger name="jdbc.sqlonly" level="OFF" />
		<Logger name="jdbc.sqltiming" level="INFO"/>
		<Logger name="jdbc.audit" level="WARN" />
		<Logger name="jdbc.resultset" level="OFF" />
		<Logger name="jdbc.resultsettable" level="INFO"/>
		<Logger name="jdbc.connection" level="INFO" />
	
		<Root level="WARN">
			<AppenderRef ref="console" />
			<AppenderRef ref="file" />
		</Root>
	</Loggers>

</Configuration>
{% endhighlight %}

#### 결과 sample

{% highlight plaintext %}
[ INFO][2017-02-01 17:24:37,691][Slf4jSpyLogDelegator: 373] SELECT 
			COUNT(*)	
		 FROM USER
		 WHERE 1=1
 {executed in 8 msec}
[ INFO][2017-02-01 17:24:37,692][Slf4jSpyLogDelegator: 610] 
|---------|
|count(*) |
|---------|
|76       |
|---------|
{% endhighlight %}
