package ru.sensoric.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Table(name = "base_stations")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class BaseStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5)
    @NaturalId
    private String serialNumber;

    @Column
    private boolean registered = false;

    @OneToMany(mappedBy = "station", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Sensor> sensors = new ArrayList<>();

    public BaseStation(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
