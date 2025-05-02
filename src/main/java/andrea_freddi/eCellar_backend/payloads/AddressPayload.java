package andrea_freddi.eCellar_backend.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * AddressPayload is a record that represents the payload for an address.
 * It contains fields for label, address line, city, province, postal code, and country.
 * Each field has validation constraints to ensure the data is valid.
 */

public record AddressPayload(

        @NotBlank(message = "Label is required (e.g., 'Home', 'Work')")
        @Size(max = 30, message = "Label must be at most 30 characters")
        String label,

        @NotBlank(message = "Address line is required")
        @Size(max = 100)
        String addressLine,

        @NotBlank(message = "City is required")
        @Size(max = 50)
        String city,

        @NotBlank(message = "Province is required")
        @Size(max = 50)
        String province,

        @NotBlank(message = "Postal code is required")
        @Size(max = 20)
        String postalCode,

        @NotBlank(message = "Country is required")
        @Size(max = 50)
        String country
) {
}

