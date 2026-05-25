package ws.academy.auction.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ws.academy.auction.api.dto.rq.users.FullNameRq;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;
import ws.academy.auction.api.dto.rs.users.UserDetails;
import ws.academy.auction.core.entity.Participant;
import ws.academy.auction.core.entity.User;
import ws.academy.auction.core.enums.UserRole;
import ws.academy.auction.core.mapper.ParticipantMapper;
import ws.academy.auction.core.mapper.UserMapper;
import ws.academy.auction.core.repository.ParticipantRepository;
import ws.academy.auction.core.repository.UserRepository;
import ws.academy.auction.core.service.CurrentUserService;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final UserMapper userMapper;
    private final ParticipantMapper participantMapper;

    @Override
    public UserDetails showCurrentUser() {
        User user = getCurrentUser();
        UserDetails userDetails = userMapper.buildUserDetails(user);
        if (!user.getRole().equals(UserRole.PARTICIPANT)) {
            return userDetails;
        }
        Participant participant = participantRepository.findByUserGuid(user.getGuid());
        ParticipantDetails participantDetails = participantMapper.buildParticipantDetails(participant);
        participantDetails.setEmail(user.getEmail());
        userDetails.setParticipant(participantDetails);
        return userDetails;
    }

    @Override
    public void updateCurrentUser(FullNameRq request) {
        User user = getCurrentUser();
        if (!user.getRole().equals(UserRole.PARTICIPANT)) {
            return;
        }
        Participant participant = participantRepository.findByUserGuid(user.getGuid());
        participant.setFullName(request.getFullName());
        participantRepository.save(participant);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
        String guidKeycloak = jwtAuth.getToken().getClaimAsString("sub");
        return userRepository.findByGuidKeycloak(UUID.fromString(guidKeycloak))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
