# jules_test

以下是一份完整的代辦事項管理應用程式（To-do App）開發規格（Spec），採用前端 Next.js、後端 Spring Boot，資料庫為 H2，並具備以下需求特性：

---

## 🔧 系統架構總覽

```
[ Next.js Frontend ] <--> [ Spring Boot Backend ] <--> [ H2 In-Memory DB ]
```

---

## ✅ 功能需求

### 基本功能

* 使用者註冊／登入（含預設帳號 admin/admin）
* 新增／編輯／刪除／完成 代辦事項
* 清單按照狀態分類（全部 / 未完成 / 已完成）
* 前端登入後顯示個別使用者的待辦事項

### 特別規格

* 使用 In-Memory H2 資料庫，應用每次重啟資料將清空
* 預設建立一組使用者帳密 admin/admin
* 採用 JWT 作為身份驗證機制

---

## 🧱 系統元件結構

### 前端（Next.js）

#### 資料夾結構

```
/app
  /auth
    login.tsx
    register.tsx
  /todos
    index.tsx
    create.tsx
    edit.tsx
/components
  TodoItem.tsx
  AuthForm.tsx
/services
  api.ts
  auth.ts
  todos.ts
/middleware
  authMiddleware.ts
```

### 後端（Spring Boot）

#### 模組結構

```
com.todoapp
  ├── controller
  │   ├── AuthController.java
  │   └── TodoController.java
  ├── service
  │   ├── UserService.java
  │   └── TodoService.java
  ├── model
  │   ├── User.java
  │   └── Todo.java
  ├── repository
  │   ├── UserRepository.java
  │   └── TodoRepository.java
  ├── security
  │   ├── JwtUtil.java
  │   ├── JwtFilter.java
  │   └── SecurityConfig.java
  └── TodoAppApplication.java
```

---

## 📡 API 設計

### 🔐 Auth API

| Method | Endpoint           | Description  | Request Body                               | Response             |
| ------ | ------------------ | ------------ | ------------------------------------------ | -------------------- |
| POST   | /api/auth/register | 使用者註冊        | `{ "username": "...", "password": "..." }` | 201 Created or 400   |
| POST   | /api/auth/login    | 使用者登入並回傳 JWT | `{ "username": "...", "password": "..." }` | `{ "token": "..." }` |

---

### ✅ Todo API

> 所有請求需攜帶 Authorization: `Bearer <token>`

| Method | Endpoint        | Description | Request Body / Params                                         | Response               |
| ------ | --------------- | ----------- | ------------------------------------------------------------- | ---------------------- |
| GET    | /api/todos      | 取得所有待辦事項    | 無                                                             | `[{ id, title, ... }]` |
| POST   | /api/todos      | 建立新待辦事項     | `{ "title": "...", "description": "..." }`                    | 201 Created            |
| PUT    | /api/todos/{id} | 更新待辦事項      | `{ "title": "...", "description": "...", "completed": true }` | 200 OK                 |
| DELETE | /api/todos/{id} | 刪除待辦事項      | 無                                                             | 204 No Content         |

---

## 🔐 安全設計（JWT）

1. 使用者登入成功後回傳 JWT token
2. 前端保存 token 並在每次 API 請求中附加在 `Authorization` header
3. Spring Boot 使用 `JwtFilter` 驗證請求中的 token 並解析出使用者資訊

---

## 🗃️ 資料模型設計

### User.java

```java
@Entity
public class User {
  @Id @GeneratedValue
  private Long id;

  private String username;
  private String password;

  @OneToMany(mappedBy = "user")
  private List<Todo> todos;
}
```

### Todo.java

```java
@Entity
public class Todo {
  @Id @GeneratedValue
  private Long id;

  private String title;
  private String description;
  private boolean completed;

  @ManyToOne
  private User user;
}
```

---

## ⚙️ 開發建議

* Spring Boot 啟動時透過 CommandLineRunner 初始化 `admin/admin` 使用者
* 使用 Spring Security + JWT 實作 API 保護
* H2 資料庫建議開啟 console（`spring.h2.console.enabled=true`）
* 可考慮使用 Swagger/OpenAPI 生成功能文件
* Next.js 可使用 Context 或 Zustand 管理登入狀態與 token
* 所有前端呼叫都封裝於 `services/` 目錄，方便維護與擴充

---

是否需要我幫你接著生成：

* Spring Boot 專案的基本骨架？
* Next.js 頁面與 API 呼叫範例？
* Postman 測試文件？

請告訴我接下來要深入哪一塊。
