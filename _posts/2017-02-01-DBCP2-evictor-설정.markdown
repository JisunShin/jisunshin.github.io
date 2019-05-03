---
layout: post
title:  "DBCP2 evictor 설정"
category: dbcp
tags: [apache, commons]
---
network 설정등에 따라 일정시간이 지나면 connection 이 끊어지는 경우 주기적으로 idle  상태의 db connection pool 에 대한 정리가 필요하다.

# 참고
* commons-dbcp2 2.1.1

####  common-context.xml
timeBetweenEvictionRunsMillis 주기로 evictor 체크
minEvictableIdleTimeMillis 이상 idle 상태인 connection 을 close 처리함

 {% highlight xml %}
<bean id="dataSource.main" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close" >
	<property name="driverClassName" value="${jdbc.main.driver}" /> 
	<property name="url" value="${jdbc.main.url}" /> 
	<property name="username" value="${jdbc.main.username}" /> 
	<property name="password" value="${jdbc.main.password}" /> 
	<property name="maxTotal" value="5" />
	<property name="maxIdle" value="5" />
	<property name="maxWaitMillis" value="30000" />
	<property name="validationQuery" value="${jdbc.main.poolPingQuery}" />
	<property name="testOnBorrow" value="false" />
	<property name="timeBetweenEvictionRunsMillis" value="120000"/>
	<property name="minEvictableIdleTimeMillis" value="120000"/>
	<property name="testWhileIdle" value="true" />
	<property name="maxOpenPreparedStatements" value="5" />
	<property name="poolPreparedStatements" value="true" />
</bean> 
{% endhighlight %}
	
#### evictor 실행여부 확인 
{% highlight text %}
[ INFO][2017-02-01 16:19:26,863][Slf4jSpyLogDelegator: 373] SELECT 1 FROM DUAL
 {executed in 6 msec}

{% endhighlight %}
  
