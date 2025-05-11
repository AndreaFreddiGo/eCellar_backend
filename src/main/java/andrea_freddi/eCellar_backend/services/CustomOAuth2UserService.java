package andrea_freddi.eCellar_backend.services;

import andrea_freddi.eCellar_backend.entities.CustomOAuth2User;
import andrea_freddi.eCellar_backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UsersService usersService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("name");
        String pictureUrl = oAuth2User.getAttribute("picture");

        User user = usersService.findOrCreateGoogleUser(email, fullName, pictureUrl);

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }

}
