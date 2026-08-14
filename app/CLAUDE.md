[필수 사항]
0. figma의 디자인을 자세히 분석후 ui작업을 진행할 것
1. figma의 dp값을 참조할것 단 , 휴대폰 비율에 맞출것
2. 색상도 동일하게 figma안에서 사용할거 @app/src/main/res/values/colors.xml 참고
3. 애매한 부분은 나한테 먼저 질문할것
4. MVP 패턴에 맞게 패키지 분리를 할 것
5. login
    ㄴmodel
    ㄴview
    ㄴpresenter
 테마별 구분을하고 그 아래에 model view presenter 등이 들어가는 구조 필요시 리팩터링 진행
6. 하나의 작업이 끝나면 검토후에 진행 (검토후 직접 git push 할 예정)

https://www.figma.com/design/Du7XucL9G3mlyfAtXr9CLI/Android-%EB%B3%B5%EC%82%AC%EB%B3%B8?node-id=47-117&t=ziXVsFlYyW0FisyD-1
피그마 url 링크를 들어가면

---

# 남은 작업 (2026-08-14 기준)

요구사항 8개는 모두 구현했다. 아래는 이어서 할 일이며, 위에서 아래로 갈수록 급하지 않다.
디자인 원본은 `movie.pen` — 이 PC에서는 pencil 데스크톱 앱이 연결 안 돼 있어, 공유 링크(`https://app.pen.dev/s/-TgGPSrsJFkw482RfRWqIhVZLqdsbfaNbNSYoK54_H8`)를 브라우저로 열어서 확인했다.

## 1. 상영일이 정적 하드코딩이라 시간이 지나면 다시 과거로 밀려난다
`common/repository/MovieRepository.kt`의 상영 시작일을 `2024.3.x` → `2026.8.1~8.28`로 옮겨서 지금은 `ScreeningAlarmPolicy.scheduleFor()`가 정상 동작한다.
다만 `defaultScreeningDate()`(=상영 시작일)는 여전히 고정값이라, 오늘(2026.8.14) 기준으로 id0~4는 이미 시작일이 지났다.
그 경우 예매 시 날짜를 수동으로 미래 날짜로 바꿔야 알림이 울린다. 2026.8.28을 넘기면 전체 목록이 다시 과거로 밀려 원래 버그가 재발한다.
→ 근본적으로 고치려면 상영 시작일을 `LocalDate.now()` 기준 상대 날짜로 계산하도록 바꿔야 한다.

## 2. 확인이 필요한 결정들
- `MoviePrimary`를 `#6200EE` → `#7A0FF7`로 바꿨다(디자인 `$purple` 기준). 앱 전체 색이 바뀌므로 확정 또는 롤백 필요.
- 알림 제목 문자열 `notification_title` = "상영 알림"은 디자인에 없어서 임의로 정했다.

## 3. 검증되지 않은 것
- ~~Room DAO 실제 조회/저장~~ → `RoomReservationHistoryRepositoryTest`(androidTest, 인메모리 DB)로 검증 완료 (2026-08-14)
- AlarmManager 발화 및 알림 표시
- 모든 Compose 화면 렌더링, androidTest 실행
