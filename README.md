# jwp-subway-path

## [1단계]

### API 명세서

| Method | URI                        | Description                 |
|--------|----------------------------|-----------------------------|
| POST   | /lines/{lineId}/register   | 노선에 역을 등록하는 API             |
| DELETE | /lines/{lineId}/unregister | 노선의 역을 삭제하는 API             |
| GET    | /lines/{id}                | 노선의 역들을 조회하는 API            |
| GET    | /lines                     | 모든 노선과 노선에 포함된 역들을 조회하는 API |

### Request Body

#### [POST] /lines/{lineId}/register

```json
{
  "beforeStationName": "잠실",
  "nextStationName": "강남",
  "distance": 10
}
```

#### [DELETE] /lines/{lineId}/unregister

```json
{
  "name": "잠실"
}
```

### 기능 요구사항

- [x] 노선에 역 등록 API 신규 구현
- [x] 노선에 역 제거 API 신규 구현
- [x] 노선 조회 API 수정
    - [x] 노선에 포함된 역을 순서대로 보여주도록 응답을 개선합니다.
- [x] 노선 목록 조회 API 수정
    - [x] 노선에 포함된 역을 순서대로 보여주도록 응답을 개선합니다.

#### 노선에 역 등록

- [x] 노선에 등록되는 역의 위치는 자유롭게 지정할 수 있어야 합니다.
- [x] 노선에 역이 등록될 때 거리 정보도 함께 포함되어야 합니다.
    - [x] 거리 정보는 양의 정수로 제한합니다.
- [x] 노선에 역이 하나도 등록되지 않은 상황에서 최초 등록 시 두 역을 동시에 등록해야 합니다.
- [x] 하나의 역은 여러 노선에 등록이 될 수 있습니다.
- [x] 노선은 갈래길을 가질 수 없습니다.
- [x] 노선 가운데 역이 등록 될 경우 거리 정보를 고려해야 합니다.
    - 기존 역 사이에 추가된 역과의 거리의 합은 기존 두 역의 거리와 같아야 합니다.

#### 노선에 역 제거

- [x] 노선에서 역을 제거할 경우 정상 동작을 위해 재배치 되어야 합니다.
- [x] 노선에서 역이 제거될 경우 역과 역 사이의 거리도 재배정되어야 합니다.
- [x] 노선에 등록된 역이 2개 인 경우 하나의 역을 제거할 때 두 역이 모두 제거되어야 합니다.

### 피드백 요구사항

- [x] LineService의 추가할 section을 찾는 중복 로직 제거
- [x] LineService의 `emptyLine` 네이밍 수정 (빈 노선을 반환하는 것으로 오해할 수 있음)
- [x] 중복 역 등록 방어 로직 작성
- [x] 테스트 코드 깨짐 확인
- [x] dto 의존성 관리

## [2단계]

### 기능 요구사항

- [ ] 프로덕션과 테스트 데이터베이스 환경 분리
    - 프로덕션은 로컬에 저장
    - 테스트는 인메모리에 저장
- [ ] 경로 조회 API 구현
- [ ] 요금 조회 기능 추가
    - 10㎞ 이내: 1,250원
    - 10km~50km: 5km 까지 마다 100원 추가
    - 50km 초과: 8km 까지 마다 100원 추가

### API 명세서

| Method | URI                    | Description |
|--------|------------------------|-------------|
| POST   | /lines/sections/path   | 경로 조회 API   |
| POST   | /lines/sections/charge | 요금 조회 API   |

### Request Body

#### [POST] /lines/sections/path && [POST] /lines/sections/charge

```json
{
  "startStationName": "잠실",
  "endStationName": "강남"
}
```
