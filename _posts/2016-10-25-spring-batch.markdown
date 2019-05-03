---
layout: post
title:  "Spring Batch"
category: spring
tags: spring batch
---

Spring-Batch Framework 기반 batch 실행 환경을 구성하고자 하였다. 

# 요구사항
 * schedule 기능(=> Quartz)
 * 원격호출기능(=>Restful)
 * Batch 수행결과를 조회
 
 이와 같은 요구사항을 수행할 수 있도록 Spring-Batch Framework을 이용하여 Job을 구성하고 기존 서비스와는 별도의 applicateion server 를 띄워서 의존성을 최소화
 Application Server에서 직접 Quartz 로 스케줄링 기능을 제공하여 OS 등의 환경독립적으로 구성
 Restful 서비스를 접목해서 원격호출기능 제공하도록 구성함. Batch 수행결과는 spring-batch에서 기본적으로 제공하는 DB Repository 기능 활용

 

# 환경
* egovframework.rte.bat.core 3.1.0
* spring-batch-core 2.1.9
* spring-web-mvc 3.2.9

# Quartz

# Job

#### 구조

# Step

#### 종류

1. Chunk

    대량의 데이터를 읽어서(ItemReader) 처리/변환하고(ItemProcessor 생략가능) 저장하는(ItemWriter) 전형적인 배치작업유형에 적합
    xml 설정을 이용해서 대부분의 작업이 설정 가능하고 chunk 단위를 설정으로 세팅가능.
   
     
    1. ItemReader
    2. ItemProcessor
    3. ItemWriter



2. MethodInvoke

    POJO 를 활용하는 방법. 비정형적인 작업에 적합. reader-processor-writer 의 구조로 정의되기 어려운 작업은 POJO(* MVC 패턴의 model 개념과 유사)로 작성하고 해당 Method만 호출한다.

    단점은 모든 작업이 하나의 step 으로 처리되므로 결과를 조회했을때 readCount, Rollback Count  등이 모두 0 으로 표시되며 정상종료시 Commit Count = 1 로 표시됨.
    spring-batch 레벨의 상세한 오류처리 등이 불가능하고 자체적으로 처리해야함.


#### 기타 기능

1. step id는 전체 project에서 unique 해야한다!! 

2. SingleInstanceJobListener

    하나의 job을 동일한 parameter 로 동시에 수행되는 것을 막기 위해 job명 + parameter 를 instance 에 등록하고 이미 수행중인 job이 있는지 체크하여 stop한다.
    Spring-Batch 기본기능은 동일한 parameter 한번이라도 수행되었으면 실행이 불가능하다. 따라서 주기적으로 실행되는 schedule 이 가능하게 하기 위해 timestamp(현재시간) parameter 를 공통적으로 추가하였다
    
    그러나 Quartz 가 아닌 Restful을 통해 사용자가 job 수행시 동시에 multi job 이 수행되어서는 곤란하다. 
    
    따라서 매번 job 수행시 호출되는 JobListener 기능을 이용해서 custom 으로 동시수행 금지처리하였다.
   
3. JobParameterListener

# Restful
