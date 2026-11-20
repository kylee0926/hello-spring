package hello.core

class OrderService(val discountPolicy: DiscountPolicy) {
    fun createOrder(grade: Grade, price: Int): Order {
        val discountPrice: Int = discountPolicy.discount(grade, price)
        val order: Order = Order(price, discountPrice)
        return order
    }
}