package andrea_freddi.CAPSTONE_PROJECT.mappers;

import andrea_freddi.CAPSTONE_PROJECT.entities.Address;
import andrea_freddi.CAPSTONE_PROJECT.payloads.AddressDTO;
import org.springframework.stereotype.Component;

// This class is responsible for mapping between Address entities and AddressDTOs

@Component
public class AddressMapper {

    // Converts an Address entity to an AddressDTO
    public AddressDTO addressToDTO(Address address) {
        if (address == null) return null;

        return new AddressDTO(
                address.getId(),
                address.getLabel(),
                address.getAddressLine(),
                address.getCity(),
                address.getProvince(),
                address.getPostalCode(),
                address.getCountry(),
                address.getUser() != null ? address.getUser().getId() : null
        );
    }
}
