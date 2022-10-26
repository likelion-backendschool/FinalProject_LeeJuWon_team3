package com.project.Week_Mission.app.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.Week_Mission.app.base.entity.BaseEntity;
import com.project.Week_Mission.app.cart.entity.CartItem;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.mybook.MyBook;
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

import static javax.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Product extends BaseEntity {

    private String subject;
    private int price;

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




    public Product(long id) {
        super(id);
    }

    public int getSalePrice() {
        return getPrice();
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
}
