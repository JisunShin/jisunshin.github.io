---
layout: post
title:  "MITM Man-in-the-middle Attack"
category: security
tags: [공격, Public Key]
---

중간자공격은 네트워크 통신구간에서 조작하여 통신내용을 도청하거나 조작하는 공격기법니다.

# 예

![Alice-Mallory-Bob](https://en.wikipedia.org/wiki/File:Man_in_the_middle_attack.svg)
Alice가 Bob에게 통신하려고 한다. 이때 Mallory가 이들의 대화에 끼어들어 엿듣고 Bob에게 잘못된 메시지를 전달하여 공격한다.
  1. Alice "Bob, Public key 줘" -> Mallory : Bob
  2. Alice : Malloy "나 Alice임. Public key 줘" ->  Bob
  3. Bob은 이에 자신의 Public key를 보내준다
     Alice : Mallory <- [Bob의 키] Bob
  4. Mallory는 자신의 Public key를 대신 보낸다
     Alice <- [Mallory의 키] Mallory : Bob
  5. Alice는 Mallory의 키를 이용해 메시지를 암호화하여 Bob에게 보낸다
     Alice "옥상에서 만나자"[Mallory의 키] -> Mallory : Bob
  6. Mallory는 메시지를 열어보고 변조하여 다시 Bob에게 보낸다
     Alice : Mallory "카페에서 만나자"[Bob의 키] -> Bob
  7. Bob은 받은 메세지가 실제로 Alice에게서 온 것으로 생각한다
  
# 방어

1. Authentication

2. Tamper detection
  특정한 상확에서 잠재적 공격의 가능성 감지. 보통의 경우 20초가 걸리는데, 60초 이상 걸리는 경우 제 3자가 있다고 볼 수 있다
  
3. 포렌식 분석
  네트워크 트래픽을 캡쳐하여 분석
  
