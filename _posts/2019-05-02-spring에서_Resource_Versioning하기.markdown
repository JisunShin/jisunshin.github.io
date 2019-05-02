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
이 문제를 해결하기 위한 방법을 spring에서 제공한다. Resource 별로 versionging기능을 제공한다.

## ResourceHandlerRegistry 세팅

<highlight>

</highlight>

## JSTL을 이용해서 reousrce의 URL이 변경되도록 한다

## Filter 등록


## CSS파일내에서의 Link를 자동으로 변경


