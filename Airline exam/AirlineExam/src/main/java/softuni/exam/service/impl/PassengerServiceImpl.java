package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.jsons.PassengerImportDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

@Service
public class PassengerServiceImpl implements PassengerService {

    private static final String PASSENGER_PATH = "src/main/resources/files/json/passengers.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final PassengerRepository passengerRepository;
    private final ValidationUtil validationUtil;
    private final TownRepository townRepository;


    @Autowired
    public PassengerServiceImpl(ModelMapper modelMapper, Gson gson, PassengerRepository passengerRepository, ValidationUtil validationUtil, TownRepository townRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.passengerRepository = passengerRepository;
        this.validationUtil = validationUtil;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return this.passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(PASSENGER_PATH)));
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder sb = new StringBuilder();
        PassengerImportDto[] passengerImportDtos = this.gson.fromJson(this.readPassengersFileContent(), PassengerImportDto[].class);

        for (PassengerImportDto passengerImportDto : passengerImportDtos) {
            if(this.validationUtil.isValid(passengerImportDto)){
                Passenger passenger = this.modelMapper.map(passengerImportDto, Passenger.class);
                Town town = this.townRepository.findByName(passengerImportDto.getTown());
                passenger.setTown(town);

                this.passengerRepository.saveAndFlush(passenger);

                sb.append(String.format("Successfully imported Passenger %s - %s",
                        passengerImportDto.getLastName(), passengerImportDto.getEmail()))
                        .append(System.lineSeparator());
            }else{
                sb.append("Invalid passenger.")
                        .append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder sb = new StringBuilder();
        Set<Passenger> passengers = this.passengerRepository.findPassengers();

        for (Passenger passenger : passengers) {
            sb.append(String.format("Passenger %s %s", passenger.getFirstName(), passenger.getLastName()))
                    .append(System.lineSeparator())
                    .append(String.format("\tEmail - %s", passenger.getEmail()))
                    .append(System.lineSeparator())
                    .append(String.format("\tPhone - %s", passenger.getPhoneNumber()))
                    .append(System.lineSeparator())
                    .append(String.format("\tNumber of tickets - %d", passenger.getTickets().size()))
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}
