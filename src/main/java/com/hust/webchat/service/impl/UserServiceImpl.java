package com.hust.webchat.service.impl;

import com.hust.webchat.domain.FriendRelation;
import com.hust.webchat.domain.User;
import com.hust.webchat.dto.UserDTO;
import com.hust.webchat.dto.request.UserRegisterRequest;
import com.hust.webchat.dto.request.UserUpdateRequest;
import com.hust.webchat.mapper.UserMapper;
import com.hust.webchat.repository.FriendsRelationRepository;
import com.hust.webchat.repository.UserRepository;
import com.hust.webchat.security.AuthoritiesConstants;
import com.hust.webchat.security.SecurityUtils;
import com.hust.webchat.security.jwt.TokenProvider;
import com.hust.webchat.service.MailService;
import com.hust.webchat.service.UserService;
import com.hust.webchat.util.Constants;
import com.hust.webchat.util.CookieUtil;
import com.hust.webchat.util.RandomUtil;
import com.hust.webchat.web.error.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Value("${redirect-url}")
    private String redirectUrl;

    private final String AVATAR_DEFAULT_URL = "/images/default-avatar.jpg";

    private final String AVATAR_COVER_DEFAULT_URL = "/images/default-cover-avatar.jpg";

    private final String URL_IMG = "http://localhost:8081/api/public/files/";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final TokenProvider tokenProvider;

    private final UserDetailsService userDetailsService;

    private final FriendsRelationRepository friendsRelationRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService, TokenProvider tokenProvider, UserDetailsService userDetailsService, FriendsRelationRepository friendsRelationRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.friendsRelationRepository = friendsRelationRepository;
        this.userMapper = userMapper;
    }

    @Override
    public String login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        Optional<User> user = findByLogin(username);
        String result = "Tên đăng nhập hoặc mật khẩu không chính xác!!!";
        if (user.isPresent()) {
            User userByLogin = user.get();
            String salt = userByLogin.getSalt();
            String passwordSalt = password.concat(".").concat(salt);
            String hashed = userByLogin.getPassword();
            if (!BCrypt.checkpw(passwordSalt, hashed)) {
                return result;
            }
            if (!user.get().getActivated()) {
                return "Tài khoản của bạn chưa được kích hoạt, vui lòng kích hoạt email để sử dụng dịch vụ";
            }
            User user1 = authenticate(request, response, userByLogin);
            if (user1 == null) {
                return result;
            }
            return redirectUrl;
        }
        return result;
    }

    @Override
    public User authenticate(HttpServletRequest request, HttpServletResponse response, User user) {
        if (user != null) {
            //authority
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetailsService.loadUserByUsername(user.getUsername())
                            , null,
                            authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // đưa authentication vào securityContextHolder để nó quản lý
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // add jwt to cookie
            String jwt = tokenProvider.createToken(authentication, false);
            Cookie jwtCookie = new Cookie("jwt-token", jwt);
            Cookie usernameCookie = new Cookie("username", user.getUsername());
            Cookie userIdCookie = new Cookie("userId", user.getId().toString());

            CookieUtil.addCookies(response, jwtCookie, usernameCookie, userIdCookie);
        }
        return user;
    }

    @Override
    public Optional<User> findByLogin(String username) {
        return userRepository.findByUsernameAndStatus(username, Constants.ENTITY_STATUS.ACTIVE);
    }

    @Override
    public ResponseEntity<Map<String, String>> register(UserRegisterRequest userRegisterRequest, HttpServletResponse response, HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        Optional<User> optionalByUsername = this.findByLogin(userRegisterRequest.getUsername());
        if ((optionalByUsername.isPresent())) {

            result.put("username", "Tên tài khoản đã tồn tại!");
            return ResponseEntity.ok(result);
        }
        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getRepeatPassword())) {
            result.put("password", "Mật khẩu nhập lại không khớp");
            return ResponseEntity.ok(result);
        }

        // đăng ký với thông tin vừa nhập
        User user = this.registerAccount(userRegisterRequest.getUsername(), userRegisterRequest.getPassword(), userRegisterRequest.getEmail(), Constants.ENTITY_STATUS.INACTIVE);

        // gửi mail xác thực
        result.put("success", "Đăng ký thành công");
        mailService.sendMailActivate(user);
        return ResponseEntity.ok(result);
    }

    @Override
    public String activateEmail(String key) {
        Optional<User> user = findByKeyActivate(key);
        user.ifPresent(u -> {
            User userDomain = user.get();
            userDomain.setActivated(Boolean.TRUE);
            userDomain.setActivationKey(null);
            save(userDomain);
        });
        return "redirect:/";
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByKeyActivate(String key) {
        return userRepository.findByActivationKey(key);
    }

    @Override
    public List<UserDTO> getAllUser() {
        List<User> users = userRepository.findAll();
        return userMapper.toDto(users);
    }

    @Override
    public List<UserDTO> getFriendsOfCurrentUser() {
        User currentUser = getCurrentUser();

        // lay list cac moi quan he
        List<FriendRelation> friendRelations = friendsRelationRepository.findAllByUser(currentUser.getId());
        List<Long> friendIds = new ArrayList<>();

        // lay danh sach ban be da xac nhan by isFriend = true
        List<Long> friendIdsByFromUser = friendRelations.stream().map(FriendRelation::getFromUser).collect(Collectors.toList());
        List<Long> friendIdsByToUser = friendRelations.stream().map(FriendRelation::getToUser).collect(Collectors.toList());
        friendIds.addAll(friendIdsByFromUser);
        friendIds.addAll(friendIdsByToUser);
        friendIds.remove(currentUser.getId());

        List<User> friends = userRepository.getUserByIds(friendIds.stream().filter(userId -> !Objects.equals(currentUser.getId(), userId))
        .collect(Collectors.toList()));
        return userMapper.toDto(friends);
    }

    @Override
    public UserDTO addFriend(Long userId) {
        User currentUserLogin = null;
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if (userLogin.isPresent()) {
            Optional<User> userOptional = findByLogin(userLogin.get());
            if (userOptional.isPresent()) {
                currentUserLogin = userOptional.get();

                // add friends <->
                Optional<User> optionalFriend = userRepository.findById(userId);
                if (optionalFriend.isPresent()) {
                    // add friends for current user
                    Optional<FriendRelation> friendRelationOptional = friendsRelationRepository.findByUserRelation(userId, currentUserLogin.getId());
                    if (friendRelationOptional.isPresent()) {
                        throw new BadRequestAlertException("This account is pending to accept", "", "pendingToAccept");
                    } else {
                        sendFriendInvitation(currentUserLogin.getId(), userId);
                    }
                } else {
                    throw new BadRequestAlertException("Friend not found", "", "friendNotFound");
                }
            }
        } else {
            throw new BadRequestAlertException("User Invalid", "", "userInvalid");
        }
        return userMapper.toDto(currentUserLogin);
    }

    @Override
    public List<UserDTO> getUserNotFriend() {
        User currentUser = getCurrentUser();
        List<UserDTO> friends = getFriendsOfCurrentUser();
        List<Long> friendIds = friends.stream().map(UserDTO::getId).collect(Collectors.toList());
        List<User> userNotFriends;
        if (CollectionUtils.isEmpty(friendIds)) {
            userNotFriends = getAllUser().stream().map(userMapper::toEntity)
                    .filter(user -> !Objects.equals(user.getId(), currentUser.getId()))
                    .collect(Collectors.toList());
        } else {
            userNotFriends = userRepository.getUserByIdsNotIn(friendIds).stream()
                    .filter(user -> !Objects.equals(user.getId(), currentUser.getId()))
                    .collect(Collectors.toList());
        }
        return userMapper.toDto(userNotFriends);
    }

    @Override
    public Boolean approveFriend(Long userId) {
        User user = getCurrentUser();
        Optional<FriendRelation> optionalFriendRelation = friendsRelationRepository.findByUserRelation(user.getId(), userId);
        if (optionalFriendRelation.isPresent()) {
            FriendRelation friendRelation = optionalFriendRelation.get();
            friendRelation.setIsFriend(Boolean.TRUE);
            friendsRelationRepository.save(friendRelation);
            return true;
        } else {
            sendFriendInvitation(user.getId(), userId);
        }
        return false;
    }

    @Override
    public List<UserDTO> getListApproveFriends() {
        User user = getCurrentUser();
        List<FriendRelation> friendRelations = friendsRelationRepository.getAllApproveFriends(user.getId());
        List<Long> fromUserIds = friendRelations.stream().map(FriendRelation::getFromUser).collect(Collectors.toList());
        List<User> users = userRepository.getUserByIds(fromUserIds);
        return userMapper.toDto(users);
    }

    @Override
    public User getCurrentUser() {
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if (userLogin.isPresent()) {
            return findByLogin(userLogin.get()).orElseThrow(() ->
                    new BadRequestAlertException("User Invalid", "", "userInvalid"));
        }
        throw new BadRequestAlertException("User Invalid", "", "userInvalid");
    }

    @Override
    public void cancelRequestFriend(Long userId) {
        User currentUser = getCurrentUser();
        Optional<FriendRelation> friendRelation = friendsRelationRepository.findByFromUserAndToUser(currentUser.getId(), userId);
        if (friendRelation.isPresent()) {
            friendsRelationRepository.delete(friendRelation.get());
        }
    }

    @Override
    public User updateImage(Long coverImgId, String type) {
        User user = getCurrentUser();
        String avatarNew = URL_IMG + coverImgId.toString() + "/view";
        if (Constants.TYPE_IMAGE.AVATAR.equals(type)) {
            user.setAvatarUrl(avatarNew);
        }

        if (Constants.TYPE_IMAGE.COVER.equals(type)) {
            user.setCoverAvatarUrl(avatarNew);
        }

        userRepository.save(user);
        return user;
    }

    @Override
    public User update(UserUpdateRequest request) {
        User currentUser = getCurrentUser();
        currentUser.setFullName(request.getFullName());
        currentUser.setPhoneNumber(request.getPhoneNumber());
        currentUser.setAddress(request.getAddress());
        currentUser.setGender(request.getGender());
        currentUser.setEmail(request.getMail());

        userRepository.save(currentUser);
        return currentUser;
    }

    @Override
    public List<UserDTO> searchUser(String keyword) {
        List<User> users = userRepository.searchUser("%" + keyword + "%");
        return userMapper.toDto(users);
    }

    /**
     * dang ky tai khoan
     *
     * @param username
     * @param password
     * @param email
     * @param isAuthorized
     * @return
     */
    public User registerAccount(String username, String password, String email, Integer isAuthorized) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setActivated(true);
        user.setStatus(Constants.ENTITY_STATUS.ACTIVE);
        user.setCoverAvatarUrl(AVATAR_COVER_DEFAULT_URL);
        user.setAvatarUrl(AVATAR_DEFAULT_URL);
        user.setActivationKey(RandomUtil.generateActivationKey());

        // set password with salt
        this.getSaltWithPassword(password, user);
        User userAfterRegister = userRepository.save(user);
        return userAfterRegister;
    }

    /**
     * generate password with random salt + password by password encoder
     *
     * @param password
     * @param user
     */
    public void getSaltWithPassword(String password, User user) {
        String salt = RandomUtil.generateSalt();
        user.setSalt(salt);
        String passwordHash = password.toLowerCase().concat(".").concat(salt);
        user.setPassword(passwordEncoder.encode(passwordHash));
    }

    /**
     * tao loi moi ket ban
     *
     * @param fromUser
     * @param toUser
     * @return
     */
    private FriendRelation sendFriendInvitation(Long fromUser, Long toUser) {
        FriendRelation friendRelation = new FriendRelation();
        friendRelation.setFromUser(fromUser);
        friendRelation.setToUser(toUser);
        friendRelation.setIsFriend(Boolean.FALSE);
        friendsRelationRepository.save(friendRelation);
        return friendRelation;
    }


}
