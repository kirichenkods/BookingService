package ru.sber.bookingservice.model;

import jakarta.persistence.*;
import lombok.*;
import ru.sber.bookingservice.model.enums.DurationType;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "public", name = "users_resources")
public class BookingInfo {
    @Id
    @SequenceGenerator(name = "pk_duration_seq", sequenceName = "users_resources_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_duration_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_user"))
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "resource_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_resource"))
    private Resource resource;

    @Column(name = "duration_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DurationType durationType;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "date_start", nullable = false)
    private LocalDateTime dateStart;

    @Column(name = "date_end", nullable = false)
    private LocalDateTime dateEnd;
}
