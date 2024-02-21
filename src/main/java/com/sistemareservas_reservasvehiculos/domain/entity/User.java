package com.sistemareservas_reservasvehiculos.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Anotaciones de Lombok para reducir el boilerplate (código repetitivo)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity // Indica que esta clase es una entidad JPA
@Table(name = "\"user\"", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})}) // Mapea la entidad a una tabla llamada "user" con restricción única en "email"
public class User implements UserDetails { // Implementa UserDetails para integración con Spring Security

  @Id // Marca el campo como la clave primaria
  @SequenceGenerator(
          name = "user_id_sequence",
          sequenceName = "user_id_sequence"
  )
  @GeneratedValue(
          strategy = GenerationType.SEQUENCE,
          generator = "user_id_sequence"
  ) // Configura la generación de ID usando una secuencia
  private Integer id; // Identificador único del usuario
  private String firstName; // Nombre del usuario
  private String lastName; // Apellido del usuario
  private String password; // Contraseña para autenticación
  private String email; // Email del usuario, utilizado como username
  private String phone; // Teléfono del usuario
  private Boolean enable; // Indica si el usuario está habilitado

  @ElementCollection(targetClass = ERole.class, fetch = FetchType.EAGER) // Colección de roles cargada de manera eager
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id")) // Mapea los roles a una tabla separada
  @Enumerated(EnumType.STRING)
  @Column(name = "role_name") // Nombres de los roles
  private Set<ERole> roles; // Conjunto de roles del usuario

  @JsonManagedReference
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) // Relación uno a muchos con Booking, cargada de manera lazy
  @ToString.Exclude // Excluye las reservas del método toString para evitar recursión
  private List<Booking> bookings; // Lista de reservas asociadas al usuario

  // Sobrescribe equals para proporcionar una comparación efectiva de instancias de User, incluyendo el manejo de proxies de Hibernate
  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    User user = (User) o;
    return getId() != null && Objects.equals(getId(), user.getId());
  }

  // Sobrescribe hashCode para asegurar una generación de hash coherente, especialmente importante para el manejo de instancias de proxy
  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }

  // Implementa el método getAuthorities de UserDetails, convirtiendo roles a GrantedAuthority
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
            .collect(Collectors.toList());
  }

  // Implementa getUsername de UserDetails, usando el email como nombre de usuario
  @Override
  public String getUsername() {
    return this.email;
  }

  // Implementa getPassword de UserDetails, devolviendo la contraseña del usuario
  @Override
  public String getPassword() {
    return this.password;
  }

  // Métodos de UserDetails para verificar el estado de la cuenta y las credenciales
  @Override
  public boolean isAccountNonExpired() {
    return true; // La cuenta nunca expira
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  } // La cuenta nunca está bloqueada

  // Implementa isCredentialsNonExpired de UserDetails, indicando que las credenciales nunca expiran
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  // Implementa isEnabled de UserDetails para verificar si el usuario está habilitado o no
  @Override
  public boolean isEnabled() {
    return this.enable; // Utiliza el campo 'enable' para determinar si el usuario está habilitado
  }
}
