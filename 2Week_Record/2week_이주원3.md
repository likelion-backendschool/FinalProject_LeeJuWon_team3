
# Title: [2Week] 이주원3

## 주요 **엔드 포인트**

## 1. **장바구니**

- [x] **품목리스트** GET /cart/list

- [ ] **품목삭제** **POST /cart/remove/{productId}**

- [x] **품목추가** **POST /cart/add/{productId}**


## 2. **주문**

- [ ] **주문생성** **POST /order/create**

- [ ] **주문리스트** **GET /order/list**

- [ ] **주문상세** **GET /order/{id}**

- [ ] **주문취소** **POST /order/{id}/cancel**

- [ ] **결제처리** **POST /order/{id}/pay**

- [ ] **환불처리** **POST /order/{id}/refund**

---

## 2주차 미션 요약

**[접근 방법]**
- 1주차 강사님 코드를 가져와서 진행하였다.
- erd부터 매핑하고 진행하였다.


**[특이사항]**
- `[아쉬웠던 점]` 처음이 아닌 중간에 테스트 코드 작성을 시도하였던 것이 아쉽다. DTO로 구현하던 중에 테스트 코드 작성을 시도하였다. 

- `[Refactoring]` 장바구니에 상품 넣을 때 ProductOption을 이용하여 구현해보기.
