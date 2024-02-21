package com.sistemareservas_reservasvehiculos.domain.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

    @Id
    @SequenceGenerator(
            name = "vehicle_id_sequence",
            sequenceName = "vehicle_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "vehicle_id_sequence"
    )

    private Integer id;
    private String brand; //marca
    private String reference;
    private String typeVehicle;
    private String manufactureYear; //anio fabricacion
    private String color;
    private String typeTransmission;
    private String numberDoors;
    private String typeFuel;
    private String imageUrl;
    private Double price;
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "id_booking")
    private Booking booking;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Vehicle vehicle = (Vehicle) o;
        return getId() != null && Objects.equals(getId(), vehicle.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }


}
