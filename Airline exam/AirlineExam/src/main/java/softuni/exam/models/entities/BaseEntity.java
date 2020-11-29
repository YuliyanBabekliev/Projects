package softuni.exam.models.entities;

import javax.persistence.*;

@MappedSuperclass
public class BaseEntity {
    private Integer id;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
