package shopify.Data.Models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    private User assignedUser;

    public Product(){

    }
    public Product(Long id, String name, String description, Integer price){
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Product(String name, String description, Integer price, User assignedUser){
        this.name = name;
        this.description = description;
        this.price = price;
        this.assignedUser = assignedUser;
    }

    public Product(String name, String description, Integer price,
                   User assignedUser,String imageName, String imageType, byte[] imageData){
        this.name = name;
        this.description = description;
        this.price = price;
        this.assignedUser = assignedUser;
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
