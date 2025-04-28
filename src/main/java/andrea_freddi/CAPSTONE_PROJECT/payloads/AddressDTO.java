package andrea_freddi.CAPSTONE_PROJECT.payloads;

/*
 * Data Transfer Object for Address entity
 * This class is used to transfer address data between different layers of the application
 * It contains all the necessary fields to represent a address
 */

import java.util.UUID;

public record AddressDTO(
        UUID id,
        String label,
        String addressLine,
        String city,
        String province,
        String postalCode,
        String country,
        UUID userId
) {
}
