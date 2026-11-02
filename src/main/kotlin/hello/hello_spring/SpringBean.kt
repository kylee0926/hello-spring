package hello.hello_spring

import hello.hello_spring.service.MemberService
import hello.hello_spring.repository.MemberRepository
import hello.hello_spring.repository.MemoryMemberRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// 코드로 직접 빈 등록 (@service, @Repository 등이 없을때 이렇게 주입 가능)
@Configuration
class SpringBean {

    @Bean
    fun memberService(): MemberService {
        return MemberService(memberRepository())
    }

    @Bean
    fun memberRepository(): MemberRepository {
        return MemoryMemberRepository()
    }
}