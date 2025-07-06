# KT-Library 모노레포

> 목차
> - [📌 프로젝트 소개](#프로젝트-소개)
> - [👩‍👩‍👧‍👧 팀원 소개](#팀원-소개)
> - [✏️ 주요 기능](#주요-기능)
> - [🔗 링크 모음](#링크-모음)
> - [📡 API 명세](#api-명세)
> - [🚩 시작 가이드](#시작-가이드)
> - [🖥️ 기술 스택](#기술-스택)

<br>

## 프로젝트 소개

KT-Library는 마이크로서비스 아키텍처로 구성된 도서 관리 시스템입니다. 이 저장소는 백엔드 마이크로서비스, API 게이트웨이, 프론트엔드를 포함하는 모노레포 형태로 관리됩니다.

### 서비스 소개
KT-Library는 다음과 같은 주요 서비스를 제공합니다:
*   **백엔드 마이크로서비스**: `point`, `author`, `customer`, `manuscript`, `ai`, `platform` 등 도서 관리 시스템의 핵심 비즈니스 로직을 담당합니다.
*   **API 게이트웨이 (Spring Gateway)**: 모든 마이크로서비스로의 요청을 라우팅하고, 공통 기능을 처리합니다.
*   **프론트엔드**: React 기반의 사용자 인터페이스를 제공하여 도서 검색, 관리 등의 기능을 수행합니다.

<br>

## 팀원 소개

|        | 이성훈                                                      | 김민수                                                    | 박소연                                                     | 배소정                                                   | 장준혁                                                       | 조승빈                                                         |
|--------|----------------------------------------------------------|--------------------------------------------------------|---------------------------------------------------------|-------------------------------------------------------|-----------------------------------------------------------|-------------------------------------------------------------|
| 역할     | 조장, 작가 관리 서비스 개발                                         | AI 서비스 개발                                              | 플랫폼 서비스 개발                                              | 포인트 서비스 개발                                            | 집필 관리 서비스 개발                                              | 고객 관리 서비스 개발                                                |
| E-Mail | p.plue1881@gmail.com                                     | minsue9608@naver.com                                   | gumza9go@gmail.com                                      | bsj9278@gmail.com                                     | kalina01255@naver.com                                     | benscience@naver.com                                        |
| GitHub | https://github.com/NextrPlue                             | https://github.com/K-Minsu                             | https://github.com/sorasol9                             | https://github.com/BaeSJ1                             | https://github.com/angrynison                             | https://github.com/changeme4585                             |
|        | <img src="https://github.com/NextrPlue.png" width=100px> | <img src="https://github.com/K-Minsu.png" width=100px> | <img src="https://github.com/sorasol9.png" width=100px> | <img src="https://github.com/BaeSJ1.png" width=100px> | <img src="https://github.com/angrynison.png" width=100px> | <img src="https://github.com/changeme4585.png" width=100px> |


<br>

## 주요 기능

*   **도서 관리**: 도서 정보 등록, 조회, 수정, 삭제
*   **사용자/저자 관리**: 사용자 및 저자 정보 관리
*   **포인트/원고 관리**: 포인트 및 원고 관련 기능
*   **AI 서비스 연동**: (AI 서비스의 구체적인 기능이 있다면 추가)
*   **API 게이트웨이**: 서비스 라우팅 및 통합 API 제공
*   **프론트엔드**: 사용자 친화적인 웹 인터페이스

<br>

## 링크 모음

*   **모델**: [www.msaez.io/#/98487008/storming/3a2cbc79628d3b4fe6cf3c4308a39b66](www.msaez.io/#/98487008/storming/3a2cbc79628d3b4fe6cf3c4308a39b66)

<br>

## API 명세

API 게이트웨이는 `8088` 포트에서 실행되며, `httpie` 또는 다른 API 클라이언트를 사용하여 테스트할 수 있습니다.

*   **point**
    ```bash
    http :8088/points id="id" point="point" createAt="createAt" updateAt="updateAt" customerId="customerId"
    ```
*   **author**
    ```bash
    http :8088/authors id="id" email="email" name="name" introduction="introduction" isApproved="isApproved" createdAt="createdAt" updatedAt="updatedAt"
    ```
*   **customer**
    ```bash
    http :8088/customers id="id" name="name" email="email" isKtUser="isKTUser" createdAt="createdAt" updatedAt="updatedAt"
    http :8088/subsciptions id="id" isValid="isValid" startDate="startDate" endDate="endDate" createdAt="createdAt" updatedAt="updatedAt"
    ```
*   **manuscript**
    ```bash
    http :8088/manuscripts id="id" manuscriptTitle="manuscriptTitle" manuscriptContent="manuscriptContent" authorId="authorId" authorName="authorName" authorIntroduction="authorIntroduction" createdAt="createdAt" updatedAt="updatedAt"
    ```
*   **ai**
    ```bash
    http :8088/books id="id" summary="summary" coverUrl="coverUrl" bookUrl="bookUrl" createdAt="createdAt" updatedAt="updatedAt" manuscriptTitle="manuscriptTitle" manuscriptContent="manuscriptContent" authorId="authorID" authorName="authorName" introduction="introduction" category="category" price="price"
    ```
*   **platform**
    ```bash
    http :8088/bookShelves id="id" title="title" category="category" isBestSeller="isBestSeller" viewCount="viewCount" summary="summary" coverUrl="coverUrl" fileUrl="fileUrl" authorId="authorId" price="price" authorName="authorName" introduction="introduction"
    ```

<br>

## 시작 가이드

### 사전 준비 사항

서비스를 실행하기 전에 다음 도구들이 설치되어 있는지 확인하십시오:
*   Docker 및 Docker Compose
*   Node.js (Docker를 사용하지 않고 프론트엔드 개발/빌드 시)
*   Maven (Docker를 사용하지 않고 백엔드 개발/빌드 시)

### Docker Compose로 모든 서비스 실행하기

모든 마이크로서비스(백엔드, 게이트웨이, 프론트엔드) 및 Kafka를 시작하려면:

1.  **Docker 이미지 빌드 및 서비스 시작:**
    ```bash
    docker-compose -f build-docker-compose.yml up --build
    ```
    이 명령어는 필요한 모든 Docker 이미지를 빌드한 다음 `build-docker-compose.yml`에 정의된 서비스를 시작합니다.

2.  **애플리케이션 접속:**
    모든 서비스가 실행되면 브라우저에서 프론트엔드 애플리케이션에 접속할 수 있습니다:
    ```
    http://localhost:8088
    ```

### 개별 서비스 실행하기 (개발용)

Docker Compose 외부에서 개발 또는 디버깅을 위해 개별 서비스를 실행해야 하는 경우:

#### 백엔드 마이크로서비스 실행
각 마이크로서비스 디렉토리 내의 `README.md` 파일을 참조하여 특정 지침을 확인하십시오:
*   `point`
*   `author`
*   `customer`
*   `manuscript`
*   `ai`
*   `platform`

#### API 게이트웨이 실행 (Spring Gateway)
```bash
cd gateway
mvn spring-boot:run
```

#### 프론트엔드 실행
```bash
cd frontend
npm install
npm start
```

### 필수 유틸리티

다음 유틸리티는 개발 및 운영에 유용할 수 있습니다:

*   **httpie** (curl / POSTMAN 대체 도구) 및 네트워크 유틸리티
    ```bash
    sudo apt-get update
    sudo apt-get install net-tools
    sudo apt install iputils-ping
    pip install httpie
    ```

*   **kubernetes 유틸리티 (kubectl)**
    ```bash
    curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
    sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
    ```

*   **aws cli (aws)**
    ```bash
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
    unzip awscliv2.zip
    sudo ./aws/install
    ```

*   **eksctl**
    ```bash
    curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
    sudo mv /tmp/eksctl /usr/local/bin
    ```

<br>

## 기술 스택

KT-Library 프로젝트는 다음과 같은 기술 스택을 활용합니다:

### 백엔드 (Spring Boot Microservices)
*   **언어**: Java
*   **프레임워크**: Spring Boot
*   **빌드 도구**: Maven
*   **메시징**: Kafka

### API 게이트웨이
*   **프레임워크**: Spring Cloud Gateway

### 프론트엔드
*   **프레임워크/라이브러리**: React
*   **패키지 매니저**: npm

### 컨테이너 및 오케스트레이션
*   **컨테이너**: Docker
*   **오케스트레이션**: Kubernetes (AKS), Docker Compose

### CI/CD
*   Azure Pipelines