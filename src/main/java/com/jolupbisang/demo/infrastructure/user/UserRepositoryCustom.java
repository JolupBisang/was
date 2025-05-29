package com.jolupbisang.demo.infrastructure.user;

import com.jolupbisang.demo.domain.user.User;
import java.util.List;

public interface UserRepositoryCustom {
    List<User> findByMeetingId(Long meetingId);
} 