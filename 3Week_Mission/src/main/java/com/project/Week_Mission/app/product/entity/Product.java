package com.project.Week_Mission.app.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.Week_Mission.app.base.entity.BaseEntity;
import com.project.Week_Mission.app.cart.entity.CartItem;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.mybook.entity.MyBook;
import com.project.Week_Mission.app.order.entity.OrderItem;
import com.project.Week_Mission.app.postkeyword.entity.PostKeyword;
import com.project.Week_Mission.app.productTag.entity.ProductTag;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Product extends BaseEntity {

    private String subject;
    private int stockQuantity;
    private int price;
    private int pricePerOne;
    private int salePrice;
    private int quantity;

    private boolean isSoldOut; // 관련 옵션들이 전부 판매불능 상태일 때 '주의:품절이 아님'

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id")
    private Member author;


    @ManyToOne(fetch = LAZY)
    private PostKeyword postKeyword;


    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<MyBook> myBooks = new ArrayList<>();


    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true)
    private List<ProductOption> productOptions = new ArrayList<>();


    public void addOption(ProductOption option) {
        option.setProduct(this);
        option.setPrice(getPrice());

        productOptions.add(option);
    }

    public Product(long id) {
        super(id);
    }


    public Product(Member author, PostKeyword postKeyword) {
        this.author = author;
        this.postKeyword = postKeyword;
    }


    // cartItem 다 가져오는 문제 있음 -> order.price로 처리함
    // 사용하지 않는 메서드
    public int getSalePrice() {
        int price = 0;

        for (CartItem cartItem : cartItems) {
//            price += cartItem.getPrice() * cartItem.getQuantity();
            price += cartItem.getPrice();
         }

        return price;
    }

    public int getWholesalePrice() {
        return (int) Math.ceil(getPrice() * 0.4);
    }

    public boolean isOrderable() {
        return true;
    }

    public String getJdenticon() {
        return "product__" + getId();
    }

    public String getExtra_inputValue_hashTagContents() {
        Map<String, Object> extra = getExtra();

        if (extra.containsKey("productTags") == false) {
            return "";
        }

        List<ProductTag> productTags = (List<ProductTag>) extra.get("productTags");

        if (productTags.isEmpty()) {
            return "";
        }

        return productTags
                .stream()
                .map(productTag -> "#" + productTag.getProductKeyword().getContent())
                .sorted()
                .collect(Collectors.joining(" "));
    }

    public String getExtra_productTagLinks() {
        Map<String, Object> extra = getExtra();

        if (extra.containsKey("productTags") == false) {
            return "";
        }

        List<ProductTag> productTags = (List<ProductTag>) extra.get("productTags");

        if (productTags.isEmpty()) {
            return "";
        }

        return productTags
                .stream()
                .map(productTag -> {
                    String text = "#" + productTag.getProductKeyword().getContent();

                    return """
                            <a href="%s" class="text-link">%s</a>
                            """
                            .stripIndent()
                            .formatted(productTag.getProductKeyword().getListUrl(), text);
                })
                .sorted()
                .collect(Collectors.joining(" "));
    }

    public void updateSubject(String subject) {
        this.subject = subject;
    }

    public void updatePrice(int price) {
        this.price = price;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
