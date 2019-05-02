---
layout: post
title:  "spring에서 Resource Versioning하기"
category: 개발팁
tags: [static, resource, cache, spring]
---
js파일과 같은 static resource는 cache를 적용하는 것이 유리하다.
Cache-Control: max-age=31536000‬

* ETAG를 이용할수 도 있음

그래서 Cache 를 사용하도록 하면 실제로 내용이 변경되었을때도 사용자 브라우저에서 cache를 사용하고 변경된 파일을 읽어오지 않는 경우가 있다.
이 문제를 해결하기 위한 방법을 spring에서 제공한다. Static Resource의 versioning기능을 제공한다.

1. ResourceHandlerRegistry 세팅
   * cache-control 세팅
   * Contents에 따라 Version을 생성
   * http://localhost:8080/resource/foo.js -> http://localhost:8080/resource/foo-46944c7e3a9bd20cc30fdc085cae46f2.js

2. JSP 파일내에서 URL이 변경되도록한다.
   * ResourceUrlEncodingFilter 를 등록한다.
   * Static resource를 link 할때 JSTL을 이용해서 URL Link가 version을 포함하도록 한다.

3. CSS파일내에서의 Link를 자동으로 변경

### 소스참고
{% highlight java %}
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
          .addResourceHandler("/resources/**")
          .addResourceLocations("/resources/")  //실제 위치 src/main/webapp/resources 
          .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))  //cache-control setting
          .resourceChain(false)
          .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**")) // resouce 파일에 버전을 생성한다.
          .addTransformer(new CssLinkResourceTransformer());  //CSS파일내에서의 Link를 자동으로 변경
    }
    
    @Bean
    public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
        return new ResourceUrlEncodingFilter();
    }
}
{% endhighlight %}


{% highlight jsp %}
<link href="<c:url value="/resources/myCss.css" />" rel="stylesheet">
{% endhighlight  %}
