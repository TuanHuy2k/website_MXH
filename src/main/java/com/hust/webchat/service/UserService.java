package com.hust.webchat.service;

import com.hust.webchat.domain.User;
import com.hust.webchat.dto.UserDTO;
import com.hust.webchat.dto.request.UserRegisterRequest;
import com.hust.webchat.dto.request.UserUpdateRequest;
import liquibase.pro.packaged.L;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    /**
     * xac thuc user
     *
     * @param request
     * @param response
     * @param user
     * @return
     */
    User authenticate(HttpServletRequest request, HttpServletResponse response, User user);

    /**
     * dang nhap
     *
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    String login(String username, String password, HttpServletRequest request, HttpServletResponse response);

    /**
     * find user by username
     *
     * @param username
     * @return
     */
    Optional<User> findByLogin(String username);

    /**
     * register account
     *
     * @param userRegisterRequest
     * @param response
     * @param request
     * @return
     */
    ResponseEntity<Map<String, String>> register(UserRegisterRequest userRegisterRequest, HttpServletResponse response, HttpServletRequest request);

    /**
     * active account by key
     *
     * @param key
     * @return
     */
    String activateEmail(String key);

    /**
     * save user
     *
     * @param user
     * @return
     */
    User save(User user);

    /**
     * find user by key active
     *
     * @param key
     * @return
     */
    Optional<User> findByKeyActivate(String key);

    /**
     * get danh sach tat ca nguoi dung
     *
     * @return
     */
    List<UserDTO> getAllUser();

    /**
     * get danh sach ban be cua tai khoan dang nhap hien tai
     *
     * @return
     */
    List<UserDTO> getFriendsOfCurrentUser();

    /**
     * Them ban be
     *
     * @param userId
     * @return
     */
    UserDTO addFriend(Long userId);

    /**
     * get danh sach user chua phai ban voi tai khoan hien tai
     *
     * @return
     */
    List<UserDTO> getUserNotFriend();

    /**
     * dong y loi moi ket ban
     *
     * @param userId
     * @return
     */
    Boolean approveFriend(Long userId);

    /**
     * get danh sach loi moi ket ban
     *
     * @return
     */
    List<UserDTO> getListApproveFriends();

    /**
     * get user dang dang nhap hien tai
     *
     * @return
     */
    User getCurrentUser();

    /**
     * huy loi moi ket ban
     *
     * @param userId
     */
    void cancelRequestFriend(Long userId);

    /**
     *  cap nhat anh
     *
     * @param coverImgId
     * @return
     */
    User updateImage(Long coverImgId, String type);

    /**
     *  cap nhat thong tin user
     *
     * @param request
     * @return
     */
    User update(UserUpdateRequest request);

    /**
     * search user theo keyword
     *
     * @param keyword
     * @return
     */
    List<UserDTO> searchUser(String keyword);

}
