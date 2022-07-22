package nl.kooi.camundarules;

import lombok.Getter;
import lombok.Setter;
import nl.kooi.camundarules.enums.Label;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class CustomerLabel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    @Enumerated(EnumType.STRING)
    private Label label;
}
