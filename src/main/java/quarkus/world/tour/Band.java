package quarkus.world.tour;


import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Band extends PanacheEntity {

    @NotBlank
    @Column(unique = true)
    public String name;
    public boolean alive;

    @Min(1945)
    public int creationYear;
    public int terminationYear = -1;

    public static Uni<List<Band>> listAlive() {
        return Band.list("alive", true);
    }
}
