package hello.hello_spring.service
import hello.hello_spring.domain.Member
import hello.hello_spring.repository.MemberRepository
import hello.hello_spring.repository.MemoryMemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(val memberRepository: MemberRepository) {

    // 회원 가입
    fun join(member: Member): Long{
        // 같은 이름 회원 x
        memberRepository.findByName(member.name)?.let {
            throw IllegalStateException("이미 존재")
        }
        
        memberRepository.save(member)
        return member.id!!
    }

    // 전체 회원 조회
    fun findMembers(): List<Member>{
        return memberRepository.findAll()
    }

    fun findOne(id: Long): Member? {
        return memberRepository.findById(id)
    }

}