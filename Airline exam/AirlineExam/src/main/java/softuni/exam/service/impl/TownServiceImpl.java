package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.jsons.TownImportDto;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TownServiceImpl implements TownService {

    private static final String TOWN_PATH = "src/main/resources/files/json/towns.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;

    @Autowired
    public TownServiceImpl(ModelMapper modelMapper, Gson gson, TownRepository townRepository, ValidationUtil validationUtil) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(TOWN_PATH)));
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder sb = new StringBuilder();
        TownImportDto[] townImportDtos = this.gson.fromJson(this.readTownsFileContent(), TownImportDto[].class);

        for (TownImportDto townImportDto : townImportDtos) {
            if(this.validationUtil.isValid(townImportDto)){
                Town town = this.modelMapper.map(townImportDto, Town.class);
                this.townRepository.saveAndFlush(town);
                sb.append(String.format("Successfully imported Town %s - %d"
                        , townImportDto.getName(), townImportDto.getPopulation()))
                        .append(System.lineSeparator());
            }else{
                sb.append("Invalid Town.")
                        .append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }
}
