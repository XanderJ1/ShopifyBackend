package shopify.Data.DTOs;


import jakarta.persistence.Lob;
import lombok.Data;
import shopify.Data.Models.Product;

import java.util.Base64;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private String imageName;
    private String imageType;
    private String imageData;


    public ProductDTO(){

    }
    public ProductDTO(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imageName = product.getImageName();
        this.imageType = product.getImageType();
        this.imageData = Base64.getEncoder().encodeToString(product.getImageData());
    }


}
