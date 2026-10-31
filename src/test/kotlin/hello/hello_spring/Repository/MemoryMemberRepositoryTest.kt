package hello.hello_spring.Repository
import hello.hello_spring.domain.Member
import hello.hello_spring.repository.MemberRepository
import hello.hello_spring.repository.MemoryMemberRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class MemoryMemberRepositoryTest {

    val repository: MemoryMemberRepository = MemoryMemberRepository()

    @AfterEach
    fun afterEach(){
        repository.clearStore()
    }

    @Test
    fun save(){
        val member: Member = Member()
        val name: String = "Springgg"
        member.name = name
        repository.save(member)
        val memberId: Long = member.id!!
        val result: Member? = repository.findById(memberId)
        Assertions.assertThat(member).isEqualTo(result)
    }

    @Test
    fun findByName(){
        val member1: Member = Member()
        member1.name = "Spring1"
        repository.save(member1)

        val member2: Member = Member()
        member2.name = "Spring2"
        repository.save(member2)

        val result: Member? = repository.findByName("Spring1")
        Assertions.assertThat(result).isEqualTo(member1)
    }

    @Test
    fun findAll(){
        val member1: Member = Member()
        member1.name = "Spring1"
        repository.save(member1)

        val member2: Member = Member()
        member2.name = "Spring2"
        repository.save(member2)

        val result: List<Member> = repository.findAll()
        Assertions.assertThat(result.size).isEqualTo(2)
    }
}