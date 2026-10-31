// 앱이 처음 진입할때 실행되는 지점
package hello.hello_spring.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

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

    @GetMapping("hello-mvc")
    fun helloMVC(@RequestParam(value="name", required = false) name: String, model: Model): String {
        model.addAttribute("name", name)
        return "hello-template"
    }

    @GetMapping("hello-string")
    @ResponseBody
    fun helloString(@RequestParam(value="name") name: String): String{
        """
        1. ResponseBody 사용 시, viewResolver가 아닌 HttpMessageConverter가 동작
        2. 기본 문자처리 - stringHttpMessageConverter
        3. 기본 객체처리 - MappingJackson2HttpMessageConverter
            
        """

        return "hello " + name // response body에 이 내용을 전달하겠다.
    }
}