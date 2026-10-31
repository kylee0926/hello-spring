package hello.hello_spring.repository

import hello.hello_spring.domain.Member

class MemoryMemberRepository: MemberRepository {

    val store: MutableMap<Long, Member> = mutableMapOf()
    var sequence: Long = 0L

    override fun save(member: Member): Member {
        val memberId: Long = ++sequence
        store[memberId] = member
        member.id = memberId
        return member
    }
    override fun findAll(): List<Member> {
        return store.values.toList()
    }
    override fun findById(id: Long): Member? {
        val memberByStore: Member? = store[id]
        return memberByStore
    }

    override fun findByName(name: String): Member? {
        return store.values.filter{
            member -> member.name.equals(name)
        }.firstOrNull()
    }

    fun clearStore(){
        store.clear()
    }
}