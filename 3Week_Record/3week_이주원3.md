Title: [3Week] 이주원3
주요 엔드 포인트
1. 홈
관리자페이지(adm) GET /adm/home/main

2. 주문
정산데이터생성 폼(adm) GET /adm/rebate/makeData
정산데이터생성(adm) POST /adm/rebate/makeData

3. 정산
정산데이터리스트(adm) GET /adm/rebate/rebateOrderItemList
정산(전체)(adm) POST /adm/rebate/rebate 
정산(건별)(adm) POST /adm/rebate/rebateOne/{rebateOrderItemId}

3주차 미션 요약
[접근 방법]
강의하신 강사님 코드를 바탕으로 구현하였습니다.

[특이사항]

[아쉬웠던 점] 배치 부분에 대한 공부가 필요합니다.

[Refactoring] Product Service에 대해서 CQS 적용하였습니다.
