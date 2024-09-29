package kr.co.polycube.backendtest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "lotto") // Ensure this matches your SQL table name
public class Lotto {
    @Id
    private Long id;
    @Column(name = "number_1")
    private int number_1;
    @Column(name = "number_2")
    private int number_2;
    @Column(name = "number_3")
    private int number_3;
    @Column(name = "number_4")
    private int number_4;
    @Column(name = "number_5")
    private int number_5;
    @Column(name = "number_6")
    private int number_6;
}
