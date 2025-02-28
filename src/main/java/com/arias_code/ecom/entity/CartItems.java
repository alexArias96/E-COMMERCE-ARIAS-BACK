package com.arias_code.ecom.entity;

import com.arias_code.ecom.dto.CartItemsDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long price;

    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public CartItemsDTO getCartDto(){
        CartItemsDTO cartItemsDTO = new CartItemsDTO();
        cartItemsDTO.setId(id);
        cartItemsDTO.setPrice(price);
        cartItemsDTO.setProductId(product.getId());
        cartItemsDTO.setQuantity(quantity);
        cartItemsDTO.setUserId(user.getId());
        cartItemsDTO.setProductName(product.getName());
        cartItemsDTO.setReturnedImg(product.getImg());

        return cartItemsDTO;
    }
}
