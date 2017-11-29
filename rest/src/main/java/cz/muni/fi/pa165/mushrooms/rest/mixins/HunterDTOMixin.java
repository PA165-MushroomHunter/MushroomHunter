package cz.muni.fi.pa165.mushrooms.rest.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "passwordHash"})
public class HunterDTOMixin {

}
