package hello.hello_spring.service

import hello.hello_spring.domain.Member
import hello.hello_spring.repository.MemoryMemberRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MemberServiceTest {
    val memberRepository: MemoryMemberRepository = MemoryMemberRepository()
    val memberService: MemberService = MemberService(memberRepository = memberRepository)

    @BeforeEach
    fun beforeEach() {
        val memberRepository: MemoryMemberRepository = MemoryMemberRepository()
        val memberService = MemberService(memberRepository = memberRepository)
    }

    @AfterEach
    fun afterEach(){
        memberRepository.clearStore()
    }


    @Test
    fun join() {
        // given
        val member: Member = Member()
        member.name = "Hello"

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