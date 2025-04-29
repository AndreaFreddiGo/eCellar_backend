package andrea_freddi.CAPSTONE_PROJECT.services;

import andrea_freddi.CAPSTONE_PROJECT.entities.Address;
import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.exception.BadRequestException;
import andrea_freddi.CAPSTONE_PROJECT.mappers.AddressMapper;
import andrea_freddi.CAPSTONE_PROJECT.payloads.AddressDTO;
import andrea_freddi.CAPSTONE_PROJECT.payloads.AddressPayload;
import andrea_freddi.CAPSTONE_PROJECT.repositories.AddressesRepository;
import andrea_freddi.CAPSTONE_PROJECT.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


// This service class manages CRUD operations related to Address entities
// All operations are restricted to the authenticated user who owns the address.

@Service
public class AddressesService {

    // This field is used to access the AddressesRepository, which is used to interact with the database
    @Autowired
    private AddressesRepository addressesRepository;

    // The SecurityUtils is injected into this service to handle security-related operations
    @Autowired
    private SecurityUtils securityUtils;

    // The AddressMapper is injected to convert between Address entities and AddressDTOs
    @Autowired
    private AddressMapper addressMapper;

    // This method finds an address by its ID
    public Address findById(UUID addressId) {
        return this.addressesRepository.findById(addressId).orElseThrow(
                () -> new BadRequestException("Address with id " + addressId + " not found!")
        );
    }

    // This method finds an address by its ID and user
    public AddressDTO findByIdAndUser(UUID addressId, User user) {
        Address found = this.findById(addressId);
        // Check if the address belongs to the user or if the user is an admin
        securityUtils.checkOwnershipOrAdmin(user, found.getUser().getId());
        return addressMapper.addressToDTO(found);
    }

    // This method finds an address by its label and user
    public AddressDTO findByLabelAndUser(String label, User user) {
        Address found = this.addressesRepository.findByLabelAndUser(label, user).orElseThrow(
                () -> new BadRequestException("Address with label '" + label + "' not found!")
        );
        // Check if the address belongs to the user or if the user is an admin
        securityUtils.checkOwnershipOrAdmin(user, found.getUser().getId());
        return addressMapper.addressToDTO(found);
    }

    // This method finds all addresses with pagination and sorting
    public Page<AddressDTO> findAll(int page, int size, String sortBy) {
        if (size > 20) size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.addressesRepository.findAll(pageable).map(addressMapper::addressToDTO);
    }

    // This method finds all addresses by user
    public List<AddressDTO> findAllByUser(User user) {
        List<Address> addresses = this.addressesRepository.findAllByUser(user);
        return addresses.stream()
                .map(addressMapper::addressToDTO)
                .collect(Collectors.toList());
    }

    // This method saves a new address
    public AddressDTO save(AddressPayload body, User user) {
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
        return addressMapper.addressToDTO(this.addressesRepository.save(newAddress));
    }

    // This method updates an existing address by its ID
    public AddressDTO findByIdAndUserAndUpdate(UUID addressId, AddressPayload body, User user) {
        // Find the address and check ownership or admin status
        Address found = this.findById(addressId);
        if (!user.isOwnerOf(found) && !user.isAdmin()) {
            throw new BadRequestException("You are not authorized to update this address!");
        }
        // Check if another address with the same label already exists for the user
        this.addressesRepository.findByLabelAndUser(body.label(), user).ifPresent(existing -> {
            if (!existing.getId().equals(addressId)) {
                throw new BadRequestException("You already have an address labeled '" + body.label() + "'!");
            }
        });
        // Apply the updates
        found.setLabel(body.label());
        found.setAddressLine(body.addressLine());
        found.setCity(body.city());
        found.setProvince(body.province());
        found.setPostalCode(body.postalCode());
        found.setCountry(body.country());
        return addressMapper.addressToDTO(this.addressesRepository.save(found));
    }

    // This method deletes a cellar by its ID
    public void findByIdAndUserAndDelete(UUID addressId, User user) {
        // Find the cellar and check ownership or admin status
        Address found = this.findById(addressId);
        securityUtils.checkOwnershipOrAdmin(user, found.getUser().getId());
        // Delete the cellar
        this.addressesRepository.delete(found);
    }
}
