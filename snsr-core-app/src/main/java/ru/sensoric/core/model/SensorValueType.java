package ru.sensoric.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Table(name = "sensor_value_types")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class SensorValueType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String code;

    @Column
    private String typeName;

    public SensorValueType(String name, String code) {
        this.code = code;
        this.typeName = name;
    }
}
