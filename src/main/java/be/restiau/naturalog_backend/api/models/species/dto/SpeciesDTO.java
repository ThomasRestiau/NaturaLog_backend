package be.restiau.naturalog_backend.api.models.species.dto;

import be.restiau.naturalog_backend.dl.entities.Species;

public record SpeciesDTO (
        String gbifId,
        String scientificName,
        String vernacularName,
        String canonicalName
){
   public static SpeciesDTO fromSpecies(Species species) {
       return new SpeciesDTO(
               species.getGbifId(),
               species.getScientificName(),
               species.getVernacularName(),
               species.getCanonicalName()
       );
   }
}
