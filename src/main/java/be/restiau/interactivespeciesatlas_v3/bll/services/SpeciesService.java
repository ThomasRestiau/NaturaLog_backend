package be.restiau.interactivespeciesatlas_v3.bll.services;

import be.restiau.interactivespeciesatlas_v3.dl.entities.Species;
import be.restiau.interactivespeciesatlas_v3.dl.entities.User;

public interface SpeciesService {

    void addSpecies(User user, Species species);
    void removeSpecies(User user, Species species);
}
