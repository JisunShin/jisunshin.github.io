---
layout: post
title:  "Jenkins로 Tomcat8 Deploy 하기"
categories: CI
---

# 환경
1. Jenkins 2.19.1
2. Deploy to container Plugin 1.10
3. tomcat 8.5.6 (Windows64bit)

# 설정

## Jenkins
1. 빌드 후 조치에서 'Deploy war/war to a container' 선택
   * WAR/EAR files = target/*.war  (default directory는 해당 빌드의 기본 workspace)
   * context path = /
   * Container  = Tomcat 8 이 없으므로 Tomcat 7.x 선택
   * Tomcat URL : http://127.0.0.1:8001


## Tomcat
1. conf/tomcat-users.xml 에 manager App 사용자를 추가한다
    {% highlight xml %}
        <tomcat-users>
            <user username="tomcat" password="비밀번호" roles="manager-gui"/>
           <user username="jenkins" password="비밀번호" roles="manager-script"/>
        </tomcat-users>
    {% endhighlight %}
    
2. manager context 의 webapps/manager/META-INF/context.xml 수정

    `<Valve className="org.apache.catalina.valves.RemoteAddrValve" ...>` 구문을 보면 manager context 접근권한을 ip설정을 통해 제한하고 있음 '127...'로만 접근가능
    Jenkins 와 Tomcat의 IP가 다른경우 이 구문을 comment 한다
   