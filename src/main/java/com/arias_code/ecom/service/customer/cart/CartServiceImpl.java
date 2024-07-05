package com.arias_code.ecom.service.customer.cart;

import com.arias_code.ecom.dto.AddProductInCartDTO;
import com.arias_code.ecom.dto.CartItemsDTO;
import com.arias_code.ecom.dto.OrderDTO;
import com.arias_code.ecom.entity.CartItems;
import com.arias_code.ecom.entity.Order;
import com.arias_code.ecom.entity.Product;
import com.arias_code.ecom.entity.User;
import com.arias_code.ecom.enums.OrderStatus;
import com.arias_code.ecom.repository.CartItemsRepository;
import com.arias_code.ecom.repository.OrderRepository;
import com.arias_code.ecom.repository.ProductRepository;
import com.arias_code.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<?> addProductToCart(AddProductInCartDTO addProductInCartDTO){
        Order activeOrder = orderRepository
                .findByUserIdAndOrderStatus(
                        addProductInCartDTO.getUserId(), OrderStatus.PENDING
                );
        Optional<CartItems> optionalCartItems = cartItemsRepository
                .findByProductIdAndOrderIdAndUserId(
                        addProductInCartDTO.getProductId(),
                        activeOrder.getId(),
                        addProductInCartDTO.getUserId()
                );
        if (optionalCartItems.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }else {
            Optional<Product> optionalProduct = productRepository.findById(addProductInCartDTO.getProductId());
            Optional<User> optionalUser = userRepository.findById(addProductInCartDTO.getUserId());

            if (optionalProduct.isPresent() && optionalUser.isPresent()){

                CartItems cart = new CartItems();
                cart.setProduct(optionalProduct.get());
                cart.setPrice(optionalProduct.get().getPrice());
                cart.setQuantity(1L);
                cart.setUser(optionalUser.get());
                cart.setOrder(activeOrder);

                CartItems updatedCart = cartItemsRepository.save(cart);

                activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cart.getPrice());
                activeOrder.setAmount(activeOrder.getAmount() + cart.getPrice());
                activeOrder.getCartItems().add(cart);

                orderRepository.save(activeOrder);

                return ResponseEntity.status(HttpStatus.CREATED).body(cart);

            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Product not found");
            }
        }
    }

    public OrderDTO getCartByUserId(Long userId){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);

        List<CartItemsDTO> cartItemsDTOS = activeOrder
                .getCartItems()
                .stream()
                .map(CartItems::getCartDto)
                .toList(); //collect(Collectors.toList()) es la factorization

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAmount(activeOrder.getAmount());
        orderDTO.setId(activeOrder.getId());
        orderDTO.setOrderStatus(activeOrder.getOrderStatus());
        orderDTO.setDiscount(activeOrder.getDiscount());
        orderDTO.setTotalAmount(activeOrder.getTotalAmount());
        orderDTO.setCartItems(cartItemsDTOS);
        
        return orderDTO;

    }
}
