package andrea_freddi.CAPSTONE_PROJECT.services;

import andrea_freddi.CAPSTONE_PROJECT.entities.Address;
import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.exception.BadRequestException;
import andrea_freddi.CAPSTONE_PROJECT.payloads.AddressPayload;
import andrea_freddi.CAPSTONE_PROJECT.repositories.AddressesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


// This service class manages CRUD operations related to Address entities
// All operations are restricted to the authenticated user who owns the address.

@Service
public class AddressesService {

    // This field is used to access the AddressesRepository, which is used to interact with the database
    @Autowired
    private AddressesRepository addressesRepository;

    // This method finds an address by its ID
    public Address findById(UUID addressId) {
        return this.addressesRepository.findById(addressId).orElseThrow(
                () -> new BadRequestException("Address with id " + addressId + " not found!")
        );
    }

    // This method finds an address by its ID and user
    public Address findByIdAndUser(UUID addressId, User user) {
        Address found = this.findById(addressId);
        // Check if the address belongs to the user or if the user is an admin
        if (!user.isOwnerOf(found) && !user.isAdmin()) {
            throw new BadRequestException("You are not authorized to access this address!");
        }
        return found;
    }

    // This method finds an address by its label and user
    public Address findByLabelAndUser(String label, User user) {
        Address found = this.addressesRepository.findByLabelAndUser(label, user).orElseThrow(
                () -> new BadRequestException("Address with label '" + label + "' not found!")
        );
        // Check if the address belongs to the user or if the user is an admin
        if (!user.isOwnerOf(found) && !user.isAdmin()) {
            throw new BadRequestException("You are not authorized to access this address!");
        }
        return found;
    }

    // This method finds all addresses with pagination and sorting
    public Page<Address> findAll(int page, int size, String sortBy) {
        if (size > 20) size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.addressesRepository.findAll(pageable);
    }

    // This method finds all addresses by user
    public List<Address> findAllByUser(User user) {
        return this.addressesRepository.findAllByUser(user);
    }

    // This method saves a new address
    public Address save(AddressPayload body, User user) {
        // Check if the address name already exists for the user
        this.addressesRepository.findByLabelAndUser(body.label(), user).ifPresent(
                address -> {
                    throw new BadRequestException("Address with label " + body.label() + " already exists!");
                }
        );
        // If it doesn't exist, create a new address and save it
        Address newAddress = new Address(
                body.label(),
                body.addressLine(),
                body.city(),
                body.province(),
                body.postalCode(),
                body.country(),
                user
        );
        return this.addressesRepository.save(newAddress);
    }

    // This method updates an existing address by its ID
    public Address findByIdAndUserAndUpdate(UUID addressId, AddressPayload body, User user) {
        // Find the address and check ownership or admin status
        Address found = this.findById(addressId);
        if (!user.isOwnerOf(found) && !user.isAdmin()) {
            throw new BadRequestException("You are not authorized to update this address!");
        }
        // Check if another cellar with the same label already exists for the user
        this.addressesRepository.findByLabelAndUser(body.label(), user).ifPresent(existing -> {
            if (!existing.getId().equals(addressId)) {
                throw new BadRequestException("You already have an address with label: + " + body.label());
            }
        });
        // Apply the updates
        found.setLabel(body.label());
        found.setAddressLine(body.addressLine());
        found.setCity(body.city());
        found.setProvince(body.province());
        found.setPostalCode(body.postalCode());
        found.setCountry(body.country());
        return this.addressesRepository.save(found);
    }

    // This method deletes a cellar by its ID
    public void findByIdAndUserAndDelete(UUID addressId, User user) {
        // Find the cellar and check ownership or admin status
        Address found = this.findById(addressId);
        if (!user.isOwnerOf(found) && !user.isAdmin()) {
            throw new BadRequestException("You are not authorized to delete this address!");
        }
        // Delete the cellar
        this.addressesRepository.delete(found);
    }
}
