# TIL 2026-11-12: Kotlin + JPA 기초 (Django 개발자 시점)

> 관련 코드: `src/main/kotlin/hello/hello_spring/domain/Member.kt`, `src/main/kotlin/hello/hello_spring/repository/JpaMemberRepository.kt`

## 1. Kotlin으로 JPA 엔티티 작성하기

Java:

```java
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String name;
```

Kotlin에서는 생성자 파라미터에 어노테이션을 바로 붙인다.

```kotlin
@Entity
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String = "",
)
```

- `id`는 저장할 때 DB가 채워주므로 처음엔 `null`이다. 그래서 nullable 타입(`Long?`)으로 둔다.
- Kotlin 프로퍼티는 getter/setter가 자동으로 생긴다.

Django로 치면 아래와 같다.

```python
class Member(models.Model):
    # id = models.AutoField(primary_key=True)  ← 기본으로 자동 생성
    name = models.CharField(max_length=255)
```

## 2. `allOpen`과 `plugin.jpa`

### 문제

- Kotlin 클래스는 **기본이 `final`** 이다. 상속할 수 없다.
- Hibernate는 지연 로딩(LAZY)을 할 때 엔티티를 **상속한 프록시 클래스**를 만든다.
- 그래서 `final` 엔티티는 프록시를 못 만들고 지연 로딩이 제대로 동작하지 않는다.
- JPA는 기본 생성자(no-arg)도 필요하다.

### 해결 (`build.gradle.kts`)

```kotlin
plugins {
    kotlin("plugin.jpa") version "2.3.21"   // @Entity에 no-arg 생성자 자동 생성
}

allOpen {                                    // 지정한 어노테이션이 붙은 클래스를 자동으로 open 처리
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}
```

- `kotlin("plugin.spring")`도 all-open 플러그인이다. `@Component`, `@Service`, `@Transactional` 등을 open 처리한다. 하지만 **`@Entity`는 포함되지 않으므로** 따로 추가해야 한다.

## 3. Django ORM과 JPA 비교

| JPA (Kotlin) | Django |
|---|---|
| 엔티티 `@Entity class Member` | 모델 `class Member(models.Model)` |
| `EntityManager` (`em`) | 직접 대응하는 것 없음 |
| `em.persist(member)` | `member.save()` |
| `em.find(Member::class.java, id)` | `Member.objects.filter(pk=id).first()` |
| JPQL `select m from Member m where m.name = :name` | `Member.objects.filter(name=name)` |
| `@Transactional` | `@transaction.atomic` |

### 설계 철학 차이

- **Django는 Active Record 방식이다.** 객체가 스스로 저장한다(`m.save()`).
- **JPA는 Data Mapper 방식이다.** 엔티티는 데이터일 뿐이고, 저장과 조회는 `EntityManager`가 맡는다.

```python
# Django
m = Member.objects.get(pk=1)
m.name = "lee"
m.save()            # save()를 빼먹으면 저장되지 않는다
```

```kotlin
// JPA
val m = em.find(Member::class.java, 1L)
m.name = "lee"      // save() 없이 끝. 트랜잭션이 끝날 때 자동으로 UPDATE된다
```

## 4. 영속성 컨텍스트 (Persistence Context)

> `EntityManager`가 트랜잭션 동안 들고 있는 **"내가 관리 중인 엔티티 목록"**. Django에는 없는 개념이다.

### 1) 1차 캐시: 같은 행이면 같은 객체

```kotlin
val a = em.find(Member::class.java, 1L)  // SELECT
val b = em.find(Member::class.java, 1L)  // SELECT 없이 캐시에서 꺼냄
a === b  // true
```

Django에서는 `get()`을 두 번 하면 SELECT도 두 번 나가고, 결과는 서로 다른 객체(`a is b`가 `False`)다.

### 2) 변경 감지 (Dirty Checking)

- 조회 시점의 스냅샷을 기억해 두었다가, 트랜잭션이 끝날 때 바뀐 엔티티만 골라 UPDATE한다.
- 그래서 `update()`나 `save()`를 호출할 필요가 없다.

### 3) 쓰기 지연 (flush)

- SQL을 모아두었다가 commit 직전에 한꺼번에 보낸다(**flush**).
- 예외로 `IDENTITY` 전략은 DB가 id를 만들어야 하므로 `persist()` 즉시 INSERT가 나간다. 그래서 `persist()` 직후에 `member.id`가 채워져 있다.

### 생명주기

```
@Transactional 시작          ← with transaction.atomic():
  │  영속성 컨텍스트 생성
  │  em.persist(member)       → 목록에 등록
  │  em.find(...)             → 목록에 등록 (캐시)
  │  member.name = "..."      → 아직 SQL 없음
@Transactional 끝
  │  flush → 바뀐 것을 비교해서 INSERT/UPDATE
  │  commit
  └  영속성 컨텍스트 소멸
```

- Django는 autocommit이 기본이라 `atomic` 없이도 `save()`가 동작한다.
- **JPA는 변경 작업을 트랜잭션 없이 하면 에러가 난다.** 그래서 서비스에 `@Transactional`이 필요하다.

## 5. JPA, Hibernate, EntityManager

| 이름 | 정체 |
|---|---|
| **JPA** | ORM **표준 규격**(인터페이스). "ORM은 이렇게 생겨야 한다"는 약속 |
| **Hibernate** | JPA의 **구현체**. 실제로 SQL을 만들어 실행하는 엔진 |
| **EntityManager** | JPA에서 ORM 작업(`persist`, `find`, `createQuery`)을 하는 창구 |

`spring-boot-starter-data-jpa`를 추가하면 Hibernate가 함께 들어온다. `em.persist()`를 호출하면 실제로는 Hibernate가 동작한다.

## 6. `createQuery`와 `getResultList`

```kotlin
em.createQuery("select m from Member m where m.name = :name", Member::class.java)  // ① 쿼리 객체 생성 (실행 안 함)
  .setParameter("name", name)                                                        // ② 파라미터 바인딩
  .getResultList()                                                                   // ③ 여기서 SQL 실행 → List<Member>
```

Django QuerySet이 lazy한 것과 같은 구조다.

```python
qs = Member.objects.filter(name=name)   # ①② 아직 DB에 안 감
result = list(qs)                       # ③ 실행
```

### 실행 메서드

| JPA (`TypedQuery`) | 결과 | Django |
|---|---|---|
| `getResultList()` | `List`. 결과가 없으면 빈 리스트 | `list(qs)` |
| `getSingleResult()` | 1개. 0개나 2개 이상이면 **예외** | `qs.get()` |
| `getResultStream()` | 스트림 | `qs.iterator()` |

- 결과가 없을 수 있는 조회는 `getResultList().firstOrNull()` 패턴을 쓴다. Django의 `.filter(...).first()`와 같다.

### JPQL

- SQL처럼 생겼지만 **테이블과 컬럼이 아니라 엔티티 클래스와 필드**를 대상으로 쓴다(`Member`, `m.name`).
- `:name`은 이름 기반 파라미터이고, `setParameter("name", value)`로 값을 넣는다.
- 나중에 Spring Data JPA를 쓰면 `fun findByName(name: String): Member?`처럼 메서드 이름만으로 쿼리가 자동 생성된다.

## 7. Kotlin 문법 메모

| Kotlin | Python |
|---|---|
| `class Repo(val em: EntityManager)` | `__init__`에서 `self.em = em`까지 한 줄로 처리 |
| `class Repo(...) : MemberRepository` | 인터페이스 구현 |
| `override fun save(member: Member): Member` | `def save(self, member: Member) -> Member` |
| `Member?` | `Optional[Member]`. `?`가 없으면 컴파일러가 null을 막는다 |
| `Member::class.java` | 클래스 객체 자체 (`Member`) |
| `val` / `var` | 재할당 불가 / 가능 |
| `list.firstOrNull()` | `lst[0] if lst else None` |
| `a === b` | `a is b` (참조 비교) |
| `.getResultList()` == `.resultList` | Java getter는 Kotlin에서 프로퍼티처럼 접근할 수 있다 |

## 8. 실행되는 SQL 확인하기

`application.properties`:

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Django의 `print(queryset.query)`처럼 확인할 수 있다. 같은 id로 `find`를 두 번 하면 SELECT가 한 번만 찍히는 것(1차 캐시)과, 필드만 바꿨는데 UPDATE가 나가는 것(변경 감지)을 직접 볼 수 있다.
