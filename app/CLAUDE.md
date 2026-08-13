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

# 남은 작업 (2026-08-13 기준)

요구사항 8개는 모두 구현했다. 아래는 이어서 할 일이며, 위에서 아래로 갈수록 급하지 않다.
디자인 원본은 `C:\Users\apf_temp_admin\Desktop\movie.pen` (pencil MCP로 읽는다. Figma MCP는 Starter 요금제라 호출 제한에 걸린다).

## 1. 상영일이 2024년으로 고정돼 있어 알림이 실제로는 울리지 않는다
`common/repository/MovieRepository.kt`의 상영 시작일이 `2024.3.1` ~ `2024.3.29` 하드코딩이다.
오늘 날짜가 이미 지나서 `ScreeningAlarmPolicy.scheduleFor()`가 항상 `Skip`을 반환한다.
현재는 `AlarmManagerScreeningAlarmScheduler`의 `BuildConfig.DEBUG` 10초 알림으로만 확인 가능하다.
→ 상영 시작일을 `LocalDate.now()` 기준 상대 날짜로 바꾸고, 디버그용 10초 알림을 제거할지 결정한다.

## 2. 알림을 눌러도 해당 예매 정보로 이동하지 않는다
`notification/ScreeningNotifier.kt`의 PendingIntent가 `EXTRA_RESERVATION_ID`를 이미 싣고 있지만,
`MainActivity` / `MovieTicketApp`에서 이 값을 읽지 않는다.
→ intent에서 id를 꺼내 `ReservationHistoryRepository.findById(id)`로 조회한 뒤 `Screen.ReservationDetail`로 이동시킨다.
   `onNewIntent`도 함께 처리해야 앱이 떠 있는 상태에서 눌러도 동작한다.

## 3. 알림 권한 안내 다이얼로그 미구현
디자인 `4단계 - 홈 / 권한 확인` 화면이 아직 없다. 지금은 시스템 권한창만 뜬다.
→ `RequestNotificationPermissionOnce()` 앞단에 안내 다이얼로그를 넣는다.

## 4. 영화 목록 아이템 수치가 디자인과 다르다
| 항목 | 현재 | 디자인 |
|---|---|---|
| 아이템 높이 | 131 | 132 |
| padding | 6/12/10/12 | 12 |
| 요소 간격 | 13 | 15 |
| 지금 예매 버튼 | 88x32, radius 4, 13sp | 88x36, radius 6, 14sp |

## 5. `poster_1/2/3.png`가 미사용 상태다
drawable에 포스터 3장이 있는데 영화는 5개다. 어떤 영화에 어떤 포스터를 붙일지 정해지지 않았다.
→ 3장을 순환시킬지, 나머지 2장을 받을지 결정 필요.

## 6. 확인이 필요한 결정들
- `MoviePrimary`를 `#6200EE` → `#7A0FF7`로 바꿨다(디자인 `$purple` 기준). 앱 전체 색이 바뀌므로 확정 또는 롤백 필요.
- 알림 제목 문자열 `notification_title` = "상영 알림"은 디자인에 없어서 임의로 정했다.

## 7. 검증되지 않은 것 (에뮬레이터가 없어 컴파일만 확인함)
- Room DAO 실제 조회/저장 → 인메모리 DB로 androidTest 작성하면 좋다
- AlarmManager 발화 및 알림 표시
- 모든 Compose 화면 렌더링, androidTest 실행
