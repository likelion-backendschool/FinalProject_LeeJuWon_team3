# Title: [3Week] 이주원3

## 주요 엔드 포인트

## 1. 홈
- [x] 관리자페이지(adm) GET /adm/home/main

## 2. 주문
- [x] 정산데이터생성 폼(adm) GET /adm/rebate/makeData
- [x] 정산데이터생성(adm) POST /adm/rebate/makeData

## 3. 정산
- [x] 정산데이터리스트(adm) GET /adm/rebate/rebateOrderItemList
- [x] 정산(전체)(adm) POST /adm/rebate/rebate 
- [x] 정산(건별)(adm) POST /adm/rebate/rebateOne/{rebateOrderItemId}

## 4. 출금
- [ ] 출금신청(member) 폼 입력 GET /withdraw/apply
- [ ] 출금신청(member) 폼 처리 POST /withdraw/apply
- [ ] 출금처리(adm) 출금신청리스트 GET /adm/withdraw/applyList
- [ ] 출금신청(adm) 출금처리 POST /adm/withdraw/{withdrawApplyId}

--- 

## 3주차 미션 요약
[접근 방법]
- 홈, 주문, 정산 구현에 있어서 강사님 깃허브 코드를 이용했습니다.
- Product 서비스에 대해서 CQS(Command Query Separation) 패턴을 적용하였습니다.
- Post, Product에 DTO를 적용하였습니다.

[특이사항]

[아쉬웠던 점]
- 정산, 수수료에 대한 요구사항 간과부분 입니다.
- 테스트코드 미작성입니다. 
- 배치 부분에 대한 공부가 필요합니다.

[Refactoring]
- DTO 미적용 부분 DTO 적용할 예정입니다.
