package cz.muni.fi.pa165.mushrooms.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The DTO for transferring data relevant to a user login attempt.
 *
 * @author bkompis
 */
public class MushroomHunterAuthenticateDTO {
    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;
    @NotNull
    @Size(min = 8, max = 150)
    private String unencryptedPassword;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return unencryptedPassword;
    }

    public void setPassword(String password) {
        this.unencryptedPassword = password;
    }
}
