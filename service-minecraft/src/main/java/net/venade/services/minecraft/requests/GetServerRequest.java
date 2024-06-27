package net.venade.services.minecraft.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Nikolas Rummel
 * @since 07.05.22
 */
@Data
@AllArgsConstructor
public class GetServerRequest {

    private String email;

}
