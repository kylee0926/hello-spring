package hello.hello_spring.controller

import hello.hello_spring.domain.Member
import hello.hello_spring.service.MemberService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping

@Controller
class MemberController(val memberService: MemberService){
    // @Autowired를 써서 어노테이션이 붙은 클래스의 객체를 직접 만들어 스프링 컨테이너에 보관 가능하며, 이렇게 관리되는 객체를 빈이라고 함. 즉, 이 자리에 필요한 ㅏ입의 빈을 컨테이너에서 찾아서 자동으로 넣어달라는 표시
    // 생성자가 하나뿐이면 autowired 생략해도 자동 주입

    @GetMapping("/members/new")
    fun createForm(model: Model): String{
        return "members/createMemberForm"
    }

    @PostMapping("/members/new")
    fun create(form: MemberForm): String {
        val member: Member = Member()
        member.name = form.name
        memberService.join(member)
        return "redirect:/"
    }

    @GetMapping("/members")
    fun list(model: Model): String {
        val members: List<Member> = memberService.findMembers()
        model.addAttribute("members", members)
        return "members/memberList"
    }
}
