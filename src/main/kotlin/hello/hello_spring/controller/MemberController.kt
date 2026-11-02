package hello.hello_spring.controller

import hello.hello_spring.service.MemberService
import org.springframework.stereotype.Controller

@Controller
class MemberController(
    // @Autowired를 써서 어노테이션이 붙은 클래스의 객체를 직접 만들어 스프링 컨테이너에 보관 가능하며, 이렇게 관리되는 객체를 빈이라고 함. 즉, 이 자리에 필요한 ㅏ입의 빈을 컨테이너에서 찾아서 자동으로 넣어달라는 표시
    // 생성자가 하나뿐이면 autowired 생략해도 자동 주입
    private val memberService: MemberService // 주 생성자 파라미터 = 생성자 주입. 스프링이 컨테이너에서 MemberService 빈을 찾아 넣어줌

)
