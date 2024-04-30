## Flow Chart

1. 회원가입

```mermaid
  sequenceDiagram
    autonumber
    client ->> server: 회원가입 요청
    alt 성공한 경우
    server -->> client: 성공 반환
    else 아이디가 중복된 경우 
    server -->> client: reason code와 함께 실패 반환
    end
```