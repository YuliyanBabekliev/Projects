package softuni.exam.models.dtos.xmls;

import org.hibernate.validator.constraints.Length;
import softuni.exam.config.LocalDateTimeAdapter;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlRootElement(name = "ticket")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketImportDto {
    @XmlElement(name = "serial-number")
    @Length(min = 2)
    private String serialNumber;
    @XmlElement
    @Min(value = 0)
    private BigDecimal price;
    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime takeOff;
    @XmlElement(name = "from-town")
    private FromTownImportDto fromTown;
    @XmlElement(name = "to-town")
    private ToTownImportDto toTown;
    @XmlElement
    private PassengerImportForTicketDto passenger;
    @XmlElement
    private PlaneImportForTicketDto plane;

    public TicketImportDto() {
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getTakeOff() {
        return takeOff;
    }

    public void setTakeOff(LocalDateTime takeOff) {
        this.takeOff = takeOff;
    }

    public FromTownImportDto getFromTown() {
        return fromTown;
    }

    public void setFromTown(FromTownImportDto fromTown) {
        this.fromTown = fromTown;
    }

    public ToTownImportDto getToTown() {
        return toTown;
    }

    public void setToTown(ToTownImportDto toTown) {
        this.toTown = toTown;
    }

    public PassengerImportForTicketDto getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerImportForTicketDto passenger) {
        this.passenger = passenger;
    }

    public PlaneImportForTicketDto getPlane() {
        return plane;
    }

    public void setPlane(PlaneImportForTicketDto plane) {
        this.plane = plane;
    }
}
