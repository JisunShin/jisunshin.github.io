---
layout: post
title:  "Telegram bot 으로 알림메시지 보내기"
date:   2017-2-23
categories: Telegram bot
---
서비스에서 사용자에게 알림메시지를 발송해야하는경우 기존에는 문자메시지를 많이 이용하였으나 비용등의 문제로 구축의 문제가 생긴다.
telegram을 이용하면 무료로 알림을 보낼수 있고 메시지 수신기능을 이용하면 더욱 발전된 서비스도 가능하다.


#### 유사한 알림 기능 비교
Telegrame  알림   :  카카오톡 플러스  친구톡
야간발송제한없음 : 야간발송제한없음
비용없음 : 사업자만 사용가능. 비용발생

카카오톡 친구톡 참고 [페이지 이동](https://bizmessage.kakao.com/)


#### Telegram BOT 만들기
@BotFather  는  telegram bot을 만들어주는 bot이다.
1.  @BotFather 채팅창만들기
telegram에서 연락처 ' @BotFather' 로 검색해서 채팅창을 만들다
2.  Bot을 만든다
* /start
* /newbot
     * name 입력 :  이름을 입력한다(예,알림봇)
	 * username 입력 :  'XXXbot' 으로 끝나야한다. (예, eCrossAttentionBot)
* 생성완료후 token정보를 알려준다. token정보를 보관한다.
     예) 1324251:AHWdfokjskWsi23kWbxlijdflkAIWKDJfok9
3. Bot 정보조회
URL을 조회하면 생성된 Bot의 ID등 정보를 조회할  수있다.
{% highlight plaintext %}
 https://api.telegram.org/bot<token>/getMe
{% endhighlight %}


#### Telegram BOT 이용해서 메시지보내기
채널을 생성한 후 그 채널에 메시지를 보낸다

1. 채널만들기
telegram  채팅방 > 새채널 을 선택한다.
* 채널명 : MyTestChannel
* 속성: 비공개 또는 공개
* 채널관리자로  @eCrossAttentionBot  추가
 
 2. 채널ID 알아내기
채널의  ID를 알기위해  채널정보를 조회한다. 단, 채널속성이 비공개인 경우에는 정보가 표시되지 않는다.
{% highlight plaintext %}
  https://api.telegram.org/bot<token>/getChat?chat_id=@MyTestChannel
{% endhighlight %}

3.  비공개채널의 채널ID 알아내기
	* telegram 채널채팅창에서 테스트용 메시지를 보낸다.
	* Bot의 수신메시지를 조회한다.
	 {% highlight json %}
	    {"ok":true,"result":[{"update_id":931155568, "channel_post":{"message_id":4,"chat":{"id":-100160445321521,"title":"eCrossTest","type":"channel"},"date":1488162346,"text":"\uc548\ub155\ud558\uc138\uc694"}}]}
		{% endhighlight %}
		
	* chatid : 예) -100160445321521

4. 해당 채팅창에 메시지 보내기
{% highlight plaintext %}
  https://api.telegram.org/bot<token>/sendMessage?chat_id=<chatId>&text=메시지를보냅니다
  {"ok":true,"result":{"message_id":5,"chat":{"id":<chatId>,"title":"eCrossTest","type":"channel"},"date":1488162612,"text":"\uba54\uc2dc\uc9c0\ub97c\ubcf4\ub0c5\ub2c8\ub2e4"}}
{% endhighlight %}

* 해당채널에 관리자로 추가되지 않은경우 오류메시지
{% highlight json %}
 {"ok":false,"error_code":403,"description":"Forbidden: bot is not a member of the channel chat"}
{% endhighlight %}
