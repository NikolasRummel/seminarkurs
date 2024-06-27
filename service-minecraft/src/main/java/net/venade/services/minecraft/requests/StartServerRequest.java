package net.venade.services.minecraft.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Nikolas Rummel
 * @since 23.04.22
 */
@Data
@AllArgsConstructor
public class StartServerRequest {

    private String email;

}
