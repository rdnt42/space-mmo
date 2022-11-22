package marowak.dev.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 22.11.2022
 * Time: 0:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    private String email;

    private boolean isActivate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
//    @CreationTimestamp
    private LocalDateTime updatedAt;

    @UpdateTimestamp
    private LocalDateTime lastLoggedIn;
}
