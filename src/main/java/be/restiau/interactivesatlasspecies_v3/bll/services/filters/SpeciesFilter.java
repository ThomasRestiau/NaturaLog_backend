package be.restiau.interactivesatlasspecies_v3.bll.services.filters;

import be.restiau.interactivesatlasspecies_v3.bll.models.dto.SpeciesDetailsEnriched;

import java.util.function.Predicate;

public class SpeciesFilter {

    private String scientificName;
    private String vernacularName;
    private String kingdom;
    private String phylum;
    private String order;
    private String family;
    private String genus;
    private String species;
    private String country;

    public SpeciesFilter scientificName(String scientificName) {
        this.scientificName = scientificName;
        return this;
    }

    public SpeciesFilter vernacularName(String vernacularName) {
        this.vernacularName = vernacularName;
        return this;
    }

    public SpeciesFilter kingdom(String kingdom) {
        this.kingdom = kingdom;
        return this;
    }

    public SpeciesFilter phylum(String phylum) {
        this.phylum = phylum;
        return this;
    }

    public SpeciesFilter order(String order) {
        this.order = order;
        return this;
    }

    public SpeciesFilter family(String family) {
        this.family = family;
        return this;
    }

    public SpeciesFilter genus(String genus) {
        this.genus = genus;
        return this;
    }

    public SpeciesFilter species(String species) {
        this.species = species;
        return this;
    }

    public SpeciesFilter country(String country) {
        this.country = country;
        return this;
    }

    public Predicate<SpeciesDetailsEnriched> toPredicate() {
        return s -> {
            if (scientificName != null && (s.scientificName() == null ||
                    !s.scientificName().toLowerCase().contains(scientificName.toLowerCase()))) {
                return false;
            }

            if (vernacularName != null && (s.vernacularName() ==null ||
                    !s.vernacularName().toLowerCase().contains(vernacularName.toLowerCase()))) {
                return false;
            }

            if (kingdom != null && !kingdom.equalsIgnoreCase(s.kingdom())) return false;
            if (phylum != null && !phylum.equalsIgnoreCase(s.phylum())) return false;
            if (order != null && !order.equalsIgnoreCase(s.order())) return false;
            if (family != null && !family.equalsIgnoreCase(s.family())) return false;
            if (genus != null && !genus.equalsIgnoreCase(s.genus())) return false;
            if (species != null && !species.equalsIgnoreCase(s.species())) return false;

            if (country != null) {
                if (s.coords() == null || s.coords().stream().noneMatch(coord ->
                        country.equalsIgnoreCase(coord.country()))) {
                    return false;
                }
            }

            return true;
        };
    }
}
