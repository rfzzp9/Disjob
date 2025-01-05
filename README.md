# DisJob



상권분석을 기반으로 예비 소상공인을 위한 맞춤형 점포를 추천하는 서비스입니다.

실제 존재하는 어플리케이션인 "호갱노노"의 UI를 벤치마킹했습니다.


## 📌 주요 기능


- 사용자에게 적합한 점포와 부가 정보(청소 및 시공업체, 소상공인 관련 정책) 제공
- 사용자 커뮤니티 및 스크랩 기능


## 🛠 기술 스택


<span style="font-size:100%">FRONTEND</span>  


<span style="font-size:30%">제가 맡은 파트만 기술했습니다.</span>  


|구분|Skill|
|------|---|
|Language|Java|
|Networking|Volley|
|Design|Figma|
|ETC|SharedPreferences|


## 📌 개발 내용


- 사용자 인증 및 계정 관리
    - Firebase Authentication을 사용해 이메일 기반 회원가입/로그인 구현
- 메인 지도 화면
    - Kakao Maps API를 활용하여 지도 화면 생성
    - 지도 이동시 새로운 상권 데이터를 Firebase에서 비동기로 불러와 해당 영역의 점포 마커 표시
- 점포 상세 정보 화면
    - 마커 클릭 시 점포의 상세 정보를 Firebase에서 로드해 표시
- 필터 및 검색 기능
    - 카테고리 선택에 따라 마커가 실시간으로 변경되도록 구현
- 데이터 관리
    - Firebase Realtime Database를 사용하여 상권 및 점포 데이터 관리
- 커뮤니티 기능
    - Firebase를 활용해 글쓰기, 댓글 달기, 좋아요 기능 구현
- UI/UX 개선
    - Figma로 UI 프로토타입 설계 및 Android XML로 구현


## 📌 성장 경험


### *지도 화면에서 대량 데이터 로딩으로 인한 앱 멈춤 현상 해결*

메인 화면에서 사용자가 지도를 이동할 때마다 해당하는 지역의 마커 데이터를 로드합니다. 이 과정에서 앱이 멈추는 현상을 겪었습니다.

이 문제는 지도 이동 시 데이터를 로드하고, 적합한 마커를 선별하며, 화면에 표시하는 작업이 모두 메인 스레드에서 처리되어 병목 현상이 발생한 것이 원인이었습니다.

이를 해결하기 위해 **ExecutorService**를 활용하여 비동기 처리를 구현했습니다. 데이터 로드와 가공 작업을 **ExecutorService**의 백그라운드 스레드에서 수행하고, 작업이 완료된 후에는 **Handler**를 사용해 메인 스레드에서 UI를 업데이트했습니다.

또한 불필요한 데이터 처리를 줄이고 성능을 개선하기 위해 지도에서 보이는 화면의 경계를 계산하여 해당 영역의 데이터만 서버에 요청하도록 로직을 수정했습니다.

앱 멈춤 현상은 완전히 해결되었고, 데이터 로드 속도는 60% 이상 단축되었습니다. 이 개선 작업을 통해 안드로이드 개발에서 중요한 성능 최적화 기법을 체득했습니다.


### *사용자 중심의 UI/UX 완성도 향상*

개발자로서 서비스를 완성했지만, 사용자 입장에서 보면 부족한 점이 많다는 것을 알게 되었습니다.

특히 이 서비스는 기존의 상권 분석 서비스에 비해 더 사용하기 쉬운 경험을 제공하는 것이 목표이기에, 사용자 경험에 더욱 집중해야 한다는 필요성을 느꼈습니다.

그리하여 사용자 테스트를 통해 직관적이지 않은 버튼, 메뉴 등의 UI 요소를 사용자가 쉽게 접근할 수 있도록 재배치했습니다. 

이 과정에서 기술적 완성도뿐 아니라 사용자 경험의 완성도가 서비스의 성공에 중요한 요소임을 깊이 이해하게 되었습니다.


## 📌 서비스 화면


(시연 영상 - *https://youtube.com/shorts/SlzXLgIpZ40*)


<div align="center">

| ![image1](https://github.com/user-attachments/assets/f99b49be-a73f-471d-8470-2a69ff658e22) | ![image2](https://github.com/user-attachments/assets/6e091da8-0050-4c98-9af3-f310f697a928) |
|:--------------------------------------------------------:|:--------------------------------------------------------:|
| **스클래시 화면**                                  | **메인 화면**                                  |
| ![image3](https://github.com/user-attachments/assets/6eecc877-3f07-4a90-a840-d81ae274d711) | ![image4](https://github.com/user-attachments/assets/7a126a54-4d8e-4705-ada5-eaa9c815c28a) |
| **점포 추천 상세 화면**                                  | **스크랩 화면**                                  |
| ![image5](https://github.com/user-attachments/assets/d9d603ce-10e4-4d49-977c-0eb8badeebd0) | ![image6](https://github.com/user-attachments/assets/8ea4e824-4914-4450-ad4c-ca19fd9c14df) |
| **게시판 화면**                                  | **정부 정책 게시판 화면**                                  |

</div>


