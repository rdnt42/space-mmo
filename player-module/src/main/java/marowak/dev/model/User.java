package marowak.dev.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_activate", nullable = false)
    private boolean isActivate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
//    @CreationTimestamp
    private LocalDateTime updatedAt;

    @UpdateTimestamp
    private LocalDateTime lastLoggedIn;
}
