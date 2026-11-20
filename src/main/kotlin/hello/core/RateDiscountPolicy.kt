package hello.core

class RateDiscountPolicy: DiscountPolicy {
    override fun discount(grade: Grade, price: Int): Int {
        var discountPrice: Int = 0
        when (grade) {
            Grade.VIP -> discountPrice = (price * 0.1).toInt()
            Grade.BASIC -> discountPrice = 0
        }
        return discountPrice
    }
}