package hello.core

interface DiscountPolicy {
    fun discount(grade: Grade, price: Int): Int
}