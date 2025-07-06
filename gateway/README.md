# API 게이트웨이 서비스

API 게이트웨이 서비스는 KT-Library 시스템의 모든 마이크로서비스로의 요청을 라우팅하고, 공통 기능을 처리합니다.

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
docker-compose -f build-docker-compose.yml up --build gateway
```

## Kubernetes 배포

API 게이트웨이 서비스는 Azure Pipelines를 통해 Kubernetes 클러스터에 배포됩니다. 배포 설정은 `kubernetes/deployment.yaml` 파일을 참조하십시오.

전체 시스템 배포에 대한 자세한 내용은 [프로젝트 루트 README.md](../README.md) 파일을 참조하십시오.

## API 테스트

API 게이트웨이는 일반적으로 `8088` 포트에서 실행됩니다. 각 마이크로서비스의 API는 게이트웨이를 통해 접근할 수 있습니다. 자세한 API 테스트 방법은 [프로젝트 루트 README.md](../README.md) 파일을 참조하십시오.