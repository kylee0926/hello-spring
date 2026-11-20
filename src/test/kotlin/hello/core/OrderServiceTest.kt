package hello.core

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions

class OrderServiceTest {
    @Test
    fun fixedDiscount() {
        val orderService: OrderService = OrderService(discountPolicy = FixDiscountPolicy())
        val order = orderService.createOrder(grade=Grade.VIP, 200_000)
        Assertions.assertThat(order.discountPrice).isEqualTo(1000)
    }

   @Test
   fun rateDiscount() {
       val orderService: OrderService = OrderService(discountPolicy = RateDiscountPolicy())
       val order = orderService.createOrder(grade=Grade.VIP, 200_000)
       Assertions.assertThat(order.discountPrice).isEqualTo(20000)
   }

}