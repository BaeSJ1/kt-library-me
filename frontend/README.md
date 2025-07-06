# 프론트엔드 서비스

프론트엔드 서비스는 KT-Library 시스템의 사용자 인터페이스를 제공합니다. React 기반으로 개발되었습니다.

## 로컬 개발 환경에서 실행

```bash
npm install
npm start
```

애플리케이션은 일반적으로 [http://localhost:3000](http://localhost:3000)에서 실행됩니다.

## Docker 환경에서 실행

프로젝트 루트 디렉토리에서 `docker-compose`를 사용하여 모든 서비스를 함께 실행할 수 있습니다.

```bash
cd .. # 프로젝트 루트 디렉토리로 이동
docker-compose -f build-docker-compose.yml up --build frontend
```

## Kubernetes 배포

프론트엔드 서비스는 Azure Pipelines를 통해 Kubernetes 클러스터에 배포됩니다. 배포 설정은 `kubernetes/deployment.yaml` 파일을 참조하십시오.

전체 시스템 배포에 대한 자세한 내용은 [프로젝트 루트 README.md](../README.md) 파일을 참조하십시오.

## 빌드

프로덕션 배포를 위한 빌드를 생성하려면:

```bash
npm run build
```

빌드된 파일은 `build` 폴더에 생성됩니다.