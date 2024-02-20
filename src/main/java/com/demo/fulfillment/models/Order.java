package com.demo.fulfillment.models;

import com.demo.fulfillment.models.subtypes.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Access(AccessType.FIELD)
@Table(name = "orders")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "customer_id")
    private UUID customerId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    @Fetch(FetchMode.JOIN)
    @LazyGroup("customers")
    @JoinColumn(name = "customer_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_customer_id_order"))
    private Customer customer;

    @Type(JsonBinaryType.class)
    @Column(name = "items", columnDefinition = "jsonb")
    private List<OrderItem> items;

    @Column(name = "created_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @UpdateTimestamp
    private Date updatedAt;
}
