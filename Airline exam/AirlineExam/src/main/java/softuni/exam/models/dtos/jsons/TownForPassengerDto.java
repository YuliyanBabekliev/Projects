package softuni.exam.models.dtos.jsons;

import com.google.gson.annotations.Expose;

public class TownForPassengerDto {
    @Expose
    private int id;

    public TownForPassengerDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
