package hello.core

class OrderService {
    fun createOrder(grade: Grade, price: Int): Order {
        val discountPrice: Int = FixDiscountPolicy().discount(grade, price)
        val order: Order = Order(price, discountPrice)
        return order
    }
}