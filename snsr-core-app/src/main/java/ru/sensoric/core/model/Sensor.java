package ru.sensoric.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Table(name = "sensors")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5)
    @NaturalId
    private String serialNumber;

    @Column
    private boolean registered = false;

    @JsonIgnore
    @OneToMany(mappedBy = "sensor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SensorValue> values = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "base_station_id")
    private BaseStation station;

    public Sensor(String serialNumber, BaseStation station) {
        this.serialNumber = serialNumber;
        this.station = station;
    }
}
