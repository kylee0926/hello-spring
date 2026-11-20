package hello.core

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions

class OrderServiceTest {
    val orderService: OrderService = OrderService()

    @Test
    fun createOrder(){
        // given
        val grade: Grade = Grade.VIP
        val price: Int = 200000
        val discountPrice: Int = 1000

        // when
        val order: Order = orderService.createOrder(grade=grade, price=price)

        // then
        Assertions.assertThat(order.discountPrice).isEqualTo(discountPrice)
    }

}