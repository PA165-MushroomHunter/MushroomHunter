package cz.muni.fi.pa165.mushrooms.mvc;

import javax.servlet.http.HttpServletRequest;

import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterDTO;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

public class Tools {

    public static String redirectNonAdmin(HttpServletRequest request, UriComponentsBuilder builder, RedirectAttributes redirectAttributes) {
        MushroomHunterDTO user = (MushroomHunterDTO) request.getSession().getAttribute("user");
        if(user == null || !user.isAdmin()){
            redirectAttributes.addFlashAttribute("alert_warning", "You are not authorized!");
            return "redirect:" + builder.path("/").build().toUriString();
        }
        return null;
    }
}