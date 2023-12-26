# 숏과외
🙋 **질문**하고 싶은 학생과 **답변**할 수 있는 선생님을 위한 **온라인 과외 플랫폼** 👨‍🏫  
<br/>
# Download
[for Android](https://play.google.com/store/apps/details?id=org.softwaremaestro.shorttutoring) ❤️  
<br/>

# Key Function
<img src="https://github.com/amicably-until-the-end/ShortTutoring/assets/63138511/bc650a6a-c9a4-424e-b000-82bdfd248340" height="430" width=30%>
<img src="https://github.com/amicably-until-the-end/ShortTutoring/assets/63138511/1f241725-e585-4aa2-86b2-6482a6058b9f" height="430" width=30%>
<img src="https://github.com/amicably-until-the-end/ShortTutoring/assets/63138511/0b053b07-a3bc-4aeb-bccf-bde7d2751ea8" height="430" width=30%>  
<br/><br/>

## 1. 수업 예약
- 학생이 질문을 업로드하면 선생님의 질문을 보고, 수업을 제안할 수 있습니다
- 일정을 자유롭게 조정할 수 있도록 **채팅**을 이용합니다
- 빠르게 답변받기 위한 **모든 선생님**께 질문하거나, **원하는 선생님**이 있다면 지정해서 질문할 수 있습니다.  
<br/>

## 2. 강의실
- 화이트 보드 및 음성을 공유하며 수업을 진행합니다
- 수업이 끝난 후에는 학생이 선생님과의 수업을 별점으로 **평가**합니다
- **수업 영상**은 **자동 저장**되어 원할 때마다 다시 시청하며 복습할 수 있습니다  
<br/>

# Architecture
View를 담당하는 Presenter 계층, Data를 담당하는 Data 계층, 비즈니스 로직을 담당하는 Domain 계층으로 구성됩니다.  
구글의 [앱 아키텍처 가이드](https://developer.android.com/topic/architecture?hl=ko)을 따릅니다.  
<br/>
![Clean Architecture-001](https://github.com/amicably-until-the-end/ShortTutoring/assets/63138511/1ed57f51-35b2-4a45-8516-1a754a2f0b1f)


## Domain Layer
서버 또는 사용자 기기와 Data를 주고 받습니다.  
Repository, Model(DTO)을 갖습니다.  
<br/>

## Domain Layer
Presenter 계층과 Data 계층 사이에서 비즈니스 로직을 담당합니다. UseCase, Repository, VO를 갖습니다.  
DI를 통해 Data, Presenter 계층에 의존성을 가지지 않습니다.  
<br/>

## Presenter Layer
View를 구성하고, 사용자와 상호작용합니다.  
MVVM 패턴을 활용했습니다.  
<br/>

# Tech Stack
- Minimum SDK level 26  
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines), [Flow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/): 처리하는 데에 시간이 오래 걸리는 작업을 비동기 처리합니다.  
- Clean Architecture
  - ViewModel, LiveData: UI와 로직을 분리합니다.  
  - Lifecycle: Activity/Fragment의 생명주기에 따라 기능을 수행합니다.  
- [Retrofit2, OkHttp3](https://github.com/square/retrofit): 클라이언트와 서버 사이의 통신을 담당합니다.  
- Room: 로그인 정보, 채팅 메시지 등의 내용을 사용자 기기에 저장합니다.  
- [Hilt](https://dagger.dev/hilt/): 의존성을 주입합니다.  
- [Agora](https://docs.agora.io/en/video-calling/get-started/get-started-sdk?platform=android): 화이트보드 솔루션입니다. 강의실을 생성하고, 화면과 음성을 공유합니다.  
- [Socket.IO](https://socket.io/docs/v4/tutorial/introduction): 학생과 선생님이 수업 일정을 협의할 때 채팅을 이용합니다. 채팅 시에 Socket을 통해 메시지를 주고 받습니다.  
- [ExoPlayer](https://github.com/google/ExoPlayer): 수업을 복습할 수 있는 동영샹 플레이어를 제공합니다.  
- Firebase: 푸시 메시지 전송, Google Analytics를 통한 이벤트 기록 등을 담당합니다.  
- Navigation, ViewBinding: 보일러 플레이트 코드를 없애 코드의 가독성을 높입니다.  
- [Glide](https://github.com/bumptech/glide): 이미지 파일을 수신하고 변환하여 View에 적용합니다.  
- [Gson](https://github.com/google/gson): Java Object와 Json Object를 변환합니다.
</br>

# Contributers
|Github Profile|Contact|
|--------|---------|
|[seongyunlee](https://github.com/seongyunlee)|private.seongyun@gmail.com|
|[haechan29](https://github.com/haechan29)|haechan29@naver.com|
</br>

# Support
한국정보산업연합회에서 운영하는 [소프트웨어 마에스트로](https://www.swmaestro.org/sw/main/main.do)의 지원을 받아 제작되었습니다.  
</br>
