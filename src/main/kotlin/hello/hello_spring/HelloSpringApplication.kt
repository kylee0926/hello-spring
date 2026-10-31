package hello.hello_spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HelloSpringApplication

fun main(args: Array<String>) {
	"""
    1. main에서 화살표 버튼으로 빌드하지 않고 /.graldew build 입력으로도 가능
	2. 이렇게 하면 build 파일이 레포에 생성
	3. 터미널에서 build/libs로 이동
	4. java -jar hello-spring-0.0.01-SNAPSHOT.jar 실행
	5. 배포할때 이 jar 파일하나만 하면 됨
	
	build 폴더 없애고 싶다면 - /.gradlew clean
	"""
	runApplication<HelloSpringApplication>(*args)
}

