package andrea_freddi.CAPSTONE_PROJECT.services;

import andrea_freddi.CAPSTONE_PROJECT.entities.Address;
import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.exception.BadRequestException;
import andrea_freddi.CAPSTONE_PROJECT.payloads.AddressPayload;
import andrea_freddi.CAPSTONE_PROJECT.repositories.AddressesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


// This service class manages CRUD operations related to Address entities
// All operations are restricted to the authenticated user who owns the address.

@Service
public class AddressesService {

    // Injecting the AddressesRepository to access the database
    @Autowired
    private AddressesRepository addressesRepository;

    // This method finds an address by its ID
    public Address findById(UUID addressId) {
        return this.addressesRepository.findById(addressId).orElseThrow(
                () -> new BadRequestException("Address with id " + addressId + " not found!")
        );
    }

    // This method retrieves all addresses belonging to a specific user
    public List<Address> findAllByUser(User user) {
        return this.addressesRepository.findByUser(user);
    }

    // This method saves a new address for a user
    public Address save(AddressPayload payload, User user) {
        // Check if the address already exists for the user
        this.addressesRepository.findByLabelAndUser(payload.label(), user).ifPresent(
                address -> {
                    throw new BadRequestException("Address with label " + payload.label() + " already exists!");
                }
        );
        // If not, create a new address
        Address newAddress = new Address(payload.label(), payload.addressLine(), payload.city(),
                payload.province(), payload.postalCode(), payload.country(), user);
        // Save the new address to the database
        return this.addressesRepository.save(newAddress);
    }

    // This method updates an existing address if it belongs to the authenticated user
    public Address findByIdAndUpdate(UUID addressId, AddressPayload payload, User user) {
        // Find the address by ID
        Address found = this.findById(addressId);
        // Check 

        found.setLabel(payload.label());
        found.setAddressLine(payload.addressLine());
        found.setCity(payload.city());
        found.setProvince(payload.province());
        found.setPostalCode(payload.postalCode());
        found.setCountry(payload.country());

        return this.addressesRepository.save(found);
    }

    // This method deletes an address if it belongs to the authenticated user
    public void findByIdAndDelete(UUID id, User user) {
        Address found = this.findById(id);

        if (!found.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You are not authorized to delete this address");
        }

        this.addressesRepository.delete(found);
    }
}
