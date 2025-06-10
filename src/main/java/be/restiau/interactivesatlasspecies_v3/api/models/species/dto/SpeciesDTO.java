package be.restiau.interactivesatlasspecies_v3.api.models.species.dto;

import be.restiau.interactivesatlasspecies_v3.dl.entities.Species;

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
