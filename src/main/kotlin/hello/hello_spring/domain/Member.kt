package hello.hello_spring.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null, var name: String = ""
) {

}