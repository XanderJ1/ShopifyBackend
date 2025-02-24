package shopify.Data.DTOs;


import lombok.Data;

@Data
public class ProductDTO {
    private String name;
    private String description;
    private Integer price;

    public ProductDTO(String name, String description, Integer price){
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
