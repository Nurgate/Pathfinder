package bg.softuni.pathfinder.repository;

import bg.softuni.pathfinder.model.Route;
import bg.softuni.pathfinder.model.enums.CategoryNames;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Route findByName(String name);

     List<Route> findAllByCategories_Name(CategoryNames categoryName);
}
