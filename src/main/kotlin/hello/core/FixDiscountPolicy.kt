package hello.core

class FixDiscountPolicy {
    fun discount(grade: Grade, price: Int): Int{
        when (grade) {
            Grade.BASIC -> return 0
            Grade.VIP -> return 1000
        }
    }
}