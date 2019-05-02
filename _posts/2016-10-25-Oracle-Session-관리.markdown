---
layout: post
title:  "Oracle Session 관리용 Query"
categories: oracle
---

#### Session 현황
{% highlight sql %}
 SELECT *
   FROM V$SESSION
{% endhighlight %}

#### Slow쿼리 찾기
{% highlight sql %}
 SELECT ROWNUM NO
 , PARSING_SCHEMA_NAME
 , to_char(ELAPSED_TIME/(1000000 * decode(executions,null,1,0,1,executions)),999999.99 ) 평균실행시간
 , executions 실행횟수
 , SQL_TEXT 쿼리 
 , SQL_FULLTEXT
 FROM V$SQL
 WHERE  LAST_ACTIVE_TIME > SYSDATE-(1/24*2)
 -- AND LAST_ACTIVE_TIME  BETWEEN  to_Date('20111226163000','YYYYMMDDHH24MISS') AND to_Date('20111226170000','YYYYMMDDHH24MISS')
 AND ELAPSED_TIME >= 1 * 1000000 * decode(executions,null,1,0,1,executions)
 ORDER BY 평균실행시간 DESC, 실행횟수 DESC   
{% endhighlight %}


#### 락걸린 테이블 확인 
{% highlight sql %}
SELECT  DO.OBJECT_NAME, DO.OWNER, DO.OBJECT_TYPE, DO.OWNER,
        VO.XIDUSN, VO.SESSION_ID, VO.LOCKED_MODE
FROM    V$LOCKED_OBJECT VO, DBA_OBJECTS DO
WHERE   VO.OBJECT_ID = DO.OBJECT_ID;
{% endhighlight %}

#### 락발생 사용자와 SQL, OBJECT 조회
{% highlight sql %}
SELECT DISTINCT X.SESSION_ID, 
       A.SERIAL#, 
       D.OBJECT_NAME, 
       A.MACHINE, 
       A.TERMINAL,
       A.PROGRAM, 
       B.ADDRESS, 
       B.PIECE, 
       B.SQL_TEXT
FROM V$LOCKED_OBJECT X, V$SESSION A, V$SQLTEXT B, DBA_OBJECTS D
WHERE X.SESSION_ID = A.SID 
  AND X.OBJECT_ID = D.OBJECT_ID 
  AND A.SQL_ADDRESS = B.ADDRESS 
ORDER BY B.ADDRESS, B.PIECE;
{% endhighlight %}

#### 현재 접속자의 SQL 분석
{% highlight sql %}
SELECT DISTINCT A.SID, 
       A.SERIAL#,
       A.MACHINE, 
       A.TERMINAL, 
       A.PROGRAM,
       B.ADDRESS, 
       B.PIECE, 
       B.SQL_TEXT
FROM  V$SESSION A, V$SQLTEXT B
WHERE A.SQL_ADDRESS = B.ADDRESS
ORDER BY A.SID, A.SERIAL#, B.ADDRESS, B.PIECE
{% endhighlight %}

#### 락 세션 죽이기
{% highlight sql %}
  SELECT A.SID,   
         A.SERIAL#
  FROM V$SESSION A,  
       V$LOCK B,
       DBA_OBJECTS C
 WHERE A.SID = B.SID
   AND B.ID1 = C.OBJECT_ID
   AND B.TYPE = 'TM'
   AND C.OBJECT_NAME = '테이블명';
   
   alter system kill session 'sid, serial#';
   {% endhighlight %}
   
#### 세션별 메모리 사용량
{% highlight sql %}
  select a.sid, a.username, substr(a.program, 1, 25) as pgm, a.terminal,
  max(decode(c.name, 'session pga memory', trunc(value/1000)||'K', 0)) pga,
  max(decode(c.name, 'session uga memory', trunc(value/1000)||'K', 0)) uga, 
  max(decode(c.name, 'session pga memory max', trunc(value/1000)||'K', 0)) pga_max,
  max(decode(c.name, 'session uga memory max', trunc(value/1000)||'K', 0)) uga_max 
  from v$session a, v$sesstat b, v$statname c
  where a.sid = b.sid
  and b.statistic# = c.statistic#
  and c.name like 'session%'
  group by a.sid, a.username, substr(a.program, 1, 25), a.terminal;
{% endhighlight %}


#### 총 메모리 사용량
{% highlight sql %}
  select 'PGA, UGA session memory SUM:' as sum,
  sum(decode(c.name, 'session pga memory', trunc(value/1000),0))||'K' pga_sum, 
  sum(decode(c.name, 'session uga memory', trunc(value/1000),0))||'K' uga_sum, 
  sum(decode(c.name, 'session pga memory max', trunc(value/1000), 0))||'K' pga_m_sum, 
  sum(decode(c.name, 'session uga memory max', trunc(value/1000), 0))||'K' uga_m_sum  
  from v$session a, v$sesstat b, v$statname c
  where a.sid = b.sid
  and b.statistic# = c.statistic#
  and c.name like 'session%';
{% endhighlight %}
