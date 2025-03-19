package shopify.Data.Models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private String imageName;
    private String imageType;
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private byte[] imageData;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private Seller seller;

    @ManyToMany(mappedBy = "cart")
    @JsonBackReference
    private Set<Buyer> buyers = new HashSet<>();

    @ManyToOne
    @JsonBackReference
    private Order order;

    public Product(){

    }
    public Product(Long id, String name, String description, Integer price){
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Product(String name, String description, Integer price, Seller seller){
        this.name = name;
        this.description = description;
        this.price = price;
        this.seller = seller;
    }


    // Seller Constructor
    public Product(String name, String description, Integer price,
                   Seller seller, String imageName, String imageType, byte[] imageData){
        this.name = name;
        this.description = description;
        this.price = price;
        this.seller = seller;
        this.imageData = imageData;
        this.imageName = imageName;
        this.imageType = imageType;

    }

    //Buyer Constructor
    public Product(String name, String description, Integer price,
                   Set<Buyer> seller, String imageName, String imageType, byte[] imageData){
        this.name = name;
        this.description = description;
        this.price = price;
        this.buyers = seller;
        this.imageData = imageData;
        this.imageName = imageName;
        this.imageType = imageType;

    }

    public Product(String name, String description, Integer price,
                   String imageName, String imageType, byte[] imageData){
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageData = imageData;
        this.imageName = imageName;
        this.imageType = imageType;

    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
