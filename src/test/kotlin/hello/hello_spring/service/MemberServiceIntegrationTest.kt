package hello.hello_spring.service

import hello.hello_spring.domain.Member
import hello.hello_spring.repository.MemberRepository
import hello.hello_spring.repository.MemoryMemberRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional // test에서 트랜잭션 시작하고, test 끝나면 롤백해줌. db에 데이터가 남지 않아 다음 테스트에 영향 안 줌
class MemberServiceIntegrationTest {
    val memberRepository: MemoryMemberRepository = MemoryMemberRepository()
    val memberService: MemberService = MemberService(memberRepository = memberRepository)

    @Test
    fun join() {
        // given
        val member: Member = Member()
        member.name = "spring"

        // when
        val saveId: Long = memberService.join(member)

        // then
        val findMember: Member? = memberService.findOne(saveId)
        Assertions.assertThat(member.name).isEqualTo(findMember?.name)
    }

    @Test
    fun duplicateMember(){
        // given
        val member1: Member = Member()
        val member2: Member = Member()

        member1.name = "Hello"
        member2.name = "Hello"

        // when
        memberService.join(member1)

        // java: assertThrows(IllegalStateException.class, () -> memberService.join(member2))
        assertThrows<IllegalStateException> { memberService.join(member2) }
//        try {
//            memberService.join(member2)
//            fail("Member already exists")
//        } catch (e: IllegalStateException) {
//            Assertions.assertThat(e.message).isEqualTo("실패 메시지")
//        }


        // then
    }

    @Test
    fun findMembers() {
    }

    @Test
    fun findOne() {
    }
}