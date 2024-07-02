# 연금복권 선택&구매 서비스(Server)

연금복권 번호를 선택하고, 티켓을 구매할 수 있는 서비스

## Front
https://github.com/encore-full-stack-5/DH_lottery

</br>

## 사용 기술

#### 서버 구축
![Spring-Boot](https://img.shields.io/badge/spring--boot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)


#### 데이터베이스
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)

#### CI/CD
![Jenkins](https://img.shields.io/badge/jenkins-%232C5263.svg?style=for-the-badge&logo=jenkins&logoColor=white)
![Google Cloud](https://img.shields.io/badge/GoogleCloud-%234285F4.svg?style=for-the-badge&logo=google-cloud&logoColor=white)
![Kubernetes](https://img.shields.io/badge/kubernetes-%23326ce5.svg?style=for-the-badge&logo=kubernetes&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)


#### 그 외 프레임워크
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)


## Diagram
</br>

### Architecture Diagram
![구매서버](https://github.com/encore-full-stack-5/NODAJI_Pension_Buy_BE/assets/92596263/59e86811-de82-4f4a-8e08-898e8b6ff300)

</br>

### Data Flow Diagram
![구매 DFD](https://github.com/encore-full-stack-5/NODAJI_Pension_Buy_BE/assets/92596263/562a7a70-90a2-4d72-845a-c9816695629a)

</br>

## Trouble Shooting
### 동시성 처리
연금복권 특성상 한 장의 티켓밖에 구매하지 못하기 때문에 동시에 구매 요청이 들어오는 동시성 문제를 해결해야 했습니다.

Synchronized를 사용하기엔 서버를 여러 개 띄울 경우 효과가 없고, 성능도 좋지 않았기 때문에 단기 메모리를 사용하는 redis를 사용하여

분산락을 통해서 동시성을 처리했습니다.


### 구매 속도 이슈
MSA로 구성한 프로젝트이기에 선택&구매 서비스와 당첨 매칭 서비스가 나뉘어져 있습니다.

초기 아키텍처는 구매가 진행된 경우 Feign 요청을 통해 매칭 서버로 구매한 티켓 리스트를 보내주고, 매칭 서버의 DB에 결과 정보를 포함해 DB에 저장하고,

구매 서버의 DB에는 결과 정보를 제외하고 DB에 저장해주었습니다.

이 과정에서 동시성 처리와 Feign 요청, DB 저장까지 모두 이루어져야했기에 속도도 느리고 데이터의 중복도 생겼습니다.



![10초](https://github.com/encore-full-stack-5/NODAJI_Pension_Buy_BE/assets/92596263/7cc74bc2-0433-4fe5-b103-bd3a1aa7cd89)

이 문제를 해결하기 위해서 Feign 요청을 제거하고, 두 개의 서버를 하나의 데이터베이스를 바라보게 수정하였습니다.

그 결과 속도 문제와 데이터 중복 문제를 모두 해결하였습니다.

![111밀리초](https://github.com/encore-full-stack-5/NODAJI_Pension_Buy_BE/assets/92596263/0954a230-8355-4e3a-a0da-b61a3f80909e)
