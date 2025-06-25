package shopify.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopify.Data.DTOs.OrderDTO;
import shopify.Data.Models.Order;
import shopify.Services.AuthenticationService;
import shopify.Services.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;
    private final AuthenticationService authenticationService;

    public OrderController(OrderService orderService, AuthenticationService authenticationService) {
        this.orderService = orderService;
        this.authenticationService = authenticationService;
    }

    public Long userId(){
        return authenticationService.userId();
    }
    @PostMapping("")
    public ResponseEntity<String> buy(@RequestBody List<Long> productIds, @RequestParam(required = false) Integer discount){
        return orderService.order(userId(), productIds, discount);
    }

    @GetMapping("/cartSize")
    public ResponseEntity<Integer> numberOfItems(){
        return orderService.getNumberOfItems();
    }

    @GetMapping("/buyerOrders")
    public ResponseEntity<List<OrderDTO>> buyerOrders(){
        return orderService.fetchBuyerOrders(userId());
    }

    @GetMapping("/sellerOrders")
    public ResponseEntity<List<Order>> sellerOrders(){
        return orderService.fetchSellerOrders(userId());
    }

}
