// 앱이 처음 진입할때 실행되는 지점
package hello.hello_spring.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HelloController {
    // /hello로 들어오면 이걸 호출, GetMapping = HTTP GET
    @GetMapping("hello")
    fun hello(model: Model): String {
        model.addAttribute("data", "hello!!")
        """
        1. resources/templates/hello.html를 찾아가라 (Spring의 기본 세팅)
        2. 컨트롤러에서 리턴 값으로 문자를 반환하면 viewResolver가 화면을 찾아서 처리한다.
        3. 스프링 부트 템플릿 엔진 기본 viewName 매핑
           resoueces:tempates/+viewName+.html
        4 파일을 웹 브라우저에 그대로 내려주는것으로 정적 컨텐츠라고함
        """
        return "hello"
    }
}