package shopify.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import shopify.Data.DTOs.OrderDTO;
import shopify.Data.DTOs.PaymentDTO;
import shopify.Data.DTOs.ProductDTO;
import shopify.Data.Models.*;
import shopify.Repositories.OrderRepository;
import shopify.Repositories.ProductRepository;
import shopify.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final WebClient webClient;


    public OrderService(UserRepository userRepository,
                        ProductRepository productRepository,
                        OrderRepository orderRepository, WebClient webClient) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.webClient = webClient;
    }

    public Mono<String > url(PaymentDTO paymentDTO) throws JsonProcessingException {

        return webClient.post()
                .uri("https://api.paystack.co/transaction/initialize")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer sk_test_14fd1596d1f2f9539a03e484b7ca736f954bbc6e")
                .bodyValue(paymentDTO)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    // Extract the response body which contains detailed error information
                    String responseBody = ex.getResponseBodyAsString();

                    // Log both the exception and the response body
                    System.err.println("Status code: " + ex.getStatusCode());
                    System.err.println("Response body: " + responseBody);

                    // Return a more informative error
                    return Mono.error(new RuntimeException("Paystack API error: " +
                            ex.getStatusCode() + " - " + responseBody));
                });
    }

    @Transactional
    public ResponseEntity<String> order(Long userId, List<Long> productIds, Integer discount) {

        try {
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        Buyer buyer = (Buyer) user;
        List<Product> products = new ArrayList<>();
        int sum = 0;
            for (Long productId : productIds) {
                Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
                products.add(product);
                sum += product.getPrice();
            }
        Order order = new Order(products, buyer);
        order.setStatus(Status.INITIATED);
        orderRepository.save(order);
        PaymentDTO paymentDTO = new PaymentDTO((sum - discount) * 100, "bzakariyya6@gmail.com");
        var response = url(paymentDTO).block();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response);
        String url = node.path("data").path("authorization_url").asText();
        log.info(String.format("Url: %s", url));
        return ResponseEntity.status(HttpStatus.CREATED).body(url);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @Transactional
    public ResponseEntity<List<OrderDTO>> fetchBuyerOrders(Long aLong) {

        User user = userRepository.findById(aLong).orElseThrow(RuntimeException::new);
        Buyer buyer = (Buyer) user;
        List<Order> orders = buyer.getOrders();
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order: orders) {
            OrderDTO orderDTO = new OrderDTO(buyer.getId(), order.getId());
            orderDTO.setProducts(order.getProducts().stream().map(ProductDTO::new).collect(Collectors.toList()));
            orderDTOs.add(orderDTO);
        }


        return ResponseEntity.status(HttpStatus.OK).body(orderDTOs);
    }

    @Transactional
    public ResponseEntity<List<Order>> fetchSellerOrders(Long aLong) {

        User user = userRepository.findById(aLong).orElseThrow(RuntimeException::new);
        Seller seller = (Seller) user;
        List<Order> orders = seller.getOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }


    public ResponseEntity<Integer> getNumberOfItems() {
        Set<Order> items = new HashSet<>();
        List<Order> numItems = orderRepository.findAll();
        numItems.stream().map((order) -> {
            for(Order orders : numItems){
                items.add(orders);
            }
            return items;
        } );
        return ResponseEntity.ok(items.size());
    }
}
