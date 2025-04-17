package andrea_freddi.CAPSTONE_PROJECT.payloads;

// This class is used to receive the login data from the client (email/username and password)

public record LoginPayload(
        String identifier, // it could be email or username
        String password
) {
}
