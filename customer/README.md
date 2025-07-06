# 고객 관리 서비스

고객 관리 서비스는 KT-Library 시스템에서 고객 정보 및 관련 기능을 담당합니다.

## 로컬 개발 환경에서 실행

**사전 준비 사항:**
*   Kafka 서버가 실행 중이어야 합니다. (프로젝트 루트의 `infra/docker-compose.yml`을 사용하여 Kafka를 실행할 수 있습니다.)

```bash
mvn spring-boot:run
```

## Docker 환경에서 실행

프로젝트 루트 디렉토리에서 `docker-compose`를 사용하여 모든 서비스를 함께 실행할 수 있습니다.

```bash
cd .. # 프로젝트 루트 디렉토리로 이동
docker-compose -f build-docker-compose.yml up --build customer
```

## Kubernetes 배포

고객 관리 서비스는 Azure Pipelines를 통해 Kubernetes 클러스터에 배포됩니다. 배포 설정은 `kubernetes/deployment.yaml` 파일을 참조하십시오.

전체 시스템 배포에 대한 자세한 내용은 [프로젝트 루트 README.md](../README.md) 파일을 참조하십시오.

## API 테스트

API 게이트웨이를 통해 고객 관리 서비스의 API를 테스트할 수 있습니다. (API 게이트웨이는 일반적으로 `8088` 포트에서 실행됩니다.)

```bash
http :8088/customers id="id" name="name" email="email" isKtUser="isKTUser" createdAt="createdAt" updatedAt="updatedAt"
http :8088/subsciptions id="id" isValid="isValid" startDate="startDate" endDate="endDate" createdAt="createdAt" updatedAt="updatedAt"
```