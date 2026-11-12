package hello.hello_spring.repository

import hello.hello_spring.domain.Member
import jakarta.persistence.EntityManager

/**
 * JPA 기반 회원 저장소 구현체.
 *
 * JDBC처럼 SQL을 직접 짜고 ResultSet을 매핑하는 대신,
 * JPA의 EntityManager에게 엔티티(Member) 단위로 저장/조회를 맡긴다.
 *
 * - EntityManager: JPA의 핵심 객체. 스프링 부트가 DataSource와 설정을 바탕으로
 *   자동으로 만들어 주며, 생성자 주입으로 받아서 사용한다.
 * - JPA를 통한 모든 데이터 변경(persist 등)은 트랜잭션 안에서 실행돼야 하므로,
 *   이 저장소를 사용하는 서비스 쪽에 @Transactional이 필요하다.
 */
class JpaMemberRepository(val em: EntityManager): MemberRepository {

    /**
     * 회원 저장.
     * persist()를 호출하면 엔티티가 영속성 컨텍스트에 등록되고, JPA가 INSERT 쿼리를 만들어 실행한다.
     * 이때 @GeneratedValue로 생성된 id가 member 객체의 id 필드에 자동으로 채워진다.
     */
    override fun save(member: Member): Member {
        em.persist(member)
        return member
    }

    /**
     * PK(id)로 회원 단건 조회.
     * em.find(엔티티 클래스, PK)는 "select ... from member where id = ?"를 대신 실행한다.
     * 결과가 없으면 null을 반환하므로, 반환 타입을 nullable(Member?)로 그대로 돌려준다.
     * (non-null 타입 변수로 받으면 Kotlin이 null 체크를 넣어 NullPointerException이 발생하니 주의)
     */
    override fun findById(id: Long): Member? {
        return em.find(Member::class.java, id)
    }

    /**
     * 이름으로 회원 조회.
     * PK가 아닌 컬럼으로 조회할 때는 JPQL을 직접 작성해야 한다.
     *
     * - JPQL "select m from Member m where m.name = :name"
     *   : 테이블(member)이 아니라 엔티티(Member)와 그 필드(name)를 대상으로 하는 쿼리.
     *     `select m` 은 컬럼이 아닌 Member 엔티티 자체를 조회한다는 뜻이다.
     * - :name 은 이름 기반 파라미터 바인딩 → setParameter("name", name)으로 값을 넣는다.
     * - getResultList()로 결과를 리스트로 받고, 첫 번째 값(없으면 null)을 반환한다.
     */
    override fun findByName(name: String): Member? {
        val result: List<Member> = em.createQuery("select m from Member m where m.name = :name", Member::class.java).setParameter(
            "name", name
        ).getResultList()
        return result.firstOrNull()
    }

    /**
     * 전체 회원 조회.
     * JPQL "select m from Member m" → 모든 Member 엔티티를 조회해 리스트로 반환한다.
     */
    override fun findAll(): List<Member> {
        val result: List<Member> = em.createQuery(
            "select m from Member m", Member::class.java
        ).getResultList()
        return result
    }

}
