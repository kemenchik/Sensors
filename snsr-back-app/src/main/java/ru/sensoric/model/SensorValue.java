package ru.sensoric.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sensoric.model.audit.DateAudit;

import javax.persistence.*;

@Table(name = "sensor_values")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class SensorValue extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double value;

    @Column
    private String code;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    public SensorValue(Double value, String code, Sensor sensor) {
        this.value = value;
        this.code = code;
        this.sensor = sensor;
    }

    public SensorValue(Double value, String code) {
        this.value = value;
        this.code = code;
    }
}
