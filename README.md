# 숏과외
"필요한 문제만 질문하자"
질문하고 싶은 학생과 답변할 수 있는 선생님을 위한 온라인 과외 플랫폼

# Download
[for Android](https://play.google.com/store/apps/details?id=org.softwaremaestro.shorttutoring)

# Key Function
## 1. 수업 예약
- 학생이 질문을 업로드하면 선생님의 질문을 보고, 수업을 제안할 수 있음
- 일정을 자유롭게 조정할 수 있도록 채팅 이용
- 빠르게 답변받기 위한 일반 질문과, 원하는 선생님에게 질문하는 지정 질문이 있음

## 2. 강의실
- 기본적인 필기 기능이 제공되고, 화이트 보드 및 음성을 공유하며 수업 진행
- 수업이 끝난 후에는 학생이 선생님과의 수업을 평가
- 수업 영상은 자동 저장되어 원할 때마다 재시청할 수 있음

# Architecture
[공식 앱 아키텍처 가이드]에 따라 View를 담당하는 Presenter 계층, Data를 담당하는 Data 계층, 비즈니스 로직을 담당하는 Domain 계층으로 구성됩니다.



## Domain Layer
서버 또는 사용자 기기와 Data를 주고 받습니다. Repository, Model(DTO)을 갖습니다.
[공식 앱 아키텍처 가이드]:(https://developer.android.com/topic/architecture?hl=ko)

이미지

## Domain Layer
Presenter 계층과 Data 계층 사이에서 비즈니스 로직을 담당합니다. UseCase, Repository, VO를 갖습니다.
DI를 통해 Data, Presenter 계층에 의존성을 가지지 않습니다.

##Presenter Layer
View를 구성하고, 사용자와 상호작용합니다. MVVM 패턴을 활용했습니다.

# Tech Stack
- Minimum SDK level 26
- [__Coroutines__]((http://zeldahagoshipda.com), __[Flow]__: 처리하는 데에 시간이 오래 걸리는 작업을 비동기 처리합니다.
- __Clean Architecture__
  - __ViewModel, LiveData__: UI와 로직을 분리합니다.
  - __Lifecycle__: Activity/Fragment의 생명주기에 따라 기능을 수행합니다.
- __[Retrofit2,OkHttp3]__: 클라이언트와 서버 사이의 통신을 담당합니다.
- __[Room]__: 로그인 정보, 채팅 메시지 등의 내용을 사용자 기기에 저장합니다.
- __[Hilt]__: 의존성을 주입합니다.
- __[Agora]__: 화이트보드 솔루션입니다. 강의실을 생성하고, 화면과 음성을 공유합니다. 
- __[Socket.IO]__: 학생과 선생님이 수업 일정을 협의할 때 채팅을 이용합니다. 채팅 시에 Socket을 통해 메시지를 주고 받습니다.
- __[ExoPlayer]__: 수업을 복습할 수 있는 동영샹 플레이어를 제공합니다.
- __[Firebase]__: 푸시 메시지 전송, Google Analytics를 통한 이벤트 기록 등을 담당합니다.
- __Navigation, ViewBinding__: 보일러 플레이트 코드를 없애 코드의 가독성을 높입니다.
- __[Glide]__: 이미지 파일을 수신하고 변환하여 View에 적용합니다.
- __[Gson]__: Java Object와 Json Object를 변환합니다.
[Flow]:(https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)
[Retrofit2,OkHttp3]:(https://github.com/square/retrofit)
[Hilt]:(https://dagger.dev/hilt/)
[Agora]:(https://docs.agora.io/en/video-calling/get-started/get-started-sdk?platform=android)
[Socket.IO]:(https://socket.io/docs/v4/tutorial/introduction)
[ExoPlayer]:(https://github.com/google/ExoPlayer)
[Glide]:(https://github.com/google/gson](https://github.com/bumptech/glide)
[Gson]:(https://github.com/google/gson)

# Contributers
|Github Profile|Contact|
|--------|---------|
|[seongyunlee](https://github.com/seongyunlee)|private.seongyun@gmail.com|
|[haechan29](https://github.com/haechan29)|haechan29@naver.com|

# Support
한국정보산업연합회에서 운영하는 __소프트웨어 마에스트로__의 지원을 받아 제작되었습니다.
