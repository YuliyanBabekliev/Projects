package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.xmls.TicketImportDto;
import softuni.exam.models.dtos.xmls.TicketImportRootDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Plane;
import softuni.exam.models.entities.Ticket;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TicketService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TicketServiceImpl implements TicketService {

    private static final String TICKET_PATH = "src/main/resources/files/xml/tickets.xml";
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final TicketRepository ticketRepository;
    private final PlaneRepository planeRepository;
    private final PassengerRepository passengerRepository;
    private final TownRepository townRepository;

    @Autowired
    public TicketServiceImpl(ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, TicketRepository ticketRepository, PlaneRepository planeRepository, PassengerRepository passengerRepository, TownRepository townRepository) {
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.ticketRepository = ticketRepository;
        this.planeRepository = planeRepository;
        this.passengerRepository = passengerRepository;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return this.ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(TICKET_PATH)));
    }

    @Override
    public String importTickets() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        TicketImportRootDto ticketImportRootDto = this.xmlParser.parseXml(TicketImportRootDto.class, TICKET_PATH);

        for (TicketImportDto ticketDto : ticketImportRootDto.getTickets()) {
            if(this.validationUtil.isValid(ticketDto)) {
                Town town1 = this.townRepository.findByName(ticketDto.getFromTown().getName());
                Town town2 = this.townRepository.findByName(ticketDto.getToTown().getName());
                Passenger passenger = this.passengerRepository.findByEmail(ticketDto.getPassenger().getEmail());
                Plane plane = this.planeRepository.findByRegisterNumber(ticketDto.getPlane().getRegisterNumber());
                Ticket ticket = this.modelMapper.map(ticketDto, Ticket.class);
                ticket.setFromTown(town1);
                ticket.setToTown(town2);
                ticket.setPassenger(passenger);
                ticket.setPlane(plane);

                this.ticketRepository.saveAndFlush(ticket);
                sb.append(String.format("Successfully imported Ticket %s - %s",
                        ticketDto.getFromTown().getName(), ticketDto.getToTown().getName()))
                        .append(System.lineSeparator());
            }else{
                sb.append("Invalid ticket")
                        .append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }
}
