package bg.softuni.pathfinder.service.impl;

import bg.softuni.pathfinder.exceptions.RouteNotFoundException;
import bg.softuni.pathfinder.model.Route;
import bg.softuni.pathfinder.model.dto.binding.AddRouteBindingModel;
import bg.softuni.pathfinder.model.dto.view.RouteDetailsViewModel;
import bg.softuni.pathfinder.model.dto.view.RouteViewModel;
import bg.softuni.pathfinder.repository.RouteRepository;
import bg.softuni.pathfinder.service.RouteService;
import bg.softuni.pathfinder.service.session.LoggedUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RouteServiceImpl implements RouteService {

    private static final String BASE_GPX_COORDINATES_PATH = ".\\src\\main\\resources\\coordinates\\";
    private final RouteRepository routeRepository;
    private final ModelMapper modelMapper;

    private final LoggedUser loggedUser;

    public RouteServiceImpl(RouteRepository routeRepository,
                            ModelMapper modelMapper, LoggedUser loggedUser) {
        this.routeRepository = routeRepository;
        this.modelMapper = modelMapper;
        this.loggedUser = loggedUser;
    }

    @Override
    public List<RouteViewModel> getAll() {
        return routeRepository.findAll().stream()
                .map(route -> modelMapper.map(route, RouteViewModel.class))
                .toList();
    }

    @Override
    public void add(AddRouteBindingModel addRouteBindingModel) {

        Route route = modelMapper.map(addRouteBindingModel, Route.class);

        String filePath = getFilePath(route.getName());


        try {
            File file = new File(BASE_GPX_COORDINATES_PATH + filePath);
            file.getParentFile().mkdirs();
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(addRouteBindingModel.getGpxCoordinates().getBytes());
            route.setGpxCoordinates(filePath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        String regex = "v=(.*)";
        Pattern compile = Pattern.compile(regex);

        Matcher matcher = compile.matcher(addRouteBindingModel.getVideoUrl());
        if (matcher.find()) {
            String url = matcher.group(1);
            route.setVideoUrl(url);
        }

        System.out.println();

        routeRepository.save(route);
    }

    private String getFilePath(String routeName) {
        String pathPattern = "%s\\%s_%s.xml";
        return String.format(pathPattern ,
                loggedUser.getUsername(),
                transformRouteName(routeName),
                UUID.randomUUID());
    }

    private String transformRouteName(String routeName) {
        return routeName.toLowerCase()
                .replaceAll("\\s+", "_");
    }

    @Override
    public RouteDetailsViewModel getDetails(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException("Route with id: " + id + " was not found!"));

        RouteDetailsViewModel routeDetailsViewModel = modelMapper.map(route, RouteDetailsViewModel.class);
        routeDetailsViewModel.setAuthorName(route.getAuthor().getFullName());

        return routeDetailsViewModel;
    }
}
